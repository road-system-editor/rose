package edu.kit.rose.controller.roadsystem;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.roadsystem.elements.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test the {@link RoseRoadSystemController} class.
 */
public class RoseRoadSystemControllerTest {

  private ChangeCommandBuffer changeCommandBuffer;
  private StorageLock storageLock;
  private SelectionBuffer selectionBuffer;
  private Project project;

  private RoadSystemController roadSystemController;

  private UnitObserver<ZoomSetting> observer;


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
    int[] targetResult = new int[] { 10 };

    ZoomSetting zoomSetting = Mockito.mock(ZoomSetting.class);
    Mockito.when(project.getZoomSetting()).thenReturn(zoomSetting);
    Mockito.doAnswer(invocation -> {
      Assertions.assertEquals(targetResult[0], (int) invocation.getArgument(0));
      targetResult[0]++;
      return null;
    }).when(zoomSetting).setZoomLevel(Mockito.anyInt());

    roadSystemController.setZoomLevel(10);
    roadSystemController.setZoomLevel(11);
  }

  @Test
  public void testSetEditorPosition() {
    Position targetPosition = Mockito.mock(Position.class);

    ZoomSetting zoomSetting = Mockito.mock(ZoomSetting.class);
    Mockito.when(project.getZoomSetting()).thenReturn(zoomSetting);
    Mockito.doAnswer(invocation -> {
      Assertions.assertEquals(targetPosition, invocation.getArgument(0));
      return null;
    }).when(zoomSetting).setCenterOfView(Mockito.any(Position.class));

    roadSystemController.setEditorPosition(targetPosition);
  }

  @Test
  public void testCreateStreetSegment() {
  }

  @Test
  public void testDeleteStreetSegment() {

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
