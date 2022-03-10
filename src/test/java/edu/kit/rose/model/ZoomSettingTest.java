package edu.kit.rose.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.kit.rose.infrastructure.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link ZoomSetting}.
 */
class ZoomSettingTest {
  private static final int X = 1;
  private static final int Y = 1;
  private static final int DEFAULT_ZOOM_LEVEL = 1;

  private Position defaultCenterOfView;
  private ZoomSetting zoomSetting;

  @BeforeEach
  void beforeEach() {
    this.defaultCenterOfView = new Position(X, Y);
    this.zoomSetting = new ZoomSetting(this.defaultCenterOfView);
  }

  @Test
  public void testConstructorChecksArguments() {
    assertThrows(NullPointerException.class, () -> new ZoomSetting(null));
  }

  @Test
  public void testConstructorCopiesArgument() {
    assertEquals(defaultCenterOfView, zoomSetting.getCenterOfView());

    // check whether zoom settings has actually copied the center of view
    defaultCenterOfView.setX(-1);
    assertEquals(X, zoomSetting.getCenterOfView().getX());
  }

  @Test
  public void testSetCenterOfView() {
    var position = new Position(103, -42);
    zoomSetting.setCenterOfView(position);
    assertEquals(position, zoomSetting.getCenterOfView());
  }

  @Test
  public void testGetCenterOfView() {
    zoomSetting.getCenterOfView().setX(-1);
    assertEquals(defaultCenterOfView, zoomSetting.getCenterOfView());
  }

  @Test
  public void testSetZoomLevel() {
    zoomSetting.setZoomLevel(7);
    assertEquals(7, zoomSetting.getZoomLevel());
  }

  @Test
  public void testReset() {
    zoomSetting.setCenterOfView(new Position(-3, 7));
    zoomSetting.setZoomLevel(10);
    zoomSetting.reset();
    assertEquals(defaultCenterOfView, zoomSetting.getCenterOfView());
    assertEquals(DEFAULT_ZOOM_LEVEL, zoomSetting.getZoomLevel());
  }

  @Test
  void testGetThis() {
    assertSame(zoomSetting, zoomSetting.getThis());
  }
}