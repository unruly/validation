package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidatorMatchers;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidatorToAndFromOptionalTest {

    @Test
    public void shouldCreateASuccessFromNonEmptyOptional() throws Exception {
        Validator<String, NoSuchElementException> validator = Validator.from(Optional.of("hello!"));
        assertThat(validator, ValidatorMatchers.isSuccessNotFailure());
        assertThat(validator, ValidatorMatchers.hasValue("hello!"));
    }

    @Test
    public void shouldCreateAFailureFromEmptyOptional() throws Exception {
        Validator<String, NoSuchElementException> validator = Validator.from(Optional.empty());
        assertThat(validator, ValidatorMatchers.isFailureNotSuccess());
        assertThat(validator, ValidatorMatchers.hasErrorValueWhichIsAnException(new NoSuchElementException()));
    }

    @Test
    public void shouldCreateANonEmptyOptionalFromASuccess() throws Exception {
        Validator<String,String> success = Validator.success("woop");
        Optional<String> opt = success.toOptional();
        assertThat(opt.get(), is("woop"));
    }

    @Test
    public void shouldCreateEmptyOptionalFromFailure() throws Exception {
        Validator<Object, String> validator = Validator.failure("meh");
        Optional<Object> optional = validator.toOptional();
        assertThat(optional,is(Optional.empty()));
    }


}
