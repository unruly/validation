package com.unrulymedia.util.testutils;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class OptionalMatchersTest {

    @Test
    public void empty() throws Exception {
        assertThat(Optional.empty(),OptionalMatchers.empty());
    }

    @Test
    public void nonEmpty() throws Exception {
        assertThat(Optional.of(1),not(OptionalMatchers.empty()));
    }

    @Test
    public void contains() throws Exception {
        assertThat(Optional.of("Hi!"), OptionalMatchers.contains("Hi!"));
    }

    @Test
    public void containsDifferent() throws Exception {
        assertThat(Optional.of("Hi!"), not(OptionalMatchers.contains("Yay")));
    }

    @Test
    public void containsEmpty() throws Exception {
        assertThat(Optional.empty(),not(OptionalMatchers.contains("Woot")));
    }

    @Test
    public void containsMatching() throws Exception {
        assertThat(Optional.of(4),OptionalMatchers.contains(Matchers.greaterThan(3)));
    }

    @Test
    public void containsMatchingEmpty() throws Exception {
        assertThat(Optional.empty(),not(OptionalMatchers.contains(Matchers.lessThanOrEqualTo(19))));
    }
}
