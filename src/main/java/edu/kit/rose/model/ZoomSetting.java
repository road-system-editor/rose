package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseUnitObservable;
import java.util.Objects;

/**
 * A ZoomSetting describes the Position and "level of Zoom" a possible View of the RoadSystem has.
 * It uses a Number to describe the amount of Zoom a view has and a {@link Position}
 * for the center of the current view.
 */
public class ZoomSetting extends RoseUnitObservable<ZoomSetting> {
  private static final double DEFAULT_ZOOM_LEVEL = 1.0;

  private final Position defaultCenterOfView;
  private Position centerOfView;
  private double zoomLevel;

  /**
   * Initializes a new zoom setting with the default zoom level and the given center position.
   */
  public ZoomSetting(Position defaultCenterOfView) {
    Objects.requireNonNull(defaultCenterOfView);
    this.defaultCenterOfView = new Position(defaultCenterOfView.getX(), defaultCenterOfView.getY());

    this.centerOfView = this.defaultCenterOfView;
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

  /**
   * Resets the zoom settings to the default values given in the constructor.
   */
  public void reset() {
    this.zoomLevel = DEFAULT_ZOOM_LEVEL;
    this.centerOfView = this.defaultCenterOfView;
  }

  @Override
  public ZoomSetting getThis() {
    return this;
  }
}
