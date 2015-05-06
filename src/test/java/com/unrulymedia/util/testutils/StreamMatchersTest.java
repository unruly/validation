package com.unrulymedia.util.testutils;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.stream.BaseStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.unrulymedia.util.testutils.StreamMatchers.*;
import static com.unrulymedia.util.testutils.StreamMatchers.equalTo;
import static com.unrulymedia.util.testutils.StreamMatchers.startsWith;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class StreamMatchersTest {
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
        testFailingMatcher(empty(),Stream.of(3),"An empty Stream","A non empty Stream starting with <3>");
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
    public void allMatch_success() throws Exception {
        assertThat(Stream.of("bar","baz"),allMatch(containsString("a")));
    }

    @Test
    public void allMatch_failure() throws Exception {
        Matcher<Stream<String>> matcher = StreamMatchers.allMatch(containsString("a"));
        Stream<String> testData = Stream.of("bar", "bar", "foo", "grault", "garply", "waldo");
        testFailingMatcher(matcher, testData, "All to match <a string containing \"a\">", "Item failed to match: \"foo\"");
    }

    @Test
    public void allMatch_empty() throws Exception {
        assertThat(Stream.empty(),allMatch(containsString("foo")));
    }

    @Test
    public void anyMatch_success() throws Exception {
        assertThat(Stream.of("bar", "bar", "foo", "grault", "garply", "waldo"),StreamMatchers.anyMatch(containsString("ald")));
    }

    @Test
    public void anyMatch_failure() throws Exception {
        Matcher<Stream<String>> matcher = StreamMatchers.anyMatch(containsString("z"));
        Stream<String> testData = Stream.of("bar", "bar", "foo", "grault", "garply", "waldo");
        testFailingMatcher(matcher,testData,"Any to match <a string containing \"z\"","None of these items matched: [\"bar\",\"bar\",\"foo\",\"grault\",\"garply\",\"waldo\"]");
    }

    @Test
    public void anyMatch_empty() throws Exception {
        assertThat(Stream.empty(),not(anyMatch(containsString("foo"))));
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
        Matcher<BaseStream<String, Stream<String>>> matcher = equalTo(Stream.of("a", "b", "c", "d", "e", "f", "g", "h"));
        Stream<String> testData = Stream.of("a", "b", "c", "d", "e");
        testFailingMatcher(matcher, testData,"Stream of [\"a\",\"b\",\"c\",\"d\",\"e\",\"f\",\"g\",\"h\"]","Stream of [\"a\",\"b\",\"c\",\"d\",\"e\"]");
    }


    @Test
    public void shouldShowExpectedAndActualIfError_contains() throws Exception {
        Stream<String> testData = Stream.of("a", "b", "c", "d", "e");
        Matcher<BaseStream<String, Stream<String>>> matcher = contains("a", "b", "c", "d", "e", "f", "g", "h");
        testFailingMatcher(matcher,testData,"Stream of [\"a\",\"b\",\"c\",\"d\",\"e\",\"f\",\"g\",\"h\"]","Stream of [\"a\",\"b\",\"c\",\"d\",\"e\"]");
    }

    @Test
    public void shouldShowErrorOfBaseStream() throws Exception {
        IntStream testData = IntStream.range(8, 10);
        Matcher<BaseStream<Integer, IntStream>> matcher = equalTo(IntStream.range(0, 6));
        testFailingMatcher(matcher,testData,"Stream of [<0>,<1>,<2>,<3>,<4>,<5>]", "Stream of [<8>,<9>]");
    }

    @Test
    public void startsWithAll_success() throws Exception {
        assertThat(Stream.generate(() -> 10), StreamMatchers.startsWithAll(Matchers.equalTo(10),100));
    }

    @Test
    public void startsWithAll_fail() throws Exception {
        testFailingMatcher(StreamMatchers.startsWithAll(Matchers.equalTo(10), 100), Stream.generate(() -> 11), "All to match <<10>>", "Item failed to match: <11>");
    }

    @Test
    public void startsWithAny_success() throws Exception {
        assertThat(Stream.iterate(0, i -> i + 1), StreamMatchers.startsWithAny(Matchers.equalTo(10), 100));
    }

    @Test
    public void startsWithAny_fail() throws Exception {
        testFailingMatcher(StreamMatchers.startsWithAny(Matchers.equalTo(-1), 10),Stream.iterate(0, i -> i + 1), "Any to match <<-1>>", "None of these items matched: [<0>,<1>,<2>,<3>,<4>,<5>,<6>,<7>,<8>,<9>]");
    }

    private <S> void testFailingMatcher(Matcher<S> matcher, S testData, String expectedDescription, String actualDescription) {
        try {
            assertThat(testData,matcher);
            throw new Exception();
        } catch (AssertionError e) {
            assertThat(e.toString(), stringContainsInOrder(asList(expectedDescription, actualDescription)));
        } catch (Exception ignored) {
            fail();
        }
    }
}
