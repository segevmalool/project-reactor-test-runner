package com.segbaus.projectreactor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.Collections.synchronizedList;

public class ProjectReactorTestExecutor {
  private Logger log = Logger.getLogger(ProjectReactorTestExecutor.class.getName());
  private List<TestResult> testReport;

  public ProjectReactorTestExecutor() {
    this.testReport = synchronizedList(
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
      log.info("No test cases found... exiting");
      return;
    }

    log.info("main thread: " + Thread.currentThread());
    log.info("found tests: " + testPublishers.toString());

    Flux.merge(testPublishers)
        .doOnEach((Signal s) -> {
          // In any case
          log.fine( "main thread: " + Thread.currentThread());
          log.fine(this.testReport.toString());
        })
        .doOnNext((Serializable testResult) -> {
          log.info(testResult.toString());
          // Just in case the testResult is a valid json string...
          ObjectMapper mapper = new ObjectMapper();
          JsonNode testResultJson = null;
          try {
            testResultJson = mapper.readTree(testResult.toString());
            // If the parsing works, then submit the result as jackson json node.
            this.submitResults(new TestResult(testResultJson, true));
          } catch (JsonProcessingException ex) {
            // Otherwise, just submit the testResult as is.
            this.submitResults(new TestResult(testResult, true));
          }
        })
        .onErrorContinue((Throwable err, Object o) -> {
          this.submitResults(new TestResult(err, false));
        })
        .subscribeOn(Schedulers.parallel())
        .blockLast();

    try {
      this.writeTestReport();
    } catch (IOException ex) {
      log.severe(ex.toString());
    }
  }

  private void submitResults(TestResult result) {
    testReport.add(result);
  }

  private void writeTestReport() throws IOException {
    log.info("Writing test results");
    ObjectMapper mapper = new ObjectMapper();
    new File("testResult.json").createNewFile();
    mapper.writeValue(
        new File("testResult.json"),
        testReport
    );
  }

  private class TestResult {
    public Object testResult;
    public Boolean pass;

    public TestResult(Object testResult, Boolean pass) {
      this.testResult = testResult;
      this.pass = pass;
    }
  }
}
