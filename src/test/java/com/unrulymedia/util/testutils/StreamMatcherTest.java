package com.unrulymedia.util.testutils;

import org.junit.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.unrulymedia.util.testutils.StreamMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class StreamMatcherTest {
    @Test
    public void shouldNotMatchStreamsOfDifferingSingleItem() throws Exception {
        assertThat(Stream.of("a"), is(not(equalTo(Stream.of("b")))));
    }

    @Test
    public void shouldNotMatchStreamOfDifferingSingleItem() throws Exception {
        assertThat(Stream.of("a"), is(not(contains("b"))));
    }

    @Test
    public void shouldNotMatchStreamsOfDifferingLength() throws Exception {
        assertThat(Stream.of("a"), is(not(equalTo(Stream.of("a", "b")))));
    }

    @Test
    public void shouldNotMatchStreamOfDifferingLength() throws Exception {
        assertThat(Stream.of("a"), is(not(contains("a", "b"))));
    }

    @Test
    public void shouldNotMatchStreamsOfDifferingItems() throws Exception {
        assertThat(Stream.of("a","c"), is(not(equalTo(Stream.of("a", "b")))));
    }

    @Test
    public void shouldNotMatchStreamOfDifferingItems() throws Exception {
        assertThat(Stream.of("a","c"), is(not(contains("a", "b"))));
    }

    @Test
    public void shouldMatchEmptyStreams() throws Exception {
        assertThat(Stream.empty(), equalTo(Stream.empty()));
    }

    @Test
    public void emptyStream() throws Exception {
        assertThat(Stream.empty(),empty());
    }

    @Test
    public void nonEmptyStreamIsNotEmpty() throws Exception {
        try {
            assertThat(Stream.of(3), StreamMatchers.empty());
            fail();
        } catch (AssertionError e) {
            assertThat(e.toString(), containsString("A non empty Stream starting with 3"));
        }
    }

    @Test
    public void shouldMatchIntStream() throws Exception {
        assertThat(IntStream.range(1, 10), equalTo(IntStream.range(1, 10)));
    }

    @Test
    public void shouldMatchIntStream_varargs() throws Exception {
        assertThat(IntStream.range(1, 4), contains(1,2,3));
    }

    @Test
    public void shouldMatchIdenticalStreams() throws Exception {
        assertThat(Stream.of("a", "b", "c"), equalTo(Stream.of("a", "b", "c")));
    }

    @Test
    public void shouldMatchStreamWithIdenticalItems() throws Exception {
        assertThat(Stream.of("a", "b", "c"), contains("a", "b", "c"));
    }

    @Test
    public void shouldMatchUpToLimit() throws Exception {
        assertThat(Stream.iterate(0,i -> i + 1), startsWith(Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 10));
    }

    @Test
    public void shouldMatchStartingItems() throws Exception {
        assertThat(Stream.of("a","b","c","d","e", "f", "g", "h"), startsWith("a", "b", "c", "d", "e"));
    }

    @Test
    public void shouldMatchStartingItems_int() throws Exception {
        assertThat(IntStream.range(0,Integer.MAX_VALUE), startsWithInt(0, 1, 2, 3, 4));
    }

    @Test
    public void shouldShowExpectedAndActualIfError_equalTo() throws Exception {
        try {
            assertThat(Stream.of("a","b","c","d","e"), equalTo(Stream.of("a", "b", "c", "d", "e", "f", "g", "h")));
            fail();
        } catch (AssertionError e) {
            assertThat(e.toString(), containsString("Expected: a Stream of [\"a\",\"b\",\"c\",\"d\",\"e\",\"f\",\"g\",\"h\"]"));
            assertThat(e.toString(),containsString("but: a Stream of [\"a\",\"b\",\"c\",\"d\",\"e\"]"));
        }
    }


    @Test
    public void shouldShowExpectedAndActualIfError_contains() throws Exception {
        try {
            assertThat(Stream.of("a","b","c","d","e"), contains("a", "b", "c", "d", "e", "f", "g", "h"));
            fail();
        } catch (AssertionError e) {
            assertThat(e.toString(), containsString("Expected: a Stream of [\"a\",\"b\",\"c\",\"d\",\"e\",\"f\",\"g\",\"h\"]"));
            assertThat(e.toString(),containsString("but: a Stream of [\"a\",\"b\",\"c\",\"d\",\"e\"]"));
        }
    }

    @Test
    public void shouldShowErrorOfBaseStream() throws Exception {
        try {
            assertThat(IntStream.range(8,10), equalTo(IntStream.range(0, 6)));
            fail();
        } catch (AssertionError e) {
            assertThat(e.toString(), containsString("Expected: a Stream of [<0>,<1>,<2>,<3>,<4>,<5>]"));
            assertThat(e.toString(),containsString("but: a Stream of [<8>,<9>]"));
        }
    }
}
