package com.unrulymedia.util;

import com.unrulymedia.util.function.ExceptionalFunction;
import com.unrulymedia.util.function.ExceptionalPredicate;
import com.unrulymedia.util.function.ExceptionalSupplier;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public final class Validation<T,S> {
    private final Optional<T> value;
    private final List<S> errors;

    private Validation() {
        throw new RuntimeException("don't do that");
    }

    private Validation(T value, List<S> errors) {
        this.value = Optional.ofNullable(value);
        this.errors = errors;
    }

    public static <U,V> Validation<U,V> success(U value) {
        return new Validation<>(Objects.requireNonNull(value), Collections.<V>emptyList());
    }

    public static <U,V> Validation<U,V> failure(V error) {
        return new Validation<>(null, asList(error));
    }

    public static <U,V> Validation<U,V> failure(List<V> error) {
        return new Validation<>(null, error);
    }

    @SuppressWarnings("unchecked")
    public static <U, V extends Exception> Validation<U,V> tryTo(ExceptionalSupplier<U,V> f) {
        try {
            return Validation.<U,V>success(f.get());
        } catch (Exception e) {
            return Validation.<U,V>failure((V) e);
        }
    }

    public static <U> Validation<U, NoSuchElementException> from(Optional<U> opt) {
        Validation<U, NoSuchElementException> failure = (Validation.failure(new NoSuchElementException()));
        return opt.map(Validation::<U, NoSuchElementException>success).orElse(failure);
    }


    public boolean isSuccess() {
        return value.isPresent();
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    public T get() {
        return value.get();
    }

    public List<S> getErrors() {
        return errors;
    }

    public T orElse(T other) {
        return value.orElse(other);

    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return value.orElseThrow(exceptionSupplier);
    }

    public T orElseGet(Supplier<? extends T> other) {
        return value.orElseGet(other);
    }

    public Optional<T> toOptional() {
        return value;
    }

    public <U> Validation<U, ?> map(ExceptionalFunction<? super T, ? extends U, ? extends Exception> mapper) {
        Objects.requireNonNull(mapper);
        if(isFailure()) {
            return new Validation<>(null, errors);
        } else {
            try {
                U mapped = mapper.apply(value.get());
                if(mapped == null) {
                    return failure(new NullPointerException());
                }
                return new Validation<>(mapped,null);
            } catch (Exception e) {
                return Validation.failure(e);
            }
        }
    }

    public <U> Validation<U, ?> flatMap(ExceptionalFunction<? super T, ? extends Validation<U, ?>, ? extends Exception> mapper) {
        Objects.requireNonNull(mapper);
        if(isFailure()) {
            return new Validation<>(null, errors);
        } else {
            try {
                Validation<U, ?> mapped = mapper.apply(value.get());
                return mapped == null ? Validation.failure(new NullPointerException()) : mapped;
            } catch (Exception e) {
                return Validation.failure(e);
            }
        }
    }

    public Validation<T, ?> filter(ExceptionalPredicate<? super T, ? extends Exception> predicate) {
        Objects.requireNonNull(predicate);
        if (isFailure()) {
            return this;
        } else {
            try {
                T actualValue = value.get();
                return predicate.test(actualValue) ? this : failure(actualValue);
            } catch (Exception e) {
                return failure(e);
            }
        }
    }

    public Validation<T,S> compose(Validation<T,S> that, BiFunction<T,T,T> f) {
        if(this.isFailure() && that.isFailure()) {
            List<S> composedErrors = Stream.concat(this.getErrors().stream(),that.getErrors().stream()).collect(Collectors.toList());
            return failure(composedErrors);
        }
        if(that.isFailure()) {
            return that;
        }
        if(this.isFailure()) {
            return this;
        }
        return Validation.success(f.apply(this.get(), that.get()));
    }

    public interface IntegerValidationSupplier<E> extends Supplier<Validation<Integer,E>> {}
    @SafeVarargs
    public static <E> Validation<Integer,E> compose(IntegerValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce((v1,v2) -> v1.compose(v2, (a,b) -> a + b))
                .get();
    }

    public interface LongValidationSupplier<E> extends Supplier<Validation<Long,E>> {}
    @SafeVarargs
    public static <E> Validation<Long,E> compose(LongValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce((v1, v2) -> v1.compose(v2, (a, b) -> a + b))
                .get();
    }

    public interface FloatValidationSupplier<E> extends Supplier<Validation<Float,E>> {}
    @SafeVarargs
    public static <E> Validation<Float,E> compose(FloatValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce((v1, v2) -> v1.compose(v2, (a, b) -> a + b))
                .get();
    }

    public interface DoubleValidationSupplier<E> extends Supplier<Validation<Double,E>> {}
    @SafeVarargs
    public static <E> Validation<Double,E> compose(DoubleValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce((v1, v2) -> v1.compose(v2, (a, b) -> a + b))
                .get();
    }

    public interface BooleanValidationSupplier<E> extends Supplier<Validation<Boolean,E>> {}
    @SafeVarargs
    public static <E> Validation<Boolean,E> compose(BooleanValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce((v1, v2) -> v1.compose(v2, (a, b) -> a && b))
                .get();
    }

    public interface StringValidationSupplier<E> extends Supplier<Validation<String,E>> {}
    @SafeVarargs
    public static <E> Validation<String,E> compose(StringValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce((v1, v2) -> v1.compose(v2, (a, b) -> a + b))
                .get();
    }

    public interface ListValidationSupplier<T,E> extends Supplier<Validation<List<T>,E>> {}
    @SafeVarargs
    public static <T,E> Validation<List<T>,E> compose(ListValidationSupplier<T,E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce((v1, v2) -> v1.compose(v2, (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toList())))
                .get();
    }

    public interface SetValidationSupplier<T,E> extends Supplier<Validation<Set<T>,E>> {}
    @SafeVarargs
    public static <T,E> Validation<Set<T>,E> compose(SetValidationSupplier<T,E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce((v1, v2) -> v1.compose(v2, (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toSet())))
                .get();
    }

    public interface MapValidationSupplier<K,V,E> extends Supplier<Validation<Map<K,V>,E>> {}
    @SafeVarargs
    public static <K,V,E> Validation<Map<K,V>,E> compose(MapValidationSupplier<K,V,E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce((v1, v2) -> v1.compose(v2, (a, b) -> Stream.of(a, b).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c, d) -> d))))
                .get();
    }

    @Override
    public String toString() {
        return "com.unrulymedia.util.Validator{" +
                "value=" + value +
                ", errors=" + errors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Validation that = (Validation) o;

        return errors.equals(that.errors) && value.equals(that.value);

    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + errors.hashCode();
        return result;
    }
}
