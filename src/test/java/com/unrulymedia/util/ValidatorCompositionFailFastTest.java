package com.unrulymedia.util;

import static org.junit.Assert.assertThat;

public class ValidatorCompositionFailFastTest {
  /* @Test
    public void theCompositionOfASuccessAndASuccess_shouldBeASuccess() throws Exception {
        Validator<String, Object> first = Validator.success("first");
        Validator<String, Object> second = Validator.success("second");

        ComposedValidator<String, Object> composition = first.compose(second);
        assertThat(composition.get(), Matchers.allisSuccessNotFailureComposed());
        assertThat(composition.get(), Matchers.contains("first", "second"));
    }

    @Test
    public void theCompositionOfASequenceOfSuccesses_shouldBeASuccess() throws Exception {
        Validator<String, Object> first = Validator.success("first");
        Validator<String, Object> second = Validator.success("second");
        Validator<String, Object> third = Validator.success("third");

        ComposedValidator<String, Object> composition = first.compose(second).compose(third);
        assertThat(composition, isSuccessNotFailureComposed());
        assertThat(composition.get(), Matchers.contains("first", "second", "third"));
    }*/

   /* @Test
    public void shouldBarf() throws Exception {
        Validator<String, Object> first = Validator.success("first");
        Validator<Integer, Object> second = Validator.success(1);
        Validator<Boolean, Object> third = Validator.success(true);

        ComposedValidator<Boolean, Object> compose = first.compose(second).compose(third);

        System.out.print(compose);
    }*/
}
