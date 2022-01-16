package edu.kit.rose.controller;

import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.controller.project.ProjectController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LanguageSelector;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;

/**
 * Encapsulates the creation of all controllers define in the
 * edu.kit.rose.controller package and its subpackages.
 * It also handles the constructor arguments which are necessary for
 * multiple controller to work together.
 */
public class ControllerFactory {

  /**
   * Creates a new instance of {@link ControllerFactory}.
   *
   * @param navigator             the navigator to initialize all controller with
   * @param languageSelector      component on which controllers can set the applications language
   * @param applicationDataSystem the model facade for application data
   * @param project               the model facade for project specific data
   */
  public ControllerFactory(Navigator navigator, LanguageSelector languageSelector,
                           ApplicationDataSystem applicationDataSystem, Project project) {

  }

  /**
   * Returns an application controller.
   *
   * @return application controller instance
   */
  public ApplicationController getApplicationController() {
    return null;
  }

  /**
   * Returns an attribute controller.
   *
   * @return attribute controller instance
   */
  public AttributeController getAttributeController() {
    return null;
  }

  /**
   * Returns a hierarchy controller.
   *
   * @return hierarchy controller instance
   */
  public HierarchyController getHierarchyController() {
    return null;
  }

  /**
   * Returns a measurement controller.
   *
   * @return measurement controller instance
   */
  public MeasurementController getMeasurementController() {
    return null;
  }

  /**
   * Returns a plausibility controller.
   *
   * @return plausibility controller instance
   */
  public PlausibilityController getPlausibilityController() {
    return null;
  }

  /**
   * Returns an project controller.
   *
   * @return project controller instance
   */
  public ProjectController getProjectController() {
    return null;
  }

  /**
   * Returns a roadsystem controller.
   *
   * @return roadsystem controller instance
   */
  public RoadSystemController getRoadSystemController() {
    return null;
  }
}
