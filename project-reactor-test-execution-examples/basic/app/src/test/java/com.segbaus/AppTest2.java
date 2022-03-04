/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.segbaus;

import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.suite.api.IncludeEngines;
import reactor.core.publisher.Mono;

@IncludeEngines("reactor")
public class AppTest2 {
  Logger log = LoggerFactory.getLogger(AppTest.class);

  @Testable
  public static Mono<Boolean> pass1() {
    return Mono.just(true);
  }

  @Testable
  public static Mono<Boolean> pass2() {
    return Mono.just(true);
  }

  @Testable
  public static Mono<Boolean> pass3() {
    return Mono.just(true);
  }

  @Testable
  public static Mono<Boolean> fail1() {
    return Mono.just(false);
  }
}
