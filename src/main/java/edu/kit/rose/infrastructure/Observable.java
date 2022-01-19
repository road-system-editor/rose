package edu.kit.rose.infrastructure;

/**
 * An Observable provides the methods that make it possible to add and remove subscribers
 * as well as to notify them.
 * Subscribers will get notified whenever the Observable undergoes a change.
 * An Observer is either an {@link UnitObserver} or an {@link SetObserver}.
 *
 * @param <T> the type of {@link UnitObserver} that is allowed to observe this Observable
 * @param <S> the type of DualSetObservable, can be used as a 'normal' {@link Observable}
 */
public interface Observable<T extends UnitObserver<S>, S> {

  /**
   * Adds a given Observer so that it will be notified by the Observable in case of change
   * to the Observable.
   *
   * @param observer The Observer that wants to be notified by this Observable.
   */
  void addSubscriber(T observer);

  /**
   * Removes a given Observer so that it will no longer be notified by the Observable.
   *
   * @param observer The Observer that does no longer want to be notified by this Observable.
   */
  void removeSubscriber(T observer);

  /**
   * Notifies all Observers. The Observable provides itself as the Parameter in the notify-call
   * of the Observer.
   */
  void notifySubscribers();

}
