package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidatorMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class ValidationMapAndFlatMapTest {

    // Map

    @Test
    public void shouldMapOverASuccess() throws Exception {
        Validation<Integer, ?> validation = Validation.success(3);
        assertThat(validation.map( a -> a + " cows"), ValidatorMatchers.hasValue("3 cows"));
    }

    @Test
    public void shouldMapOverAFailure() throws Exception {
        Validation<Integer, Integer> failure = Validation.failure(-1);
        assertThat(failure.map(a -> a + " geese"), ValidatorMatchers.isFailureNotSuccess());
        assertThat(failure, ValidatorMatchers.hasErrorValue(-1));
    }

    @Test
    public void shouldBeFailureIfMapMapperThrows() throws Exception {
        Validation<Integer,?> validation = Validation.success(3);

        Validation<String,?> mapped = validation.map(a -> {
            throw new Exception("oh dear");
        });
        assertThat(mapped, ValidatorMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidatorMatchers.hasErrorValueWhichIsAnException(new Exception("oh dear")));
    }

    @Test
    public void shouldBeFailureIfMapMapperReturnsNull()  {
        Validation<Integer, ?> validation = Validation.success(3);

        Validation<String, ?> mapped = validation.map(a -> null);
        assertThat(mapped, ValidatorMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidatorMatchers.hasErrorValueWhichIsAnException(new NullPointerException()));
    }

    // FlatMap

    @Test
    public void shouldFlatMapOverASuccess() throws Exception {
        Validation<Integer, ?> validation = Validation.success(3);
        assertThat(validation.flatMap(a -> Validation.success(a + " toots on the horn")), ValidatorMatchers.hasValue("3 toots on the horn"));
    }

    @Test
    public void shouldFlatMapOverAFailure() throws Exception {
        Validation<?, String> validation = Validation.failure("whoops");
        assertThat(validation.flatMap(a -> Validation.success(a + " toots on the horn")), ValidatorMatchers.isFailureNotSuccess());
        assertThat(validation, ValidatorMatchers.hasErrorValue("whoops"));
    }

    @Test
    public void shouldBeFailureIfFlatMapMapperThrows() throws Exception {
        Validation<Integer, ?> validation = Validation.success(3);
        Validation<String, ? > mapped = validation.flatMap((a) -> {
            throw new Exception("foo");
        });
        assertThat(mapped, ValidatorMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidatorMatchers.hasErrorValueWhichIsAnException(new Exception("foo")));
    }

    @Test
    public void shouldBeFailureIfFlatMapMapperReturnsNull() throws Exception {
        Validation<Integer, ?> validation = Validation.success(3);
        Validation<String, ?> mapped = validation.flatMap((a) -> null);
        assertThat(mapped, ValidatorMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidatorMatchers.hasErrorValueWhichIsAnException(new NullPointerException()));
    }

}
