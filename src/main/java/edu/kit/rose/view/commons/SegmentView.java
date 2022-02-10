package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
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
  private boolean isSelected;

  /**
   * Determines if the segment can drag itself.
   */
  private boolean isActive;

  /**
   * The roadSystemController to notify about drag movements.
   */
  protected final RoadSystemController controller;

  /**
   * The text provider for translated string.
   */
  private final LocalizedTextProvider translator;

  protected ConnectorView draggedConnectorView;

  protected Consumer<ConnectorView> onConnectorViewDragged;
  protected Consumer<ConnectorView> onConnectorViewDragEnd;

  private Position initialPos;
  private Point2D startPoint;


  /**
   * Creates a new segment view that acts as visual representation of a given segment.
   *
   * @param segment the segment to create a visual representation for
   */
  protected SegmentView(T segment, RoadSystemController controller,
                        LocalizedTextProvider translator) {
    this.segment = segment;
    this.isActive = true;
    this.controller = controller;
    this.translator = translator;

    this.widthProperty().addListener(e -> draw());
    this.heightProperty().addListener(e -> draw());

    setOnMouseClicked(Event::consume);
  }

  protected boolean canBeMoved(Node target, Movement movement) {
    final Bounds bounds = target.getLayoutBounds();
    final Point2D topLeft = localToParent(bounds.getMinX(), bounds.getMinY());
    final Point2D topRight = localToParent(bounds.getMaxX(), bounds.getMinY());
    final Point2D bottomLeft = localToParent(bounds.getMinX(), bounds.getMaxY());
    final Point2D bottomRight = localToParent(bounds.getMaxX(), bounds.getMaxY());
    final Point2D movementVector = new Point2D(movement.getX(), movement.getY());
    final Bounds gridBounds = getParent().getLayoutBounds();
    List<Supplier<Boolean>> checks = List.of(
        () -> gridBounds.contains(topLeft.add(movementVector)),
        () -> gridBounds.contains(topRight.add(movementVector)),
        () -> gridBounds.contains(bottomLeft.add(movementVector)),
        () -> gridBounds.contains(bottomRight.add(movementVector))
    );
    return checks.stream().allMatch(Supplier::get);
  }

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
    return isSelected;
  }

  /**
   * Sets if the segment view is drawn with a selection indicator.
   *
   * @param drawAsSelected determines if selection indicator is visible
   */
  public void setDrawAsSelected(boolean drawAsSelected) {
    this.isSelected = drawAsSelected;
  }

  /**
   * Returns whether the segment view can be interacted with.
   *
   * @return whether the segment view can be interacted with.
   */
  public boolean isActive() {
    return this.isActive;
  }

  /**
   * Sets whether the segment view can be interacted with.
   *
   * @param isActive whether the segment view can be interacted with.
   */
  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public void setOnConnectorViewDragged(Consumer<ConnectorView> onConnectorViewDragged) {
    this.onConnectorViewDragged = onConnectorViewDragged;
  }

  public void setOnConnectorViewDragEnd(Consumer<ConnectorView> onConnectorViewDragEnd) {
    this.onConnectorViewDragEnd = onConnectorViewDragEnd;
  }

  /**
   * Returns the localized text provider instance of the segment view.
   *
   * @return the localized text provider instance of the segment view.
   */
  protected LocalizedTextProvider getTranslator() {
    return translator;
  }

  protected void setDraggedConnectorView(ConnectorView connectorView) {
    this.draggedConnectorView = connectorView;
  }

  /**
   * Draws the segment on a given graphical context.
   *
   */
  protected abstract void redraw();

  /**
   * Provides the {@link ConnectorView}s of this segment view.
   *
   * @return the connector views
   */
  public abstract List<ConnectorView> getConnectorViews();
}
