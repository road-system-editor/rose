package edu.kit.rose.view.panel.segmentbox;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.commons.SegmentViewFactory;
import javafx.scene.layout.Pane;

/**
 * A segment blueprint represents a segment type by dislaying an example of what the segment could look like.
 * Clicking a blueprint will create a segment of the selected type in the editor.
 */
class SegmentBlueprint extends Pane {
    /**
     * These segments provide the data for the segment renderer.
     */
    private static final Segment[] SEGMENT_DATA = new Segment[]{
        new Base(),
        new Entrance(),
        new Exit()
    };


    private RoadSystemController controller;
    /**
     * The type of segment that this blueprint displays.
     */
    private SegmentType type;
    /**
     * The renderer for the given segment.
     */
    private SegmentView<? extends Segment> renderer;

    /**
     * Creates a new segment blueprint for the given type.
     * @param type
     */
    public SegmentBlueprint(LocalizedTextProvider translator, RoadSystemController controller, SegmentType type) {
        this.controller = controller;
        this.type = type;
        this.renderer = new SegmentViewFactory(translator, controller).createForSegment(getSegmentDataForType(type));
    }

    private Segment getSegmentDataForType(SegmentType type) {
        for (Segment s : SEGMENT_DATA) {
            if (s.getSegmentType() == type) {
                return s;
            }
        }
        return null;
    }
}
