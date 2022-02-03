package edu.kit.rose.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

  static int defaultCriteriaAmount;

  ApplicationDataSystem applicationDataSystem;
  SetObserver<AttributeType, ApplicationDataSystem> observer;
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

    applicationDataSystem = new RoseApplicationDataSystem(CONFIG_FILE);
    var criteriaManager = applicationDataSystem.getCriteriaManager();
    var timeSliceSetting = mock(TimeSliceSetting.class);
    this.roadSystem = new GraphRoadSystem(criteriaManager, timeSliceSetting);
    criteriaManager.setRoadSystem(this.roadSystem);

    observer = mockObserver();
    applicationDataSystem.addSubscriber(observer);
  }

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
  void testSaveOnLanguageChanged() {
    // the following should trigger a save since GERMAN is not the default language
    applicationDataSystem.setLanguage(Language.GERMAN);
    assertTrue(Files.exists(CONFIG_FILE));
  }

  @Test
  void testSaveOnCriterionChanges() throws IOException {
    var criterion = applicationDataSystem.getCriteriaManager().createCompatibilityCriterion();
    assertTrue(Files.exists(CONFIG_FILE));
    Files.delete(CONFIG_FILE);

    criterion.setName("Totally creative name!");
    assertTrue(Files.exists(CONFIG_FILE));
    Files.delete(CONFIG_FILE);

    applicationDataSystem.getCriteriaManager().removeCriterion(criterion);
    assertTrue(Files.exists(CONFIG_FILE));
    Files.delete(CONFIG_FILE);
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
  void testImportCriteria() throws URISyntaxException {
    Assumptions.assumeTrue(CRITERIA_IMPORT_URL != null);
    var criteriaPath = Paths.get(CRITERIA_IMPORT_URL.toURI());

    var defaultAmount = applicationDataSystem.getCriteriaManager().getCriteria().getSize();
    applicationDataSystem.importCriteriaFromFile(criteriaPath);
    assertEquals(defaultAmount + CRITERIA_IMPORT_AMOUNT,
        applicationDataSystem.getCriteriaManager().getCriteria().getSize());
  }

  /**
   * Helper method to extract the "unchecked" (but correct) cast of the observer mock.
   */
  @SuppressWarnings("unchecked")
  private static SetObserver<AttributeType, ApplicationDataSystem> mockObserver() {
    return mock(SetObserver.class);
  }
}
