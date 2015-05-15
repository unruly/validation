package com.unrulymedia.util.testutils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

public class TimeMatchers {

    public static <T extends Comparable<T> & Temporal> Matcher<T> after(T time) {
        return new TypeSafeMatcher<T>() {
            @Override
            protected boolean matchesSafely(T actual) {
                return actual.compareTo(time) > 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("after ").appendValue(time);
            }
        };
    }

    public static <T extends Comparable<T> & Temporal> Matcher<T> before(T time) {
        return new TypeSafeMatcher<T>() {
            @Override
            protected boolean matchesSafely(T actual) {
                return actual.compareTo(time) < 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("before ").appendValue(time);
            }
        };
    }

    public static <T extends Comparable<T> & TemporalAmount> Matcher<T> longerThan(T amount) {
        return new TypeSafeMatcher<T>() {

            @Override
            protected boolean matchesSafely(T actual) {
                return actual.compareTo(amount) > 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("longer than ").appendValue(amount);
            }
        };
    }

    public static <T extends Comparable<T> & TemporalAmount> Matcher<T> shorterThan(T amount) {
        return new TypeSafeMatcher<T>() {

            @Override
            protected boolean matchesSafely(T actual) {
                return actual.compareTo(amount) < 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("shorter than ").appendValue(amount);
            }
        };
    }

    public static Matcher<Period> periodMatches(Matcher<Integer> yearsMatcher, Matcher<Integer> monthsMatcher, Matcher<Integer> daysMatcher) {
        return new TypeSafeMatcher<Period>() {
            @Override
            protected boolean matchesSafely(Period item) {
                return yearsMatcher.matches(item.getYears()) && monthsMatcher.matches(item.getMonths()) && daysMatcher.matches(item.getDays());
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("a Period with years matching ")
                        .appendValue(yearsMatcher)
                        .appendText(" months matching ")
                        .appendValue(yearsMatcher)
                        .appendText(" and days matching ")
                        .appendValue(daysMatcher);
            }
        };
    }
}
