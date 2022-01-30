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
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseApplicationDataSystem}.
 */
public class RoseApplicationDataSystemTest {
  static final Path CONFIG_FILE = Path.of("build/tmp/test_workspace/config");

  ApplicationDataSystem applicationDataSystem;
  SetObserver<AttributeType, ApplicationDataSystem> observer;

  @BeforeEach
  void beforeEach() {
    applicationDataSystem = new RoseApplicationDataSystem(CONFIG_FILE);

    observer = mockObserver();
    applicationDataSystem.addSubscriber(observer);
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

  /**
   * Helper method to extract the "unchecked" (but correct) cast of the observer mock.
   */
  @SuppressWarnings("unchecked")
  private static SetObserver<AttributeType, ApplicationDataSystem> mockObserver() {
    return mock(SetObserver.class);
  }
}
