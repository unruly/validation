package com.unrulymedia.util;

import java.util.*;

public class ComposedValidator<T,S> {
    private final List<Validation<T,S>> validations;

    private ComposedValidator() {
        throw new RuntimeException("don't do that");
    }

    private ComposedValidator(List<Validation<T,S>> validations) {
        this.validations = validations;
    }

    public ComposedValidator<T,S> compose(Validation<T,S> other) {
        return null;
    }

    public List<Validation<T,S>> get() {
        return validations;
    }

    @Override
    public String toString() {
        return "com.unrulymedia.util.Validator{" +
                "validators=" + validations +
                '}';
    }
}
