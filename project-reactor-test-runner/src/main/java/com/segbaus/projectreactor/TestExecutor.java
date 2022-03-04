package com.segbaus.projectreactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.core.scheduler.Schedulers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.synchronizedList;

public class TestExecutor {
  private List<Serializable> accumulatedTestResults;

  public TestExecutor() {
    this.accumulatedTestResults = synchronizedList(
        new ArrayList<>()
    );
  }

  /**
   * The basic idea is that a test emits once with some test result data,
   * then either completes or errors (in Reactor terminology, a Mono).
   * Internally, there may be any number of conversions between
   * CorePublishers, providing some level of flexibility.
   *
   * @param testPublishers A list of Reactor publishers representing test cases.
   */
  public void execute(
      List<Mono<Serializable>> testPublishers
  ) {

    if (testPublishers.isEmpty()) {
      return;
    }

    System.out.println("main thread: " + Thread.currentThread());
    System.out.println("found tests: " + testPublishers.toString());

    Flux.merge(testPublishers)
        .doOnEach((Signal s) -> {
          // In any case
          System.out.println("main thread: " + Thread.currentThread());
          System.out.println(this.accumulatedTestResults.toString());
        })
        .doOnNext((Serializable testResult) -> {
          this.submitResults(testResult.toString());
        })
        .onErrorContinue((Throwable err, Object o) -> {
          this.submitResults(err.toString());
        })
        .subscribeOn(Schedulers.parallel())
        .blockLast();
  }

  public void submitResults(Serializable result) {
    accumulatedTestResults.add(result);
  }
}
