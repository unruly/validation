package com.unrulymedia.util.testutils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.BaseStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class FiniteStreamMatcher {

    private static <T> boolean recur(Iterator<T> expectedIterator, Iterator<T> actualIterator, List<T> observedExpected, List<T> observedActual) {
        if (!expectedIterator.hasNext() && !actualIterator.hasNext()) {
            return true;
        }
        if (expectedIterator.hasNext() && actualIterator.hasNext()) {
            T nextExpected = expectedIterator.next();
            observedExpected.add(nextExpected);
            T nextActual = actualIterator.next();
            observedActual.add(nextActual);
            if(nextExpected.equals(nextActual)) {
                return recur(expectedIterator, actualIterator, observedExpected, observedActual);
            }
        }
        expectedIterator.forEachRemaining(observedExpected::add);
        actualIterator.forEachRemaining(observedActual::add);
        return false;
    }

    private static abstract class BaseStreamMatcher<T,S extends BaseStream<T,?>> extends TypeSafeMatcher<S> {
        protected final List<T> expectedAccumulator = new LinkedList<>();
        protected final List<T> actualAccumulator = new LinkedList<>();

        @Override
        public void describeTo(Description description) {
            describe(description, expectedAccumulator);
        }

        @Override
        protected void describeMismatchSafely(S item, Description description) {
            describe(description, actualAccumulator);
        }

        private void describe(Description description, List<T> values) {
            description.appendText("a Stream of ");
            description.appendValueList("[",",","]", values);
        }
    }

    public static <T,S extends BaseStream<T,S>> Matcher<BaseStream<T,S>> hasSameItems(BaseStream<T, S> expected) {
        return new BaseStreamMatcher<T,BaseStream<T,S>>() {
            @Override
            protected boolean matchesSafely(BaseStream<T,S> actual) {
                Iterator<T> actualIterator = actual.iterator();
                Iterator<T> expectedIterator = expected.iterator();
                
                return recur(expectedIterator, actualIterator, expectedAccumulator, actualAccumulator);
            }
        };
    }

    public static <T> Matcher<Stream<T>> hasSameItems(Stream<T> expected) {
        return new BaseStreamMatcher<T,Stream<T>>() {
            @Override
            protected boolean matchesSafely(Stream<T> actual) {
                Iterator<T> actualIterator = actual.iterator();
                Iterator<T> expectedIterator = expected.iterator();

                return recur(expectedIterator, actualIterator, expectedAccumulator, actualAccumulator);
            }
        };
    }

    @Test
    public void shouldNotMatchStreamsOfDifferingSingleItem() throws Exception {
        assertThat(Stream.of("a"), is(not(hasSameItems(Stream.of("b")))));
    }

    @Test
    public void shouldNotMatchStreamsOfDifferingLength() throws Exception {
        assertThat(Stream.of("a"), is(not(hasSameItems(Stream.of("a", "b")))));
    }

    @Test
    public void shouldNotMatchStreamsOfDifferingItems() throws Exception {
        assertThat(Stream.of("a","c"), is(not(hasSameItems(Stream.of("a", "b")))));
    }

    @Test
    public void shouldMatchEmptyStreams() throws Exception {
        assertThat(Stream.empty(), hasSameItems(Stream.empty()));
    }


    @Test
    public void shouldMatchIntStream() throws Exception {
        assertThat(IntStream.range(1, 10), hasSameItems(IntStream.range(1, 10)));
    }

    @Test
    public void shouldMatchIdenticalStreams() throws Exception {
        assertThat(Stream.of("a", "b", "c"), hasSameItems(Stream.of("a", "b", "c")));
    }

    @Test
    public void shouldShowErrorOfStream() throws Exception {
        try {
            assertThat(Stream.of("a","b","c","d","e"), hasSameItems(Stream.of("a", "b", "c", "d", "e", "f", "g", "h")));
        } catch (AssertionError e) {
            assertThat(e.toString(), containsString("Expected: a Stream of [\"a\",\"b\",\"c\",\"d\",\"e\",\"f\",\"g\",\"h\"]"));
            assertThat(e.toString(),containsString("but: a Stream of [\"a\",\"b\",\"c\",\"d\",\"e\"]"));
        }
    }

    @Test
    public void shouldShowErrorOfBaseStream() throws Exception {
        try {
            assertThat(IntStream.range(8,10), hasSameItems(IntStream.range(0, 6)));
        } catch (AssertionError e) {
            assertThat(e.toString(), containsString("Expected: a Stream of [<0>,<1>,<2>,<3>,<4>,<5>]"));
            assertThat(e.toString(),containsString("but: a Stream of [<8>,<9>]"));
        }
    }
}
