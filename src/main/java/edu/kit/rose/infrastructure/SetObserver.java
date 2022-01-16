package edu.kit.rose.infrastructure;

/**
 * A SetObserver observes a set of type S that holds elements of type T.
 *
 * @param <T> the element type.
 * @param <S> the set type.
 */
public interface SetObserver<T, S> extends UnitObserver<S> {

  /**
   * To be called when a new Object is added to the set.
   *
   * @param unit the new Object that was added.
   */
  void notifyAddition(T unit);

  /**
   * To be called when an Object is removed from the set.
   *
   * @param unit the Object that was removed.
   */
  void notifyRemoval(T unit);
}
