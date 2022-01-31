package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.DataType;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.ConnectorType;
import edu.kit.rose.model.roadsystem.elements.Segment;

/**
 * Represents a class that encapsulates the recreation of
 * {@link edu.kit.rose.model.roadsystem.elements.Segment}s.
 */
public class SegmentFactory {

  private static final String INVALID_ATTRIBUTE_ACCESSOR_DATA_TYPE_MESSAGE
      = "Invalid attribute accessor datatype.";

  private final Project project;
  private final Segment oldSegment;

  public SegmentFactory(Project project, Segment oldSegment) {
    this.project = project;
    this.oldSegment = oldSegment;
  }

  /**
   * Creates a copy of the {@link SegmentFactory#SegmentFactory(Project, Segment)}}
   * segment parameter.
   */
  public Segment createSegment() {
    Segment newSegment = project.getRoadSystem().createSegment(this.oldSegment.getSegmentType());
    newSegment.move(new Movement(oldSegment.getCenter().getX(), oldSegment.getCenter().getY()));

    newSegment.getConnectors().forEach(connector -> {
      Connector connectorEquivalent = getConnectorOfType(oldSegment, connector.getType());
      if (connectorEquivalent != null) {
        connector.getPosition().setX(connectorEquivalent.getPosition().getX());
        connector.getPosition().setY(connectorEquivalent.getPosition().getY());
      }
    });

    transferAttributeAccessorValues(newSegment);

    return newSegment;
  }

  private Connector getConnectorOfType(Segment segment, ConnectorType type) {
    for (Connector connector : segment.getConnectors()) {
      if (connector.getType() == type) {
        return connector;
      }
    }
    return null;
  }

  private void transferAttributeAccessorValues(Segment newSegment) {
    for (AttributeAccessor<?> newAccessor : newSegment.getAttributeAccessors()) {
      for (AttributeAccessor<?> oldAccessor : oldSegment.getAttributeAccessors()) {
        if (newAccessor.getAttributeType() == oldAccessor.getAttributeType()) {
          transferAttributeAccessorValue(newAccessor, oldAccessor);
        }
      }

    }
  }

  private static void transferAttributeAccessorValue(
      AttributeAccessor<?> newAccessor, AttributeAccessor<?> oldAccessor) {
    if (newAccessor.getAttributeType().getDataType()
        != oldAccessor.getAttributeType().getDataType()) {
      return;
    }

    switch (newAccessor.getAttributeType().getDataType()) {
      case STRING -> SegmentFactory.<String>executeTransfer(newAccessor, oldAccessor);
      case BOOLEAN -> SegmentFactory.<Boolean>executeTransfer(newAccessor, oldAccessor);
      case INTEGER -> SegmentFactory.<Integer>executeTransfer(newAccessor, oldAccessor);
      case FRACTIONAL -> SegmentFactory.<Double>executeTransfer(newAccessor, oldAccessor);
      default -> throw new IllegalArgumentException(INVALID_ATTRIBUTE_ACCESSOR_DATA_TYPE_MESSAGE);
    }
  }

  private static <T> void executeTransfer(
      AttributeAccessor<?> newAccessor, AttributeAccessor<?> oldAccessor) {
    AttributeAccessor<T> newStringAccessor = (AttributeAccessor<T>) newAccessor;
    AttributeAccessor<T> oldStringAccessor = (AttributeAccessor<T>) oldAccessor;
    newStringAccessor.setValue(oldStringAccessor.getValue());
  }
}
