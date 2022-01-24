package edu.kit.rose.infrastructure;

import java.util.Iterator;
import java.util.List;

/**
 * A standard implementation for a SortedBox.
 *
 * @param <T> The Type of the Objects in the Box.
 */
public class SimpleSortedBox<T> extends SimpleBox<T> implements SortedBox<T> {


  /**
   * Constructor.
   *
   * @param content the Elements that are supposed to be in the Box.
   */
  public SimpleSortedBox(List<T> content) {
    super(content);
  }

  /**
   * Constructor.
   *
   * @param content the Elements that are supposed to be in the Box.
   */
  @SafeVarargs
  public SimpleSortedBox(T... content) {
    super(content);
  }

  @Override
  public T get(int index) {
    return content.get(index);
  }

  @Override
  public Iterator<T> iterator() {
    return content.iterator();
  }
}
