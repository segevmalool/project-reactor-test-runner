/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.segbaus.projectreactor.junit;

import com.segbaus.projectreactor.TestExecutor;

import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;
import reactor.core.CorePublisher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ProjectReactorTestEngine implements TestEngine {

  Logger log = LoggerFactory.getLogger(ProjectReactorTestEngine.class);

  @Override
  public String getId() {
    return "reactor";
  }

  @Override
  public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
    ArrayList<CorePublisher<Boolean>> testPublishers = new ArrayList<>();

    // Get all methods annotated with @Testable organized by the test class
    for (ClassSelector selector: discoveryRequest.getSelectorsByType(ClassSelector.class)) {
      for (Method method: selector.getJavaClass().getDeclaredMethods()) {
        if (!method.isAnnotationPresent(Testable.class)) {
          continue;
        }

        try {
          testPublishers.add((CorePublisher<Boolean>) method.invoke(selector));
        } catch (IllegalAccessException | InvocationTargetException ex) {
          log.error(() -> "Failed to invoke test method: " + ex.toString());
          log.error(() -> "exiting...");
          System.exit(1);
        }
      }
    }

    return new ProjectReactorTestDescriptor<Boolean>().setTestPublishers(testPublishers);
  }

  @Override
  public void execute(ExecutionRequest request) {
    (new TestExecutor()).execute(
        ((ProjectReactorTestDescriptor) request.getRootTestDescriptor()).getTestPublishers()
    );
  }
}
