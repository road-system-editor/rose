package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;

/**
 * A ZoomSetting describes the Position and "level of Zoom" a possible View of the RoadSystem has.
 * It uses a Number to describe the amount of Zoom a view has and a {@link Position}
 * for the center of the current view.
 */
public class ZoomSetting implements UnitObservable<ZoomSetting> {

  /**
   * Provides the Position of the center of the View.
   *
   * @return the Position of the center of the View.
   */
  Position getCenterOfView() {
    return null;
  }

  /**
   * Sets the new Position of the view.
   *
   * @param position the new Position of the view.
   */
  void setCenterOfView(Position position) {

  }

  /**
   * Provides the level of Zoom the View has.
   *
   * @return the level of Zoom the View has.
   */
  int getZoomLevel() {
    return 0;
  }

  /**
   * Sets the new level of zoom of the view.
   *
   * @param zoomLevel the new level of zoom of the view.
   */
  void setZoomLevel(int zoomLevel) {

  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public void addSubscriber(UnitObserver<ZoomSetting> observer) {

  }

  @Override
  public void removeSubscriber(UnitObserver<ZoomSetting> observer) {

  }
}
