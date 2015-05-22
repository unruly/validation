package co.unruly.util;

import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ValidationCompositionTest {

    @Test
    public void shouldComposeTwoSuccessesWithComposeFunction() throws Exception {
        Validation<Integer, Object> composed = Validation.success(3).compose(Validation.success(4), (a, b) -> a * b);
        Validation<Integer, Object> expected = Validation.success(12);
        assertThat(composed,is(expected));
    }

    @Test
    public void shouldComposeSuccessAndFailureAsFailure() throws Exception {
        Validation<Integer, String> composed = Validation.<Integer,String>success(3).compose(Validation.failure(asList("nope!")), (a, b) -> a + b);
        assertThat(composed,is(Validation.failure(asList("nope!"))));
    }

    @Test
    public void shouldComposeFailureAndSuccessAsFailure() throws Exception {
        Validation<Integer, String> composed = Validation.<Integer,String>failure(asList("nope!")).compose(Validation.success(3), (a, b) -> a + b);
        assertThat(composed,is(Validation.failure(asList("nope!"))));
    }

    @Test
    public void shouldComposeFailures() throws Exception {
        Validation<Object, String> composed = Validation.failure(asList("nope!")).compose(Validation.failure(asList("nein!")), (a, b) -> a);
        assertThat(composed,is(Validation.failure(asList("nope!","nein!"))));
    }

    @Test
    public void shouldComposeIntValidationWithAdditionByDefault() throws Exception {
        Validation<Integer, Object> composed = Validation.compose(() -> Validation.success(3), () -> Validation.success(4));
        assertThat(composed,is(Validation.success(7)));
    }

    @Test
    public void shouldComposeManyIntValidationWithAdditionByDefault() throws Exception {
        Validation<Integer, Object> composed = Validation.compose(() -> Validation.success(3), () -> Validation.success(4), () -> Validation.success((5)));
        assertThat(composed,is(Validation.success(12)));
    }

    @Test
    public void shouldComposeLongValidationWithAdditionByDefault() throws Exception {
        Validation<Long, Object> composed = Validation.compose(() -> Validation.success(3L), () -> Validation.success(4L));
        assertThat(composed,is(Validation.success(7L)));
    }

    @Test
    public void shouldComposeManyLongValidationWithAdditionByDefault() throws Exception {
        Validation<Long, Object> composed = Validation.compose(() -> Validation.success(3L), () -> Validation.success(4L), () -> Validation.success(5L));
        assertThat(composed,is(Validation.success(12L)));
    }

    @Test
    public void shouldComposeFloatValidationWithAdditionByDefault() throws Exception {
        Validation<Float, Object> composed = Validation.compose(() -> Validation.success(3f), () -> Validation.success(4f));
        assertThat(composed,is(Validation.success(7f)));
    }

    @Test
    public void shouldComposeManyFloatValidationWithAdditionByDefault() throws Exception {
        Validation<Float, Object> composed = Validation.compose(() -> Validation.success(3f), () -> Validation.success(4f), () -> Validation.success(5f));
        assertThat(composed,is(Validation.success(12f)));
    }

    @Test
    public void shouldComposeDoubleValidationWithAdditionByDefault() throws Exception {
        Validation<Double, Object> composed = Validation.compose(() -> Validation.success(3d), () -> Validation.success(4d));
        assertThat(composed,is(Validation.success(7d)));
    }

    @Test
    public void shouldComposeManyDoubleValidationWithAdditionByDefault() throws Exception {
        Validation<Double, Object> composed = Validation.compose(() -> Validation.success(3d), () -> Validation.success(4d), () -> Validation.success(5d));
        assertThat(composed,is(Validation.success(12d)));
    }

    @Test
    public void shouldComposeBooleanValidationWithAdditionByDefault() throws Exception {
        Validation<Boolean, Object> composed = Validation.compose(() -> Validation.success(true), () -> Validation.success(true));
        assertThat(composed,is(Validation.success(true)));
    }

    @Test
    public void shouldComposeManyBooleanValidationWithAdditionByDefault() throws Exception {
        Validation<Boolean, Object> composed = Validation.compose(() -> Validation.success(false), () -> Validation.success(false), () -> Validation.success(false));
        assertThat(composed,is(Validation.success(false)));
    }
    
    @Test
    public void shouldComposeStringValidationWithConcatenationByDefault() throws Exception {
        Validation<String, Object> composed = Validation.compose(() -> Validation.success("foo"), () -> Validation.success("bar"));
        assertThat(composed,is(Validation.success("foobar")));
    }

    @Test
    public void shouldComposeManyStringValidationWithConcatenationByDefault() throws Exception {
        Validation<String, Object> composed = Validation.compose(() -> Validation.success("foo"), () -> Validation.success("bar"), () -> Validation.success("baz"));
        assertThat(composed,is(Validation.success("foobarbaz")));
    }

    @Test
    public void shouldComposeListValidation() throws Exception {
        Validation<List<Long>, Object> composed = Validation.compose(() -> Validation.success(asList(3L)), () -> Validation.success(asList(4L)));
        assertThat(composed,is(Validation.success(asList(3L, 4L))));
    }

    @Test
    public void shouldComposeManyListValidation() throws Exception {
        Validation<List<Long>, Object> composed = Validation.compose(() -> Validation.success(asList(3L)), () -> Validation.success(asList(4L)), () -> Validation.success(asList(5L)));
        assertThat(composed,is(Validation.success(asList(3L, 4L, 5L))));
    }

    @Test
    public void shouldComposeSetValidation() throws Exception {
        Validation<Set<Long>, Object> composed = Validation.compose(() -> Validation.success(new HashSet<>(asList(3L))), () -> Validation.success(new HashSet<>(asList(4L))));
        assertThat(composed,is(Validation.success(new HashSet<>(asList(3L, 4L)))));
    }

    @Test
    public void shouldComposeManySetValidation() throws Exception {
        Validation<Set<Long>, Object> composed = Validation.compose(() -> Validation.success(new HashSet<>(asList(3L))), () -> Validation.success(new HashSet<>(asList(4L))),() -> Validation.success(new HashSet<>(asList(5L))));
        assertThat(composed,is(Validation.success(new HashSet<>(asList(3L, 4L, 5L)))));
    }

    @Test
    public void shouldComposeMapValidation() throws Exception {
        HashMap<String, Long> first = new HashMap<>();
        first.put("foo",3L);
        first.put("bar",4L);
        HashMap<String, Long> second = new HashMap<>();
        second.put("bar",5L);
        second.put("baz",6L);
        HashMap<String, Long> result = new HashMap<>();
        result.put("foo",3L);
        result.put("bar",5L);
        result.put("baz",6L);
        Validation<Map<String, Long>, Object> composed = Validation.compose(() -> Validation.success(first), () -> Validation.success(second));
        assertThat(composed, is(Validation.success(result)));
    }

    @Test
    public void shouldComposeManyMapValidation() throws Exception {
        HashMap<String, Long> first = new HashMap<>();
        first.put("foo",3L);
        first.put("bar",4L);
        HashMap<String, Long> second = new HashMap<>();
        second.put("bar",5L);
        second.put("baz",6L);
        HashMap<String, Long> third = new HashMap<>();
        second.put("quux",7L);
        HashMap<String, Long> result = new HashMap<>();
        result.put("foo",3L);
        result.put("bar",5L);
        result.put("baz",6L);
        result.put("quux", 7L);
        Validation<Map<String, Long>, Object> composed = Validation.compose(() -> Validation.success(first), () -> Validation.success(second), () -> Validation.success(third));
        assertThat(composed,is(Validation.success(result)));
    }

    @Test
    public void shouldComposeLotsOfSuccessesAndAFailure() throws Exception {
        Validation<Integer, String> composed = Validation.compose(() -> Validation.success(1), () -> Validation.success(3), () -> Validation.failure("doh"));
        assertThat(composed, is(Validation.failure("doh")));
    }
}