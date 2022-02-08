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
import java.net.URL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
   * The Preview for the Segment.
   */
  private final ImageView view;

  /**
   * Creates a new segment blueprint for the given {@code type}.
   *
   * @param type the type of segment to display.
   */
  public SegmentBlueprint(LocalizedTextProvider translator, RoadSystemController controller,
                          SegmentType type) {
    this.controller = controller;
    this.type = type;
    setOnMouseClicked(this::handleDoubleClick);
    setOnDragDetected(this::handleOnDragDetected);
    getStylesheets().add("edu/kit/rose/view/panel/segmentbox/SegmentBoxListView.css");
    getStyleClass().add("segment-blueprint");
    setMaxHeight(HEIGHT);
    setMaxWidth(WIDTH);
    setTranslateX(TRANSLATE_X);
    URL baseUrl = this.getClass().getResource("base_segment_raw.png");
    //TODO: update pngs to be centered.
    URL exitUrl = this.getClass().getResource("exit_segment_raw.png");
    URL entranceUrl = this.getClass().getResource("entrance_segment_raw.png");
    Image image;
    switch (type) {
      case BASE -> {
        image = new Image(baseUrl.toString());
        view = new ImageView(image);
        view.setFitWidth(130);
        view.setFitHeight(50);
      }
      case EXIT -> {
        image = new Image(exitUrl.toString());
        view = new ImageView(image);
        view.setFitWidth(80);
        view.setFitHeight(90);
      }
      case ENTRANCE -> {
        image = new Image(entranceUrl.toString());
        view = new ImageView(image);
        view.setFitWidth(80);
        view.setFitHeight(90);
      }
      default -> throw new IllegalArgumentException("SegmentType doesnt exist");
    }


    this.getChildren().add(view);
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
    db.setDragView(this.view.snapshot(null, null));
    event.consume();
  }

  private void handleDoubleClick(MouseEvent event) {
    if (event.getClickCount() == DOUBLE_CLICK && !event.isConsumed()) {
      event.consume();
      this.controller.createStreetSegment(type);
    }
  }
}
