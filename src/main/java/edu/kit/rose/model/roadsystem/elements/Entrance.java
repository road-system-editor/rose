package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import java.util.List;

/**
 * Represents a freeway entrance.
 * An Entrance Segment is a {@link Segment} that provides one entrance from the main road
 * and one exit from it
 * as well as a ramp by which cars can enter the Road.
 */
public class Entrance extends RampSegment {

  private static final int INITIAL_RAMP_DISTANCE_TO_CENTER_X = 30;

  /**
   * Standard Constructor.
   * Initializes all values to default ones.
   */
  public Entrance() {
    super(SegmentType.ENTRANCE);
  }

  /**
   * Constructor.
   * Uses the name and initializes all values to default ones.
   *
   * @param name the name for the Entrance Segment
   */
  public Entrance(String name) {
    super(SegmentType.ENTRANCE, name);
  }

  @Override
  protected void initRampConnector(List<AttributeAccessor<?>> rampAttributesList) {
    var rampPosition =
        new Position(INITIAL_RAMP_DISTANCE_TO_CENTER_X, -INITIAL_RAMP_DISTANCE_TO_CENTER_Y);
    this.rampConnector = new Connector(ConnectorType.RAMP_ENTRY, rampPosition, rampAttributesList);
  }
}
