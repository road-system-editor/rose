package edu.kit.rose.controller.plausibility;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.RoseStorageLock;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


class RosePlausibilityControllerTest {
  @Mock
  CriteriaManager criteriaManager;
  @Mock
  private Project project;
  @Mock
  private ApplicationDataSystem applicationDataSystem;
  private StorageLock storageLock;
  @Mock
  private Navigator navigator;
  private PlausibilityController controller;

  @BeforeEach
  public void setUp() {
    this.applicationDataSystem = mock(ApplicationDataSystem.class);
    this.storageLock = new RoseStorageLock();
    this.navigator = mock(Navigator.class);
    this.criteriaManager = mock(CriteriaManager.class);
    this.project = mock(Project.class);
    when(applicationDataSystem.getCriteriaManager()).thenAnswer(e -> this.criteriaManager);
    this.controller = new RosePlausibilityController(this.storageLock,
            this.navigator, this.project, this.applicationDataSystem);
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
    Assertions.assertTrue(onBeginRun.get());
    Assertions.assertTrue(onEndRun.get());
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
    Violation violation = mock(Violation.class);
    ZoomSetting zoomSetting = mock(ZoomSetting.class);
    AtomicReference<Position> position = new AtomicReference<>();
    AtomicReference<Collection<Segment>> segment = new AtomicReference<>();
    segment.set(List.of(new Base()));
    when(violation.offendingSegments()).thenAnswer(e -> segment.get());
    doAnswer(e -> {
      position.set(new Position(0, 0));
      return null; })
            .when(zoomSetting).setCenterOfView(any());
    when(this.project.getZoomSetting()).thenReturn(zoomSetting);

    this.controller.jumpToCriterionViolation(violation);

    Assertions.assertEquals(0, position.get().getX());
    Assertions.assertEquals(0, position.get().getY());
  }

}