# Execute tests concurrently using Project Reactor

## How to write tests using reactive programming

## How to configure the "reactor" test engine for junit platform
1. Add the dependency on `project-reactor-junit-platform-engine`.
2. Mark your test classes with JUnit's `@IncludeEngines`
```java
@IncludeEngine("reactor")
public class MyTestClass() {
  
}
```
3. Mark your static test methods with JUnit's `@Testable`
```java
@Testable
public static Flux<Boolean> myTestMethod() {
  
}
```

Notes:
- You **must** declare all test methods static. The test engine will not instantiate the test classes.

## How to create reusable test components

## To do list

1. Add a way to represent a partial ordering of test methods.
2. Add utilities for testing remote services using reactive web client. 