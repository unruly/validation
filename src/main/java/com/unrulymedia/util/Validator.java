package com.unrulymedia.util;

import com.unrulymedia.util.function.ExceptionalFunction;
import com.unrulymedia.util.function.ExceptionalPredicate;
import com.unrulymedia.util.function.ExceptionalSupplier;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Validator<T,S> {
    private final List<T> value;
    private final Optional<S> error;

    private Validator() {
        throw new RuntimeException("don't do that");
    }

    private Validator(T value, S error) {
        this.value = Optional.ofNullable(value).map(Arrays::asList).orElse(Collections.<T>emptyList());
        this.error = Optional.ofNullable(error);
    }

    public static <U,V> Validator<U,V> success(U value) {
        return new Validator<>(Objects.requireNonNull(value), null);
    }

    public static <U,V> Validator<U,V> failure(V error) {
        return new Validator<>(null, error);
    }

    public static <U, V extends Exception> Validator<U,V> tryTo(ExceptionalSupplier<U,V> f) {
        try {
            return Validator.<U,V>success(f.get());
        } catch (Exception e) {
            return Validator.<U,V>failure((V)e);
        }
    }

    public static <U> Validator<U, NoSuchElementException> from(Optional<U> opt) {
        Validator<U, java.util.NoSuchElementException> failure = (Validator.failure(new NoSuchElementException()));
        return opt.map(Validator::<U, NoSuchElementException>success).orElse(failure);
    }


    public boolean isSuccess() {
        return !value.isEmpty();
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    public T get() {
        return Optional.ofNullable(value.get(0)).get();
    }

    public S getError() {
        return error.get();
    }

    public T orElse(T other) {
        return Optional.ofNullable(value.get(0)).orElse(other);

    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return Optional.ofNullable(value.get(0)).orElseThrow(exceptionSupplier);
    }

    public T orElseGet(Supplier<? extends T> other) {
        return Optional.ofNullable(value.get(0)).orElseGet(other);
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(value.get(0));
    }

    public <U> Validator<U, ?> map(ExceptionalFunction<? super T, ? extends U, ? extends Exception> mapper) {
        Objects.requireNonNull(mapper);
        if(isFailure()) {
            return new Validator<>(null,error);
        } else {
            try {
                U mapped = mapper.apply(Optional.ofNullable(value.get(0)).get());
                if(mapped == null) {
                    return failure(new NullPointerException());
                }
                return new Validator<>(mapped,null);
            } catch (Exception e) {
                return Validator.failure(e);
            }
        }
    }

    public <U> Validator<U, ?> flatMap(ExceptionalFunction<? super T, ? extends Validator<U, ?>, ? extends Exception> mapper) {
        Objects.requireNonNull(mapper);
        if(isFailure()) {
            return new Validator<>(null,error);
        } else {
            try {
                Validator<U, ?> mapped = mapper.apply(Optional.ofNullable(value.get(0)).get());
                return mapped == null ? Validator.failure(new NullPointerException()) : mapped;
            } catch (Exception e) {
                return Validator.failure(e);
            }
        }
    }

    public Validator<T, ?> filter(ExceptionalPredicate<? super T, ? extends Exception> predicate) {
        Objects.requireNonNull(predicate);
        if (isFailure()) {
            return this;
        } else {
            try {
                T actualValue = Optional.ofNullable(value.get(0)).get();
                return predicate.test(actualValue) ? this : failure(actualValue);
            } catch (Exception e) {
                return failure(e);
            }
        }
    }

    @Override
    public String toString() {
        return "com.unrulymedia.util.Validator{" +
                "value=" + value +
                ", error=" + error +
                '}';
    }

    public <V> Validator<List<V>, S> compose(Validator<V,S> other) {
        /*if(value.get() instanceof List) {
            Stream<V> concat = Stream.concat(
                    ((List<V>) value.get()).stream(),
                    Stream.of(other.get())
                    );
            List<V> collect = concat.collect(Collectors.toList());
            return Validator.<List<V>,S>success(collect);
        }
        Stream<V> stream = Stream.<V>of((V)value.get(), other.get());
        List<V> collect = stream.collect(Collectors.toList());
        return success(collect);*/
        return null;
    }

}
