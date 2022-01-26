package edu.kit.rose.controller;

import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.application.RoseApplicationController;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.attribute.RoseAttributeController;
import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.command.RoseChangeCommandBuffer;
import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.controller.hierarchy.RoseHierarchyController;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.controller.measurement.RoseMeasurementController;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.controller.plausibility.RosePlausibilityController;
import edu.kit.rose.controller.project.ProjectController;
import edu.kit.rose.controller.project.RoseProjectController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.controller.roadsystem.RoseRoadSystemController;
import edu.kit.rose.controller.selection.RoseSelectionBuffer;
import edu.kit.rose.controller.selection.SelectionBuffer;
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
  private final Navigator navigator;
  private final LanguageSelector languageSelector;
  private final ApplicationDataSystem applicationDataSystem;
  private final Project project;
  private final ChangeCommandBuffer changeCommandBuffer;
  private final SelectionBuffer selectionBuffer;
  private final StorageLock storageLock;

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
    this.navigator = navigator;
    this.languageSelector = languageSelector;
    this.applicationDataSystem = applicationDataSystem;
    this.project = project;
    this.changeCommandBuffer = new RoseChangeCommandBuffer();
    this.selectionBuffer = new RoseSelectionBuffer();
    this.storageLock = new RoseStorageLock();
  }

  /**
   * Returns an application controller.
   *
   * @return application controller instance
   */
  public ApplicationController getApplicationController() {
    return new RoseApplicationController(this.changeCommandBuffer, this.storageLock, this.navigator,
            this.languageSelector, this.applicationDataSystem);
  }

  /**
   * Returns an attribute controller.
   *
   * @return attribute controller instance
   */
  public AttributeController getAttributeController() {
    return new RoseAttributeController(this.changeCommandBuffer, this.storageLock,
        this.navigator, this.project, this.applicationDataSystem);
  }

  /**
   * Returns a hierarchy controller.
   *
   * @return hierarchy controller instance
   */
  public HierarchyController getHierarchyController() {
    return new RoseHierarchyController(this.storageLock,
            this.changeCommandBuffer, this.selectionBuffer, this.project, this.navigator);
  }

  /**
   * Returns a measurement controller.
   *
   * @return measurement controller instance
   */
  public MeasurementController getMeasurementController() {
    return new RoseMeasurementController(this.storageLock, this.navigator, this.project);
  }

  /**
   * Returns a plausibility controller.
   *
   * @return plausibility controller instance
   */
  public PlausibilityController getPlausibilityController() {
    return new RosePlausibilityController(this.storageLock, this.navigator, this.project,
            this.applicationDataSystem);
  }

  /**
   * Returns an project controller.
   *
   * @return project controller instance
   */
  public ProjectController getProjectController() {
    return new RoseProjectController(this.storageLock, this.navigator, this.project);
  }

  /**
   * Returns a roadsystem controller.
   *
   * @return roadsystem controller instance
   */
  public RoadSystemController getRoadSystemController() {
    return new RoseRoadSystemController(this.changeCommandBuffer, this.storageLock, this.navigator,
        this.selectionBuffer, this.project);
  }
}
