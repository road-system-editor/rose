package edu.kit.rose.view.window;

import edu.kit.rose.view.GuiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.control.LabeledMatchers;


/**
 * Test the menu of Rose.
 */
public class MenuTest extends GuiTest {

  @BeforeEach
  void setUp() {

  }

  @Test
  void testChangeLanguage() {
    clickOn("#language").clickOn("#germanLanguage");
    FxAssert.verifyThat("#project", LabeledMatchers.hasText("Projekt"));
  }
}
