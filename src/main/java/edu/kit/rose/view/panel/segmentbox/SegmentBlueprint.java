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
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A segment blueprint represents a segment type by displaying an example of what the segment
 * could look like.
 * Clicking a blueprint will create a segment of the selected type in the editor.
 */
class SegmentBlueprint extends StackPane {
  private static final int TRANSLATE_X = 48;
  private static final int TRANSLATE_Y = 25;
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
  private final Circle renderer; //TODO: change back to SegmentView<? extends Segment>

  /**
   * Creates a new segment blueprint for the given {@code type}.
   *
   * @param type the type of segment to display.
   */
  public SegmentBlueprint(LocalizedTextProvider translator, RoadSystemController controller,
                          SegmentType type) {
    this.controller = controller;
    this.type = type;
    this.renderer = new Circle(30, Color.rgb((int) (Math.random() * 255),
        (int) (Math.random() * 255), (int) (Math.random() * 255))); //TODO: create segment preview
    setOnMouseClicked(this::handleDoubleClick);
    setOnDragDetected(this::handleOnDragDetected);
    setTranslateX(TRANSLATE_X);
    setTranslateY(TRANSLATE_Y);
    this.getChildren().add(renderer);
    Label segmentLabel = new Label(translator.getLocalizedText("segmentType." + type.toString()));
    segmentLabel.setTranslateY(-TRANSLATE_Y);
    this.getChildren().add(segmentLabel);
  }

  private Segment getSegmentDataForType(SegmentType type) {
    for (Segment s : SEGMENT_DATA) {
      if (s.getSegmentType() == type) {
        return s;
      }
    }
    return null;
  }

  void handleOnDragDetected(MouseEvent event) {
    Dragboard db = startDragAndDrop(TransferMode.ANY);
    ClipboardContent content = new ClipboardContent();
    content.putString(type.toString());
    db.setContent(content);
    db.setDragView(this.renderer.snapshot(null, null));
    event.consume();
  }

  void handleDoubleClick(MouseEvent event) {
    if (event.getClickCount() == DOUBLE_CLICK) {
      this.controller.createStreetSegment(type);
    }
  }
}
