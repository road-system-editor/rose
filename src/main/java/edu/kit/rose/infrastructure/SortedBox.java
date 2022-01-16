package edu.kit.rose.infrastructure;


/**
 * A SortedBox is a Read only container that contains sorted Objects of the Type T.
 *
 * @param <T> The Type of the Objects in the Box.
 */
public interface SortedBox<T> extends Box<T> {
  /**
   * Provides the Object of type T at the given index in the Box.
   *
   * @param index An int describing the index.
   * @return The Object of Type T that is at the given index in the Box.
   */
  T get(int index);
}
