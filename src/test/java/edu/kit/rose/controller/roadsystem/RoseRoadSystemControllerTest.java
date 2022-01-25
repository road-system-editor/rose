package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
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
  private SelectionBuffer selectionBuffer;
  private Project project;

  private RoadSystemController roadSystemController;


  @BeforeEach
  private void setUp() {
    changeCommandBuffer = Mockito.mock(ChangeCommandBuffer.class);
    storageLock = Mockito.mock(StorageLock.class);
    selectionBuffer = Mockito.mock(SelectionBuffer.class);
    project = Mockito.mock(Project.class);


    roadSystemController = new RoseRoadSystemController(
        changeCommandBuffer,
        storageLock,
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
  public void testBeginDragStreetSegment() {

  }

  @Test
  public void testEndDragStreetSegment() {

  }

  @Test
  public void testDragStreetSegment() {

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

  }

  @Test
  public void testBeginDragSegmentEnd() {

  }

  @Test
  public void testEndDragSegmentEnd() {

  }

  @Test
  public void testDragSegmentEnd() {

  }
}
