package com.unrulymedia.util.testutils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.*;
import java.util.stream.*;

import static org.junit.Assert.assertThat;

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

    public static <T> Matcher<DoubleStream> startsWith(DoubleStream expected, long limit) {
        return new BaseStreamMatcher<Double,DoubleStream>() {
            @Override
            protected boolean matchesSafely(DoubleStream actual) {
                return remainingItemsEqual(expected.iterator(), actual.limit(limit).iterator());
            }
        };
    }

    public static <T> Matcher<IntStream> startsWith(IntStream expected, long limit) {
        return new BaseStreamMatcher<Integer,IntStream>() {
            @Override
            protected boolean matchesSafely(IntStream actual) {
                return remainingItemsEqual(expected.iterator(), actual.limit(limit).iterator());
            }
        };
    }

    public static <T> Matcher<LongStream> startsWith(LongStream expected, long limit) {
        return new BaseStreamMatcher<Long,LongStream>() {
            @Override
            protected boolean matchesSafely(LongStream actual) {
                return remainingItemsEqual(expected.iterator(), actual.limit(limit).iterator());
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
        return new TypeSafeMatcher<Stream<T>>() {
            private T nonMatching;

            @Override
            protected boolean matchesSafely(Stream<T> actual) {
                return actual.peek(i -> nonMatching = i).allMatch(matcher::matches);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("All to match ").appendValue(matcher);
            }

            @Override
            protected void describeMismatchSafely(Stream<T> actual, Description mismatchDescription) {
                mismatchDescription.appendText("Item failed to match: ").appendValue(nonMatching);
            }
        };
    }

    public static <T> Matcher<Stream<T>> anyMatch(Matcher<T> matcher) {
        return new TypeSafeMatcher<Stream<T>>() {
            List<T> accumulator = new LinkedList<>();

            @Override
            public void describeTo(Description description) {
                description.appendText("Any to match ").appendValue(matcher);
            }

            @Override
            protected boolean matchesSafely(Stream<T> actual) {
                return actual.peek(accumulator::add).anyMatch(matcher::matches);
            }

            @Override
            protected void describeMismatchSafely(Stream<T> actual, Description mismatchDescription) {
                mismatchDescription.appendText("None of these items matched: ").appendValueList("[",",","]",accumulator);
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
            description.appendText("Stream of ").appendValueList("[", ",", "]", values);
        }

        protected boolean remainingItemsEqual(Iterator<T> expectedIterator, Iterator<T> actualIterator) {
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

    private static class IntArrayIterator implements Iterator<Integer> {
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
        public Integer next() {
            return expected[currentPos++];
        }
    }

    private static class LongArrayIterator implements Iterator<Long> {
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
        public Long next() {
            return expected[currentPos++];
        }
    }

    private static class DoubleArrayIterator implements Iterator<Double> {
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
        public Double next() {
            return expected[currentPos++];
        }
    }
}
