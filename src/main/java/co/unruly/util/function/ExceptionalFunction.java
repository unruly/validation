package co.unruly.util.function;

import java.util.Objects;

@FunctionalInterface
public interface ExceptionalFunction<T, R, E extends Exception> {
    R apply(T t) throws E;

    default <V> ExceptionalFunction<V, R, E> compose(ExceptionalFunction<? super V, ? extends T, ? extends E> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> ExceptionalFunction<T, V, E> andThen(ExceptionalFunction<? super R, ? extends V, ? extends E> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    static <T, E extends Exception> ExceptionalFunction<T, T, E> identity() {
        return t -> t;
    }
}
