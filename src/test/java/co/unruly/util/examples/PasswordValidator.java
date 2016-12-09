package co.unruly.util.examples;

import co.unruly.util.Validator;
import co.unruly.util.Validation;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static co.unruly.matchers.ValidationMatchers.*;
import static co.unruly.util.Validator.allOf;
import static co.unruly.util.Validator.from;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class PasswordValidator {

    private List<String> oldPasswords;

    Predicate<String> hasLengthAtLeast10 = pw -> pw.length() >= 10;
    Predicate<String> hasAtLeastOneNumeral = pw -> pw.matches(".*\\d.*");
    Predicate<String> hasNotBeenUsedBefore = pw -> !oldPasswords.contains(pw);

    Validator<String,String> numeral = from(hasAtLeastOneNumeral, "Has fewer than 1 numeral");
    Validator<String,String> length  = from(hasLengthAtLeast10, "Has fewer than 10 characters");
    Validator<String,String> usedBefore  = from(hasNotBeenUsedBefore, "Has been used before");

    Validator<String,String> passwordValidation = length.compose(usedBefore).compose(numeral);

    @Test
    public void shouldValidateAPassword() throws Exception {
        oldPasswords = Collections.emptyList();

        Validation<String, String> validation = passwordValidation.validate("password!");

        assertThat(validation, isFailureNotSuccess());
        assertThat(validation.getErrors(), contains("Has fewer than 10 characters", "Has fewer than 1 numeral"));
    }

    @Test
    public void shouldSuccessfullyValidateAGoodPassword() throws Exception {
        oldPasswords = Arrays.asList("1234", "6789");

        Validation<String, String> validation = passwordValidation.validate("1234567890");

        assertThat(validation,isSuccessNotFailure());
        assertThat(validation,hasValue("1234567890"));
        assertThat(validation.getErrors(),empty());
    }

    @Test
    public void shouldBuildACompoundValidatorUsingAllOf() throws Exception {
        oldPasswords = Arrays.asList("password!");
        final Validator<String, String> newPasswordValidator = allOf(
            from(pw -> pw.length() >= 10, "Has fewer than 10 characters"),
            from(pw -> pw.matches("\\d"), "Has fewer than 1 numeral"),
            from(oldPasswords::contains, "Has been used before")
        );

        Validation<String, String> validation = newPasswordValidator.validate("password!");

        assertThat(validation, isFailureNotSuccess());
        assertThat(validation.getErrors(), contains("Has fewer than 10 characters", "Has fewer than 1 numeral"));
    }
}
