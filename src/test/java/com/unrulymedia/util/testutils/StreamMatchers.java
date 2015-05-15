package com.unrulymedia.util.testutils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.*;

public class StreamMatchers {

    public static <T,S extends BaseStream<T,S>> Matcher<BaseStream<T,S>> empty() {
        return new TypeSafeMatcher<BaseStream<T, S>>() {

            private Iterator<T> actualIterator;

            @Override
            protected boolean matchesSafely(BaseStream<T, S> actual) {
                actualIterator = actual.iterator();
                return !actualIterator.hasNext();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("An empty Stream");
            }

            @Override
            protected void describeMismatchSafely(BaseStream<T, S> item, Description description) {
                description.appendText("A non empty Stream starting with ").appendValue(actualIterator.next());
            }
        };
    }

    public static <T,S extends BaseStream<T,S>> Matcher<BaseStream<T,S>> equalTo(BaseStream<T, S> expected) {
        return new BaseStreamMatcher<T,BaseStream<T,S>>() {
            @Override
            protected boolean matchesSafely(BaseStream<T,S> actual) {
                return remainingItemsEqual(expected.iterator(), actual.iterator());
            }
        };
    }

    public static <T> Matcher<Stream<T>> startsWith(Stream<T> expected, long limit) {
        return new BaseStreamMatcher<T,Stream<T>>() {
            @Override
            protected boolean matchesSafely(Stream<T> actual) {
                return remainingItemsEqual(expected.iterator(), actual.limit(limit).iterator());
            }
        };
    }

    public static Matcher<DoubleStream> startsWith(DoubleStream expected, long limit) {
        return new BaseStreamMatcher<Double,DoubleStream>() {
            @Override
            protected boolean matchesSafely(DoubleStream actual) {
                return remainingItemsEqual(expected.iterator(), actual.limit(limit).iterator());
            }
        };
    }

    public static Matcher<IntStream> startsWith(IntStream expected, long limit) {
        return new BaseStreamMatcher<Integer,IntStream>() {
            @Override
            protected boolean matchesSafely(IntStream actual) {
                return remainingItemsEqual(expected.iterator(), actual.limit(limit).iterator());
            }
        };
    }

    public static Matcher<LongStream> startsWith(LongStream expected, long limit) {
        return new BaseStreamMatcher<Long,LongStream>() {
            @Override
            protected boolean matchesSafely(LongStream actual) {
                return remainingItemsEqual(expected.iterator(), actual.limit(limit).iterator());
            }
        };
    }

    private static void describeToStartsAllWith(Description description, long limit, Matcher<?> matcher) {
        description
                .appendText("First ")
                .appendText(Long.toString(limit))
                .appendText(" to match ")
                .appendValue(matcher);
    }

    public static <T> Matcher<Stream<T>> startsWithAll(Matcher<T> matcher, long limit) {
        return new StreamAllMatches<T>(matcher) {
            @Override
            protected boolean matchesSafely(Stream<T> actual) {
                return super.matchesSafely(actual.limit(limit));
            }

            @Override
            public void describeTo(Description description) {
                describeToStartsAllWith(description, limit, matcher);
            }
        };

    }

    public static Matcher<LongStream> startsWithAllLong(Matcher<Long> matcher, long limit) {
        return new LongStreamAllMatches(matcher) {
            @Override
            protected boolean matchesSafely(LongStream actual) {
                return super.matchesSafely(actual.limit(limit));
            }

            @Override
            public void describeTo(Description description) {
                describeToStartsAllWith(description, limit, matcher);
            }
        };
    }

    public static Matcher<IntStream> startsWithAllInt(Matcher<Integer> matcher, long limit) {
        return new IntStreamAllMatches(matcher) {
            @Override
            protected boolean matchesSafely(IntStream actual) {
                return super.matchesSafely(actual.limit(limit));
            }

            @Override
            public void describeTo(Description description) {
                describeToStartsAllWith(description, limit, matcher);
            }
        };
    }

    public static Matcher<DoubleStream> startsWithAllDouble(Matcher<Double> matcher, long limit) {
        return new DoubleStreamAllMatches(matcher) {
            @Override
            protected boolean matchesSafely(DoubleStream actual) {
                return super.matchesSafely(actual.limit(limit));
            }

            @Override
            public void describeTo(Description description) {
                describeToStartsAllWith(description, limit, matcher);
            }
        };
    }

    private static void describeToStartsAnyWith(Description description, long limit, Matcher<?> matcher) {
        description
                .appendText("Any of first ")
                .appendText(Long.toString(limit))
                .appendText(" to match ")
                .appendValue(matcher);
    }

    public static <T> Matcher<Stream<T>> startsWithAny(Matcher<T> matcher, long limit) {
        return new StreamAnyMatches<T>(matcher) {
            @Override
            protected boolean matchesSafely(Stream<T> actual) {
                return super.matchesSafely(actual.limit(limit));
            }

            @Override
            public void describeTo(Description description) {
                describeToStartsAnyWith(description, limit, matcher);
            }
        };
    }

    public static Matcher<LongStream> startsWithAnyLong(Matcher<Long> matcher, long limit) {
        return new LongStreamAnyMatches(matcher) {
            @Override
            protected boolean matchesSafely(LongStream actual) {
                return super.matchesSafely(actual.limit(limit));
            }

            @Override
            public void describeTo(Description description) {
                describeToStartsAnyWith(description, limit, matcher);
            }
        };
    }

    public static Matcher<DoubleStream> startsWithAnyDouble(Matcher<Double> matcher, long limit) {
        return new DoubleStreamAnyMatches(matcher) {
            @Override
            protected boolean matchesSafely(DoubleStream actual) {
                return super.matchesSafely(actual.limit(limit));
            }

            @Override
            public void describeTo(Description description) {
                describeToStartsAnyWith(description, limit, matcher);
            }
        };
    }

    public static Matcher<IntStream> startsWithAnyInt(Matcher<Integer> matcher, long limit) {
        return new IntStreamAnyMatches(matcher) {
            @Override
            protected boolean matchesSafely(IntStream actual) {
                return super.matchesSafely(actual.limit(limit));
            }

            @Override
            public void describeTo(Description description) {
                describeToStartsAnyWith(description, limit, matcher);
            }
        };
    }

    @SafeVarargs
    public static <T,S extends BaseStream<T,S>> Matcher<BaseStream<T,S>> contains(T... expected) {
        return new BaseStreamMatcher<T,BaseStream<T,S>>() {
            @Override
            protected boolean matchesSafely(BaseStream<T,S> actual) {
                return remainingItemsEqual(new ArrayIterator<>(expected), actual.iterator());
            }
        };
    }

    public static <T> Matcher<Stream<T>> allMatch(Matcher<T> matcher) {
        return new StreamAllMatches<T>(matcher) {
            @Override
            public void describeTo(Description description) {
                description.appendText("All to match ").appendValue(matcher);
            }
        };
    }

    public static Matcher<IntStream> allMatchInt(Matcher<Integer> matcher) {
        return new IntStreamAllMatches(matcher) {
            @Override
            public void describeTo(Description description) {
                description.appendText("All to match ").appendValue(matcher);
            }
        };
    }

    public static Matcher<LongStream> allMatchLong(Matcher<Long> matcher) {
        return new LongStreamAllMatches(matcher) {
            @Override
            public void describeTo(Description description) {
                description.appendText("All to match ").appendValue(matcher);
            }
        };
    }

    public static Matcher<DoubleStream> allMatchDouble(Matcher<Double> matcher) {
        return new DoubleStreamAllMatches(matcher) {
            @Override
            public void describeTo(Description description) {
                description.appendText("All to match ").appendValue(matcher);
            }
        };
    }

    public static <T> Matcher<Stream<T>> anyMatch(Matcher<T> matcher) {
        return new StreamAnyMatches<T>(matcher) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Any to match ").appendValue(matcher);
            }
        };
    }

    public static Matcher<LongStream> anyMatchLong(Matcher<Long> matcher) {
        return new LongStreamAnyMatches(matcher) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Any to match ").appendValue(matcher);
            }
        };
    }

    public static Matcher<DoubleStream> anyMatchDouble(Matcher<Double> matcher) {
        return new DoubleStreamAnyMatches(matcher) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Any to match ").appendValue(matcher);
            }
        };
    }

    public static Matcher<IntStream> anyMatchInt(Matcher<Integer> matcher) {
        return new IntStreamAnyMatches(matcher) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Any to match ").appendValue(matcher);
            }
        };
    }

    @SafeVarargs
    public static <T> Matcher<Stream<T>> startsWith(T... expected) {
        return new BaseStreamMatcher<T,Stream<T>>() {
            @Override
            protected boolean matchesSafely(Stream<T> actual) {
                return remainingItemsEqual(new ArrayIterator<>(expected), actual.limit(expected.length).iterator());
            }
        };
    }

    public static Matcher<DoubleStream> startsWithDouble(double... expected) {
        return new BaseStreamMatcher<Double,DoubleStream>() {
            @Override
            protected boolean matchesSafely(DoubleStream actual) {
                return remainingItemsEqual(new DoubleArrayIterator(expected), actual.limit(expected.length).iterator());
            }
        };
    }

    public static Matcher<LongStream> startsWithLong(long... expected) {
        return new BaseStreamMatcher<Long,LongStream>() {
            @Override
            protected boolean matchesSafely(LongStream actual) {
                return remainingItemsEqual(new LongArrayIterator(expected), actual.limit(expected.length).iterator());
            }
        };
    }

    public static Matcher<IntStream> startsWithInt(int... expected) {
        return new BaseStreamMatcher<Integer,IntStream>() {
            @Override
            protected boolean matchesSafely(IntStream actual) {
                return remainingItemsEqual(new IntArrayIterator(expected), actual.limit(expected.length).iterator());
            }
        };
    }

    private static abstract class BaseStreamMatcher<T,S extends BaseStream<T,?>> extends TypeSafeMatcher<S> {
        final List<T> expectedAccumulator = new LinkedList<>();
        final List<T> actualAccumulator = new LinkedList<>();

        @Override
        public void describeTo(Description description) {
            describe(description, expectedAccumulator);
        }

        @Override
        protected void describeMismatchSafely(S item, Description description) {
            describe(description, actualAccumulator);
        }

        private void describe(Description description, List<T> values) {
            description.appendText("Stream of ").appendValueList("[", ",", "]", values);
        }

        boolean remainingItemsEqual(Iterator<T> expectedIterator, Iterator<T> actualIterator) {
            if (!expectedIterator.hasNext() && !actualIterator.hasNext()) {
                return true;
            }
            if (expectedIterator.hasNext() && actualIterator.hasNext()) {
                T nextExpected = expectedIterator.next();
                expectedAccumulator.add(nextExpected);
                T nextActual = actualIterator.next();
                actualAccumulator.add(nextActual);
                if(nextExpected.equals(nextActual)) {
                    return remainingItemsEqual(expectedIterator, actualIterator);
                }
            }
            expectedIterator.forEachRemaining(expectedAccumulator::add);
            actualIterator.forEachRemaining(actualAccumulator::add);
            return false;
        }
    }

    private static void allMatchMismatch(Description mismatchDescription, long position, Object nonMatch) {
        mismatchDescription.appendText("Item ").appendText(Long.toString(position)).appendText(" failed to match: ").appendValue(nonMatch);
    }

    private static abstract class StreamAllMatches<T> extends TypeSafeMatcher<Stream<T>> {
        private T nonMatching;
        private long positionNonMatching = -1L;
        private final Matcher<T> matcher;

        StreamAllMatches(Matcher<T> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(Stream<T> actual) {
            return actual
                    .peek(i -> {nonMatching = i; positionNonMatching++;})
                    .allMatch(matcher::matches);
        }

        @Override
        protected void describeMismatchSafely(Stream<T> actual, Description mismatchDescription) {
            allMatchMismatch(mismatchDescription, positionNonMatching, nonMatching);
        }
    }

    private static abstract class IntStreamAllMatches extends TypeSafeMatcher<IntStream> {
        private int nonMatching;
        private long positionNonMatching = -1L;
        private final Matcher<Integer> matcher;

        IntStreamAllMatches(Matcher<Integer> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(IntStream actual) {
            return actual
                    .peek(i -> {nonMatching = i; positionNonMatching++;})
                    .allMatch(matcher::matches);
        }

        @Override
        protected void describeMismatchSafely(IntStream actual, Description mismatchDescription) {
            allMatchMismatch(mismatchDescription, positionNonMatching, nonMatching);
        }
    }

    private static abstract class LongStreamAllMatches extends TypeSafeMatcher<LongStream> {
        private long nonMatching;
        private long positionNonMatching = -1L;
        private final Matcher<Long> matcher;

        LongStreamAllMatches(Matcher<Long> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(LongStream actual) {
            return actual
                    .peek(i -> {nonMatching = i; positionNonMatching++;})
                    .allMatch(matcher::matches);
        }

        @Override
        protected void describeMismatchSafely(LongStream actual, Description mismatchDescription) {
            allMatchMismatch(mismatchDescription, positionNonMatching, nonMatching);
        }
    }

    private static abstract class DoubleStreamAllMatches extends TypeSafeMatcher<DoubleStream> {
        private double nonMatching;
        private long positionNonMatching = -1L;
        private final Matcher<Double> matcher;

        DoubleStreamAllMatches(Matcher<Double> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(DoubleStream actual) {
            return actual
                    .peek(i -> {nonMatching = i; positionNonMatching++;})
                    .allMatch(matcher::matches);
        }

        @Override
        protected void describeMismatchSafely(DoubleStream actual, Description mismatchDescription) {
            allMatchMismatch(mismatchDescription, positionNonMatching, nonMatching);
        }
    }

    private static void anyMatchMismatch(Description mismatchDescription, List<?> accumulator) {
        mismatchDescription
                .appendText("None of these items matched: ")
                .appendValueList("[", ",", "]", accumulator);
    }

    private static abstract class StreamAnyMatches<T> extends TypeSafeMatcher<Stream<T>> {
        final List<T> accumulator = new LinkedList<>();
        final Matcher<T> matcher;

        StreamAnyMatches(Matcher<T> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(Stream<T> actual) {
            return actual.peek(accumulator::add).anyMatch(matcher::matches);
        }

        @Override
        protected void describeMismatchSafely(Stream<T> actual, Description mismatchDescription) {
            anyMatchMismatch(mismatchDescription,accumulator);
        }
    }

    private static abstract class LongStreamAnyMatches extends TypeSafeMatcher<LongStream> {
        final List<Long> accumulator = new LinkedList<>();
        final Matcher<Long> matcher;

        LongStreamAnyMatches(Matcher<Long> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(LongStream actual) {
            return actual.peek(accumulator::add).anyMatch(matcher::matches);
        }

        @Override
        protected void describeMismatchSafely(LongStream actual, Description mismatchDescription) {
            anyMatchMismatch(mismatchDescription, accumulator);
        }
    }

    private static abstract class IntStreamAnyMatches extends TypeSafeMatcher<IntStream> {
        final List<Integer> accumulator = new LinkedList<>();
        final Matcher<Integer> matcher;

        IntStreamAnyMatches(Matcher<Integer> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(IntStream actual) {
            return actual.peek(accumulator::add).anyMatch(matcher::matches);
        }

        @Override
        protected void describeMismatchSafely(IntStream actual, Description mismatchDescription) {
            anyMatchMismatch(mismatchDescription, accumulator);
        }
    }

    private static abstract class DoubleStreamAnyMatches extends TypeSafeMatcher<DoubleStream> {
        final List<Double> accumulator = new LinkedList<>();
        final Matcher<Double> matcher;

        DoubleStreamAnyMatches(Matcher<Double> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(DoubleStream actual) {
            return actual.peek(accumulator::add).anyMatch(matcher::matches);
        }

        @Override
        protected void describeMismatchSafely(DoubleStream actual, Description mismatchDescription) {
            anyMatchMismatch(mismatchDescription, accumulator);
        }
    }


    private static class ArrayIterator<T> implements Iterator<T> {
        private final T[] expected;
        private int currentPos = 0;

        @SafeVarargs
        public ArrayIterator(T... expected) {
            this.expected = expected;
        }

        @Override
        public boolean hasNext() {
            return currentPos < expected.length;
        }

        @Override
        public T next() {
            return expected[currentPos++];
        }
    }

    private static class IntArrayIterator implements PrimitiveIterator.OfInt {
        private final int[] expected;
        private int currentPos = 0;
        
        public IntArrayIterator(int... expected) {
            this.expected = expected;
        }

        @Override
        public boolean hasNext() {
            return currentPos < expected.length;
        }

        @Override
        public int nextInt() {
            return expected[currentPos++];
        }
    }

    private static class LongArrayIterator implements PrimitiveIterator.OfLong {
        private final long[] expected;
        private int currentPos = 0;

        public LongArrayIterator(long... expected) {
            this.expected = expected;
        }

        @Override
        public boolean hasNext() {
            return currentPos < expected.length;
        }

        @Override
        public long nextLong() {
            return expected[currentPos++];
        }
    }

    private static class DoubleArrayIterator implements PrimitiveIterator.OfDouble {
        private final double[] expected;
        private int currentPos = 0;

        public DoubleArrayIterator(double... expected) {
            this.expected = expected;
        }

        @Override
        public boolean hasNext() {
            return currentPos < expected.length;
        }

        @Override
        public double nextDouble() {
            return expected[currentPos++];
        }
    }
}
