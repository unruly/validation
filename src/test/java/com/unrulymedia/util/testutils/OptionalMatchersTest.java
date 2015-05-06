package com.unrulymedia.util.testutils;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class OptionalMatchersTest {

    @Test
    public void empty_success() throws Exception {
        assertThat(Optional.empty(),OptionalMatchers.empty());
    }

    @Test
    public void empty_failure() throws Exception {
        assertThat(Optional.of(1),not(OptionalMatchers.empty()));
    }

    @Test
    public void empty_failureMessage() throws Exception {
        Helper.testFailingMatcher(OptionalMatchers.empty(),Optional.of(1),"An empty Optional","<Optional[1]>");
    }

    @Test
    public void contains_success() throws Exception {
        assertThat(Optional.of("Hi!"), OptionalMatchers.contains("Hi!"));
    }

    @Test
    public void contains_failureNonEmpty() throws Exception {
        assertThat(Optional.of("Hi!"), not(OptionalMatchers.contains("Yay")));
    }

    @Test
    public void contains_failureEmpty() throws Exception {
        assertThat(Optional.empty(),not(OptionalMatchers.contains("Woot")));
    }


    @Test
    public void contains_failureMessages() throws Exception {
        Helper.testFailingMatcher(OptionalMatchers.contains(2),Optional.of(1),"Optional[2]","<Optional[1]>");
    }

    @Test
    public void containsMatcher_success() throws Exception {
        assertThat(Optional.of(4),OptionalMatchers.contains(Matchers.greaterThan(3)));
    }

    @Test
    public void containsMatcher_failure() throws Exception {
        assertThat(Optional.empty(),not(OptionalMatchers.contains(Matchers.lessThanOrEqualTo(19))));
    }

    @Test
    public void containsMatcher_failureMessage() throws Exception {
        Helper.testFailingMatcher(OptionalMatchers.contains(Matchers.equalTo(4)),Optional.of(2),"Optional with an item that matches<4>","<Optional[2]>");
    }
}
