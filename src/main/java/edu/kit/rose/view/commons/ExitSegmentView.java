package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import javafx.scene.canvas.GraphicsContext;

class ExitSegmentView extends SegmentView<Exit> {
    ExitSegmentView(Exit segment, RoadSystemController controller, LocalizedTextProvider translator) {
        super(segment, controller, translator);
    }

    @Override
    protected void redraw(GraphicsContext graphicsContext) {

    }

    @Override
    public void notifyChange(Element unit) {

    }
}
