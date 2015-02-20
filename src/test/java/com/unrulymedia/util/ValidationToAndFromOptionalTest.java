package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidatorMatchers;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidationToAndFromOptionalTest {

    @Test
    public void shouldCreateASuccessFromNonEmptyOptional() throws Exception {
        Validation<String, NoSuchElementException> validation = Validation.from(Optional.of("hello!"));
        assertThat(validation, ValidatorMatchers.isSuccessNotFailure());
        assertThat(validation, ValidatorMatchers.hasValue("hello!"));
    }

    @Test
    public void shouldCreateAFailureFromEmptyOptional() throws Exception {
        Validation<String, NoSuchElementException> validation = Validation.from(Optional.empty());
        assertThat(validation, ValidatorMatchers.isFailureNotSuccess());
        assertThat(validation, ValidatorMatchers.hasErrorValueWhichIsAnException(new NoSuchElementException()));
    }

    @Test
    public void shouldCreateANonEmptyOptionalFromASuccess() throws Exception {
        Validation<String,String> success = Validation.success("woop");
        Optional<String> opt = success.toOptional();
        assertThat(opt.get(), is("woop"));
    }

    @Test
    public void shouldCreateEmptyOptionalFromFailure() throws Exception {
        Validation<Object, String> validation = Validation.failure("meh");
        Optional<Object> optional = validation.toOptional();
        assertThat(optional,is(Optional.empty()));
    }


}
