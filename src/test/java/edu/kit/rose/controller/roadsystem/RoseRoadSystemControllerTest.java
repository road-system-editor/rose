package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.ConnectorType;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Test the {@link RoseRoadSystemController} class.
 */
public class RoseRoadSystemControllerTest {

  private ChangeCommandBuffer changeCommandBuffer;
  private StorageLock storageLock;
  private Navigator navigator;
  private SelectionBuffer selectionBuffer;
  private Project project;

  private RoadSystemController roadSystemController;


  @BeforeEach
  private void setUp() {
    changeCommandBuffer = Mockito.mock(ChangeCommandBuffer.class);
    storageLock = Mockito.mock(StorageLock.class);
    navigator = Mockito.mock(Navigator.class);
    selectionBuffer = Mockito.mock(SelectionBuffer.class);
    project = Mockito.mock(Project.class);


    roadSystemController = new RoseRoadSystemController(
        changeCommandBuffer,
        storageLock,
        navigator,
        selectionBuffer,
        project);
  }

  @Test
  public void testSetZoomLevel() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);
    int[] targetResult = new int[] { 10 };

    ZoomSetting zoomSetting = Mockito.mock(ZoomSetting.class);
    Mockito.when(project.getZoomSetting()).thenReturn(zoomSetting);
    Mockito.doAnswer(invocation -> {
      Assertions.assertEquals(targetResult[0], (int) invocation.getArgument(0));
      targetResult[0]++;
      called.set(true);
      return null;
    }).when(zoomSetting).setZoomLevel(Mockito.anyInt());

    roadSystemController.setZoomLevel(10);
    roadSystemController.setZoomLevel(11);
    Assertions.assertTrue(called.get());
  }

  @Test
  public void testSetEditorPosition() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);

    Position targetPosition = Mockito.mock(Position.class);
    ZoomSetting zoomSetting = Mockito.mock(ZoomSetting.class);
    Mockito.when(project.getZoomSetting()).thenReturn(zoomSetting);
    Mockito.doAnswer(invocation -> {
      Assertions.assertSame(targetPosition, invocation.getArgument(0));
      called.set(true);
      return null;
    }).when(zoomSetting).setCenterOfView(Mockito.any(Position.class));

    roadSystemController.setEditorPosition(targetPosition);
    Assertions.assertTrue(called.get());
  }

  @Test
  public void testCreateStreetSegment() {

    AtomicReference<Boolean> called = new AtomicReference<>(false);

    RoadSystem roadSystem = Mockito.mock(RoadSystem.class);
    Mockito.when(project.getRoadSystem()).thenReturn(roadSystem);
    Mockito.doAnswer(invocation -> {
      Assertions.assertEquals(SegmentType.BASE, invocation.getArgument(0));
      called.set(true);
      return null;
    }).when(roadSystem).createSegment(Mockito.any(SegmentType.class));

    roadSystemController.createStreetSegment(SegmentType.BASE);
    Assertions.assertTrue(called.get());
  }

  @Test
  public void testDeleteStreetSegment() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);

    Segment segment = Mockito.mock(Segment.class);

    RoadSystem roadSystem = Mockito.mock(RoadSystem.class);
    Mockito.when(project.getRoadSystem()).thenReturn(roadSystem);

    Mockito.doAnswer(invocation -> {
      Assertions.assertSame(segment, invocation.getArgument(0));
      called.set(true);
      return null;
    }).when(roadSystem).removeElement(Mockito.any(Element.class));

    roadSystemController.deleteStreetSegment(segment);
    Assertions.assertTrue(called.get());
  }


  @Test
  public void testDragStreetSegment() {
    final Position initialPosition = new Position(10, 10);
    final Position endPosition = new Position(20, 20);

    AtomicReference<Integer> centerPositionX = new AtomicReference<>(0);
    AtomicReference<Integer> centerPositionY = new AtomicReference<>(0);

    Segment segment = Mockito.mock(Segment.class);
    Mockito.doAnswer(invocation -> {
      Movement movement = invocation.getArgument(0);
      centerPositionX.set(centerPositionX.get() + movement.getX());
      centerPositionY.set(centerPositionY.get() + movement.getY());
      return null;
    }).when(segment).move(Mockito.any(Movement.class));

    Mockito.when(segment.getCenter()).thenReturn(new Position(
        centerPositionX.get(),
        centerPositionY.get()));

    roadSystemController.beginDragStreetSegment(initialPosition);

    roadSystemController.endDragStreetSegment(endPosition);

    Assertions.assertEquals(10, segment.getCenter().getX());
    Assertions.assertEquals(10, segment.getCenter().getY());
  }

  @Test
  public void testToggleSegmentSelection() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);
    AtomicReference<Boolean> selectedFlag = new AtomicReference<>(false);

    Segment segment = Mockito.mock(Segment.class);

    Mockito.doAnswer(invocation -> {
      if (segment == invocation.getArgument(0)) {
        called.set(true);
        selectedFlag.set(selectedFlag.get());
      }
      return null;
    }).when(selectionBuffer).toggleSegmentSelection(Mockito.any(Segment.class));

    roadSystemController.toggleSegmentSelection(segment);
    Assertions.assertTrue(selectedFlag.get());
    Assertions.assertTrue(called.get());

    called.set(false);
    roadSystemController.toggleSegmentSelection(segment);
    Assertions.assertFalse(selectedFlag.get());
    Assertions.assertTrue(called.get());

    Segment otherSegment = Mockito.mock(Segment.class);
    selectedFlag.set(false);
    called.set(false);
    roadSystemController.toggleSegmentSelection(otherSegment);
    Assertions.assertFalse(selectedFlag.get());
    Assertions.assertFalse(called.get());
  }

  @Test
  public void testSelectSegmentsInRectangle() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);

    Connector segmentInRangeConnector = Mockito.mock(Connector.class);
    Mockito.when(segmentInRangeConnector.getPosition())
            .thenReturn(new Position(10, 10));
    Segment segmentInRange = Mockito.mock(Segment.class);

    Mockito.when(segmentInRange.getConnectors())
        .thenReturn(new RoseBox<Connector>(List.of(segmentInRangeConnector)));


    Connector segmentOutRangeConnector = Mockito.mock(Connector.class);
    Mockito.when(segmentInRangeConnector.getPosition())
        .thenReturn(new Position(20, 20));
    Segment segmentOutRange = Mockito.mock(Segment.class);

    Mockito.when(segmentOutRange.getConnectors())
        .thenReturn(new RoseBox<Connector>(List.of(segmentOutRangeConnector)));

    Mockito.doAnswer(invocation ->  {
      Assertions.assertEquals(segmentInRange, invocation.getArgument(0));
      called.set(true);
      return null;
    }).when(selectionBuffer).addSegmentSelection(Mockito.any(Segment.class));

    roadSystemController.selectSegmentsInRectangle(
        new Position(0, 0),
        new Position(15, 15));

    Assertions.assertTrue(called.get());
  }

  @Test
  public void testDragSegmentEnd() {

    /*final Position initialPosition = new Position(10, 10);
    final Position finalPosition = new Position(20, 20);

    Connector connector = Mockito.mock(Connector.class);

    roadSystemController.beginDragSegmentEnd(connector, initialPosition);
    roadSystemController.endDragSegmentEnd(finalPosition);

    Assertions.assertEquals(10, connector.);*/
  }
}
