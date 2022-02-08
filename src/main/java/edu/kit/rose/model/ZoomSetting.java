package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * A ZoomSetting describes the Position and "level of Zoom" a possible View of the RoadSystem has.
 * It uses a Number to describe the amount of Zoom a view has and a {@link Position}
 * for the center of the current view.
 */
public class ZoomSetting implements UnitObservable<ZoomSetting> {
  private static final double DEFAULT_ZOOM_LEVEL = 1.0;

  private Position centerOfView = new Position();
  private double zoomLevel = 1;
  private final List<UnitObserver<ZoomSetting>> observers = new ArrayList<>();

  public ZoomSetting(Position centerOfView) {
    this.centerOfView = centerOfView;
    this.zoomLevel = DEFAULT_ZOOM_LEVEL;
  }

  /**
   * Provides the Position of the center of the View.
   *
   * @return the Position of the center of the View.
   */
  public Position getCenterOfView() {
    return new Position(this.centerOfView.getX(), this.centerOfView.getY());
  }

  /**
   * Sets the new Position of the view.
   *
   * @param position the new Position of the view.
   */
  public void setCenterOfView(Position position) {
    this.centerOfView = position;
    notifySubscribers();
  }

  /**
   * Provides the level of Zoom the View has.
   *
   * @return the level of Zoom the View has.
   */
  public double getZoomLevel() {
    return this.zoomLevel;
  }

  /**
   * Sets the new level of zoom of the view.
   *
   * @param zoomLevel the new level of zoom of the view.
   */
  void setZoomLevel(int zoomLevel) {
    this.zoomLevel = zoomLevel;
    notifySubscribers();
  }

  @Override
  public void notifySubscribers() {
    observers.forEach(e -> e.notifyChange(this));
  }

  @Override
  public ZoomSetting getThis() {
    return this;
  }

  @Override
  public void addSubscriber(UnitObserver<ZoomSetting> observer) {
    if (!this.observers.contains(observer)) {
      this.observers.add(observer);
    }
  }

  @Override
  public void removeSubscriber(UnitObserver<ZoomSetting> observer) {
    this.observers.remove(observer);
  }
}
