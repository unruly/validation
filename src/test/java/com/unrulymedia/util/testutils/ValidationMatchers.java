package com.unrulymedia.util.testutils;

import com.unrulymedia.util.Validation;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ValidationMatchers {
    public static <T, U> Matcher<? super Validation<? extends T,? extends U>> isFailureNotSuccess() {
        return new TypeSafeMatcher<Validation<? extends T,? extends U>>() {
            @Override
            protected boolean matchesSafely(Validation<? extends T,? extends U> item) {
                return item.isFailure() && !item.isSuccess();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a failure com.unrulymedia.util.Validator");
            }
        };
    }

    public static <T, U> Matcher<Validation<? extends T,? extends U>> isSuccessNotFailure() {
        return new TypeSafeMatcher<Validation<? extends T,? extends U>>() {
            @Override
            protected boolean matchesSafely(Validation<? extends T,? extends U> item) {
                return item.isSuccess() && !item.isFailure();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a success com.unrulymedia.util.Validator");
            }
        };
    }

    public static <T, U> Matcher<? super Validation<? extends T,? extends U>> hasValue(T expected) {
        return new TypeSafeMatcher<Validation<? extends T,? extends U>>() {
            @Override
            protected boolean matchesSafely(Validation<? extends T,? extends U> item) {
                return item.get().equals(expected);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a success com.unrulymedia.util.Validator containing a value of " + expected);
            }
        };
    }

    public static <T, U> Matcher<? super Validation<? extends T,?>> hasErrorValue(U expected) {
        return new TypeSafeMatcher<Validation<? extends T,?>>() {
            @Override
            protected boolean matchesSafely(Validation<? extends T,?> item) {
                return item.getErrors().contains(expected);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a failure com.unrulymedia.util.Validator containing an error value of " + expected);
            }
        };
    }

    public static <E extends Exception> Matcher<? super Validation<?, ?>> hasErrorValueWhichIsAnException(E e) {
        return new TypeSafeMatcher<Validation<?,?>>() {
            @Override
            protected boolean matchesSafely(Validation<?, ?> item) {
                Exception error = (Exception)item.getErrors().get(0);
                if(!error.getClass().equals(e.getClass())) {
                    return false;
                } else if (error.getMessage() == null && e.getMessage() == null) {
                    return true;
                }
                return error.getMessage() != null && error.getMessage().equals(e.getMessage());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a failure with exception " + e);
            }
        };
    }
}
