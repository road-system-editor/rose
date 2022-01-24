package edu.kit.rose.infrastructure;

/**
 * A Standard Implementation of a SetObservable.
 * A SetObservable is a Container that notifies its subscribers in case of a change to itself or
 * to the units held within.
 *
 * @param <T> the type of Object that can be Observed.
 * @param <S> the type of RoseSetObservable, should always be the same as the extending class.
 */
public abstract class RoseSetObservable<T, S>
        extends SubscriberManager<SetObserver<T, S>, S> implements SetObservable<T, S> {
}
