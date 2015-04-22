package com.unrulymedia.util.testutils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Optional;

public class OptionalMatchers {
    public static <T> Matcher<Optional<T>> empty() {
        return new TypeSafeMatcher<Optional<T>>() {
            @Override
            protected boolean matchesSafely(Optional<T> item) {
                return !item.isPresent();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("An empty Optional");
            }
        };
    }

    public static <T> Matcher<Optional<T>> contains(T content) {
        return new TypeSafeMatcher<Optional<T>>() {
            @Override
            protected boolean matchesSafely(Optional<T> item) {
                return item.map(content::equals).orElse(false);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(Optional.of(content).toString());
            }
        };
    }

    public static <T> Matcher<Optional<T>> contains(Matcher<T> matcher) {
        return new TypeSafeMatcher<Optional<T>>() {
            @Override
            protected boolean matchesSafely(Optional<T> item) {
                return item.map(matcher::matches).orElse(false);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Optional with an item that matches" + matcher);
            }
        };
    }
}
