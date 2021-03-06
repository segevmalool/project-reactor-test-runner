/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.segbaus.projectreactor.junit;

import com.segbaus.projectreactor.ProjectReactorTestExecutor;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;
import reactor.core.publisher.Mono;

import java.io.Serializable;
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

		ArrayList<Mono<Serializable>> testPublishers = new ArrayList<>();

		// Get all methods annotated with @Testable organized by the test class
		for (ClassSelector selector : discoveryRequest.getSelectorsByType(ClassSelector.class)) {
			for (Method method : selector.getJavaClass().getDeclaredMethods()) {
				if (!method.isAnnotationPresent(ReactorTest.class)) {
					continue;
				}

				try {
					Mono<Serializable> testCasePublisher = (Mono<Serializable>) method.invoke(selector);
					testPublishers.add(testCasePublisher);
				} catch (IllegalAccessException | InvocationTargetException ex) {
					log.error(() -> "Failed to invoke test method: " + ex.toString());
					log.error(() -> "exiting...");
					System.exit(1);
				}
			}
		}

		return new ProjectReactorTestDescriptor<Serializable>().setTestPublishers(testPublishers);
	}

	@Override
	public void execute(ExecutionRequest request) {
		(new ProjectReactorTestExecutor())
				.execute(((ProjectReactorTestDescriptor) request.getRootTestDescriptor()).getTestPublishers());
	}
}
