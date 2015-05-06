package com.unrulymedia.util.testutils;

import org.hamcrest.Matcher;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class Helper {
    static <S> void testFailingMatcher(Matcher<S> matcher, S testData, String expectedDescription, String actualDescription) {
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
