package edu.kit.rose.infrastructure;

import java.util.Iterator;

/**
 * A standard implementation of a Box.
 *
 * @param <T> The Type of the Objects in the Box.
 */
public class SimpleBox<T> implements Box<T> {

  @Override
  public int getSize() {
    return 0;
  }

  @Override
  public Iterator<T> iterator() {
    return null;
  }
}
