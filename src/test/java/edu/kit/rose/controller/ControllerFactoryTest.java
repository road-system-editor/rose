package edu.kit.rose.controller;

import static org.mockito.Mockito.mock;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControllerFactoryTest {
  ControllerFactory controllerFactory;

  @BeforeEach
  void setUp() {
    controllerFactory = new ControllerFactory(mock(Navigator.class), mock(LanguageSelector.class),
            mock(ApplicationDataSystem.class), mock(Project.class));
  }

  @Test
  void getApplicationController() {
    ApplicationController controller = controllerFactory.getApplicationController();
    Assertions.assertNotNull(controller);
  }

  @Test
  void getAttributeController() {
    AttributeController controller = controllerFactory.getAttributeController();
    Assertions.assertNotNull(controller);
  }

  @Test
  void getHierarchyController() {
    HierarchyController controller = controllerFactory.getHierarchyController();
    Assertions.assertNotNull(controller);
  }

  @Test
  void getMeasurementController() {
    MeasurementController controller = controllerFactory.getMeasurementController();
    Assertions.assertNotNull(controller);
  }

  @Test
  void getPlausibilityController() {
    PlausibilityController controller = controllerFactory.getPlausibilityController();
    Assertions.assertNotNull(controller);
  }

  @Test
  void getProjectController() {
    ProjectController controller = controllerFactory.getProjectController();
    Assertions.assertNotNull(controller);
  }

  @Test
  void getRoadSystemController() {
    RoadSystemController controller = controllerFactory.getRoadSystemController();
    Assertions.assertNotNull(controller);
  }
}