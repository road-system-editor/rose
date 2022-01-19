package edu.kit.rose.infrastructure;

/**
 * A Standard Implementation of a UnitObservable.
 * A UnitObservable notifies its subscribers in case of a change.
 * It provides a method to allow for explicit notification of all subscribers.
 *
 * @param <T> the type of Object that can be Observed.
 */
public class SimpleUnitObservable<T> extends SubscriberManager<UnitObserver<T>, T>
    implements UnitObservable<T> {
}
