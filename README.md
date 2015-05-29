# Validation

A class for functional error handling in Java 8, either a success with a value or a failure with error(s)

## Create a validation

    Validation<String,String> success = Validation.success("woot");
    Validation<String,String> failure = Validation.failure("awww");

The first type parameter T is the type of a successful value, the latter S the type of error(s)

## Is this a success or a failure?

    boolean success = validation.isSuccess()
    boolean failure = validation.isFailure()

## Get the value

    T value = validation.get()

## Get the errors

    List<S> errors = validation.getErrors()

## Map or flatMap

    Validation.success(2).map(i -> i + 1);
    Validation.success(2).flatMap(i -> Validation.success(i + 1));

Map and flatMap operate on the value of a successful Validation. They do not affect a failure Validation.


## Filter

    Validation.success(2).filter(i -> i > 1);

Filtering a success can turn it into a failure with empty errors. It does not affect a failure.

## Catch exceptions

    Validation.tryTo(() -> /* do something that may throw */)

If an exception is thrown tryTo will return a failure validation with the exception in the errors.

## Map, flatMap or filter with potentially exception throwing functions

    validation.tryMap(...)
    validation.tryFlatMap(...)
    validation.tryFilter(...)

If there is an exception the Validation is a failure with the exception in the errors.

There are a couple of caveats here:

* The type of the errors of the returned Validation is Object
* If function f throws and function g catches then map(f).map(g) may not equal map(g compose f)

## Composition

    Validation.success(3).compose(Validation.success(4), (a, b) -> a * b);

Two composed successes produce a success with the values combined using the provided function. Otherwise a failure is
produced, including errors from each.

There are also some static methods for composing many Validations of type T for a few common types T
(Integer, Long, Float, Double, Boolean, Collection, Map) with a sensible default composition function:

    Validation.compose(() -> Validation.success(3L), () -> Validation.success(4L));

The use of lambda suppliers here is [a pattern][0] for working around Java's type erasure.

[0]: http://benjiweber.co.uk/blog/2015/02/20/work-around-java-same-erasure-errors-with-lambdas/ 'Work around Java “same erasure” errors with Lambdas'

## Inspiration

From Scala:

* scala.util.Either
* scala.util.Try

From Scalaz:

* scalaz.\/
* scalaz.Validation

From Rust:

* std::result::Result

There are many similar types in other functional languages.