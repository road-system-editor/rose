package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

/**
 * The {@link SegmentView} is the base class for the visual representation of a street segment.
 *
 * @param <T> the segment type the segment view represents
 * @implNote A {@link SegmentView} is supposed to be placed on a managed pane. It then draws itself
 * on that pane.
 */
public abstract class SegmentView<T extends Segment> extends Pane implements UnitObserver<Element> {

  /**
   * The segment which this segment view represents
   */
  private T segment;

  /**
   * The canvas on which the segment view is drawn
   */
  private Canvas canvas;

  /**
   * Determines if segment view is drawn with a selection indicator
   */
  private boolean drawAsSelected;

  /**
   * Determines if the segment can drag itself
   */
  private boolean isDraggable;

  /**
   * The roadSystemController to notify about drag movements
   */
  private RoadSystemController controller;

  /**
   * The text provider for translated string
   */
  private LocalizedTextProvider translator;

  /**
   * Creates a new segment view that acts as visual representation of a given segment
   *
   * @param segment the segment to create a visual representation for
   */
  protected SegmentView(T segment, RoadSystemController controller,
                        LocalizedTextProvider translator) {
    this.segment = segment;
    this.isDraggable = true;
    this.controller = controller;
    this.translator = translator;
    canvas = new Canvas(getWidth(), getHeight());
    canvas.widthProperty().bind(this.widthProperty());
    canvas.heightProperty().bind(this.heightProperty());

    this.widthProperty().addListener(e -> draw());
    this.heightProperty().addListener(e -> draw());

    this.getChildren().add(canvas);

  }

  /**
   * Returns the segment that is represented by the segment view
   *
   * @return represented stegment
   */
  public T getSegment() {
    return segment;
  }

  /**
   * Draws the segment
   */
  public void draw() {
    redraw(canvas.getGraphicsContext2D());
  }

  /**
   * Returns a boolean that indicates if the segment view is drawn with a selection indicator.
   *
   * @return boolean that indicates if segment is drawn with a selection indicator
   */
  public boolean getDrawAsSelected() {
    return drawAsSelected;
  }

  /**
   * Sets if the segment view is drawn with a selection indicator.
   *
   * @param drawAsSelected determines if selection indicator is visible
   */
  public void setDrawAsSelected(boolean drawAsSelected) {
    this.drawAsSelected = drawAsSelected;
  }

  /**
   * Returns whether the segment view can drag itself.
   *
   * @return boolean if the segment view can drag itself
   */
  public boolean getIsDraggable() {
    return isDraggable;
  }

  /**
   * Sets whether the segment view can drag itself.
   *
   * @param isDraggable boolean if the segment view can drag itself
   */
  public void setIsDraggable(boolean isDraggable) {
    this.isDraggable = isDraggable;
  }

  /**
   * Returns the localized text provider instance of the segment view.
   *
   * @return localizedTextProvider instance.
   */
  protected LocalizedTextProvider getTranslator() {
    return translator;
  }

  /**
   * Draws the segment on a given graphical context
   *
   * @param context graphical context
   */
  protected abstract void redraw(GraphicsContext context);
}
