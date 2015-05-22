package matchers;

import com.unrulymedia.util.Validation;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class ValidationMatchersTest {

    @Test
    public void validationSuccess_success() throws Exception {
        assertThat(Validation.success(3L),ValidationMatchers.isSuccessNotFailure());
    }

    @Test
    public void validationSuccess_failure() throws Exception {
        Helper.testFailingMatcher(Validation.failure("nope"),ValidationMatchers.isSuccessNotFailure(),"success","failure[[nope]]");
    }

    @Test
    public void validationFailure_success() throws Exception {
        assertThat(Validation.failure(3L),ValidationMatchers.isFailureNotSuccess());
    }

    @Test
    public void validationFailure_failure() throws Exception {
        Helper.testFailingMatcher(Validation.success("hurrah"),ValidationMatchers.isFailureNotSuccess(),"failure","success[hurrah]");
    }

    @Test
    public void hasValueItem_success() throws Exception {
        assertThat(Validation.success("wowzers"), ValidationMatchers.hasValue("wowzers"));
    }

    @Test
    public void hasValueItem_failureDifferingValues() throws Exception {
        Helper.testFailingMatcher(Validation.success("wowzers"), ValidationMatchers.hasValue("w00t"), "success[w00t]","success[wowzers]");
    }

    @Test
    public void hasValueItem_failureIsValidationFailure() throws Exception {
        Helper.testFailingMatcher(Validation.<String,String>failure("doh!"), ValidationMatchers.hasValue("w00t"), "success[w00t]","failure[[doh!]]");
    }

    @Test
    public void hasValueMatcher_success() throws Exception {
        assertThat(Validation.success("foo bar baz"), ValidationMatchers.hasValue(Matchers.containsString("foo")));
    }

    @Test
    public void hasValueMatcher_failureNoMatch() throws Exception {
        Helper.testFailingMatcher(Validation.success("wowzers"), ValidationMatchers.hasValue(Matchers.containsString("foo")), "a success with value matching <a string containing \"foo\">","success[wowzers]");
    }

    @Test
    public void hasValueMatcher_failureIsValidationFailure() throws Exception {
        Helper.testFailingMatcher(Validation.<String,String>failure("doh!"), ValidationMatchers.hasValue(Matchers.containsString("foo")), "a success with value matching <a string containing \"foo\">","failure[[doh!]]");
    }

    @Test
    public void hasErrorValueItem_success() throws Exception {
        assertThat(Validation.failure("wowzers"), ValidationMatchers.hasErrorValue("wowzers"));
    }

    @Test
    public void hasErrorValueItem_failureDifferingValues() throws Exception {
        Helper.testFailingMatcher(Validation.failure("wowzers"), ValidationMatchers.hasErrorValue("w00t"), "a failure with errors containing [\"w00t\"]","failure[[wowzers]]");
    }

    @Test
    public void hasErrorValueItem_failureIsValidationSuccess() throws Exception {
        Helper.testFailingMatcher(Validation.<String,String>success("doh!"), ValidationMatchers.hasErrorValue("w00t"), "a failure with errors containing [\"w00t\"]","success[doh!]");
    }

    @Test
    public void hasErrorValueItems_success() throws Exception {
        assertThat(Validation.failure(asList("wowzers", "blimey")), ValidationMatchers.hasErrorValue("wowzers","blimey"));
    }

    @Test
    public void hasErrorValueItems_failureDifferingValues() throws Exception {
        Helper.testFailingMatcher(Validation.failure(asList("wowzers", "blimey")), ValidationMatchers.hasErrorValue("w00t"), "a failure with errors containing [\"w00t\"]","failure[[wowzers, blimey]]");
    }

    @Test
    public void hasErrorValueItems_failureIsValidationSuccess() throws Exception {
        Helper.testFailingMatcher(Validation.<String,String>success("doh!"), ValidationMatchers.hasErrorValue("doh!"), "a failure with errors containing [\"doh!\"]","success[doh!]");
    }

    @Test
    public void hasErrorValueMatcher_success() throws Exception {
        assertThat(Validation.failure(asList("wowzers", "blimey")), ValidationMatchers.hasErrorValue(Matchers.equalTo("wowzers"), Matchers.equalTo("blimey")));
    }

    @Test
    public void hasErrorValueMatcher_failureTooManyMatchers() throws Exception {
        Matcher<? super Validation<?, ?>> matcher = ValidationMatchers.hasErrorValue(Matchers.equalTo("wowzers"), Matchers.equalTo("blimey"), Matchers.equalTo("golly"));
        Helper.testFailingMatcher(
                Validation.failure(asList("wowzers", "blimey")),
                matcher,
                "a failure with errors matching in turn [<\"wowzers\">, <\"blimey\">, <\"golly\">",
                "failure[[wowzers, blimey]]"
        );
    }

    @Test
    public void hasErrorValueMatcher_failureTooFewMatchers() throws Exception {
        Matcher<? super Validation<?, ?>> matcher = ValidationMatchers.hasErrorValue(Matchers.equalTo("wowzers"));
        Helper.testFailingMatcher(
                Validation.failure(asList("wowzers", "blimey")),
                matcher,
                "a failure with errors matching in turn [<\"wowzers\">",
                "failure[[wowzers, blimey]]"
        );
    }

    @Test
    public void hasErrorValueMatcher_failureNoMatch() throws Exception {
        Helper.testFailingMatcher(Validation.failure("wowzers"), ValidationMatchers.hasErrorValue(Matchers.equalTo("w00t")), "a failure with errors matching in turn [<\"w00t\">]","failure[[wowzers]]");
    }

    @Test
    public void hasErrorValueMatcher_failureIsValidationSuccess() throws Exception {
        Helper.testFailingMatcher(Validation.<String,String>success("doh!"), ValidationMatchers.hasErrorValue(Matchers.equalTo("w00t")), "a failure with errors matching in turn [<\"w00t\">]","success[doh!]");
    }


    @Test
    public void hasErrorValueException_success() throws Exception {
        assertThat(Validation.failure(new IllegalStateException("not gonna work")), ValidationMatchers.hasErrorValueWhichIsAnException(new IllegalStateException("not gonna work")));
    }

    @Test
    public void hasErrorValueException_successMany() throws Exception {
        assertThat(Validation.failure(asList(
                    new IllegalStateException("not gonna work"),
                    new IOException("the network is reliable!")
                )),
                ValidationMatchers.hasErrorValueWhichIsAnException(
                        new IllegalStateException("not gonna work"),
                        new IOException("the network is reliable!")
                ));
    }

    @Test
    public void hasErrorValueException_failureDifferingMessage() throws Exception {
        Helper.testFailingMatcher(
                Validation.failure(new IllegalStateException("not gonna work")),
                ValidationMatchers.hasErrorValueWhichIsAnException(new IllegalStateException("nah")),
                "a failure with errors containing [<java.lang.IllegalStateException: nah>]",
                "failure[[java.lang.IllegalStateException: not gonna work]]"
        );
    }

    @Test
    public void hasErrorValueException_failureDifferingType() throws Exception {
        Helper.testFailingMatcher(
                Validation.failure(new IllegalStateException("nah")),
                ValidationMatchers.hasErrorValueWhichIsAnException(new UnsupportedOperationException("nah")),
                "a failure with errors containing [<java.lang.UnsupportedOperationException: nah>]",
                "failure[[java.lang.IllegalStateException: nah]]"
        );
    }

    @Test
    public void hasErrorValueException_failureIsValidationSuccess() throws Exception {
        Helper.testFailingMatcher(
                Validation.<String,String>success("doh!"),
                ValidationMatchers.hasErrorValueWhichIsAnException(new IllegalStateException("foo")),
                "a failure with errors containing [<java.lang.IllegalStateException: foo>]",
                "success[doh!]"
        );
    }
}