package edu.kit.rose.infrastructure;


/**
 * Default implementation of the observable subscriber management logic.
 * @param <T> the type of {@link UnitObserver} that is allowed to observe this Observable
 * @param <S> the type of DualSetObservable, can be used as a 'normal' {@link Observable}
 */
abstract class SubscriberManager<T extends UnitObserver<S>,S> implements Observable<T,S> {

    @Override
    public void addSubscriber(T observer) {

    }

    @Override
    public void removeSubscriber(T observer) {

    }

    @Override
    public void notifySubscribers() {

    }
}
