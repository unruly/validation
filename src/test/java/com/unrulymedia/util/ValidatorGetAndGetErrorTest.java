package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidatorMatchers;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidatorGetAndGetErrorTest {

    // Get and getError
    @Test
    public void shouldGetTheSuccessValueFromASuccess() throws Exception {
        Validator<String, ?> val = Validator.success("woot");
        assertThat(val, ValidatorMatchers.hasValue("woot"));
    }

    @Test
    public void shouldGetTheErrorValueFromAFailure() throws Exception {
        Validator<?, String> val = Validator.failure("woops");
        assertThat(val, ValidatorMatchers.hasErrorValue("woops"));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowIfGetAFailure() throws Exception {
        Validator<?, String> val = Validator.failure("bugger");
        val.get();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowIfGetErrorASuccess() throws Exception {
        Validator<String, ?> val = Validator.success("woot");
        val.getError();
    }

    @Test
    public void shouldGetValueIfSuccessFromOrElse() throws Exception {
        Validator<String,?> validator = Validator.success("yay");
        assertThat(validator.orElse("meh"), is("yay"));
    }

    @Test
    public void shouldReturnOtherIfFailureFromOrElse() throws Exception {
        Validator<String, String> validator = Validator.failure("boo");
        assertThat(validator.orElse("meh"), is("meh"));
    }

    @Test
    public void shouldGetFromSuccessOrElseThrow() throws Exception {
        Validator<String, ?> success = Validator.success("hi there");
        assertThat(success.orElseThrow(Exception::new), is("hi there"));
    }

    @Test(expected = Exception.class)
    public void shouldThrowFromFailureOrElseThrow() throws Exception {
        Validator<?, String> failure = Validator.failure("hi there");
        failure.orElseThrow(Exception::new);
    }

    @Test
    public void shouldGetFromSuccessOrElseGet() throws Exception {
        Validator<String, ?> validator = Validator.success("yay");
        assertThat(validator.orElseGet(() -> "boo"), is("yay"));
    }

    @Test
    public void shouldReturnOtherFromFailureOrElseGet() throws Exception {
        Validator<String, String> validator = Validator.failure("yay");
        assertThat(validator.orElseGet(() -> "boo"), is("boo"));
    }

}
