package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidatorMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class ValidatorMapAndFlatMapTest {

    // Map

    @Test
    public void shouldMapOverASuccess() throws Exception {
        Validator<Integer, ?> validator = Validator.success(3);
        assertThat(validator.map( a -> a + " cows"), ValidatorMatchers.hasValue("3 cows"));
    }

    @Test
    public void shouldMapOverAFailure() throws Exception {
        Validator<Integer, Integer> failure = Validator.failure(-1);
        assertThat(failure.map(a -> a + " geese"), ValidatorMatchers.isFailureNotSuccess());
        assertThat(failure, ValidatorMatchers.hasErrorValue(-1));
    }

    @Test
    public void shouldBeFailureIfMapMapperThrows() throws Exception {
        Validator<Integer,?> validator = Validator.success(3);

        Validator<String,?> mapped = validator.map(a -> {
            throw new Exception("oh dear");
        });
        assertThat(mapped, ValidatorMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidatorMatchers.hasErrorValueWhichIsAnException(new Exception("oh dear")));
    }

    @Test
    public void shouldBeFailureIfMapMapperReturnsNull()  {
        Validator<Integer, ?> validator = Validator.success(3);

        Validator<String, ?> mapped = validator.map(a -> null);
        assertThat(mapped, ValidatorMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidatorMatchers.hasErrorValueWhichIsAnException(new NullPointerException()));
    }

    // FlatMap

    @Test
    public void shouldFlatMapOverASuccess() throws Exception {
        Validator<Integer, ?> validator = Validator.success(3);
        assertThat(validator.flatMap(a -> Validator.success(a + " toots on the horn")), ValidatorMatchers.hasValue("3 toots on the horn"));
    }

    @Test
    public void shouldFlatMapOverAFailure() throws Exception {
        Validator<?, String> validator = Validator.failure("whoops");
        assertThat(validator.flatMap(a -> Validator.success(a + " toots on the horn")), ValidatorMatchers.isFailureNotSuccess());
        assertThat(validator, ValidatorMatchers.hasErrorValue("whoops"));
    }

    @Test
    public void shouldBeFailureIfFlatMapMapperThrows() throws Exception {
        Validator<Integer, ?> validator = Validator.success(3);
        Validator<String, ? > mapped = validator.flatMap((a) -> {
            throw new Exception("foo");
        });
        assertThat(mapped, ValidatorMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidatorMatchers.hasErrorValueWhichIsAnException(new Exception("foo")));
    }

    @Test
    public void shouldBeFailureIfFlatMapMapperReturnsNull() throws Exception {
        Validator<Integer, ?> validator = Validator.success(3);
        Validator<String, ?> mapped = validator.flatMap((a) -> null);
        assertThat(mapped, ValidatorMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidatorMatchers.hasErrorValueWhichIsAnException(new NullPointerException()));
    }

}
