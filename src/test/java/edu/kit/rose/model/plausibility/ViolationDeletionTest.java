package edu.kit.rose.model.plausibility;

import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.controller.plausibility.RosePlausibilityController;
import edu.kit.rose.controller.selection.RoseSelectionBuffer;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit Tests for Violations and Criteria.
 */
public class ViolationDeletionTest {
  private static final Path CONFIG_PATH = Path.of("build/tmp/no-config.json");
  private RoadSystem roadSystem;
  private PlausibilityController controller;
  private PlausibilitySystem plausibilitySystem;
  private Base base1;
  private Connection connection;
  private CompatibilityCriterion criterion;

  @BeforeEach
  void beforeEach() throws IOException {
    Files.deleteIfExists(CONFIG_PATH);
    var modelFactory = new ModelFactory(CONFIG_PATH);
    Project project = modelFactory.createProject();
    this.roadSystem = project.getRoadSystem();
    this.plausibilitySystem = project.getPlausibilitySystem();
    Navigator navigator = Mockito.mock(Navigator.class);
    ApplicationDataSystem applicationDataSystem = modelFactory.createApplicationDataSystem();
    this.controller = new RosePlausibilityController(new RoseStorageLock(), navigator, project,
        new RoseSelectionBuffer(), applicationDataSystem);
    this.plausibilitySystem.getCriteriaManager().removeAllCriteriaOfType(PlausibilityCriterionType.COMPATIBILITY);
  }

  void setupOneViolation() {
    CriteriaManager criteriaManager = plausibilitySystem.getCriteriaManager();

    criterion = criteriaManager.createCompatibilityCriterion();
    controller.addSegmentTypeToCompatibilityCriterion(criterion, SegmentType.BASE);
    controller.setCompatibilityCriterionAttributeType(criterion, AttributeType.LANE_COUNT);
    controller.setCompatibilityCriterionValidationType(criterion, ValidationType.LESS_THAN);
    controller.setCompatibilityCriterionLegalDiscrepancy(criterion, 2);

    base1 = (Base) roadSystem.createSegment(SegmentType.BASE);
    Base base2 = (Base) roadSystem.createSegment(SegmentType.BASE);
    base1.setLaneCount(10);
    connection = roadSystem.connectConnectors(base1.getEntry(), base2.getExit());
  }

  @Test
  void testRemoveElementRemovesViolation() {
    ViolationManager violationManager = plausibilitySystem.getViolationManager();

    setupOneViolation();

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    roadSystem.removeElement(base1);
    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testMoveElementRemovesViolation() {
    ViolationManager violationManager = plausibilitySystem.getViolationManager();

    setupOneViolation();

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    roadSystem.moveSegments(List.of(base1), new Movement(10, 10));
    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testDisconnectRemovesViolation() {
    ViolationManager violationManager = plausibilitySystem.getViolationManager();

    setupOneViolation();

    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    roadSystem.disconnectConnection(connection);
    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testRemoveElementRemovesMultipleViolations() {
    ViolationManager violationManager = plausibilitySystem.getViolationManager();

    setupOneViolation();
    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    Base base = (Base) roadSystem.createSegment(SegmentType.BASE);

    connection = roadSystem.connectConnectors(base.getEntry(), base1.getExit());

    Assertions.assertEquals(2, violationManager.getViolations().getSize());

    roadSystem.removeElement(base1);
    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testRemoveCriterionRemovesViolation() {
    ViolationManager violationManager = plausibilitySystem.getViolationManager();
    CriteriaManager criteriaManager = plausibilitySystem.getCriteriaManager();

    setupOneViolation();
    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    criteriaManager.removeCriterion(criterion);
    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testRemoveCriterionRemovesMultipleViolations() {
    ViolationManager violationManager = plausibilitySystem.getViolationManager();
    CriteriaManager criteriaManager = plausibilitySystem.getCriteriaManager();

    setupOneViolation();
    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    Base base = (Base) roadSystem.createSegment(SegmentType.BASE);
    connection = roadSystem.connectConnectors(base.getEntry(), base1.getExit());
    Assertions.assertEquals(2, violationManager.getViolations().getSize());

    criteriaManager.removeCriterion(criterion);
    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }

  @Test
  void testChangeCriterionRemovesViolation() {
    ViolationManager violationManager = plausibilitySystem.getViolationManager();

    setupOneViolation();
    Assertions.assertEquals(1, violationManager.getViolations().getSize());

    controller.setCompatibilityCriterionValidationType(criterion, ValidationType.EQUALS);
    controller.setCompatibilityCriterionAttributeType(criterion, AttributeType.NAME);

    Assertions.assertEquals(0, violationManager.getViolations().getSize());
  }
}