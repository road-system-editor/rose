package edu.kit.rose.model;

import java.nio.file.Path;

/**
 * Constructs both a {@link Project} and an {@link ApplicationDataSystem}.
 */
public class ModelFactory {

  private final Project project;
  private final ApplicationDataSystem applicationDataSystem;

  /**
   * Constructs both the {@link ApplicationDataSystem} and the {@link Project} held within.
   *
   * @param configFile the config file that is to be used to configure the ApplicationDataSystem.
   */
  public ModelFactory(Path configFile) {
    applicationDataSystem = new RoseApplicationDataSystem(configFile);
    project = new RoseProject(applicationDataSystem.getCriteriaManager());
    var criteriaManager = applicationDataSystem.getCriteriaManager();
    var violationManager = project.getPlausibilitySystem().getViolationManager();
    var roadSystem = project.getRoadSystem();
    criteriaManager.setViolationManager(violationManager);
    criteriaManager.setRoadSystem(roadSystem);
  }

  /**
   * Returns the constructed {@link Project}.
   *
   * @return the constructed Project
   */
  public Project createProject() {
    return project;
  }

  /**
   * Returns the constructed {@link ApplicationDataSystem}.
   *
   * @return the constructed ApplicationDataSystem
   */
  public ApplicationDataSystem createApplicationDataSystem() {
    return applicationDataSystem;
  }
}
