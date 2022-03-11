package edu.kit.rose.view;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Tests if the program starts.
 */
class TestProgramStart extends GuiTest {

  /**
   * Represents T1.
   * With this test you will find if the program starts.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testStartProgram() {
    Assertions.assertTrue(lookup("#violationList").query().getScene().getWindow().isShowing());
  }
}
