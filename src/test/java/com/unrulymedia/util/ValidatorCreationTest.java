package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidatorMatchers;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidatorCreationTest {

    @Test
    public void shouldCreateASuccess() throws Exception {
        Validator<String, ?> val = Validator.success("woot");
        assertThat(val, ValidatorMatchers.isSuccessNotFailure());
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotBePossibleToCreateSuccessOfNull() throws Exception {
        Validator.success(null);
    }

    @Test
    public void shouldCreateAFailure() throws Exception {
        Validator<?, String> val = Validator.failure("awww");
        assertThat(val, ValidatorMatchers.isFailureNotSuccess());
        assertThat(val, ValidatorMatchers.hasErrorValue("awww"));
    }

    @Test
    public void shouldBeSuccessIfGivenFunction() throws Exception {
        Validator<String, ? extends Exception> validator = Validator.tryTo(() -> "yay!");
        assertThat(validator, ValidatorMatchers.isSuccessNotFailure());
    }

    @Test
    public void shouldBeFailureIfGivenThrowingFunction() throws Exception {
        Validator<String, Exception> validator = Validator.tryTo(() -> {
            throw new IOException("oh noes");
        });
        assertThat(validator, ValidatorMatchers.isFailureNotSuccess());
        assertThat(validator, ValidatorMatchers.hasErrorValueWhichIsAnException(new IOException("oh noes")));
    }

    @Test
    public void shouldBeFailureIfGivenNullReturningFunction() throws Exception {
        Validator<?, Exception> objectValidator = Validator.tryTo(() -> null);
        assertThat(objectValidator, ValidatorMatchers.isFailureNotSuccess());
        assertThat(objectValidator, ValidatorMatchers.hasErrorValueWhichIsAnException(new NullPointerException()));
    }


}