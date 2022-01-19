package edu.kit.rose.infrastructure;

/**
 * A Box is a read-only container that contains unsorted Objects of the Type T.
 *
 * @param <T> The Type of the Objects in the Box.
 */

public interface Box<T> extends Iterable<T> {

  /**
   * Provides an int describing the Number Objects in the Box.
   *
   * @return An int describing the Number Objects in the Box.
   */
  int getSize();

}
