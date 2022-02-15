package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseUnitObservable;

/**
 * A ZoomSetting describes the Position and "level of Zoom" a possible View of the RoadSystem has.
 * It uses a Number to describe the amount of Zoom a view has and a {@link Position}
 * for the center of the current view.
 */
public class ZoomSetting extends RoseUnitObservable<ZoomSetting> {
  private static final double DEFAULT_ZOOM_LEVEL = 1.0;

  private Position centerOfView;
  private double zoomLevel;

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
  public void setZoomLevel(double zoomLevel) {
    this.zoomLevel = zoomLevel;
    notifySubscribers();
  }

  @Override
  public ZoomSetting getThis() {
    return this;
  }
}
