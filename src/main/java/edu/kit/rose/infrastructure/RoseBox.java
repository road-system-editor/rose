package edu.kit.rose.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A standard implementation of a {@link Box}.
 *
 * @param <T> The Type of the Objects in the Box.
 */
public class RoseBox<T> implements Box<T> {

  private final List<T> content;

  /**
   * Constructor.
   *
   * @param content the Elements that are supposed to be in the Box.
   */
  public RoseBox(Collection<T> content) {
    this.content = new ArrayList<>(content);
  }

  /**
   * Constructor.
   *
   * @param content the Elements that are supposed to be in the Box.
   */
  @SafeVarargs
  public RoseBox(T... content) {
    this.content = List.of(content);
  }

  @Override
  public int getSize() {
    return content.size();
  }

  @Override
  public boolean contains(T t) {
    return content.contains(t);
  }

  @Override
  public Iterator<T> iterator() {
    return Collections.unmodifiableCollection(content).iterator();
  }

  protected List<T> getContent() {
    return content;
  }
}
