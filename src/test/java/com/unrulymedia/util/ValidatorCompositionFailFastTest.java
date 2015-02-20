package com.unrulymedia.util;

import com.unrulymedia.util.Validator;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;

import static com.unrulymedia.util.testutils.ValidatorMatchers.isSuccessNotFailure;
import static org.junit.Assert.assertThat;

public class ValidatorCompositionFailFastTest {
    @Test
    public void theCompositionOfASuccessAndASuccess_shouldBeASuccess() throws Exception {
        Validator<String, Object> first = Validator.success("first");
        Validator<String, Object> second = Validator.success("second");

        Validator<List<String>, Object> composition = first.compose(second);
        assertThat(composition, isSuccessNotFailure());
        assertThat(composition.get(), Matchers.contains("first", "second"));
    }

    @Test
    public void theCompositionOfASequenceOfSuccesses_shouldBeASuccess() throws Exception {
        Validator<String, Object> first = Validator.success("first");
        Validator<String, Object> second = Validator.success("second");
        Validator<String, Object> third = Validator.success("third");

        Validator<List<String>, Object> composition = first.compose(second).compose(third);
        assertThat(composition, isSuccessNotFailure());
        assertThat(composition.get(), Matchers.contains("first", "second", "third"));
    }

    @Test
    public void shouldBarf() throws Exception {
        Validator<String, Object> first = Validator.success("first");
        Validator<Integer, Object> second = Validator.success(1);
        Validator<Boolean, Object> third = Validator.success(true);

        Validator<List<Boolean>, Object> compose = first.compose(second).compose(third);

        System.out.print(compose);
    }
}
