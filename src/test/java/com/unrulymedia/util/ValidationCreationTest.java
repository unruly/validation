package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidationMatchers;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertThat;

public class ValidationCreationTest {

    @Test
    public void shouldCreateASuccess() throws Exception {
        Validation<String, ?> val = Validation.success("woot");
        assertThat(val, ValidationMatchers.isSuccessNotFailure());
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotBePossibleToCreateSuccessOfNull() throws Exception {
        Validation.success(null);
    }

    @Test
    public void shouldCreateAFailure() throws Exception {
        Validation<?, String> val = Validation.failure("awww");
        assertThat(val, ValidationMatchers.isFailureNotSuccess());
        assertThat(val, ValidationMatchers.hasErrorValue("awww"));
    }

    @Test
    public void shouldBeSuccessIfGivenFunction() throws Exception {
        Validation<String, ? extends Exception> validation = Validation.tryTo(() -> "yay!");
        assertThat(validation, ValidationMatchers.isSuccessNotFailure());
    }

    @Test
    public void shouldBeFailureIfGivenThrowingFunction() throws Exception {
        Validation<String, Exception> validation = Validation.tryTo(() -> {
            throw new IOException("oh noes");
        });
        assertThat(validation, ValidationMatchers.isFailureNotSuccess());
        assertThat(validation, ValidationMatchers.hasErrorValueWhichIsAnException(new IOException("oh noes")));
    }

    @Test
    public void shouldBeFailureIfGivenNullReturningFunction() throws Exception {
        Validation<?, Exception> objectValidation = Validation.tryTo(() -> null);
        assertThat(objectValidation, ValidationMatchers.isFailureNotSuccess());
        assertThat(objectValidation, ValidationMatchers.hasErrorValueWhichIsAnException(new NullPointerException()));
    }


}