package edu.kit.rose.infrastructure;

/**
 * A UnitObservable notifies its subscribers in case of a change.
 * It provides a method to allow for explicit notification of all subscribers.
 *
 * @param <T> the type of Object that can be Observed.
 */
public interface UnitObservable<T> extends Observable<UnitObserver<T>, T> {
}
