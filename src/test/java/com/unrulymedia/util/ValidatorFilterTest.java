package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidatorMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class ValidatorFilterTest {

    @Test
    public void shouldReturnSuccessIfPredicateTrue()  {
        Validator<Integer, ?> validator = Validator.success(3);
        Validator<Integer, ?> filtered = validator.filter((a) -> true);
        assertThat(filtered, ValidatorMatchers.isSuccessNotFailure());
        assertThat(filtered, ValidatorMatchers.hasValue(3));
    }

    @Test
    public void shouldReturnFailureIfPredicateFalse() throws Exception {
        Validator<Integer, Integer> validator = Validator.success(3);
        Validator<Integer, ?> filtered = validator.filter((a) -> false);
        assertThat(filtered, ValidatorMatchers.isFailureNotSuccess());
        assertThat(filtered, ValidatorMatchers.hasErrorValue(3));
    }

    @Test
    public void shouldReturnFailureIfPredicateThrows() throws Exception {
        Validator<Integer,?> validator = Validator.success(3);
        Validator<Integer,?> filtered = validator.filter((a) -> {throw new Exception("why is a predicate throwing anyway?");});
        assertThat(filtered, ValidatorMatchers.isFailureNotSuccess());
        assertThat(filtered, ValidatorMatchers.hasErrorValueWhichIsAnException(new Exception("why is a predicate throwing anyway?")));
    }

    @Test
    public void shouldReturnFailureIfFilteringFailure() throws Exception {
        Validator<?,String> validator = Validator.failure("woops");
        Validator<?, ?> filtered = validator.filter(a -> true);
        assertThat(filtered, ValidatorMatchers.isFailureNotSuccess());
        assertThat(filtered, ValidatorMatchers.hasErrorValue("woops"));
    }

}