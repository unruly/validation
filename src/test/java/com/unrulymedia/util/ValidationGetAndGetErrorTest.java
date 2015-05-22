package com.unrulymedia.util;

import matchers.ValidationMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidationGetAndGetErrorTest {

    // Get and getErrors
    @Test
    public void shouldGetTheSuccessValueFromASuccess() throws Exception {
        Validation<String, ?> val = Validation.success("woot");
        assertThat(val, ValidationMatchers.hasValue("woot"));
    }

    @Test
    public void shouldGetTheErrorValueFromAFailure() throws Exception {
        Validation<?, String> val = Validation.failure("woops");
        assertThat(val, ValidationMatchers.hasErrorValue("woops"));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowIfGetAFailure() throws Exception {
        Validation<?, String> val = Validation.failure("bugger");
        val.get();
    }

    @Test
    public void shouldHaveEmptyErrorsIfSuccess() throws Exception {
        Validation<String, ?> val = Validation.success("woot");
        assertThat(val.getErrors(), Matchers.empty());
    }

    @Test
    public void shouldGetValueIfSuccessFromOrElse() throws Exception {
        Validation<String,?> validation = Validation.success("yay");
        assertThat(validation.orElse("meh"), is("yay"));
    }

    @Test
    public void shouldReturnOtherIfFailureFromOrElse() throws Exception {
        Validation<String, String> validation = Validation.failure("boo");
        assertThat(validation.orElse("meh"), is("meh"));
    }

    @Test
    public void shouldGetFromSuccessOrElseThrow() throws Exception {
        Validation<String, ?> success = Validation.success("hi there");
        assertThat(success.orElseThrow(Exception::new), is("hi there"));
    }

    @Test(expected = Exception.class)
    public void shouldThrowFromFailureOrElseThrow() throws Exception {
        Validation<?, String> failure = Validation.failure("hi there");
        failure.orElseThrow(Exception::new);
    }

    @Test
    public void shouldGetFromSuccessOrElseGet() throws Exception {
        Validation<String, ?> validation = Validation.success("yay");
        assertThat(validation.orElseGet(() -> "boo"), is("yay"));
    }

    @Test
    public void shouldReturnOtherFromFailureOrElseGet() throws Exception {
        Validation<String, String> validation = Validation.failure("yay");
        assertThat(validation.orElseGet(() -> "boo"), is("boo"));
    }

}
