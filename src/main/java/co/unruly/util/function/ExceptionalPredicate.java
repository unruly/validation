package co.unruly.util.function;

import java.util.Objects;

@FunctionalInterface
public interface ExceptionalPredicate<T, E extends Exception> {
    boolean test(T t) throws E;

    default ExceptionalPredicate<T, E> and(ExceptionalPredicate<? super T, ? extends E> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    default ExceptionalPredicate<T,E> negate() {
        return (t) -> !test(t);
    }

    default ExceptionalPredicate<T,E> or(ExceptionalPredicate<? super T, E> other) throws E {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }

    static <T, E extends Exception> ExceptionalPredicate<T, E> isEqual(Object targetRef) {
        return (null == targetRef)
                ? Objects::isNull
                : targetRef::equals;
    }
}
