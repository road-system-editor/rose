package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.measurements.Measurement;

/**
 * A Piece of a road in a RoadSystem. (see: Pflichtenheft: "Straßensegment")
 * Is additionally to be observed by PlausibilityCriteria.
 */
public interface Segment extends Element, Comparable<Segment> {

  /**
   * Returns the {@link SegmentType} of the Segment.
   *
   * @return the {@link SegmentType} of the Segment.
   */
  SegmentType getSegmentType();

  /**
   * Returns the {@link Measurement}s of the Segment.
   *
   * @return a {@link Box} containing all {@link Measurement}s of the Segment.
   */
  Box<Measurement<?>> getMeasurements();

  /**
   * Returns the {@link Connector}s of the Segment.
   *
   * @return a {@link Box} containing all {@link Connector}s of the Segment.
   */
  Box<Connector> getConnectors();

  /**
   * Returns the center {@link Position} of the Segment.
   *
   * @return the center {@link Position} of the Segment.
   */
  Position getCenter();

  /**
   * Moves Segment. Changes both the center point and the {@link Position}s
   * of all included Connectors in order to preserve the shape of the Segment.
   *
   * @param movement the movement that is to be applied.
   */
  void move(Movement movement);

  /**
   * Increase the rotation ba a given amount of degrees.
   *
   * @param degrees the degrees by which to increase the rotation of this segment.
   */
  void rotate(int degrees);

  /**
   * Provides the rotation of a segment in degrees.
   *
   * @return the rotation.
   */
  int getRotation();

  /**
   * Provides the position of a {@link Connector} of this Segment when factoring in the current
   * rotation of this segment.
   *
   * @param connector the connector, must be of this segment!
   * @return the Position the Connector has when factoring in the rotation.
   */
  Position getAbsoluteConnectorPosition(Connector connector);
}
