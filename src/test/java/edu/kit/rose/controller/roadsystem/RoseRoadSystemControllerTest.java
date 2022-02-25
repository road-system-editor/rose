package edu.kit.rose.controller.roadsystem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.command.RoseChangeCommandBuffer;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.RoseSelectionBuffer;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


/**
 * Test the {@link RoseRoadSystemController} class.
 */
public class RoseRoadSystemControllerTest {

  private SelectionBuffer selectionBuffer;
  private Project project;
  private RoadSystem roadSystem;
  private ZoomSetting zoomSetting;
  private ReplacementLog replacementLog;
  private RoadSystemController roadSystemController;

  /**
   * Sets up mock objects.
   */
  @BeforeEach
  private void setUp() {
    selectionBuffer = new RoseSelectionBuffer();
    project = mock(Project.class);
    roadSystem = new GraphRoadSystem(new CriteriaManager(), mock(TimeSliceSetting.class));
    zoomSetting = new ZoomSetting(new Position(0, 0));
    replacementLog = new ReplacementLog();

    Mockito.when(project.getZoomSetting()).thenReturn(zoomSetting);
    Mockito.when(project.getRoadSystem()).thenReturn(roadSystem);

    roadSystemController = new RoseRoadSystemController(
        new RoseChangeCommandBuffer(),
        new RoseStorageLock(),
        mock(Navigator.class),
        selectionBuffer,
        project,
        replacementLog);
  }

  @Test
  public void testSetZoomLevel() {
    roadSystemController.setZoomLevel(10.0);
    Assertions.assertEquals(10, zoomSetting.getZoomLevel());
    roadSystemController.setZoomLevel(11.0);
    Assertions.assertEquals(11, zoomSetting.getZoomLevel());
  }

  @Test
  public void testSetEditorPosition() {
    AtomicReference<Boolean> called = new AtomicReference<>(false);

    Position targetPosition = mock(Position.class);
    ZoomSetting zoomSetting = mock(ZoomSetting.class);
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
    RoadSystem roadSystem = mock(RoadSystem.class);
    Mockito.when(project.getRoadSystem()).thenReturn(roadSystem);
    Mockito.when(roadSystem.createSegment(SegmentType.BASE)).thenReturn(new Base());

    roadSystemController.createStreetSegment(SegmentType.BASE);
    verify(roadSystem, times(1))
        .createSegment(SegmentType.BASE);
  }

  @Test
  public void testDuplicateStreetSegment() {
    ChangeCommandBuffer changeCommandBuffer = mock(ChangeCommandBuffer.class);
    StorageLock storageLock = mock(StorageLock.class);
    Navigator navigator = mock(Navigator.class);
    ReplacementLog replacementLog = new ReplacementLog();
    RoadSystemController controller = new RoseRoadSystemController(
            changeCommandBuffer,
            storageLock,
            navigator,
            selectionBuffer,
            project,
            replacementLog);
    controller.duplicateStreetSegment();
    verify(changeCommandBuffer, times(1))
            .addAndExecuteCommand(any(DuplicateStreetSegmentCommand.class));
  }

  @Test
  public void testDeleteStreetSegment() {
    Segment segment = roadSystem.createSegment(SegmentType.BASE);
    roadSystemController.deleteStreetSegment(segment);
    Assertions.assertEquals(0, roadSystem.getElements().getSize());
  }


  @Test
  public void testStreetSegmentDragging() {
    Segment segment = roadSystem.createSegment(SegmentType.BASE);
    Position position = new Position(10, 10);
    selectionBuffer.addSegmentSelection(segment);
    roadSystemController.beginDragStreetSegment(segment.getCenter());
    roadSystemController.dragStreetSegments(new Movement(position.getX(), position.getY()));
    roadSystemController.endDragStreetSegment(segment.getCenter());
    Assertions.assertEquals(position, selectionBuffer.getSelectedSegments().get(0).getCenter());
  }

  @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
  @Test
  public void testStreetSegmentDraggingWithConnection() {
    Base segment1 = (Base) roadSystem.createSegment(SegmentType.BASE);
    selectionBuffer.addSegmentSelection(segment1);
    roadSystemController.beginDragStreetSegment(segment1.getCenter());
    roadSystemController.dragStreetSegments(new Movement(0, 0));
    Base segment2 = (Base) roadSystem.createSegment(SegmentType.BASE);
    roadSystemController.endDragStreetSegment(segment1.getCenter(), segment1.getExit());
    Assertions.assertTrue(roadSystem.getConnection(segment1.getExit())
            .getConnectors().contains(segment2.getExit()));
  }

  @Test
  public void testToggleSegmentSelection() {
    Segment segment = new Base();
    roadSystemController.toggleSegmentSelection(segment);
    Assertions.assertTrue(selectionBuffer.isSegmentSelected(segment));
    roadSystemController.toggleSegmentSelection(segment);
    Assertions.assertFalse(selectionBuffer.isSegmentSelected(segment));
  }

  @Test
  public void testAddSegmentSelection() {
    Segment segment = new Base();
    roadSystemController.addSegmentSelection(segment);
    Assertions.assertTrue(selectionBuffer.isSegmentSelected(segment));
  }

  @Test
  public void testRemoveSegmentSelection() {
    Segment segment = new Base();
    roadSystemController.addSegmentSelection(segment);
    Assertions.assertTrue(selectionBuffer.isSegmentSelected(segment));
    roadSystemController.removeSegmentSelection(segment);
    Assertions.assertFalse(selectionBuffer.isSegmentSelected(segment));
  }

  @Test
  public void clearSegmentSelection() {
    Segment segment1 = new Base();
    Segment segment2 = new Base();
    roadSystemController.addSegmentSelection(segment1);
    roadSystemController.addSegmentSelection(segment2);
    roadSystemController.clearSegmentSelection();
    Assertions.assertEquals(0, selectionBuffer.getSelectedSegments().size());
  }

  @Test
  public void testPutSegmentSelection() {
    Segment segment1 = new Base();
    Segment segment2 = new Base();
    Segment segment3 = new Base();
    roadSystemController.addSegmentSelection(segment1);
    roadSystemController.addSegmentSelection(segment2);
    roadSystemController.putSegmentSelection(segment3);
    Assertions.assertEquals(1, selectionBuffer.getSelectedSegments().size());
    Assertions.assertTrue(selectionBuffer.isSegmentSelected(segment3));
  }

  @Test
  public void testSelectSegmentsInRectangle() {
    Base segmentInRange = (Base) roadSystem.createSegment(SegmentType.BASE);
    roadSystem.createGroup(Set.of(segmentInRange));
    segmentInRange.move(new Movement(10, 0));
    Base segmentOutRange = (Base) roadSystem.createSegment(SegmentType.BASE);
    segmentOutRange.move(new Movement(100, 100));
    roadSystemController.selectSegmentsInRectangle(new Position(0, 0),
                                                   new Position(15, 15));

    Assertions.assertTrue(selectionBuffer.isSegmentSelected(segmentInRange));
    Assertions.assertFalse(selectionBuffer.isSegmentSelected(segmentOutRange));
  }

  @Test
  public void testDeleteStreetSegments() {
    Segment segmentInRange = roadSystem.createSegment(SegmentType.BASE);
    selectionBuffer.addSegmentSelection(segmentInRange);
    roadSystemController.deleteStreetSegments();
    Assertions.assertEquals(0, roadSystem.getElements().getSize());
  }

  @Disabled
  @Test
  public void testDragSegmentEnd() {
    Position position = new Position(0, 17);
    Base segment1 = (Base) roadSystem.createSegment(SegmentType.BASE);
    Base segment2 = (Base) roadSystem.createSegment(SegmentType.BASE);
    roadSystemController.beginDragSegmentEnd(segment1.getEntry(),
            segment1.getEntry().getPosition());
    roadSystemController.endDragSegmentEnd(position);
    roadSystemController.beginDragSegmentEnd(segment2.getExit(), segment2.getExit().getPosition());
    roadSystemController.endDragSegmentEnd(position);
    Assertions.assertEquals(position,
            replacementLog.getCurrentConnectorVersion(segment1.getEntry()).getPosition());
    Assertions.assertEquals(position,
            replacementLog.getCurrentConnectorVersion(segment2.getExit()).getPosition());
    Assertions.assertTrue(roadSystem.getConnection(segment1.getEntry())
            .getConnectors().contains(segment2.getExit()));
  }

  @Test
  public void testNotifies() {
    SetObserver<Segment, RoadSystemController> observer = mock(SetObserver.class);
    roadSystemController.addSubscriber(observer);
    roadSystemController.notifySubscribers();
    verify(observer, times(1)).notifyChange(roadSystemController);
    roadSystemController.removeSubscriber(observer);
    roadSystemController.notifySubscribers();
    verify(observer, times(1)).notifyChange(roadSystemController);
  }

  @Test
  public void testGetThis() {
    Assertions.assertSame(roadSystemController, roadSystemController.getThis());
  }

  @Test
  public void testGetIntersectionDistance() {
    Assertions.assertEquals(30, roadSystemController.getIntersectionDistance());
  }

  @Test
  public void testRotateSegment() {
    Segment segment = roadSystem.createSegment(SegmentType.BASE);
    roadSystemController.addSegmentSelection(segment);
    roadSystemController.rotateSegment();
    Assertions.assertEquals(15, segment.getRotation());
  }
}
