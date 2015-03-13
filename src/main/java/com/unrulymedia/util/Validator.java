package com.unrulymedia.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Validator<T,U> {
    private Validator() {throw new RuntimeException();};

    private Validator(List<ValidatorPair<T, U>> validatorPairs) {
        this.validatorPairs = validatorPairs;
    }

    private static class ValidatorPair<T,U> {
        final Predicate<T> predicate;
        final U error;

        private ValidatorPair(Predicate<T> predicate, U error) {
            this.predicate = predicate;
            this.error = error;
        }
    }

    private final List<ValidatorPair<T,U>> validatorPairs;

    public static <T,U> Validator<T,U> from(Predicate<T> predicate, U failure) {
        return new Validator<>(Arrays.asList(new ValidatorPair<>(predicate,failure)));
    }

    public Validator<T,U> compose(Validator<T,U> other) {
        List<ValidatorPair<T, U>> newValidatorPairs = Stream.concat(validatorPairs.stream(), other.validatorPairs.stream()).collect(Collectors.toList());
        return new Validator<>(newValidatorPairs);
    }

    public Validation<T,U> validate(T value) {
        return validatorPairs
                .stream()
                .map(pair -> pair.predicate.test(value) ? Validation.<T, U>success(value) : Validation.<T, U>failure(pair.error))
                .reduce(Validation.<T, U>success(value), (Validation<T, U> val1, Validation<T, U> val2) -> {
                    if (val1.isSuccess() && val2.isSuccess()) {
                        return val1;
                    } else {
                        List<U> errors = Stream.concat(
                                val1.getErrors().stream(),
                                val2.getErrors().stream()
                        ).collect(Collectors.toList());
                        return Validation.<T, U>failure(errors);
                    }
                });
    }
}
