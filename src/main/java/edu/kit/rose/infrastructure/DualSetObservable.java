package edu.kit.rose.infrastructure;

/**
 * A SetObservable is a Container that notifies its subscribers in case of a change to itself or
 * to the units held within. It holds two types of units.
 * @param <T> the first type of Object that is held within.
 * @param <R> the second type of Object that is held within.
 * @param <S> the type of DualSetObservable, can be used as a 'normal' {@link Observable}
 */
public interface DualSetObservable<T,R,S> extends Observable<DualSetObserver<T,R,S>, S>{
}
