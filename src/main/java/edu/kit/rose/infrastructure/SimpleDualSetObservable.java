package edu.kit.rose.infrastructure;

public class SimpleDualSetObservable<T,R,S> extends SubscriberManager<DualSetObserver<T,R,S>, S>
    implements DualSetObservable<T,R,S> {
}
