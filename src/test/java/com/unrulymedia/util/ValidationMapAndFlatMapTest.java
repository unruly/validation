package com.unrulymedia.util;

import com.unrulymedia.util.testutils.ValidationMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class ValidationMapAndFlatMapTest {

    // Map

    @Test
    public void shouldMapOverASuccess() throws Exception {
        Validation<Integer, ?> validation = Validation.success(3);
        assertThat(validation.map( a -> a + " cows"), ValidationMatchers.hasValue("3 cows"));
    }

    @Test
    public void shouldMapOverAFailure() throws Exception {
        Validation<Integer, Integer> failure = Validation.failure(-1);
        assertThat(failure.map(a -> a + " geese"), ValidationMatchers.isFailureNotSuccess());
        assertThat(failure, ValidationMatchers.hasErrorValue(-1));
    }

    @Test
    public void shouldBeFailureIfMapMapperThrows() throws Exception {
        Validation<Integer,?> validation = Validation.success(3);

        Validation<String,?> mapped = validation.map(a -> {
            throw new Exception("oh dear");
        });
        assertThat(mapped, ValidationMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidationMatchers.hasErrorValueWhichIsAnException(new Exception("oh dear")));
    }

    @Test
    public void shouldBeFailureIfMapMapperReturnsNull()  {
        Validation<Integer, ?> validation = Validation.success(3);

        Validation<String, ?> mapped = validation.map(a -> null);
        assertThat(mapped, ValidationMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidationMatchers.hasErrorValueWhichIsAnException(new NullPointerException()));
    }

    // FlatMap

    @Test
    public void shouldFlatMapOverASuccess() throws Exception {
        Validation<Integer, ?> validation = Validation.success(3);
        assertThat(validation.flatMap(a -> Validation.success(a + " toots on the horn")), ValidationMatchers.hasValue("3 toots on the horn"));
    }

    @Test
    public void shouldFlatMapOverAFailure() throws Exception {
        Validation<?, String> validation = Validation.failure("whoops");
        assertThat(validation.flatMap(a -> Validation.success(a + " toots on the horn")), ValidationMatchers.isFailureNotSuccess());
        assertThat(validation, ValidationMatchers.hasErrorValue("whoops"));
    }

    @Test
    public void shouldBeFailureIfFlatMapMapperThrows() throws Exception {
        Validation<Integer, ?> validation = Validation.success(3);
        Validation<String, ? > mapped = validation.flatMap((a) -> {
            throw new Exception("foo");
        });
        assertThat(mapped, ValidationMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidationMatchers.hasErrorValueWhichIsAnException(new Exception("foo")));
    }

    @Test
    public void shouldBeFailureIfFlatMapMapperReturnsNull() throws Exception {
        Validation<Integer, ?> validation = Validation.success(3);
        Validation<String, ?> mapped = validation.flatMap((a) -> null);
        assertThat(mapped, ValidationMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidationMatchers.hasErrorValueWhichIsAnException(new NullPointerException()));
    }

}
