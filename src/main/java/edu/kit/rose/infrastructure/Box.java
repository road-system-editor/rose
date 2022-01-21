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

  /**
   * Provides information about the content of a Box.
   *
   * @param t the object that is to be checked for its containment status
   * @return {@code true} if the object is contained in this Box. {@code false} otherwise.
   */
  boolean contains(T t);

}
