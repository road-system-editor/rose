package edu.kit.rose.infrastructure;

/**
 * A Standard Implementation of a DualSetObservable.
 * A SetObservable is a Container that notifies its subscribers in case of a change to itself or
 * to the units held within. It holds two types of units.
 *
 * @param <T> the first type of Object that is held within.
 * @param <R> the second type of Object that is held within.
 * @param <S> the type of RoseDualSetObservable, should always be the same as the extending class.
 */
public abstract class RoseDualSetObservable<T, R, S>
        extends SubscriberManager<DualSetObserver<T, R, S>, S>
        implements DualSetObservable<T, R, S> {
}
