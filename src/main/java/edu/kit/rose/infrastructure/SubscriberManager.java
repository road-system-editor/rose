package edu.kit.rose.infrastructure;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Default implementation of the observable subscriber management logic.
 *
 * @param <T> the type of {@link UnitObserver} that is allowed to observe this Observable
 * @param <S> the type of Observable, should always be the same as the extending class.
 */
abstract class SubscriberManager<T extends UnitObserver<S>, S> implements Observable<T, S> {

  protected final Set<T> subscribers;

  public SubscriberManager() {
    subscribers = new HashSet<>();
  }

  @Override
  public void addSubscriber(T observer) {
    Objects.requireNonNull(observer, "observer may not be null");

    subscribers.add(observer);
  }

  @Override
  public void removeSubscriber(T observer) {
    subscribers.remove(observer);
  }

  @Override
  public void notifySubscribers() {
    subscribers.forEach(sub -> sub.notifyChange(getThis()));
  }

  protected Iterator<T> getSubscriberIterator() {
    return this.subscribers.iterator();
  }
}
