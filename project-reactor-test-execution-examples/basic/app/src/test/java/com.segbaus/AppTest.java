/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.segbaus;

import com.segbaus.projectreactor.junit.ReactorTest;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.suite.api.IncludeEngines;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;

@IncludeEngines("reactor")
public class AppTest {
  Logger log = LoggerFactory.getLogger(AppTest.class);

  @ReactorTest
  public static Mono<Boolean> pass1() {
    return Mono.just(true);
  }

  @ReactorTest
  public static Mono<Boolean> pass2() {
    return Mono.just(true);
  }

  @ReactorTest
  public static Mono<Boolean> fail1() {
    return Mono.just(true).map((q) -> {
      throw new Error("This test Failed");
    });
  }

  @ReactorTest
  public static Mono<Long> pass3() {
    return Flux.interval(Duration.ZERO, Duration.ofMillis(1000)).take(5).last();
  }

  @ReactorTest
  public static Mono<HashMap<String, String>> pass4() {
    HashMap<String, String> myTestResult = new HashMap<>();

    myTestResult.put("a", "1x");
    myTestResult.put("b", "2x");
    myTestResult.put("c", "3x");

    return Mono.just(myTestResult);
  }

  @ReactorTest
  public static Mono<MyTestResults> customTestResultClass() {
    return Mono.just(new MyTestResults());
  }

  /**
   * For jackson to serialize a user-defined class, it must implement serializable
   * and the instance data must be public. There may be some workaround settings.
   */
  public static class MyTestResults implements Serializable {
    public Boolean pass;
    public String myName;

    public MyTestResults() {
      this.pass = true;
      this.myName = "Bilbo Baggins";
    }
  }
}
