package co.unruly.matchers;

import co.unruly.util.Validation;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;

public class ValidationMatchers {

    /**
     * Matches a failure. Does not match a success.
     * @param <T> Type of success value
     * @param <U> Type of error values
     */
    public static <T, U> Matcher<? super Validation<? extends T,? extends U>> isFailureNotSuccess() {
        return new TypeSafeMatcher<Validation<? extends T,? extends U>>() {
            @Override
            protected boolean matchesSafely(Validation<? extends T,? extends U> item) {
                return item.isFailure() && !item.isSuccess();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a failure");
            }
        };
    }

    /**
     * Matches a success. Does not match a failure.
     * @param <T> Type of success value
     * @param <U> Type of error values
     */
    public static <T, U> Matcher<Validation<? extends T,? extends U>> isSuccessNotFailure() {
        return new TypeSafeMatcher<Validation<? extends T,? extends U>>() {
            @Override
            protected boolean matchesSafely(Validation<? extends T,? extends U> item) {
                return item.isSuccess() && !item.isFailure();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a success");
            }
        };
    }

    /**
     * Matches a success with a value equalling that given. Does not match a failure.
     * @param <T> Type of success value
     * @param <U> Type of error values
     */
    public static <T, U> Matcher<? super Validation<? extends T,? extends U>> hasValue(T expected) {
        return new TypeSafeMatcher<Validation<? extends T,? extends U>>() {
            @Override
            protected boolean matchesSafely(Validation<? extends T,? extends U> item) {
                return item.isSuccess() && item.get().equals(expected);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(Validation.success(expected));
            }
        };
    }

    /**
     * Matches a success with a value matching the given matcher. Does not match a failure.
     * @param <T> Type of success value
     * @param <U> Type of error values
     */
    public static <T, U> Matcher<? super Validation<? extends T,? extends U>> hasValue(Matcher<T> matcher) {
        return new TypeSafeMatcher<Validation<? extends T,? extends U>>() {
            @Override
            protected boolean matchesSafely(Validation<? extends T,? extends U> item) {
                return item.map(matcher::matches).orElse(false);
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("a success with value matching ")
                        .appendValue(matcher);
            }
        };
    }

    /**
     * Matches a failure with errors equalling those given in order. Does not match if the number of expected errors
     * does not equal the number of actual errors. Does not match a success.
     * @param <T> Type of success value
     * @param <U> Type of error values
     */
    @SafeVarargs
    public static <T, U> Matcher<? super Validation<? extends T,?>> hasErrorValue(U... expected) {
        return new TypeSafeMatcher<Validation<? extends T,?>>() {
            @Override
            protected boolean matchesSafely(Validation<? extends T,?> item) {
                return !item.isSuccess() && Arrays.asList(expected).equals(item.getErrors());

            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("a failure with errors containing ")
                        .appendValue(expected);
            }
        };
    }

    /**
     * Matches a failure with errors matching the given matchers in order. Does not match if the number of matchers
     * does not equal the number of actual errors. Does not match a success.
     * @param <T> Type of success value
     * @param <U> Type of error values
     */
    @SafeVarargs
    public static <T, U> Matcher<? super Validation<? extends T,?>> hasErrorValue(Matcher<U>... expected) {
        return new TypeSafeMatcher<Validation<? extends T,?>>() {
            @Override
            protected boolean matchesSafely(Validation<? extends T,?> item) {
                if (item.isSuccess()) {
                    return false;
                }

                if (expected.length != item.getErrors().size()) {
                    return false;
                }

                for(int i = 0; i < expected.length; i++) {
                    if(!expected[i].matches(item.getErrors().get(i))) {
                        return false;
                    }
                }
                return true;

            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("a failure with errors matching in turn ")
                        .appendValue(expected);
            }
        };
    }

    /**
     * Matches a failure with errors that are exceptions each of which in turn has the same class and same message
     * as the expected exception in the same position in the arguments.
     *
     * Does not match if the length of errors does not equal the length of expected exceptions.
     *
     * Does not match a success.
     *
     * @param expected exceptions to check against
     * @param <E> Super type of supplied exceptions
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <E extends Exception> Matcher<? super Validation<?, ?>> hasErrorValueWhichIsAnException(E... expected) {
        return new TypeSafeMatcher<Validation<?,?>>() {
            @Override
            protected boolean matchesSafely(Validation<?, ?> item) {
                if(item.isSuccess()) {
                    return false;
                }
                if(item.getErrors().size() != expected.length) {
                    return false;
                }

               for(int i = 0; i < expected.length; i ++) {
                   if(!errorsEqual((E)item.getErrors().get(i),expected[i])) {
                       return false;
                   }
               }

                return true;
            }

            private boolean errorsEqual(E error, E expected) {
                if(!error.getClass().equals(expected.getClass())) {
                    return false;
                } else if (error.getMessage() == null && expected.getMessage() == null) {
                    return true;
                }
                return error.getMessage() != null && error.getMessage().equals(expected.getMessage());
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("a failure with errors containing ")
                        .appendValue(expected);
            }
        };
    }
}
