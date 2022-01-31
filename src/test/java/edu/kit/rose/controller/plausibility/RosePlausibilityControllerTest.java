package edu.kit.rose.controller.plausibility;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class RosePlausibilityControllerTest {
  private CriteriaManager criteriaManager;
  private Project project;
  private ApplicationDataSystem applicationDataSystem;
  private SelectionBuffer selectionBuffer;
  private PlausibilityController controller;

  @BeforeEach
  public void setUp() {
    this.applicationDataSystem = mock(ApplicationDataSystem.class);
    this.criteriaManager = mock(CriteriaManager.class);
    this.project = mock(Project.class);
    this.selectionBuffer = mock(SelectionBuffer.class);

    StorageLock storageLock = new RoseStorageLock();
    Navigator navigator = mock(Navigator.class);

    when(applicationDataSystem.getCriteriaManager()).thenAnswer(e -> this.criteriaManager);
    this.controller = new RosePlausibilityController(storageLock,
            navigator, this.project, this.selectionBuffer, this.applicationDataSystem);
  }


  @Test
  void importCompatibilityCriteriaTest() {
    Runnable onBegin = mock(Runnable.class);
    Runnable onEnd = mock(Runnable.class);
    AtomicReference<Boolean> imported = new AtomicReference<>();
    AtomicReference<Boolean> onBeginRun = new AtomicReference<>();
    AtomicReference<Boolean> onEndRun = new AtomicReference<>();
    doAnswer(e -> {
      onBeginRun.set(true);
      return null; })
            .when(onBegin).run();
    doAnswer(e -> {
      onEndRun.set(true);
      return null; })
            .when(onEnd).run();
    doAnswer(e -> {
      imported.set(true);
      return null; })
            .when(applicationDataSystem).importCriteriaFromFile(any());
    onBeginRun.set(false);
    onEndRun.set(false);
    imported.set(false);
    this.controller.subscribeToPlausibilityIoAction(onBegin, onEnd);
    this.controller.importCompatibilityCriteria();

    Assertions.assertTrue(imported.get());
    Mockito.verify(onBegin, Mockito.times(1)).run();
    Mockito.verify(onEnd, Mockito.times(1)).run();
  }

  @Test
  void exportCompatibilityCriteriaTest() {
    Runnable onBegin = mock(Runnable.class);
    Runnable onEnd = mock(Runnable.class);
    AtomicReference<Boolean> exported = new AtomicReference<>();
    AtomicReference<Boolean> onBeginRun = new AtomicReference<>();
    AtomicReference<Boolean> onEndRun = new AtomicReference<>();
    doAnswer(e -> {
      onBeginRun.set(true);
      return null; })
            .when(onBegin).run();
    doAnswer(e -> {
      onEndRun.set(true);
      return null; })
            .when(onEnd).run();
    doAnswer(e -> {
      exported.set(true);
      return null; })
            .when(applicationDataSystem).exportCriteriaToFile(any());
    onBeginRun.set(false);
    onEndRun.set(false);
    exported.set(false);
    this.controller.subscribeToPlausibilityIoAction(onBegin, onEnd);
    this.controller.exportCompatibilityCriteria();

    Assertions.assertTrue(exported.get());
    Assertions.assertTrue(onBeginRun.get());
    Assertions.assertTrue(onEndRun.get());
  }

  @Test
  void jumpToCriterionViolationTest() {
    Segment segment1 = new Base();
    Segment segment2 = new Base();
    Segment segment3 = new Base();
    segment2.move(new Movement(1, 1));
    segment3.move(new Movement(3, 3));

    AtomicReference<List<Segment>> selectedSegments = new AtomicReference<>();
    AtomicReference<Collection<Segment>> segment = new AtomicReference<>();
    segment.set(Arrays.asList(segment1, segment2, segment3));
    selectedSegments.set(new ArrayList<>());

    ZoomSetting zoomSetting = mock(ZoomSetting.class);
    Violation violation = mock(Violation.class);
    AtomicReference<Position> position = new AtomicReference<>();
    when(violation.offendingSegments()).thenAnswer(e -> segment.get());
    doAnswer(e -> {
      position.set(new Position(2, 2));
      return null; })
            .when(zoomSetting).setCenterOfView(any());
    doAnswer((e -> {
      selectedSegments.get().add(e.getArgument(0));
      return null;
    })).when(this.selectionBuffer).addSegmentSelection(any());
    when(this.project.getZoomSetting()).thenReturn(zoomSetting);

    this.controller.jumpToCriterionViolation(violation);

    Assertions.assertEquals(2, position.get().getX());
    Assertions.assertEquals(2, position.get().getY());
    Assertions.assertTrue(selectedSegments.get().contains(segment1));
    Assertions.assertTrue(selectedSegments.get().contains(segment2));
    Assertions.assertTrue(selectedSegments.get().contains(segment3));
  }

}