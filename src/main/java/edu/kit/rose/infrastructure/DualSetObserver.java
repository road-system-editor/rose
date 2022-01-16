package edu.kit.rose.infrastructure;


/**
 * A DualSetObserver observes a set of type S that holds both units of type T and units of type R.
 *
 * @param <T> the first unit type.
 * @param <R> the second unit type.
 * @param <S> the set type.
 */
public interface DualSetObserver<T, R, S> extends SetObserver<T, S> {
  /**
   * To be called when a new Object is added to the set.
   *
   * @param unit the new Object that was added.
   */
  void notifyAdditionSecond(R unit);

  /**
   * To be called when an Object is removed from the set.
   *
   * @param unit the Object that was removed.
   */
  void notifyRemovalSecond(R unit);
}
