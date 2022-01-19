package edu.kit.rose.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A standard implementation of a {@link Box}.
 *
 * @param <T> The Type of the Objects in the Box.
 */
public class SimpleBox<T> implements Box<T> {

  private final List<T> content;

  /**
   * Constructor.
   *
   * @param content the Elements that are supposed to be in the Box.
   */
  public SimpleBox(Collection<T> content) {
    this.content = new ArrayList<>(content);
  }

  @Override
  public int getSize() {
    return content.size();
  }

  @Override
  public Iterator<T> iterator() {
    return content.iterator();
  }
}
