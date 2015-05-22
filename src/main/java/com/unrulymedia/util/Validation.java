package com.unrulymedia.util;

import com.unrulymedia.util.function.ExceptionalFunction;
import com.unrulymedia.util.function.ExceptionalPredicate;
import com.unrulymedia.util.function.ExceptionalSupplier;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public final class Validation<T,S> {
    private final Optional<T> value;
    private final List<S> errors;


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
        Validation<U, NoSuchElementException> failure = (failure(new NoSuchElementException()));
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

    public Stream<T> stream() {
        if(isSuccess()) {
            return Stream.of(value.get());
        } else {
            return Stream.<T>empty();
        }
    }

    public <U> Validation<U, ?> tryMap(ExceptionalFunction<? super T, ? extends U, ? extends Exception> mapper) {
        try {
            return map((a) -> {
                try {
                    return mapper.apply(a);
                } catch (Exception e) {
                    throw new MapException(e);
                }
            });
        } catch (MapException e) {
            return failure(e.getCause());
        }
    }

    public <U> Validation<U, S> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if(isFailure()) {
            return new Validation<>(null, errors);
        } else {
            U mapped = mapper.apply(value.get());
            return new Validation<>(mapped,null);
        }
    }

    public <U> Validation<U, ?> tryFlatMap(ExceptionalFunction<? super T, ? extends Validation<U, S>, ? extends Exception> mapper) {
        try {
            return flatMap((a) -> {
                try {
                    return mapper.apply(a);
                } catch (Exception e) {
                    throw new MapException(e);
                }
            });
        } catch (MapException e) {
            return failure(e.getCause());
        }
    }

    public <U> Validation<U, S> flatMap(Function<? super T, ? extends Validation<U, S>> mapper) {
        Objects.requireNonNull(mapper);
        if(isFailure()) {
            return new Validation<>(null, errors);
        } else {
            return  mapper.apply(value.get());
        }
    }

    public Validation<T, ?> tryFilter(ExceptionalPredicate<? super T, ? extends Exception> predicate) {
        try {
            return filter((a) -> {
                try {
                    return predicate.test(a);
                } catch (Exception e) {
                    throw new MapException(e);
                }
            });
        } catch (MapException e) {
            return failure(e.getCause());
        }
    }

    public Validation<T, S> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (isFailure()) {
            return this;
        } else {
            T actualValue = value.get();
            return predicate.test(actualValue) ? this : failure(asList());
        }
    }

    public Validation<T,S> compose(Validation<T,S> that, BinaryOperator<T> f) {
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
        return success(f.apply(this.get(), that.get()));
    }

    public interface IntegerValidationSupplier<E> extends Supplier<Validation<Integer,E>> {}
    @SafeVarargs
    public static <E> Validation<Integer,E> compose(IntegerValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce(success(0), (v1, v2) -> v1.compose(v2, Integer::sum));
    }

    public interface LongValidationSupplier<E> extends Supplier<Validation<Long,E>> {}
    @SafeVarargs
    public static <E> Validation<Long,E> compose(LongValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce(success(0L), (v1, v2) -> v1.compose(v2, Long::sum));
    }

    public interface FloatValidationSupplier<E> extends Supplier<Validation<Float,E>> {}
    @SafeVarargs
    public static <E> Validation<Float,E> compose(FloatValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce(success(0f), (v1, v2) -> v1.compose(v2, Float::sum));
    }

    public interface DoubleValidationSupplier<E> extends Supplier<Validation<Double,E>> {}
    @SafeVarargs
    public static <E> Validation<Double,E> compose(DoubleValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce(success(0d), (v1, v2) -> v1.compose(v2, Double::sum));
    }

    public interface BooleanValidationSupplier<E> extends Supplier<Validation<Boolean,E>> {}
    @SafeVarargs
    public static <E> Validation<Boolean,E> compose(BooleanValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce(success(true), (v1, v2) -> v1.compose(v2, Boolean::logicalAnd));
    }

    public interface StringValidationSupplier<E> extends Supplier<Validation<String,E>> {}
    @SafeVarargs
    public static <E> Validation<String,E> compose(StringValidationSupplier<E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce(success(""), (v1, v2) -> v1.compose(v2, String::concat));
    }

    public interface ListValidationSupplier<T,E> extends Supplier<Validation<List<T>,E>> {}
    @SafeVarargs
    public static <T,E> Validation<List<T>,E> compose(ListValidationSupplier<T,E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce(success(
                                Collections.emptyList()),
                        (v1, v2) -> v1.compose(v2, (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toList()))
                );
    }

    public interface SetValidationSupplier<T,E> extends Supplier<Validation<Set<T>,E>> {}
    @SafeVarargs
    public static <T,E> Validation<Set<T>,E> compose(SetValidationSupplier<T,E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce(success(Collections.emptySet()),
                        (v1, v2) -> v1.compose(v2, (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toSet()))
                );
    }

    public interface MapValidationSupplier<K,V,E> extends Supplier<Validation<Map<K,V>,E>> {}
    @SafeVarargs
    public static <K,V,E> Validation<Map<K,V>,E> compose(MapValidationSupplier<K,V,E> ...  validationSuppliers) {
        return asList(validationSuppliers).stream()
                .map(Supplier::get)
                .reduce(success(Collections.emptyMap()),
                        (v1, v2) -> v1.compose(v2, (a, b) -> Stream.of(a, b).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c, d) -> d)))
                );
    }

    private static class MapException extends RuntimeException{
        public MapException(Exception e ) {
            super(e);
        }
    };

    @Override
    public String toString() {
        return isSuccess()
                ? String.format("Validation.success[%s]",value.get())
                : String.format("Validation.failure[%s]",errors);
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
