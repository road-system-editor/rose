package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

/**
 * The {@link SegmentView} is the base class for the visual representation of a street segment.
 * It is responsible for drawing itself and receiving drag and drop operations.
 *
 * @param <T> the segment type the segment view represents.
 * @implNote A {@link SegmentView} is supposed to be placed on a managed pane.
 *     It then draws itself on that pane.
 */
public abstract class SegmentView<T extends Segment> extends Pane
    implements SetObserver<Element, Element> {

  /**
   * The segment which this segment view represents.
   */
  private final T segment;

  /**
   * Determines if segment view is drawn with a selection indicator.
   */
  private boolean drawAsSelected;

  /**
   * Determines if the segment can drag itself.
   */
  private boolean isDraggable;

  /**
   * The roadSystemController to notify about drag movements.
   */
  private final RoadSystemController controller;

  /**
   * The text provider for translated string.
   */
  private final LocalizedTextProvider translator;

  /**
   * Creates a new segment view that acts as visual representation of a given segment.
   *
   * @param segment the segment to create a visual representation for
   */
  protected SegmentView(T segment, RoadSystemController controller,
                        LocalizedTextProvider translator) {
    this.segment = segment;
    this.isDraggable = true;
    this.controller = controller;
    this.translator = translator;

    this.widthProperty().addListener(e -> draw());
    this.heightProperty().addListener(e -> draw());

    this.setOnMousePressed(mouseEvent -> {
      //posOnSourceX = mouseEvent.getX();
      //posOnSourceY = mouseEvent.getY();
      startPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
    });

    this.setOnDragDetected(mouseEvent -> startFullDrag());

    this.setOnMouseDragged(mouseEvent -> {
      /*System.out.print("\nX: ");
      System.out.print(this.getLayoutX() + mouseEvent.getX() - posOnSourceX);
      System.out.print("\nY: ");
      System.out.print(this.getLayoutY() + mouseEvent.getY() - posOnSourceY);

      this.setLayoutX(this.getLayoutX() + mouseEvent.getX() - posOnSourceX);
      this.setLayoutY(this.getLayoutY() + mouseEvent.getY() - posOnSourceY);*/
      var currentPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
      var movement = new Movement(currentPos.getX() - startPos.getX(),
          currentPos.getY() - startPos.getY());
      if (canBeMoved(movement)) {
        segment.move(movement);
        startPos = currentPos;
      }
      mouseEvent.consume();
    });

    this.setOnMouseDragReleased(mouseEvent -> {
      /*var currentPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
      var movement = new Movement(
          (int) Math.round(currentPos.getX() - startPos.getX()),
          (int) Math.round(currentPos.getY() - startPos.getY())
      );
      segment.move(movement);*/
    });
  }

  private boolean canBeMoved(Movement movement) {
    final var bounds = getLayoutBounds();
    final var topLeft = localToParent(bounds.getMinX(), bounds.getMinY());
    final var topRight = localToParent(bounds.getMaxX(), bounds.getMinY());
    final var bottomLeft = localToParent(bounds.getMinX(), bounds.getMaxY());
    final var bottomRight = localToParent(bounds.getMinX(), bounds.getMinY());
    final var movementVector = new Point2D(movement.getX(), movement.getY());
    final var gridBounds = getParent().getLayoutBounds();
    List<Supplier<Boolean>> checks = List.of(
        () -> gridBounds.contains(topLeft.add(movementVector)),
        () -> gridBounds.contains(topRight.add(movementVector)),
        () -> gridBounds.contains(bottomLeft.add(movementVector)),
        () -> gridBounds.contains(bottomRight.add(movementVector))
    );
    return checks.stream().allMatch(Supplier::get);
  }

  private double posOnSourceX;
  private double posOnSourceY;
  private Point2D startPos;

  /**
   * Returns the segment that is represented by the segment view.
   *
   * @return the segment that is represented by the segment view.
   */
  public T getSegment() {
    return segment;
  }

  /**
   * Draws the segment.
   */
  public void draw() {
    redraw();
  }

  /**
   * Returns whether the segment view is drawn with a selection indicator.
   *
   * @return whether the segment view is drawn with a selection indicator.
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
   * @return whether the segment view can drag itself.
   */
  public boolean getIsDraggable() {
    return isDraggable;
  }

  /**
   * Sets whether the segment view can drag itself.
   *
   * @param isDraggable whether the segment view can drag itself.
   */
  public void setIsDraggable(boolean isDraggable) {
    this.isDraggable = isDraggable;
  }

  /**
   * Returns the localized text provider instance of the segment view.
   *
   * @return the localized text provider instance of the segment view.
   */
  protected LocalizedTextProvider getTranslator() {
    return translator;
  }

  /**
   * Draws the segment on a given graphical context.
   *
   */
  protected abstract void redraw();
}
