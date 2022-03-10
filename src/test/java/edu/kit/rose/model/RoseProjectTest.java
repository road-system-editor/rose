package edu.kit.rose.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseProject}.
 */
public class RoseProjectTest {
  private static final Path EXPORT_PATH =
      Path.of("build/tmp/RoseProjectTest");
  private static final Path INVALID_EXPORT_PATH =
      Path.of("build/tmp/invalid-directory/invalid-export-file");
  private static final Path LOAD_PATH =
      Path.of("src/test/resources/edu/kit/rose/model/load-sample.rose.json");

  private static final double LOAD_SAMPLE_ZOOM_LEVEL = 10.0d;
  private static final int LOAD_SAMPLE_ELEMENT_COUNT = 4;

  CriteriaManager criteriaManager;
  RoseProject project;

  @BeforeEach
  void beforeEach() throws IOException {
    this.criteriaManager = new CriteriaManager();
    this.project = new RoseProject(criteriaManager);
    criteriaManager.setRoadSystem(project.getRoadSystem());
    criteriaManager.setViolationManager(project.getPlausibilitySystem().getViolationManager());
    Files.deleteIfExists(EXPORT_PATH);
  }

  @Test
  void testConstructor() {
    assertThrows(NullPointerException.class, () -> new RoseProject(null));

    assertNotNull(this.project.getPlausibilitySystem());
    assertSame(this.criteriaManager, this.project.getPlausibilitySystem().getCriteriaManager());

    assertNotNull(this.project.getZoomSetting());
    assertNotNull(this.project.getRoadSystem());
  }

  @Test
  void testReset() {
    this.project.getRoadSystem().createSegment(SegmentType.BASE);

    double defaultZoomLevel = this.project.getZoomSetting().getZoomLevel();
    this.project.getZoomSetting().setZoomLevel(123.456);

    this.project.reset();

    assertEquals(defaultZoomLevel, this.project.getZoomSetting().getZoomLevel());
    assertEquals(0, this.project.getRoadSystem().getElements().getSize());
  }

  @Test
  void testSave() throws IOException {
    this.project.save(EXPORT_PATH);
    assertTrue(Files.exists(EXPORT_PATH));
    assertTrue(Files.readString(EXPORT_PATH).startsWith("{"));
  }

  @Test
  void testLoad() {
    this.project.load(LOAD_PATH);

    assertTrue(this.project.getRoadSystem().getElements().getSize() > 0);
    assertEquals(LOAD_SAMPLE_ZOOM_LEVEL, this.project.getZoomSetting().getZoomLevel());
    assertEquals(LOAD_SAMPLE_ELEMENT_COUNT, this.project.getRoadSystem().getElements().getSize());
  }

  @Test
  void testExportRose() throws IOException {
    this.project.exportToFile(ProjectFormat.ROSE, EXPORT_PATH);
    assertTrue(Files.exists(EXPORT_PATH));
    assertTrue(Files.readString(EXPORT_PATH).startsWith("{"));
  }

  @Test
  void testExportYaml() throws IOException {
    this.project.exportToFile(ProjectFormat.YAML, EXPORT_PATH);
    assertTrue(Files.exists(EXPORT_PATH));
    assertTrue(Files.readString(EXPORT_PATH).contains("Segmente:"));
  }

  @Disabled("SUMO export is not part of the PSE implementation")
  @Test
  void testExportSumo() throws IOException {
    this.project.exportToFile(ProjectFormat.SUMO, EXPORT_PATH);
    assertTrue(Files.exists(EXPORT_PATH));
    assertTrue(Files.readString(EXPORT_PATH).startsWith("<?xml"));
  }

  @Test
  void testExportReturnsFalseForInvalidFile() {
    assumeFalse(Files.exists(INVALID_EXPORT_PATH));

    assertFalse(this.project.exportToFile(ProjectFormat.ROSE, INVALID_EXPORT_PATH));
    assertFalse(Files.exists(INVALID_EXPORT_PATH));

    assertFalse(this.project.exportToFile(ProjectFormat.YAML, INVALID_EXPORT_PATH));
    assertFalse(Files.exists(INVALID_EXPORT_PATH));

    assertFalse(this.project.exportToFile(ProjectFormat.SUMO, INVALID_EXPORT_PATH));
    assertFalse(Files.exists(INVALID_EXPORT_PATH));
  }
}
