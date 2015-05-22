package com.unrulymedia.util;

import matchers.ValidationMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class ValidationMapAndFlatMapTest {

    // Map

    @Test
    public void shouldMapOverASuccess() throws Exception {
        Validation<Integer, String> validation = Validation.success(3);
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
        Validation<Integer,String> validation = Validation.success(3);

        Validation<String,?> mapped = validation.tryMap(a -> {
            throw new Exception("oh dear");
        });
        assertThat(mapped, ValidationMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidationMatchers.hasErrorValueWhichIsAnException(new Exception("oh dear")));
    }

    // FlatMap

    @Test
    public void shouldFlatMapOverASuccess() throws Exception {
        Validation<Integer, String> validation = Validation.success(3);
        assertThat(validation.flatMap(a -> Validation.success(a + " toots on the horn")), ValidationMatchers.hasValue("3 toots on the horn"));
    }

    @Test
    public void shouldFlatMapOverAFailure() throws Exception {
        Validation<String, String> validation = Validation.failure("whoops");
        Validation<String, String> flatMapped = validation.flatMap(a -> Validation.success(a + " toots on the horn"));
        assertThat(flatMapped, ValidationMatchers.isFailureNotSuccess());
        assertThat(flatMapped, ValidationMatchers.hasErrorValue("whoops"));
    }

    @Test
    public void shouldBeFailureIfFlatMapMapperThrows() throws Exception {
        Validation<Integer, ?> validation = Validation.success(3);
        Validation<String, ? > mapped = validation.tryFlatMap((a) -> {
            throw new Exception("foo");
        });
        assertThat(mapped, ValidationMatchers.isFailureNotSuccess());
        assertThat(mapped, ValidationMatchers.hasErrorValueWhichIsAnException(new Exception("foo")));
    }
}
