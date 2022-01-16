package edu.kit.rose.infrastructure;

/**
 * A UnitObserver is an Observer for a single Object.
 *
 * @param <T> the type of Element to observe
 */
public interface UnitObserver<T> {

  /**
   * Called to inform the Observer of changes to an Observable.
   *
   * @param unit the observed Object that changed.
   */
  void notifyChange(T unit);

}
