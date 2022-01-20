package edu.kit.rose.infrastructure;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Default implementation of the observable subscriber management logic.
 *
 * @param <T> the type of {@link UnitObserver} that is allowed to observe this Observable
 * @param <S> the type of Observable, should always be the same as the extending class.
 */
abstract class SubscriberManager<T extends UnitObserver<S>, S> implements Observable<T, S> {

  private final List<T> subscribers;

  public SubscriberManager() {
    subscribers = new LinkedList<>();
  }

  @Override
  public void addSubscriber(T observer) {
    if (!subscribers.contains(observer)) {
      subscribers.add(observer);
    }
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
    return this.subscribers.listIterator();
  }
}
