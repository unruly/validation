# Validation

A class for functional error handling in Java 8, either a success with a value or a failure with error(s)

# Installation

Available from the Central Repository. In Maven style:

```xml
<dependency>
  <groupId>co.unruly</groupId>
  <artifactId>validation</artifactId>
  <version>1.0</version>
</dependency>
```

## Create a validation
```java
Validation<String,String> success = Validation.success("woot");
Validation<String,String> failure = Validation.failure("awww");
```
The first type parameter T is the type of a successful value, the latter S the type of error(s)

## Is this a success or a failure?
```java
boolean success = validation.isSuccess();
boolean failure = validation.isFailure();
```
## Get the value
```java
T value = validation.get();
```
## Get the errors
```java
List<S> errors = validation.getErrors()
```
## Map or flatMap
```java
// Add one to contents
Validation.success(2).map(i -> i + 1);

// Add one to contents using flatMap
Validation.success(2).flatMap(i -> Validation.success(i + 1));
```

Map and flatMap operate on the value of a **successful** Validation. They do not affect a failure Validation.

## Filter
```java
Validation.success(2).filter(i -> i > 1);
```
Filtering a success with a non-truthy predicate will turn it into a failure with empty errors.
It does not affect a failure.
## Catch exceptions
```java
Validation.tryTo(() -> /* do something that may throw */)
```
If an exception is thrown, tryTo will return a failure validation with the exception in the errors.

## Map, flatMap or filter with potentially exception throwing functions
```java
validation.tryMap(...)
validation.tryFlatMap(...)
validation.tryFilter(...)
```
If an exception is thrown, the Validation is a failure with the exception in the errors.

There are a couple of caveats here:

* The type of the errors of the returned Validation is **Object**
* If function *f* throws and function *g* catches then `map(f).map(g)` may not equal `.map(g compose f)`

## Composition
```java
Validation.success(3).compose(Validation.success(4), (a, b) -> a * b);
```
Two composed successes produce a success with the values combined using the provided function.
Otherwise a failure is produced, including errors from each.

There are also some static methods for composing many Validations of type T for a few common types T
(Integer, Long, Float, Double, Boolean, Collection, Map) with a sensible default composition function:
```java
Validation.compose(() -> Validation.success(3L), () -> Validation.success(4L));
```
The use of lambda suppliers here is [a pattern][0] for working around Java's type erasure.

[0]: http://benjiweber.co.uk/blog/2015/02/20/work-around-java-same-erasure-errors-with-lambdas/ 'Work around Java “same erasure” errors with Lambdas'

## Inspiration

From Scala:

* [scala.util.Either](http://www.scala-lang.org/api/2.11.5/index.html#scala.util.Either)
* [scala.util.Try](http://www.scala-lang.org/api/2.11.5/index.html#scala.util.Try)

From Scalaz:

* [scalaz.\/](http://docs.typelevel.org/api/scalaz/nightly/index.html#scalaz.$bslash$div)
* [scalaz.Validation](http://docs.typelevel.org/api/scalaz/nightly/index.html#scalaz.Validation)

From Rust:

* [std::result::Result](https://doc.rust-lang.org/std/result/)

There are many similar types in other functional languages.
