package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.DualSetObservable;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Collection;
import java.util.Set;

/**
 * A RoadSystem models a set of {@link Element}s that can be
 * {@link edu.kit.rose.model.roadsystem.elements.Segment}s or {@link Group}s of Segments.
 * It also manages {@link Connection}s between these Segments and
 * allows the creation of new connections as well as their removal.
 * It provides Methods to create and remove {@link edu.kit.rose.model.roadsystem.elements.Segment}s.
 * In addition to this it allows for easy access to all information held within.
 */
public interface RoadSystem
    extends DualSetObservable<Element, Connection, RoadSystem>, UnitObserver<Connector> {
  /**
   * Returns a Box containing all {@link Element}s of the RoadSystem.
   *
   * @return A {@link edu.kit.rose.infrastructure.Box} of all {@link Element}s.
   */
  Box<Element> getElements();

  /**
   * Returns a Box of all {@link Element}s with a name similar to the given String.
   * What will count as similar is left open to the implementation.
   *
   * @param name The Name of the {@link Element}s to return.
   * @return A {@link edu.kit.rose.infrastructure.Box} of all {@link Element}s with the given name.
   */
  Box<Element> getElementsByName(String name);

  /**
   * Creates a {@link Segment} with the given {@link SegmentType}.
   *
   * @param segmentType The {@link SegmentType} the {@link Segment} shall have.
   */
  Segment createSegment(SegmentType segmentType);

  /**
   * Creates a {@link Group} containing the given {@link Element}s.
   *
   * @param includedElements The {@link Element}s that shall be in the new {@link Group}
   */
  Group createGroup(Set<Element> includedElements);

  /**
   * Removes an {@link Element} from the RoadSystem. It cannot be accessed afterwards.
   *
   * @param element The {@link Element} to remove.
   */
  void removeElement(Element element);

  /**
   * Returns a {@link SortedBox} of all shared {@link AttributeAccessor}s of the given
   * {@link Element}s.
   * The AttributeAccessors will set their respective attribute in all elements of
   * the given collection.
   *
   * @param elements The Collection of {@link Element}s to get the {@link AttributeAccessor}s from.
   * @return A {@link SortedBox} containing all shared {@link AttributeAccessor}s of the
   *        {@link Element}s. Or null in case of an Error.
   */
  SortedBox<AttributeAccessor<?>> getSharedAttributeAccessors(Collection<Element> elements);

  /**
   * Returns the {@link Connection} of the two given {@link Connector}s if there is one.
   *
   * @param segment1Connector The first {@link Connector}
   * @param segment2Connector The second {@link Connector}
   */
  Connection connectConnectors(Connector segment1Connector, Connector segment2Connector);

  /**
   * Disconnects a given {@link Connection} this means the {@link Connector}s are no
   * longer connected (same for their respective {@link Segment}s).
   *
   * @param connection The {@link Connection} to disconnect.
   */
  void disconnectConnection(Connection connection);

  /**
   * Disconnect all {@link Connection}s the given {@link Segment} has. This will disconnect it
   * from all Segments it was previously connected to.
   *
   * @param segment The {@link Segment} to disconnect.
   */
  void disconnectFromAll(Segment segment);

  /**
   * Returns a {@link Box} of the {@link Segment}s a given {@link Segment} is currently
   * connected to.
   *
   * @param segment The {@link Segment} of which to return the connected {@link Segment}s.
   * @return A {@link Box} of {@link Segment}s the given {@link Segment} was connected to.
   */
  Box<Segment> getAdjacentSegments(Segment segment);

  /**
   * Returns a {@link Box} of {@link Element}s that are the uppermost in the element hierarchy.
   *
   * @return the uppermost groups.
   */
  Box<Element> getRootElements();

  /**
   * Returns the {@link Connection} a given {@link Connector} is involved in.
   *
   * @param connector the {@link Connector} to look for the {@link Connection}.
   * @return the {@link Connection} the given {@link Connector} is involved in.
   */
  Connection getConnection(Connector connector);

  /**
   * Returns all {@link Connection}s the given {@link Segment} is currently involved in.
   * (Which means one of the {@link Segment}s {@link Connector}s is part of)
   *
   * @param segment the segments of which the Connections are wanted.
   * @return a {@link Box} of all {@link Connection}s of the Segment.
   */
  Box<Connection> getConnections(Segment segment);

  /**
   * Returns all {@link Connection}s the two given {@link Segment}s have together.
   *
   * @param segment1 the first Segment.
   * @param segment2 the second Segment.
   * @return a {@link Box} of all {@link Connection}s the two given {@link Segment}s have.
   */
  Box<Connection> getConnections(Segment segment1, Segment segment2);

  /**
   * Moves all the given {@link Segment}s from their current
   * {@link edu.kit.rose.infrastructure.Position} to a new one.
   * Uses the {@link Movement} and applies it to the old
   * {@link edu.kit.rose.infrastructure.Position}.
   * Does not disconnect the segments.
   *
   * @param segments the {@link Segment}s to move.
   * @param movement the {@link Movement} describing where to move the {@link Segment}s.
   */
  void moveSegments(Collection<Segment> segments, Movement movement);

  /**
   * Rotates the given {@link Segment} on its current {@link edu.kit.rose.infrastructure.Position}
   * by a given amount of degrees.
   *
   * @param segment the {@link Segment} to rotate.
   */
  void rotateSegment(Segment segment, int degrees);

  /**
   * Provides the {@link TimeSliceSetting} this RoadSystem uses.
   *
   * @return the used TimeSliceSetting.
   */
  TimeSliceSetting getTimeSliceSetting();
}
