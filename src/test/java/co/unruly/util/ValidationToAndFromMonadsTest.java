package co.unruly.util;

import co.unruly.matchers.StreamMatchers;
import co.unruly.matchers.ValidationMatchers;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static co.unruly.matchers.OptionalMatchers.contains;
import static co.unruly.matchers.OptionalMatchers.empty;
import static org.junit.Assert.assertThat;

public class ValidationToAndFromMonadsTest {

    @Test
    public void shouldCreateASuccessFromNonEmptyOptional() throws Exception {
        Validation<String, NoSuchElementException> validation = Validation.from(Optional.of("hello!"));
        assertThat(validation, ValidationMatchers.isSuccessNotFailure());
        assertThat(validation, ValidationMatchers.hasValue("hello!"));
    }

    @Test
    public void shouldCreateAFailureFromEmptyOptional() throws Exception {
        Validation<String, NoSuchElementException> validation = Validation.from(Optional.empty());
        assertThat(validation, ValidationMatchers.isFailureNotSuccess());
        assertThat(validation, ValidationMatchers.hasErrorValueWhichIsAnException(new NoSuchElementException()));
    }

    @Test
    public void shouldCreateANonEmptyOptionalFromASuccess() throws Exception {
        Validation<String,String> success = Validation.success("woop");
        Optional<String> opt = success.toOptional();
        assertThat(opt, contains("woop"));
    }

    @Test
    public void shouldCreateEmptyOptionalFromFailure() throws Exception {
        Validation<Object, String> validation = Validation.failure("meh");
        Optional<Object> optional = validation.toOptional();
        assertThat(optional, empty());
    }

    @Test
    public void shouldCreateAnEmptyStreamFromFailure() throws Exception {
        Validation<Integer, String> validation = Validation.failure("meh");
        assertThat(validation.stream(), StreamMatchers.empty());
    }

    @Test
    public void shouldCreateASingletonStreamOfValueIfSuccess() throws Exception {
        Validation<String, Object> validation = Validation.success("Yay!");
        assertThat(validation.stream(), StreamMatchers.contains("Yay!"));
    }
}
