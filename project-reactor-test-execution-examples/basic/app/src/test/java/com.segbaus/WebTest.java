package com.segbaus;

import com.segbaus.projectreactor.junit.ReactorTest;
import org.junit.platform.suite.api.IncludeEngines;

import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@IncludeEngines("reactor")
public class WebTest {

  @ReactorTest
  public static Mono<String> webPass1() {
    return HttpClient.create()
        .baseUrl("io.projectreactor.netty:reactor-netty-http:1.0.17")
        .get()
        .uri("https://dog-api.kinduff.com/api/facts")
        .responseContent().aggregate().asString();
  }
}
