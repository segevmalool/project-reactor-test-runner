package com.segbaus.projectreactor;

import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;

import java.util.List;

public class TestExecutor {

  public void execute(List<CorePublisher<Boolean>> testPublishers) {
    if (testPublishers.isEmpty()) {
      return;
    }

    Flux.merge(testPublishers)
        .doOnEach((Signal b) -> {
          System.out.println(b);
        })
        .subscribe();
  }
}
