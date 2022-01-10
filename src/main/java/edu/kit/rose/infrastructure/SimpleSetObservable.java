package edu.kit.rose.infrastructure;

public class SimpleSetObservable<T,S> extends SubscriberManager<SetObserver<T,S>, S> implements
    SetObservable<T,S> {
}
