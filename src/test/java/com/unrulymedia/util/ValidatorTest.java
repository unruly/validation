package com.unrulymedia.util;

import org.junit.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ValidatorTest {

    @Test
    public void shouldComposeTwoSuccessesWithComposeFunction() throws Exception {
        Validation<Integer, Object> composed = Validation.success(3).compose(Validation.success(4), (a, b) -> a * b);
        Validation<Integer, Object> expected = Validation.success(12);
        assertThat(composed,is(expected));
    }

    @Test
    public void shouldComposeSuccessAndFailureAsFailure() throws Exception {
        Validation<Integer, String> composed = Validation.<Integer,String>success(3).compose(Validation.failure(asList("nope!")), (a, b) -> a + b);
        assertThat(composed,is(Validation.failure(asList("nope!"))));
    }

    @Test
    public void shouldComposeFailureAndSuccessAsFailure() throws Exception {
        Validation<Integer, String> composed = Validation.<Integer,String>failure(asList("nope!")).compose(Validation.success(3), (a, b) -> a + b);
        assertThat(composed,is(Validation.failure(asList("nope!"))));
    }

    @Test
    public void shouldComposeFailures() throws Exception {
        Validation<Object, String> composed = Validation.failure(asList("nope!")).compose(Validation.failure(asList("nein!")), (a, b) -> a);
        assertThat(composed,is(Validation.failure(asList("nope!","nein!"))));
    }
}