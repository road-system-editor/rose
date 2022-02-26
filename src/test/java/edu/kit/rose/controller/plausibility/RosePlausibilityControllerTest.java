package edu.kit.rose.controller.plausibility;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.navigation.FileDialogType;
import edu.kit.rose.controller.navigation.FileFormat;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.RoseSelectionBuffer;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class RosePlausibilityControllerTest {
  private static final Path NONEXISTENT_CRITERIA_PATH = Path.of("build/tmp/none.criteria.json");
  private CriteriaManager criteriaManager;
  private ApplicationDataSystem applicationDataSystem;
  private SelectionBuffer selectionBuffer;
  private PlausibilityController controller;
  private Navigator navigator;
  private ZoomSetting zoomSetting;

  @BeforeEach
  public void setUp() {
    this.applicationDataSystem = mock(ApplicationDataSystem.class);
    this.criteriaManager = new CriteriaManager();
    this.selectionBuffer = new RoseSelectionBuffer();
    this.zoomSetting = new ZoomSetting(new Position(0, 0));
    this.navigator = mock(Navigator.class);
    this.criteriaManager.setRoadSystem(
            new GraphRoadSystem(mock(CriteriaManager.class), mock(TimeSliceSetting.class)));
    this.criteriaManager.setViolationManager(new ViolationManager());
    Project project = mock(Project.class);
    when(project.getZoomSetting()).thenReturn(this.zoomSetting);
    when(applicationDataSystem.getCriteriaManager()).thenAnswer(e -> this.criteriaManager);
    this.controller = new RosePlausibilityController(new RoseStorageLock(),
            navigator, project, this.selectionBuffer, this.applicationDataSystem);
  }


  @Test
  void importCompatibilityCriteriaTest() {
    Runnable onBegin = mock(Runnable.class);
    Runnable onEnd = mock(Runnable.class);

    when(this.navigator.showFileDialog(FileDialogType.LOAD_FILE, FileFormat.CRITERIA))
        .thenReturn(NONEXISTENT_CRITERIA_PATH);

    this.controller.subscribeToPlausibilityIoAction(onBegin, onEnd);
    this.controller.importCompatibilityCriteria();

    verify(applicationDataSystem, times(1)).importCriteriaFromFile(any());
    Mockito.verify(onBegin, times(1)).run();
    Mockito.verify(onEnd, times(1)).run();
  }

  @Test
  void exportCompatibilityCriteriaTest() {
    Runnable onBegin = mock(Runnable.class);
    Runnable onEnd = mock(Runnable.class);

    when(this.navigator.showFileDialog(FileDialogType.SAVE_FILE, FileFormat.CRITERIA))
        .thenReturn(NONEXISTENT_CRITERIA_PATH);

    this.controller.subscribeToPlausibilityIoAction(onBegin, onEnd);
    this.controller.exportCompatibilityCriteria();

    verify(applicationDataSystem, times(1)).exportCriteriaToFile(NONEXISTENT_CRITERIA_PATH);
    verify(onBegin, times(1)).run();
    verify(onEnd, times(1)).run();
  }

  @Test
  void unsubscribeFromPlausibilityIoActionTest() {
    Runnable onBegin = mock(Runnable.class);
    Runnable onEnd = mock(Runnable.class);

    when(this.navigator.showFileDialog(FileDialogType.SAVE_FILE, FileFormat.CRITERIA))
            .thenReturn(NONEXISTENT_CRITERIA_PATH);

    this.controller.subscribeToPlausibilityIoAction(onBegin, onEnd);
    this.controller.unsubscribeFromPlausibilityIoAction(onBegin, onEnd);
    this.controller.exportCompatibilityCriteria();
    verify(onBegin, never()).run();
    verify(onEnd, never()).run();
  }

  @Test
  void jumpToCriterionViolationTest() {
    Segment segment2 = new Base();
    Segment segment3 = new Base();
    segment2.move(new Movement(1, 1));
    segment3.move(new Movement(3, 3));

    PlausibilityCriterion criterion =
            new CompatibilityCriterion(mock(RoadSystem.class), mock(ViolationManager.class));
    Violation violation = new Violation(criterion, List.of(segment2, segment3));

    this.controller.jumpToCriterionViolation(violation);

    Assertions.assertEquals(2, zoomSetting.getCenterOfView().getX());
    Assertions.assertEquals(2, zoomSetting.getCenterOfView().getY());
    Assertions.assertTrue(selectionBuffer.isSegmentSelected(segment2));
    Assertions.assertTrue(selectionBuffer.isSegmentSelected(segment3));
  }

  @Test
  void addCompatibilityCriterionTest() {
    controller.addCompatibilityCriterion();
    Assertions.assertEquals(1, criteriaManager.getCriteria().stream()
            .filter(e -> e.getType().equals(PlausibilityCriterionType.COMPATIBILITY)).count());
  }

  @Test
  void setCompatibilityNameTest() {
    CompatibilityCriterion criterion = new CompatibilityCriterion(
            mock(RoadSystem.class), mock(ViolationManager.class));
    controller.setCompatibilityCriterionName(criterion, "test");
    Assertions.assertEquals("test", criterion.getName());
  }

  @Test
  void addSegmentTypeToCompatibilityCriterionTest() {
    CompatibilityCriterion criterion = new CompatibilityCriterion(
            mock(RoadSystem.class), mock(ViolationManager.class));
    controller.addSegmentTypeToCompatibilityCriterion(criterion, SegmentType.BASE);
    Assertions.assertTrue(criterion.getSegmentTypes().contains(SegmentType.BASE));
  }

  @Test
  void removeSegmentTypeToCompatibilityCriterionTest() {
    CompatibilityCriterion criterion = new CompatibilityCriterion(
            mock(RoadSystem.class), mock(ViolationManager.class));
    controller.addSegmentTypeToCompatibilityCriterion(criterion, SegmentType.BASE);
    controller.removeSegmentTypeToCompatibilityCriterion(criterion, SegmentType.BASE);
    Assertions.assertFalse(criterion.getSegmentTypes().contains(SegmentType.BASE));
  }

  @Test
  void setCompatibilityCriterionAttributeTypeTest() {
    CompatibilityCriterion criterion = new CompatibilityCriterion(
            mock(RoadSystem.class), mock(ViolationManager.class));
    controller.setCompatibilityCriterionAttributeType(criterion, AttributeType.LENGTH);
    Assertions.assertEquals(AttributeType.LENGTH, criterion.getAttributeType());
  }

  @Test
  void setCompatibilityCriterionValidationTypeTest() {
    CompatibilityCriterion criterion = new CompatibilityCriterion(
            mock(RoadSystem.class), mock(ViolationManager.class));
    controller.setCompatibilityCriterionValidationType(criterion, ValidationType.EQUALS);
    Assertions.assertEquals(ValidationType.EQUALS, criterion.getOperatorType());
  }

  @Test
  void setCompatibilityCriterionLegalDiscrepancyTest() {
    CompatibilityCriterion criterion = new CompatibilityCriterion(
            mock(RoadSystem.class), mock(ViolationManager.class));
    controller.setCompatibilityCriterionLegalDiscrepancy(criterion, 1);
    Assertions.assertEquals(1, criterion.getLegalDiscrepancy());
  }

  @Test
  void deleteCompatibilityCriterionTest() {
    controller.addCompatibilityCriterion();
    CompatibilityCriterion criterion = (CompatibilityCriterion) criteriaManager.getCriteria()
            .stream().filter(e -> e.getType().equals(PlausibilityCriterionType.COMPATIBILITY))
            .findFirst().get();
    controller.deleteCompatibilityCriterion(criterion);
    Assertions.assertEquals(0, criteriaManager.getCriteria().stream()
            .filter(e -> e.getType().equals(PlausibilityCriterionType.COMPATIBILITY)).count());
  }

  @Test
  void deleteCompatibilityCriteriaTest() {
    controller.addCompatibilityCriterion();
    controller.addCompatibilityCriterion();
    controller.deleteAllCompatibilityCriteria();
    Assertions.assertEquals(0, criteriaManager.getCriteria().stream()
            .filter(e -> e.getType().equals(PlausibilityCriterionType.COMPATIBILITY)).count());
  }
}