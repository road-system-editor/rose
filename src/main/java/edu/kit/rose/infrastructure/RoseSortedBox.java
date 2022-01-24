package edu.kit.rose.infrastructure;

import java.util.Iterator;
import java.util.List;

/**
 * A standard implementation for a SortedBox.
 *
 * @param <T> The Type of the Objects in the Box.
 */
public class RoseSortedBox<T> implements SortedBox<T> {

  private final List<T> content;

  /**
   * Constructor.
   *
   * @param content the Elements that are supposed to be in the Box.
   */
  public RoseSortedBox(List<T> content) {
    this.content = content;
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
