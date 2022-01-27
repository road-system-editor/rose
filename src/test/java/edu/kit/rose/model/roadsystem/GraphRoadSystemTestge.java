package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.Movement;
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
  DualSetObserver<Element, Connection, RoadSystem> observer;
  ArgumentCaptor<Element> elementArgumentCaptorAddition;
  ArgumentCaptor<Connection> connectionArgumentCaptorAddition;
  ArgumentCaptor<Element> elementArgumentCaptorRemoval;
  ArgumentCaptor<Connection> connectionArgumentCaptorRemoval;

  @BeforeEach
  void setup() {
    //Mockito class setup
    elementArgumentCaptorAddition = ArgumentCaptor.forClass(Element.class);
    connectionArgumentCaptorAddition = ArgumentCaptor.forClass(Connection.class);
    elementArgumentCaptorRemoval = ArgumentCaptor.forClass(Element.class);
    connectionArgumentCaptorRemoval = ArgumentCaptor.forClass(Connection.class);
    var criteriaManager = Mockito.mock(CriteriaManager.class);
    timeSliceSetting = Mockito.mock(TimeSliceSetting.class);
    observer = Mockito.mock(DualSetObserver.class);
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
    Assertions.assertTrue(testRoadSystem.getElements().contains(initialSegment)
        && testRoadSystem.getElements().getSize() == 1);
  }

  @Test
  void testGetElementsByName() {
    var segment = createSegmentWithName(SegmentType.EXIT, "ROSEge");
    Assertions.assertTrue(testRoadSystem.getElementsByName("R").contains(segment)
        && testRoadSystem.getElementsByName("R").getSize() == 1);
  }

  @Test
  void createSegmentTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    Assertions.assertEquals(entrance.getSegmentType(), SegmentType.ENTRANCE);
    Assertions.assertEquals(entrance, elementArgumentCaptorAddition.getValue());

    var base = testRoadSystem.createSegment(SegmentType.BASE);
    Assertions.assertEquals(base.getSegmentType(), SegmentType.BASE);
    Assertions.assertEquals(base, elementArgumentCaptorAddition.getValue());

    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    Assertions.assertEquals(exit.getSegmentType(), SegmentType.EXIT);
    Assertions.assertEquals(exit, elementArgumentCaptorAddition.getValue());
  }

  @Test
  void createGroupTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var base = testRoadSystem.createSegment(SegmentType.BASE);
    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    var group = testRoadSystem.createGroup(Set.of(entrance, base, exit));
    Assertions.assertEquals(group, elementArgumentCaptorAddition.getValue());
  }

  @Test
  void removeElementTest() {
    testRoadSystem.removeElement(initialSegment);
    Assertions.assertEquals(initialSegment, elementArgumentCaptorRemoval.getValue());
    Assertions.assertEquals(0, testRoadSystem.getElements().getSize());
  }

  @Test
  void connectConnectorsTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var initialConnector = initialSegment.getConnectors().iterator().next();
    var entranceConnector = entrance.getConnectors().iterator().next();
    var connection = testRoadSystem.connectConnectors(initialConnector, entranceConnector);
    Assertions.assertEquals(connection, connectionArgumentCaptorAddition.getValue());
    var exit = testRoadSystem.createSegment(SegmentType.EXIT);
    var exitConnector = exit.getConnectors().iterator().next();
    var newConnection = testRoadSystem.connectConnectors(initialConnector, exitConnector);
    Assertions.assertEquals(newConnection, connectionArgumentCaptorAddition.getValue());
  }

  @Test
  void disconnectConnectionTest() {
    var entrance = testRoadSystem.createSegment(SegmentType.ENTRANCE);
    var initialConnector = initialSegment.getConnectors().iterator().next();
    var entranceConnector = entrance.getConnectors().iterator().next();
    var connection = testRoadSystem.connectConnectors(initialConnector, entranceConnector);
    testRoadSystem.disconnectConnection(connection);
    Assertions.assertEquals(connection, connectionArgumentCaptorRemoval.getValue());
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

  @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
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
    var justHereForCheckStyle = connection; //fuck checkstyle in test classes
    Assertions.assertEquals(newConnection, connectionArgumentCaptorRemoval.getValue());
    var connections = new LinkedList<Connection>();
    testRoadSystem.getConnections(initialSegment).forEach(connections::add);
    Assertions.assertEquals(List.of(justHereForCheckStyle), connections);
    //TODO: check movement of segments and connectors
  }

  @Test
  void rotateSegmentTest() {
    int funnyNumber = 69;
    Assertions.assertEquals(0, initialSegment.getRotation());
    testRoadSystem.rotateSegment(initialSegment, funnyNumber);
    Assertions.assertEquals(funnyNumber, initialSegment.getRotation());
  }

  @Test
  void getTimeSliceSettingTest() {
    Assertions.assertEquals(timeSliceSetting, testRoadSystem.getTimeSliceSetting());
  }

  @Test
  void getThisTest() {
    Assertions.assertEquals(testRoadSystem, testRoadSystem.getThis());
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
    assert nameAccessor.getAttributeType() == AttributeType.NAME;
    ((AttributeAccessor<String>) nameAccessor).setValue(name);
    return segment;
  }

}
