package matchers;

import com.unrulymedia.util.Validation;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;

public class ValidationMatchers {
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


    public static <E extends Exception> Matcher<? super Validation<?, ?>> hasErrorValueWhichIsAnException(E e) {
        return new TypeSafeMatcher<Validation<?,?>>() {
            @Override
            protected boolean matchesSafely(Validation<?, ?> item) {
                if(item.isSuccess()) {
                    return false;
                }
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
                description
                        .appendText("a failure with errors containing ")
                        .appendValue(e);
            }
        };
    }
}
