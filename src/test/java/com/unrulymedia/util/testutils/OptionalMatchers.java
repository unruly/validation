package com.unrulymedia.util.testutils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

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

    public static Matcher<OptionalInt> emptyInt() {
        return new TypeSafeMatcher<OptionalInt>() {
            @Override
            protected boolean matchesSafely(OptionalInt item) {
                return !item.isPresent();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("An empty OptionalInt");
            }
        };
    }

    public static Matcher<OptionalInt> containsInt(int content) {
        return new TypeSafeMatcher<OptionalInt>() {
            @Override
            protected boolean matchesSafely(OptionalInt item) {
                return item.isPresent() && item.getAsInt() == content;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(Optional.of(content).toString());
            }
        };
    }

    public static Matcher<OptionalInt> containsInt(Matcher<Integer> matcher) {
        return new TypeSafeMatcher<OptionalInt>() {
            @Override
            protected boolean matchesSafely(OptionalInt item) {
                return item.isPresent() && matcher.matches(item.getAsInt());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("OptionalInt with an item that matches" + matcher);
            }
        };
    }

    public static Matcher<OptionalLong> emptyLong() {
        return new TypeSafeMatcher<OptionalLong>() {
            @Override
            protected boolean matchesSafely(OptionalLong item) {
                return !item.isPresent();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("An empty OptionalLong");
            }
        };
    }

    public static Matcher<OptionalLong> containsLong(long content) {
        return new TypeSafeMatcher<OptionalLong>() {
            @Override
            protected boolean matchesSafely(OptionalLong item) {
                return item.isPresent() && item.getAsLong() == content;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(Optional.of(content).toString());
            }
        };
    }

    public static Matcher<OptionalLong> containsLong(Matcher<Long> matcher) {
        return new TypeSafeMatcher<OptionalLong>() {
            @Override
            protected boolean matchesSafely(OptionalLong item) {
                return item.isPresent() && matcher.matches(item.getAsLong());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("OptionalLong with an item that matches" + matcher);
            }
        };
    }

    public static Matcher<OptionalDouble> emptyDouble() {
        return new TypeSafeMatcher<OptionalDouble>() {
            @Override
            protected boolean matchesSafely(OptionalDouble item) {
                return !item.isPresent();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("An empty OptionalDouble");
            }
        };
    }

    public static Matcher<OptionalDouble> containsDouble(double content) {
        return new TypeSafeMatcher<OptionalDouble>() {
            @Override
            protected boolean matchesSafely(OptionalDouble item) {
                return item.isPresent() && item.getAsDouble() == content;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(Optional.of(content).toString());
            }
        };
    }

    public static Matcher<OptionalDouble> containsDouble(Matcher<Double> matcher) {
        return new TypeSafeMatcher<OptionalDouble>() {
            @Override
            protected boolean matchesSafely(OptionalDouble item) {
                return item.isPresent() && matcher.matches(item.getAsDouble());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("OptionalDouble with an item that matches" + matcher);
            }
        };
    }
}
