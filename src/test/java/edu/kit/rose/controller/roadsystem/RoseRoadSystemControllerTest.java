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
  private RoadSystemController roadSystemController;

  /**
   * Sets up mock objects.
   */
  @BeforeEach
  void setUp() {
    selectionBuffer = new RoseSelectionBuffer();
    project = mock(Project.class);
    CriteriaManager criteriaManager = new CriteriaManager();
    roadSystem = new GraphRoadSystem(criteriaManager, mock(TimeSliceSetting.class));
    zoomSetting = new ZoomSetting(new Position(0, 0));
    final ReplacementLog replacementLog = new ReplacementLog();
    criteriaManager.setRoadSystem(roadSystem);

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
    Position targetPosition = new Position(0, 0);

    roadSystemController.setEditorPosition(targetPosition);
    Assertions.assertEquals(targetPosition, zoomSetting.getCenterOfView());
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

  @Disabled("need to adjust to relative coordinates")
  @Test
  public void testDragSegmentEnd() {
    Base segment1 = (Base) roadSystem.createSegment(SegmentType.BASE);
    Base segment2 = (Base) roadSystem.createSegment(SegmentType.BASE);
    System.out.println(segment1.getEntry().getPosition().getX());
    System.out.println(segment1.getEntry().getPosition().getY());
    System.out.println(segment2.getExit().getPosition().getX());
    System.out.println(segment2.getExit().getPosition().getY());
    Position position1 = new Position(segment1.getEntry().getPosition().getX(),
                            segment1.getEntry().getPosition().getY() + 17);
    //Position position2 = new Position()
    roadSystemController.beginDragSegmentEnd(segment1.getEntry(),
            segment1.getEntry().getPosition());
    roadSystemController.endDragSegmentEnd(position1);
    roadSystemController.beginDragSegmentEnd(segment2.getExit(), segment2.getExit().getPosition());
    roadSystemController.endDragSegmentEnd(position1);
    Assertions.assertEquals(position1, segment1.getEntry().getPosition());
    Assertions.assertEquals(position1, segment2.getExit().getPosition());
    Assertions.assertTrue(roadSystem.getConnection(segment1.getEntry())
            .getConnectors().contains(segment2.getExit()));
  }

  @Test
  public void testNotifies() {
    SetObserver<Segment, RoadSystemController> observer = mockObserver();
    roadSystemController.addSubscriber(observer);
    roadSystemController.notifySubscribers();
    verify(observer, times(1)).notifyChange(roadSystemController);
    roadSystemController.removeSubscriber(observer);
    roadSystemController.notifySubscribers();
    verify(observer, times(1)).notifyChange(roadSystemController);
  }

  /**
   * Helper method to extract the "unchecked" (but correct) cast of the observer mock.
   */
  @SuppressWarnings("unchecked")
  private static SetObserver<Segment, RoadSystemController> mockObserver() {
    return mock(SetObserver.class);
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
