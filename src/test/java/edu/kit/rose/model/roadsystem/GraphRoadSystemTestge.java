package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Tests the {@link GraphRoadSystem}.
 */
public class GraphRoadSystemTestge {

  private RoadSystem testRoadSystem;
  private TimeSliceSetting timeSliceSetting;
  private Segment initialSegment;
  private ArgumentCaptor<Element> elementArgumentCaptorAddition;
  private ArgumentCaptor<Connection> connectionArgumentCaptorAddition;
  private ArgumentCaptor<Element> elementArgumentCaptorRemoval;
  private ArgumentCaptor<Connection> connectionArgumentCaptorRemoval;

  @BeforeEach
  void setup() {
    //Mockito class setup
    elementArgumentCaptorAddition = ArgumentCaptor.forClass(Element.class);
    connectionArgumentCaptorAddition = ArgumentCaptor.forClass(Connection.class);
    elementArgumentCaptorRemoval = ArgumentCaptor.forClass(Element.class);
    connectionArgumentCaptorRemoval = ArgumentCaptor.forClass(Connection.class);
    var criteriaManager = Mockito.mock(CriteriaManager.class);
    timeSliceSetting = Mockito.mock(TimeSliceSetting.class);
    DualSetObserver<Element, Connection, RoadSystem> observer = Mockito.mock(DualSetObserver.class);
    //Mockito method setup
    Mockito.when(criteriaManager.getCriteria()).thenReturn(new RoseSortedBox<>());
    Mockito.doAnswer(invocation -> null)
        .when(observer).notifyAddition(elementArgumentCaptorAddition.capture());
    Mockito.doAnswer(invocation -> null)
        .when(observer).notifyRemoval(elementArgumentCaptorRemoval.capture());
    Mockito.doAnswer(invocation -> null)
        .when(observer).notifyAdditionSecond(connectionArgumentCaptorAddition.capture());
    Mockito.doAnswer(invocation -> null)
        .when(observer).notifyRemovalSecond(connectionArgumentCaptorRemoval.capture());
    //other classes setup
    testRoadSystem = new GraphRoadSystem(criteriaManager, timeSliceSetting);
    initialSegment = createSegmentWithName(SegmentType.BASE, "PSEge");
    //setup observation
    testRoadSystem.addSubscriber(observer);
    //reset calls counters
  }

  @Test
  void testGetElements() {
    Assertions.assertTrue(testRoadSystem.getElements().contains(initialSegment));
    Assertions.assertEquals(1, testRoadSystem.getElements().getSize());

  }

  @Test
  void testGetElementsByName() {
    var segment = createSegmentWithName(SegmentType.EXIT, "ROSEge");
    Assertions.assertTrue(testRoadSystem.getElementsByName("R").contains(segment));
    Assertions.assertEquals(1, testRoadSystem.getElementsByName("R").getSize());
  }

  @Test
  void createSegmentTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    Assertions.assertEquals(entrance.getSegmentType(), SegmentType.ENTRANCE);
    Assertions.assertEquals(entrance, elementArgumentCaptorAddition.getValue());
    Assertions.assertTrue(testRoadSystem.getElements().contains(entrance));
    Assertions.assertEquals(0, testRoadSystem.getConnections(entrance).getSize());

    var base = testRoadSystem.createSegment(SegmentType.BASE);
    Assertions.assertEquals(base.getSegmentType(), SegmentType.BASE);
    Assertions.assertEquals(base, elementArgumentCaptorAddition.getValue());
    Assertions.assertTrue(testRoadSystem.getElements().contains(base));
    Assertions.assertEquals(0, testRoadSystem.getConnections(base).getSize());

    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    Assertions.assertEquals(exit.getSegmentType(), SegmentType.EXIT);
    Assertions.assertEquals(exit, elementArgumentCaptorAddition.getValue());
    Assertions.assertTrue(testRoadSystem.getElements().contains(exit));
    Assertions.assertEquals(0, testRoadSystem.getConnections(exit).getSize());
  }

  @Test
  void createGroupTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var base = testRoadSystem.createSegment(SegmentType.BASE);
    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    var group = testRoadSystem.createGroup(Set.of(entrance, base, exit));
    Assertions.assertEquals(group, elementArgumentCaptorAddition.getValue());
    Assertions.assertTrue(testRoadSystem.getElements().contains(group));
    Assertions.assertEquals(3, group.getElements().getSize());
    var elements = new LinkedList<Element>();
    group.getElements().forEach(elements::add);
    Assertions.assertTrue(elements.containsAll(List.of(entrance, base, exit)));
  }

  @Test
  void removeElementTest() {
    testRoadSystem.removeElement(initialSegment);
    Assertions.assertEquals(initialSegment, elementArgumentCaptorRemoval.getValue());
    Assertions.assertEquals(0, testRoadSystem.getElements().getSize());
    Assertions.assertFalse(testRoadSystem.getElements().contains(initialSegment));
  }

  @Test
  void connectConnectorsTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var initialConnector = initialSegment.getConnectors().iterator().next();
    var entranceConnector = entrance.getConnectors().iterator().next();
    var connection = testRoadSystem.connectConnectors(initialConnector, entranceConnector);
    Assertions.assertEquals(connection, connectionArgumentCaptorAddition.getValue());
    Assertions.assertEquals(connection, testRoadSystem.getConnection(initialConnector));
    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    var exitConnector = exit.getConnectors().iterator().next();
    var newConnection = testRoadSystem.connectConnectors(initialConnector, exitConnector);
    Assertions.assertEquals(newConnection, connectionArgumentCaptorAddition.getValue());
    Assertions.assertEquals(newConnection, testRoadSystem.getConnection(initialConnector));
    Assertions.assertEquals(connection, connectionArgumentCaptorRemoval.getValue());
  }

  @Test
  void disconnectConnectionTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var initialConnector = initialSegment.getConnectors().iterator().next();
    var entranceConnector = entrance.getConnectors().iterator().next();
    var connection = testRoadSystem.connectConnectors(initialConnector, entranceConnector);
    testRoadSystem.disconnectConnection(connection);
    Assertions.assertEquals(connection, connectionArgumentCaptorRemoval.getValue());
    Assertions.assertNull(testRoadSystem.getConnection(initialConnector));
  }

  @Test
  void disconnectFromAllTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    var initialConnectors = new LinkedList<Connector>();
    initialSegment.getConnectors().forEach(initialConnectors::add);
    var initialConnector1 = initialConnectors.get(0);
    var initialConnector2 = initialConnectors.get(1);
    var entranceConnector = entrance.getConnectors().iterator().next();
    var exitConnector = exit.getConnectors().iterator().next();
    var connection = testRoadSystem.connectConnectors(initialConnector1, entranceConnector);
    var newConnection = testRoadSystem.connectConnectors(initialConnector2, exitConnector);
    testRoadSystem.disconnectFromAll(initialSegment);
    Assertions.assertEquals(List.of(connection, newConnection),
        connectionArgumentCaptorRemoval.getAllValues());
    Assertions.assertNull(testRoadSystem.getConnection(initialConnector1));
    Assertions.assertNull(testRoadSystem.getConnection(initialConnector2));
  }

  @Test
  void getAdjacentSegmentsTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    var initialConnectors = new LinkedList<Connector>();
    initialSegment.getConnectors().forEach(initialConnectors::add);
    var initialConnector1 = initialConnectors.get(0);
    var initialConnector2 = initialConnectors.get(1);
    var entranceConnector = entrance.getConnectors().iterator().next();
    var exitConnector = exit.getConnectors().iterator().next();
    testRoadSystem.connectConnectors(initialConnector1, entranceConnector);
    testRoadSystem.connectConnectors(initialConnector2, exitConnector);
    var segments = new LinkedList<Segment>();
    testRoadSystem.getAdjacentSegments(initialSegment).forEach(segments::add);
    Assertions.assertEquals(List.of(entrance, exit), segments);
    var base = testRoadSystem.createSegment(SegmentType.BASE);
    Assertions.assertEquals(0, testRoadSystem.getAdjacentSegments(base).getSize());
  }

  @Test
  void getRootElementsTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var group = testRoadSystem.createGroup(Set.of(entrance));
    var roots = new LinkedList<Element>();
    testRoadSystem.getRootElements().forEach(roots::add);
    Assertions.assertEquals(List.of(initialSegment, group), roots);
  }

  @Test
  void getConnectionsTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    var initialConnectors = new LinkedList<Connector>();
    initialSegment.getConnectors().forEach(initialConnectors::add);
    var initialConnector1 = initialConnectors.get(0);
    var initialConnector2 = initialConnectors.get(1);
    var entranceConnector = entrance.getConnectors().iterator().next();
    var exitConnector = exit.getConnectors().iterator().next();
    var connection = testRoadSystem.connectConnectors(initialConnector1, entranceConnector);
    var newConnection = testRoadSystem.connectConnectors(initialConnector2, exitConnector);
    var connections = new LinkedList<Connection>();
    testRoadSystem.getConnections(initialSegment).forEach(connections::add);
    Assertions.assertEquals(List.of(connection, newConnection), connections);
    connections.clear();
    testRoadSystem.getConnections(initialSegment, entrance).forEach(connections::add);
    Assertions.assertEquals(List.of(connection), connections);
  }

  @Test
  void getConnectionTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var initialConnector = initialSegment.getConnectors().iterator().next();
    var entranceConnector = entrance.getConnectors().iterator().next();
    var connection = testRoadSystem.connectConnectors(initialConnector, entranceConnector);
    Assertions.assertEquals(connection, testRoadSystem.getConnection(initialConnector));
    Assertions.assertEquals(connection, testRoadSystem.getConnection(entranceConnector));
  }

  @Test
  void moveSegmentsTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    var initialConnectors = new LinkedList<Connector>();
    initialSegment.getConnectors().forEach(initialConnectors::add);
    var initialConnector1 = initialConnectors.get(0);
    var initialConnector2 = initialConnectors.get(1);
    var entranceConnector = entrance.getConnectors().iterator().next();
    var exitConnector = exit.getConnectors().iterator().next();
    var connection = testRoadSystem.connectConnectors(initialConnector1, entranceConnector);
    var newConnection = testRoadSystem.connectConnectors(initialConnector2, exitConnector);
    testRoadSystem.moveSegments(List.of(initialSegment, entrance), new Movement(69, 420));
    var justHereForCheckStyle = connection; //checkstyle in test classes was a great idea
    Assertions.assertEquals(newConnection, connectionArgumentCaptorRemoval.getValue());
    var connections = new LinkedList<Connection>();
    testRoadSystem.getConnections(initialSegment).forEach(connections::add);
    Assertions.assertEquals(List.of(justHereForCheckStyle), connections);
  }

  @Test
  void moveSegmentsTest2() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    var initialConnectors = new LinkedList<Connector>();
    initialSegment.getConnectors().forEach(initialConnectors::add);
    var initialConnector1 = initialConnectors.get(0);
    var initialConnector2 = initialConnectors.get(1);
    var entranceConnector = entrance.getConnectors().iterator().next();
    var exitConnector = exit.getConnectors().iterator().next();
    Segment[] segments = new Segment[]{initialSegment, entrance, exit};
    Position[] originalCenters = new Position[3];
    for (int i = 0; i < 3; i++) {
      originalCenters[i] = new Position(segments[i].getCenter().getX(),
          segments[i].getCenter().getY());
    }
    testRoadSystem.connectConnectors(initialConnector1, entranceConnector);
    testRoadSystem.connectConnectors(initialConnector2, exitConnector);
    testRoadSystem.moveSegments(List.of(initialSegment, entrance), new Movement(69, 420));
    for (int i = 0; i < 2; i++) {
      Assertions.assertEquals(originalCenters[i].getX() + 69, segments[i].getCenter().getX());
      Assertions.assertEquals(originalCenters[i].getY() + 420, segments[i].getCenter().getY());
    }
    Assertions.assertEquals(originalCenters[2].getX(), segments[2].getCenter().getX());
    Assertions.assertEquals(originalCenters[2].getY(), segments[2].getCenter().getY());
  }

  @Test
  void rotateSegmentTest() {
    int funnyNumber = 69;
    Assumptions.assumeTrue(initialSegment.getRotation() == 0);
    testRoadSystem.rotateSegment(initialSegment, funnyNumber);
    Assertions.assertEquals(funnyNumber, initialSegment.getRotation());
  }

  @Test
  void getTimeSliceSettingTest() {
    Assertions.assertSame(timeSliceSetting, testRoadSystem.getTimeSliceSetting());
  }

  @Test
  void getThisTest() {
    Assertions.assertSame(testRoadSystem, testRoadSystem.getThis());
  }

  @Test
  void notifyChangeTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var initialConnector = initialSegment.getConnectors().iterator().next();
    var entranceConnector = entrance.getConnectors().iterator().next();
    var connection = testRoadSystem.connectConnectors(initialConnector, entranceConnector);
    testRoadSystem.notifyChange(initialConnector);
    Assertions.assertEquals(connection, connectionArgumentCaptorRemoval.getValue());
  }

  private Segment createSegmentWithName(SegmentType segmentType, String name) {
    var segment = testRoadSystem.createSegment(segmentType);
    var nameAccessor = segment.getAttributeAccessors().get(0);
    Assumptions.assumeTrue(nameAccessor.getAttributeType() == AttributeType.NAME);
    ((AttributeAccessor<String>) nameAccessor).setValue(name);
    return segment;
  }

}
