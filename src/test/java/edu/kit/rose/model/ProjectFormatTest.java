package edu.kit.rose.model;

import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ProjectFormat}.
 */
class ProjectFormatTest {
  @Test
  void testEnumValuesAreDifferent() {
    assertNotSame(ProjectFormat.ROSE, ProjectFormat.SUMO);
    assertNotSame(ProjectFormat.ROSE, ProjectFormat.YAML);
    assertNotSame(ProjectFormat.SUMO, ProjectFormat.YAML);
  }
}
