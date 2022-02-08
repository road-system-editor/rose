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
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

/**
 * A segment blueprint represents a segment type by displaying an example of what the segment
 * could look like.
 * Clicking a blueprint will create a segment of the selected type in the editor.
 */
class SegmentBlueprint extends StackPane {
  private static final int HEIGHT = 90;
  private static final int WIDTH = 200;
  private static final int TRANSLATE_X = 10;
  private static final int DOUBLE_CLICK = 2;

  /**
   * These segments provide the data for the segment renderer.
   */
  private static final Segment[] SEGMENT_DATA = new Segment[] {
      new Base(),
      new Entrance(),
      new Exit()
  };


  private final RoadSystemController controller;
  /**
   * The type of segment that this blueprint displays.
   */
  private final SegmentType type;
  /**
   * The renderer for the given segment.
   */
  private final SegmentView<? extends Segment> renderer;

  /**
   * Creates a new segment blueprint for the given {@code type}.
   *
   * @param type the type of segment to display.
   */
  public SegmentBlueprint(LocalizedTextProvider translator, RoadSystemController controller,
                          SegmentType type) {
    this.controller = controller;
    this.type = type;
    this.renderer = new SegmentViewFactory(translator, controller).createForSegment(
        getSegmentDataForType(type));
    setOnMouseClicked(event -> handleDoubleClick(event));
    setOnDragDetected(event -> handleOnDragDetected(event));
    getStylesheets().add("edu/kit/rose/view/panel/segmentbox/SegmentBoxListView.css");
    getStyleClass().add("segment-blueprint");
    setMaxHeight(HEIGHT);
    setMaxWidth(WIDTH);
    setTranslateX(TRANSLATE_X);
    this.getChildren().add(renderer);
    this.renderer.draw();
  }

  private Segment getSegmentDataForType(SegmentType type) {
    for (Segment s : SEGMENT_DATA) {
      if (s.getSegmentType() == type) {
        return s;
      }
    }
    return null;
  }

  private void handleOnDragDetected(MouseEvent event) {
    Dragboard db = startDragAndDrop(TransferMode.ANY);
    ClipboardContent content = new ClipboardContent();
    content.putString(type.toString());
    db.setContent(content);
    db.setDragView(this.renderer.snapshot(null, null));
    event.consume();
  }

  private void handleDoubleClick(MouseEvent event) {
    if (event.getClickCount() == DOUBLE_CLICK) {
      this.controller.createStreetSegment(type);
    }
  }
}
