package edu.kit.rose.infrastructure;

import java.util.Iterator;
import java.util.List;

/**
 * A standard implementation for a SortedBox.
 *
 * @param <T> The Type of the Objects in the Box.
 */
public class RoseSortedBox<T> extends RoseBox<T> implements SortedBox<T> {

  /**
   * Constructor.
   *
   * @param content the Elements that are supposed to be in the Box.
   */
  public RoseSortedBox(List<T> content) {
    super(content);
  }

  /**
   * Constructor.
   *
   * @param content the Elements that are supposed to be in the Box.
   */
  @SafeVarargs
  public RoseSortedBox(T... content) {
    super(content);
  }

  @Override
  public T get(int index) {
    return getContent().get(index);
  }

  @Override
  public Iterator<T> iterator() {
    return getContent().iterator();
  }
}
