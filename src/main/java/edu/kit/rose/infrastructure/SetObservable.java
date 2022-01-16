package edu.kit.rose.infrastructure;

/**
 * A SetObservable is a Container that notifies its subscribers in case of a change to itself or
 * to the units held within.
 *
 * @param <T> the type of Object that can be Observed.
 * @param <S> the type of DualSetObservable, can be used as a 'normal' {@link Observable}
 */
public interface SetObservable<T, S> extends Observable<SetObserver<T, S>, S> {
}
