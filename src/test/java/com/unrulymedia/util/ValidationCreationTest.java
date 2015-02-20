package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidatorMatchers;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidationCreationTest {

    @Test
    public void shouldCreateASuccess() throws Exception {
        Validation<String, ?> val = Validation.success("woot");
        assertThat(val, ValidatorMatchers.isSuccessNotFailure());
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotBePossibleToCreateSuccessOfNull() throws Exception {
        Validation.success(null);
    }

    @Test
    public void shouldCreateAFailure() throws Exception {
        Validation<?, String> val = Validation.failure("awww");
        assertThat(val, ValidatorMatchers.isFailureNotSuccess());
        assertThat(val, ValidatorMatchers.hasErrorValue("awww"));
    }

    @Test
    public void shouldBeSuccessIfGivenFunction() throws Exception {
        Validation<String, ? extends Exception> validation = Validation.tryTo(() -> "yay!");
        assertThat(validation, ValidatorMatchers.isSuccessNotFailure());
    }

    @Test
    public void shouldBeFailureIfGivenThrowingFunction() throws Exception {
        Validation<String, Exception> validation = Validation.tryTo(() -> {
            throw new IOException("oh noes");
        });
        assertThat(validation, ValidatorMatchers.isFailureNotSuccess());
        assertThat(validation, ValidatorMatchers.hasErrorValueWhichIsAnException(new IOException("oh noes")));
    }

    @Test
    public void shouldBeFailureIfGivenNullReturningFunction() throws Exception {
        Validation<?, Exception> objectValidation = Validation.tryTo(() -> null);
        assertThat(objectValidation, ValidatorMatchers.isFailureNotSuccess());
        assertThat(objectValidation, ValidatorMatchers.hasErrorValueWhichIsAnException(new NullPointerException()));
    }


}