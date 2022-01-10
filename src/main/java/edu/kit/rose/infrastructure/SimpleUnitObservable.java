package edu.kit.rose.infrastructure;

public class SimpleUnitObservable<T> extends SubscriberManager<UnitObserver<T>, T>
        implements UnitObservable<T> {
}
