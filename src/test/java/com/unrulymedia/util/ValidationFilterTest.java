package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidatorMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class ValidationFilterTest {

    @Test
    public void shouldReturnSuccessIfPredicateTrue()  {
        Validation<Integer, ?> validation = Validation.success(3);
        Validation<Integer, ?> filtered = validation.filter((a) -> true);
        assertThat(filtered, ValidatorMatchers.isSuccessNotFailure());
        assertThat(filtered, ValidatorMatchers.hasValue(3));
    }

    @Test
    public void shouldReturnFailureIfPredicateFalse() throws Exception {
        Validation<Integer, Integer> validation = Validation.success(3);
        Validation<Integer, ?> filtered = validation.filter((a) -> false);
        assertThat(filtered, ValidatorMatchers.isFailureNotSuccess());
        assertThat(filtered, ValidatorMatchers.hasErrorValue(3));
    }

    @Test
    public void shouldReturnFailureIfPredicateThrows() throws Exception {
        Validation<Integer,?> validation = Validation.success(3);
        Validation<Integer,?> filtered = validation.filter((a) -> {throw new Exception("why is a predicate throwing anyway?");});
        assertThat(filtered, ValidatorMatchers.isFailureNotSuccess());
        assertThat(filtered, ValidatorMatchers.hasErrorValueWhichIsAnException(new Exception("why is a predicate throwing anyway?")));
    }

    @Test
    public void shouldReturnFailureIfFilteringFailure() throws Exception {
        Validation<?,String> validation = Validation.failure("woops");
        Validation<?, ?> filtered = validation.filter(a -> true);
        assertThat(filtered, ValidatorMatchers.isFailureNotSuccess());
        assertThat(filtered, ValidatorMatchers.hasErrorValue("woops"));
    }

}