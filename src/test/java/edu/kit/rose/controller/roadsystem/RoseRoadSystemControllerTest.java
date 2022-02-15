package edu.kit.rose.controller.roadsystem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.command.RoseChangeCommandBuffer;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;


/**
 * Test the {@link RoseRoadSystemController} class.
 */
public class RoseRoadSystemControllerTest {

  private SelectionBuffer selectionBuffer;
  private Project project;
  private RoadSystem roadSystem;

  private RoadSystemController roadSystemController;

  /**
   * Sets up mock objects.
   */
  @BeforeEach
  private void setUp() {
    selectionBuffer = Mockito.mock(SelectionBuffer.class);
    project = Mockito.mock(Project.class);
    roadSystem = Mockito.mock(RoadSystem.class);
    var zoomSetting = Mockito.mock(ZoomSetting.class);

    Mockito.when(zoomSetting.getCenterOfView()).thenReturn(new Position(0, 0));
    Mockito.when(project.getZoomSetting()).thenReturn(zoomSetting);
    Mockito.when(project.getRoadSystem()).thenReturn(roadSystem);

    ChangeCommandBuffer changeCommandBuffer = new RoseChangeCommandBuffer();
    StorageLock storageLock = Mockito.mock(StorageLock.class);
    Navigator navigator = Mockito.mock(Navigator.class);
    ReplacementLog replacementLog = new ReplacementLog();
    roadSystemController = new RoseRoadSystemController(
        changeCommandBuffer,
        storageLock,
        navigator,
        selectionBuffer,
        project,
        replacementLog);
  }

  @Test
  public void testSetZoomLevel() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);
    double[] targetResult = new double[] { 10.0 };

    ZoomSetting zoomSetting = Mockito.mock(ZoomSetting.class);
    Mockito.when(project.getZoomSetting()).thenReturn(zoomSetting);
    Mockito.doAnswer(invocation -> {
      Assertions.assertEquals(targetResult[0], (double) invocation.getArgument(0));
      targetResult[0]++;
      called.set(true);
      return null;
    }).when(zoomSetting).setZoomLevel(Mockito.anyDouble());

    roadSystemController.setZoomLevel(10.0);
    roadSystemController.setZoomLevel(11.0);
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
    }).when(zoomSetting).setCenterOfView(any(Position.class));

    roadSystemController.setEditorPosition(targetPosition);
    Assertions.assertTrue(called.get());
  }

  @Test
  public void testCreateStreetSegment() {
    RoadSystem roadSystem = Mockito.mock(RoadSystem.class);
    Mockito.when(project.getRoadSystem()).thenReturn(roadSystem);
    Mockito.when(roadSystem.createSegment(SegmentType.BASE)).thenReturn(new Base());

    roadSystemController.createStreetSegment(SegmentType.BASE);
    verify(roadSystem, times(1))
        .createSegment(SegmentType.BASE);
  }

  @Test
  public void testDeleteStreetSegment() {
    Segment segment = Mockito.mock(Segment.class);
    Mockito.when(roadSystem.getElements()).thenReturn(new RoseBox<>(List.of()));

    roadSystemController.deleteStreetSegment(segment);
    verify(roadSystem, times(1)).removeElement(segment);
  }


  @Test
  public void testStreetSegmentDragging() {
    final Position initialPosition = new Position(10, 10);
    final Position endPosition = new Position(20, 20);

    AtomicReference<Double> centerPositionX = new AtomicReference<>(0.0);
    AtomicReference<Double> centerPositionY = new AtomicReference<>(0.0);
    AtomicReference<Boolean> called = new AtomicReference<>(false);

    Segment segment = Mockito.mock(Segment.class);
    Mockito.doAnswer(invocation -> {
      Movement movement = invocation.getArgument(0);
      centerPositionX.set(centerPositionX.get() + movement.getX());
      centerPositionY.set(centerPositionY.get() + movement.getY());
      return null;
    }).when(segment).move(any(Movement.class));

    Mockito.when(segment.getCenter()).thenReturn(new Position(
        centerPositionX.get(),
        centerPositionY.get()));

    Mockito.doAnswer(invocation -> {
      Collection<Segment> segments = invocation.getArgument(0);
      called.set(true);
      return null;
    })
        .when(roadSystem)
        .moveSegments(ArgumentMatchers.anyCollection(), any(Movement.class));

    roadSystemController.beginDragStreetSegment(initialPosition);

    roadSystemController.endDragStreetSegment(endPosition);

    Assertions.assertTrue(called.get());
  }

  @Disabled("mock roadSystem does not offer functionality for moving anything")
  @Test
  public void testDragStreetSegments() {
    List<Segment> segments = new ArrayList<>();

    Mockito.when(this.selectionBuffer.getSelectedSegments()).thenReturn(segments);
    Mockito.doAnswer(invocation -> {
      if (!segments.contains(invocation.getArgument(0, Segment.class))) {
        segments.add(invocation.getArgument(0, Segment.class));
      }
      return null;
    }).when(this.selectionBuffer).addSegmentSelection(any(Segment.class));
    Mockito.doAnswer(invocation -> {
      segments.remove(invocation.getArgument(0, Segment.class));
      return null;
    }).when(this.selectionBuffer).removeSegmentSelection(any(Segment.class));

    Segment exit = new Exit();
    Segment entrance = new Entrance();
    int moveOffset = 10;
    entrance.move(new Movement(moveOffset, moveOffset));

    roadSystemController.addSegmentSelection(exit);
    roadSystemController.addSegmentSelection(entrance);

    Movement movement = new Movement(50, 50);
    roadSystemController.dragStreetSegments(movement);

    Assertions.assertEquals(movement.getX(), exit.getCenter().getX());
    Assertions.assertEquals(movement.getY(), exit.getCenter().getY());

    Assertions.assertEquals(movement.getX() + moveOffset, entrance.getCenter().getX());
    Assertions.assertEquals(movement.getY() +  moveOffset, entrance.getCenter().getY());

    roadSystemController.removeSegmentSelection(entrance);
    roadSystemController.dragStreetSegments(movement);

    Assertions.assertEquals(movement.getX() * 2, exit.getCenter().getX());
    Assertions.assertEquals(movement.getY() * 2, exit.getCenter().getY());
  }

  @Test
  public void testToggleSegmentSelection() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);
    AtomicReference<Boolean> selectedFlag = new AtomicReference<>(false);

    Segment segment = Mockito.mock(Segment.class);

    Mockito.doAnswer(invocation -> {
      if (segment == invocation.getArgument(0)) {
        called.set(true);
        selectedFlag.set(!selectedFlag.get());
      }
      return null;
    }).when(selectionBuffer).toggleSegmentSelection(any(Segment.class));

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

    //Configure segments that are in range
    Connector segmentInRangeConnector = Mockito.mock(Connector.class);
    Mockito.when(segmentInRangeConnector.getPosition())
            .thenReturn(new Position(10, 10));
    Segment segmentInRange = Mockito.mock(Segment.class);

    Mockito.when(segmentInRange.getConnectors())
        .thenReturn(new RoseBox<>(List.of(segmentInRangeConnector)));

    Mockito.when(segmentInRange.getCenter()).thenReturn(new Position(10, 0));

    //Configure segments that are out range
    Connector segmentOutRangeConnector = Mockito.mock(Connector.class);
    Mockito.when(segmentOutRangeConnector.getPosition())
        .thenReturn(new Position(20, 20));
    Segment segmentOutRange = Mockito.mock(Segment.class);

    Mockito.when(segmentOutRange.getConnectors())
        .thenReturn(new RoseBox<>(List.of(segmentOutRangeConnector)));

    Mockito.when(segmentOutRange.getCenter()).thenReturn(new Position(20, 30));

    //Configure selection buffer
    Mockito.doAnswer(invocation ->  {
      Assertions.assertEquals(segmentInRange, invocation.getArgument(0));
      called.set(true);
      return null;
    }).when(selectionBuffer).addSegmentSelection(any(Segment.class));

    //Configure getElements of Roadsystem
    Mockito.when(roadSystem.getElements())
        .thenReturn(new RoseBox<>(List.of(segmentInRange, segmentOutRange)));

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
