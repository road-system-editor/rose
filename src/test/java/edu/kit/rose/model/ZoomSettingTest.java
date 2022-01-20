package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link ZoomSetting}
 */
class ZoomSettingTest {
    private static final int X = 1;
    private static final int Y = 1;
    private static final int ZOOM_LEVEL = 1;

    @Test
    public void TestSetAndGet() {
        ZoomSetting setting = new ZoomSetting();
        Position position = new Position(X, Y);
        setting.setCenterOfView(position);
        setting.setZoomLevel(ZOOM_LEVEL);

        Assertions.assertEquals(X, setting.getCenterOfView().getX());
        Assertions.assertEquals(Y, setting.getCenterOfView().getY());
        Assertions.assertEquals(ZOOM_LEVEL, setting.getZoomLevel());
    }
}