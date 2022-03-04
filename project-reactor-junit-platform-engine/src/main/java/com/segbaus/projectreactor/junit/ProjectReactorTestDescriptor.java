package com.segbaus.projectreactor.junit;

import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.TestTag;
import org.junit.platform.engine.UniqueId;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ProjectReactorTestDescriptor<T> implements TestDescriptor {
  private List<Mono<T>> testPublishers;

  @Override
  public UniqueId getUniqueId() {
    return UniqueId.forEngine((new ProjectReactorTestEngine()).getId());
  }

  @Override
  public String getDisplayName() {
    return (new ProjectReactorTestEngine()).getId();
  }

  @Override
  public Set<TestTag> getTags() {
    return new HashSet<TestTag>();
  }

  @Override
  public Optional<TestSource> getSource() {
    return Optional.empty();
  }

  @Override
  public Optional<TestDescriptor> getParent() {
    return Optional.empty();
  }

  @Override
  public void setParent(TestDescriptor parent) {}

  @Override
  public Set<? extends TestDescriptor> getChildren() {
    // Empty set of children
    return new HashSet<ProjectReactorTestDescriptor>();
  }

  @Override
  public void addChild(TestDescriptor descriptor) {}

  @Override
  public void removeChild(TestDescriptor descriptor) {}

  @Override
  public void removeFromHierarchy() {}

  @Override
  public Type getType() {
    return Type.TEST;
  }

  @Override
  public Optional<? extends TestDescriptor> findByUniqueId(UniqueId uniqueId) {
    return Optional.empty();
  }

  public ProjectReactorTestDescriptor setTestPublishers(List<Mono<T>> testPublishers) {
    this.testPublishers = testPublishers;
    return this;
  }

  public List<Mono<T>> getTestPublishers() {
    return this.testPublishers;
  }
}
