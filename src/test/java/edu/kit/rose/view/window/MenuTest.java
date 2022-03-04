package edu.kit.rose.view.window;

import edu.kit.rose.view.GuiTest;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.Assertions;
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
    clickOn("#language").clickOn("#englishLanguage");
    FxAssert.verifyThat("#project", LabeledMatchers.hasText("Project"));
    FxAssert.verifyThat("#validation", LabeledMatchers.hasText("Validation"));
    FxAssert.verifyThat("#createGroupButton", LabeledMatchers.hasText("Group from selection"));
  }

  @Test
  void testShortcutsDisplay() {
    clickOn("#help").clickOn("#shortcuts");
    TableView tableView = lookup("#shortCutTable").query();
    Assertions.assertEquals(12, tableView.getItems().size());
  }
}
