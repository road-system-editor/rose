package edu.kit.rose.controller.commons;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class that helps with copying a hierarchy of {@link Element}s to a road system while
 * considering replacements from a {@link ReplacementLog}.
 */
public class HierarchyCopier {
  private final ReplacementLog replacementLog;
  private final RoadSystem target;
  private final boolean makeReplacement;

  /**
   * Constructor.
   *
   * @param replacementLog the log that stores the replacement. Can be null if the copy methods
   *                       should not make a replacement.
   * @param target         the roadSystem where the copies will be created.
   */
  public HierarchyCopier(ReplacementLog replacementLog, RoadSystem target) {
    this.replacementLog = replacementLog;
    makeReplacement = replacementLog != null;
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Copies a group and its contents (recursively) to the new road system.
   *
   * @param original the group to copy.
   * @return the copy of the original in the target road system.
   */
  public Group copyGroup(Group original) {
    Set<Element> elementsCopy = original.getElements().stream()
        .map(this::copyElement)
        .collect(Collectors.toCollection(LinkedHashSet::new));

    Group copy = this.target.createGroup(elementsCopy);
    this.copyAccessors(original, copy);
    if (makeReplacement) {
      this.replacementLog.replaceElement(original, copy);
    }

    return copy;
  }

  /**
   * Copies a segment to the target road system.
   *
   * @param original the segment to copy.
   * @return the copy of the original in the target road system.
   */
  public Segment copySegment(Segment original) {
    Segment copy = this.target.createSegment(original.getSegmentType());

    this.copyPositionData(original, copy);
    this.copyAccessors(original, copy);
    if (makeReplacement) {
      this.replacementLog.replaceElement(original, copy);
    }

    return copy;
  }

  /**
   * Copies an element to the target road system.
   *
   * @param original the element to copy.
   * @return the copy of the original in the target road system.
   */
  public Element copyElement(Element original) {
    return original.isContainer()
        ? this.copyGroup((Group) original)
        : this.copySegment((Segment) original);
  }

  private void copyPositionData(Segment source, Segment target) {
    target.rotate(source.getRotation());

    Movement centerMovement;

    if (source.getSegmentType() == SegmentType.BASE) {
      Base castedSource = (Base) source;
      Base castedTarget = (Base) target;

      Movement connectorDifference = getTranslationMovement(
          castedSource.getEntry().getPosition(), castedSource.getExit().getPosition());

      Position targetExitConnectorPosition = castedTarget.getEntry()
          .getPosition().add(connectorDifference);

      Movement exitConnectorMovement = getTranslationMovement(
          castedTarget.getExit().getPosition(),
          targetExitConnectorPosition);

      castedTarget.getExit().move(exitConnectorMovement);
    }

    centerMovement = getTranslationMovement(
        target.getCenter(), source.getCenter());

    target.move(centerMovement);
  }

  private Movement getTranslationMovement(Position sourcePosition, Position targetPosition) {
    return new Movement(targetPosition.getX() - sourcePosition.getX(),
                        targetPosition.getY() - sourcePosition.getY());
  }

  private void copyAccessors(Element source, Element target) {
    for (var sourceAccessor : source.getAttributeAccessors()) {
      copyAccessorValue(sourceAccessor, target.getAttributeAccessors());
    }
  }

  private <T> void copyAccessorValue(AttributeAccessor<T> sourceAccessor,
                                     SortedBox<AttributeAccessor<?>> target) {
    for (var targetAccessor : target) {
      if (sourceAccessor.getAttributeType() == targetAccessor.getAttributeType()) {
        @SuppressWarnings("unchecked") // this cast is allowed since the attribute type is the same
        var typeCheckedTargetAccessor = (AttributeAccessor<T>) targetAccessor;
        typeCheckedTargetAccessor.setValue(sourceAccessor.getValue());
      }
    }
  }
}
