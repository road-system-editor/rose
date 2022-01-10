package edu.kit.rose.view.panel.roadsystem;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import javafx.scene.control.ScrollPane;

/**
 * The zoomable ScrollPane is a ScrollPane that adds pan and zoom gesture support to its content.
 */
public class ZoomableScrollPane extends ScrollPane {
    private RoadSystemController roadSystemController;

    /**
     * Sets the RoadSystemController.
     * @param roadSystemController the RoadSystemController to assign to ZoomableScrollPane
     */
    public void setRoadSystemController(RoadSystemController roadSystemController) {
        this.roadSystemController = roadSystemController;
    }

}
