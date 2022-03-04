package com.segbaus.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

//To do
public class TestExecutorTest {
  @Test
  public void shouldExecute() {
    ArrayList<Flux> testPublishers = new ArrayList<>();

    testPublishers.add(Flux.just(true));
    testPublishers.add(Flux.just(false));

    //(new TestExecutor()).execute(testPublishers);
  }
}
