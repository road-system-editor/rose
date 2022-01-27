package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.List;

/**
 * Represents a freeway exit.
 * An Exit Segment is a {@link Segment} that has one entrance to the main road and one exit from it
 * as well as a singular ramp by which cars can leave the Road.
 */
public class Exit extends RampSegment {

  /**
   * Standard Constructor.
   * initializes all values to default ones.
   */
  public Exit() {
    super(SegmentType.EXIT);
  }

  /**
   * Constructor.
   * Uses the name and initializes all values to default ones.
   *
   * @param name the name for the Exit Segment
   */
  public Exit(String name) {
    super(SegmentType.EXIT, name);
  }

  @Override
  protected void initRampConnector(List<AttributeAccessor<?>> rampAttributesList,
                              Position rampPosition) {
    this.rampConnector = new Connector(ConnectorType.RAMP_EXIT,
        rampPosition, rampAttributesList);
  }
}
