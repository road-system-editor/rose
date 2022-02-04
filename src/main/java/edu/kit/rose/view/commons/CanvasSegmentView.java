package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Segment;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * The {@link CanvasSegmentView} class represents a {@link SegmentView} whose
 * visualization is done by drawing on a {@link Canvas}.
 *
 * @param <T> the type of segment to visualize
 */
public abstract class CanvasSegmentView<T extends Segment> extends SegmentView<T> {

  /**
   * The canvas on which the segment view is drawn.
   */
  private final Canvas canvas;

  private double startDragX;
  private double startDragY;

  /**
   * Creates a new canvas segment view that acts as visual representation of a given segment.
   *
   * @param segment    the segment to create a visual representation for
   * @param controller the {@link RoadSystemController} of the belonging road-system
   * @param translator the translator to provider localized strings
   */
  protected CanvasSegmentView(T segment,
                              RoadSystemController controller,
                              LocalizedTextProvider translator) {
    super(segment, controller, translator);

    canvas = new Canvas(getWidth(), getHeight());
    canvas.widthProperty().bind(this.widthProperty());
    canvas.heightProperty().bind(this.heightProperty());

    this.getChildren().add(canvas);

    this.setOnMousePressed(mouseEvent -> {
      startDragX = mouseEvent.getX();
      startDragY = mouseEvent.getY();

      mouseEvent.consume();
    });

    this.setOnDragDetected(mouseEvent -> {
      startFullDrag();
      mouseEvent.consume();
    });

    this.setOnMouseDragged(mouseEvent -> {
      this.setLayoutX(this.getLayoutX() + mouseEvent.getX() - startDragX);
      this.setLayoutY(this.getLayoutY() + mouseEvent.getY() - startDragY);
      mouseEvent.consume();

    });

    this.setOnMouseDragReleased(mouseEvent -> {
      Position position = new Position(
          (int) Math.round(mouseEvent.getX()),
          (int) Math.round(mouseEvent.getY()));

      mouseEvent.consume();
    });
  }

  @Override
  protected void redraw() {
    redraw(this.canvas.getGraphicsContext2D());
  }

  /**
   * Draws the segment on a given graphical context.
   *
   * @param context graphical context
   */
  protected abstract void redraw(GraphicsContext context);
}
