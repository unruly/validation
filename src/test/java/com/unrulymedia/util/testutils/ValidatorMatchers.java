package com.unrulymedia.util.testutils;

import com.unrulymedia.util.Validator;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ValidatorMatchers {
    public static <T, U> Matcher<? super Validator<? extends T,? extends U>> isFailureNotSuccess() {
        return new TypeSafeMatcher<Validator<? extends T,? extends U>>() {
            @Override
            protected boolean matchesSafely(Validator<? extends T,? extends U> item) {
                return item.isFailure() && !item.isSuccess();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a failure com.unrulymedia.util.Validator");
            }
        };
    }

    public static <T, U> Matcher<Validator<? extends T,? extends U>> isSuccessNotFailure() {
        return new TypeSafeMatcher<Validator<? extends T,? extends U>>() {
            @Override
            protected boolean matchesSafely(Validator<? extends T,? extends U> item) {
                return item.isSuccess() && !item.isFailure();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a success com.unrulymedia.util.Validator");
            }
        };
    }

    public static <T, U> Matcher<? super Validator<? extends T,? extends U>> hasValue(T expected) {
        return new TypeSafeMatcher<Validator<? extends T,? extends U>>() {
            @Override
            protected boolean matchesSafely(Validator<? extends T,? extends U> item) {
                return item.get().equals(expected);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a success com.unrulymedia.util.Validator containing a value of " + expected);
            }
        };
    }

    public static <T, U> Matcher<? super Validator<? extends T,?>> hasErrorValue(U expected) {
        return new TypeSafeMatcher<Validator<? extends T,?>>() {
            @Override
            protected boolean matchesSafely(Validator<? extends T,?> item) {
                return item.getError().equals(expected);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a failure com.unrulymedia.util.Validator containing an error value of " + expected);
            }
        };
    }

    public static <E extends Exception> Matcher<? super Validator<?, ?>> hasErrorValueWhichIsAnException(E e) {
        return new TypeSafeMatcher<Validator<?,?>>() {
            @Override
            protected boolean matchesSafely(Validator<?, ?> item) {
                Exception error = (Exception)item.getError();
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
