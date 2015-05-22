package com.unrulymedia.util;

import matchers.ValidationMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class ValidationFilterTest {

    @Test
    public void shouldReturnSuccessIfPredicateTrue()  {
        Validation<Integer, ?> validation = Validation.success(3);
        Validation<Integer, ?> filtered = validation.filter((a) -> true);
        assertThat(filtered, ValidationMatchers.isSuccessNotFailure());
        assertThat(filtered, ValidationMatchers.hasValue(3));
    }

    @Test
    public void shouldReturnFailureIfPredicateFalse() throws Exception {
        Validation<Integer, Integer> validation = Validation.success(3);
        Validation<Integer, ?> filtered = validation.filter((a) -> false);
        assertThat(filtered, ValidationMatchers.isFailureNotSuccess());
        assertThat(filtered, ValidationMatchers.hasErrorValue(3));
    }

    @Test
    public void shouldReturnFailureIfPredicateThrows() throws Exception {
        Validation<Integer,?> validation = Validation.success(3);
        Validation<Integer,?> filtered = validation.filter((a) -> {throw new Exception("why is a predicate throwing anyway?");});
        assertThat(filtered, ValidationMatchers.isFailureNotSuccess());
        assertThat(filtered, ValidationMatchers.hasErrorValueWhichIsAnException(new Exception("why is a predicate throwing anyway?")));
    }

    @Test
    public void shouldReturnFailureIfFilteringFailure() throws Exception {
        Validation<?,String> validation = Validation.failure("woops");
        Validation<?, ?> filtered = validation.filter(a -> true);
        assertThat(filtered, ValidationMatchers.isFailureNotSuccess());
        assertThat(filtered, ValidationMatchers.hasErrorValue("woops"));
    }

}