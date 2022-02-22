package edu.kit.rose.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseApplicationDataSystem}.
 */
public class RoseApplicationDataSystemTest {
  static final Path CONFIG_FILE = Path.of("build/tmp/config.json");
  static final Path CRITERIA_EXPORT_FILE = Path.of("build/tmp/criteria-export.criteria.json");
  static final URL CRITERIA_IMPORT_URL =
      RoseApplicationDataSystemTest.class.getResource("import-sample.criteria.json");
  static final URL SAMPLE_CONFIG_URL =
      RoseApplicationDataSystemTest.class.getResource("config-sample.json");
  static final int CRITERIA_IMPORT_AMOUNT = 2;
  static final Path NON_EXISTENT_PATH = Path.of("build/tmp/non-existent-folder/file");

  static int defaultCriteriaAmount;

  ApplicationDataSystem applicationDataSystem;
  DualSetObserver<AttributeType, Path, ApplicationDataSystem> observer;
  /**
   * Required for criteria manager creation.
   */
  RoadSystem roadSystem;

  @BeforeAll
  static void beforeAll() throws IOException {
    Files.deleteIfExists(CONFIG_FILE);
    defaultCriteriaAmount = new RoseApplicationDataSystem(CONFIG_FILE)
        .getCriteriaManager()
        .getCriteria()
        .getSize();
  }

  @BeforeEach
  void beforeEach() throws IOException {
    Files.deleteIfExists(CRITERIA_EXPORT_FILE);
    Files.deleteIfExists(CONFIG_FILE);
    Files.deleteIfExists(NON_EXISTENT_PATH);

    applicationDataSystem = new RoseApplicationDataSystem(CONFIG_FILE);
    var criteriaManager = applicationDataSystem.getCriteriaManager();
    criteriaManager.setViolationManager(new ViolationManager());
    var timeSliceSetting = mock(TimeSliceSetting.class);
    this.roadSystem = new GraphRoadSystem(criteriaManager, timeSliceSetting);
    criteriaManager.setRoadSystem(this.roadSystem);

    observer = mockObserver();
    applicationDataSystem.addSubscriber(observer);
  }

  @Test
  void testCantLoad() throws IOException {
    File unreadableFile = new File("build/tmp/unreadable.json");
    RandomAccessFile raf = new RandomAccessFile(unreadableFile, "rw");
    FileLock lock = raf.getChannel().lock();

    assertThrows(RuntimeException.class,
        () -> new RoseApplicationDataSystem(unreadableFile.toPath()));

    lock.release();
  }

  @Disabled("test fails because criteria manager is not initialized -> disabled to see coverage")
  @Test
  void testLoad() throws URISyntaxException {
    Assumptions.assumeTrue(SAMPLE_CONFIG_URL != null);
    var configPath = Paths.get(SAMPLE_CONFIG_URL.toURI());
    var ads = new RoseApplicationDataSystem(configPath);
    ads.getCriteriaManager().setRoadSystem(this.roadSystem);

    assertEquals(2, ads.getShownAttributeTypes().getSize());
    assertTrue(ads.getShownAttributeTypes().contains(AttributeType.SLOPE));
    assertTrue(ads.getShownAttributeTypes().contains(AttributeType.COMMENT));

    assertEquals(defaultCriteriaAmount, ads.getCriteriaManager().getCriteria().getSize());

    assertSame(Language.GERMAN, ads.getLanguage());
  }

  @Test
  void testCantSave() {
    var adsWithNonExistentPath = new RoseApplicationDataSystem(NON_EXISTENT_PATH);

    // the following should trigger a save since GERMAN is not the default language
    assertThrows(RuntimeException.class, () -> adsWithNonExistentPath.setLanguage(Language.GERMAN));
  }

  @Test
  void testSaveOnLanguageChanged() {
    // the following should trigger a save since GERMAN is not the default language
    applicationDataSystem.setLanguage(Language.GERMAN);
    assertTrue(Files.exists(CONFIG_FILE));
  }

  @Test
  void testSaveOnCriterionChanges() throws IOException {
    var criterion = applicationDataSystem.getCriteriaManager().createCompatibilityCriterion();
    assertTrue(Files.deleteIfExists(CONFIG_FILE));

    criterion.setName("Totally creative name!");
    assertTrue(Files.deleteIfExists(CONFIG_FILE));

    criterion.addSegmentType(SegmentType.EXIT);
    assertTrue(Files.deleteIfExists(CONFIG_FILE));

    criterion.removeSegmentType(SegmentType.EXIT);
    assertTrue(Files.deleteIfExists(CONFIG_FILE));

    applicationDataSystem.getCriteriaManager().removeCriterion(criterion);
    assertTrue(Files.deleteIfExists(CONFIG_FILE));

    applicationDataSystem.notifyChange(applicationDataSystem.getCriteriaManager());
    assertTrue(Files.deleteIfExists(CONFIG_FILE));
  }

  @Test
  void testSaveOnShownAttributesChanged() throws IOException {
    applicationDataSystem.addShownAttributeType(AttributeType.LANE_COUNT_RAMP);
    assertTrue(Files.exists(CONFIG_FILE));
    Files.delete(CONFIG_FILE);

    applicationDataSystem.removeShownAttributeType(AttributeType.LANE_COUNT_RAMP);
    assertTrue(Files.exists(CONFIG_FILE));
  }

  @Test
  void testDefaultLanguage() {
    assertSame(Language.DEFAULT, applicationDataSystem.getLanguage());
  }

  @Test
  void testSetLanguage() {
    applicationDataSystem.setLanguage(Language.GERMAN);
    verify(observer, times(1)).notifyChange(any());
    assertSame(Language.GERMAN, applicationDataSystem.getLanguage());
  }

  @Test
  void testGetThis() {
    assertSame(applicationDataSystem, applicationDataSystem.getThis());
  }

  @Test
  void testAddShownAttributeType() {
    // add NAME
    applicationDataSystem.addShownAttributeType(AttributeType.NAME);
    verify(observer, times(1)).notifyAddition(any());
    assertTrue(applicationDataSystem.getShownAttributeTypes().contains(AttributeType.NAME));
    assertEquals(1, applicationDataSystem.getShownAttributeTypes().getSize());

    // adding NAME again shouldn't do anything
    applicationDataSystem.addShownAttributeType(AttributeType.NAME);
    verify(observer, times(1)).notifyAddition(any());
    assertTrue(applicationDataSystem.getShownAttributeTypes().contains(AttributeType.NAME));
    assertEquals(1, applicationDataSystem.getShownAttributeTypes().getSize());

    // add COMMENT
    applicationDataSystem.addShownAttributeType(AttributeType.COMMENT);
    verify(observer, times(2)).notifyAddition(any());
    assertTrue(applicationDataSystem.getShownAttributeTypes().contains(AttributeType.NAME));
    assertTrue(applicationDataSystem.getShownAttributeTypes().contains(AttributeType.COMMENT));
    assertEquals(2, applicationDataSystem.getShownAttributeTypes().getSize());
  }

  @Test
  void testRemoveShownAttributeType() {
    // preparation
    applicationDataSystem.addShownAttributeType(AttributeType.NAME);
    applicationDataSystem.addShownAttributeType(AttributeType.COMMENT);

    // removing an element that is not contained shouldn't do anything
    applicationDataSystem.removeShownAttributeType(AttributeType.SLOPE);
    verify(observer, times(0)).notifyRemoval(any());
    assertEquals(2, applicationDataSystem.getShownAttributeTypes().getSize());

    // remove COMMENT
    applicationDataSystem.removeShownAttributeType(AttributeType.COMMENT);
    verify(observer, times(1)).notifyRemoval(any());
    assertEquals(1, applicationDataSystem.getShownAttributeTypes().getSize());
    assertTrue(applicationDataSystem.getShownAttributeTypes().contains(AttributeType.NAME));

    // remove NAME
    applicationDataSystem.removeShownAttributeType(AttributeType.NAME);
    verify(observer, times(2)).notifyRemoval(any());
    assertEquals(0, applicationDataSystem.getShownAttributeTypes().getSize());

    // removing anything if no element is contained shouldn't do anything
    applicationDataSystem.removeShownAttributeType(AttributeType.LENGTH);
    verify(observer, times(2)).notifyRemoval(any());
    assertEquals(0, applicationDataSystem.getShownAttributeTypes().getSize());
  }

  @Test
  void testDefaultShownAttributeTypes() {
    Assertions.assertEquals(0, applicationDataSystem.getShownAttributeTypes().getSize());
  }

  @Test
  void testGetCriteriaManager() {
    assertNotNull(applicationDataSystem.getCriteriaManager());
  }

  @Test
  void testExportCriteria() {
    Assumptions.assumeFalse(Files.exists(CRITERIA_EXPORT_FILE));

    applicationDataSystem.exportCriteriaToFile(CRITERIA_EXPORT_FILE);
    assertTrue(Files.exists(CRITERIA_EXPORT_FILE));
  }

  @Test
  void testCantExportCriteria() {
    assertFalse(applicationDataSystem.exportCriteriaToFile(NON_EXISTENT_PATH));
  }

  @Test
  void testImportCriteria() throws URISyntaxException {
    Assumptions.assumeTrue(CRITERIA_IMPORT_URL != null);
    var criteriaPath = Paths.get(CRITERIA_IMPORT_URL.toURI());

    var defaultAmount = applicationDataSystem.getCriteriaManager().getCriteria().getSize();
    applicationDataSystem.importCriteriaFromFile(criteriaPath);
    assertEquals(defaultAmount + CRITERIA_IMPORT_AMOUNT,
        applicationDataSystem.getCriteriaManager().getCriteria().getSize());
  }

  @Test
  void testCantImportCriteria() {
    assertFalse(applicationDataSystem.importCriteriaFromFile(NON_EXISTENT_PATH));
  }

  @Test
  void testAddRecentProjectPathNull() {
    assertThrows(NullPointerException.class,
        () -> this.applicationDataSystem.addRecentProjectPath(null));
  }

  @Test
  void testAddRecentProjectPath() {
    assertEquals(0, this.applicationDataSystem.getRecentProjectPaths().getSize());

    var somePath = Path.of("build/tmp/some-project.rose.json");
    var absolute = somePath.toAbsolutePath();
    this.applicationDataSystem.addRecentProjectPath(somePath);

    assertEquals(1, this.applicationDataSystem.getRecentProjectPaths().getSize());
    // make sure the absolute path was added
    assertTrue(this.applicationDataSystem.getRecentProjectPaths().contains(absolute));

    verify(this.observer, times(1)).notifyAdditionSecond(eq(absolute));
  }

  @Test
  void testAddRecentProjectPathAgainIsIgnored() {
    var somePath = Path.of("build", "tmp", "some-project.rose.json");

    this.applicationDataSystem.addRecentProjectPath(somePath);
    assertEquals(1, this.applicationDataSystem.getRecentProjectPaths().getSize());
    verify(observer, times(1))
        .notifyAdditionSecond(eq(somePath.toAbsolutePath()));

    // adding a path to the same file again should not go through to recent project paths
    var equalPath = Path.of(".", "build", "tmp", "some-project.rose.json");
    this.applicationDataSystem.addRecentProjectPath(equalPath);
    assertEquals(1, this.applicationDataSystem.getRecentProjectPaths().getSize());
    verify(observer, times(1)) // no additional notify
        .notifyAdditionSecond(eq(somePath.toAbsolutePath()));
  }

  @Test
  void testSaveOnAddNewRecentProjectPath() throws IOException {
    var somePath = Path.of("build", "tmp", "some-project.rose.json");
    this.applicationDataSystem.addRecentProjectPath(somePath);
    assertTrue(Files.deleteIfExists(CONFIG_FILE));

    // if the path already existed, no save should have been triggered
    this.applicationDataSystem.addRecentProjectPath(somePath);
    assertFalse(Files.exists(CONFIG_FILE));
  }

  /**
   * Helper method to extract the "unchecked" (but correct) cast of the observer mock.
   */
  @SuppressWarnings("unchecked")
  private static DualSetObserver<AttributeType, Path, ApplicationDataSystem> mockObserver() {
    return mock(DualSetObserver.class);
  }
}
