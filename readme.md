# Execute tests concurrently using Project Reactor

## How to write tests using reactive programming
A publisher represents a sequence of events. Each event may be processed by operators, and the sequence
may terminate with a completion or an error.

By representing test cases as publishers,
we can more simply reason about concurrency and parallelism, and leverage efficiency gains
from non-blocking high-latency processing (usually associated with testing remote services).

Test cases are represented as `Mono<Serializable>`. The emission of the mono should
represent a serializable test result. This choice does not detract from the expressiveness of 
reactive streams, because internally, a test method may choose to use a longer Flux, then process
the events in order to determine a pass/fail test result and associated result data.

Try running the example in `project-reactor-test-execution-examples`! 

## Set up on local
Make a `~/.m2/repository` directory, then run
```shell
./gradlew publishToMavenLocal
```

May require a maven install, not sure.

## How to configure the "reactor" test engine for junit platform
1. Add the dependency on `project-reactor-junit-platform-engine`. You will also need reactor core for Flux/Mono.
```groovy
testImplementation 'com.segbaus:project-reactor-junit-platform-engine:${reactor-test-engine-version}'
implementation platform('io.projectreactor:reactor-bom:${reactor-bom-version}')
implementation 'io.projectreactor:reactor-core'
```
2. Mark your test classes with JUnit's `@IncludeEngines`
```java
@IncludeEngine("reactor")
public class MyTestClass() {
  ...
}
```
3. Mark your static test methods with JUnit's `@Testable`
```java
@Testable
public static Mono<MyTestResultType> myTestMethod() {
    ...
}
```

Notes:
- You **must** declare all test methods static. The test engine will not instantiate the test classes.
- The test case will fail iff the Mono terminates with an error.

## How to create reusable test components

Just write classes/methods that return CorePublishers, and then compose them as you wish! ez

## To do list

1. Add a way to represent a partial ordering of test methods.
2. Add utilities for testing remote services using reactive web client (esp. ui).
3. Add option to represent test cases using gherkin feature files and step definitions.
4. Add an integration with JQuik for properties-based testing and test data generation.
5. Add test parameters.

## References

1. Project reactor: https://github.com/reactor
2. Junit5: https://junit.org/junit5/
