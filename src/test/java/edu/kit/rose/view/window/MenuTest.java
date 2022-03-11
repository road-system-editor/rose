package edu.kit.rose.view.window;

import static org.hamcrest.MatcherAssert.assertThat;

import edu.kit.rose.view.GuiTest;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.testfx.api.FxAssert;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TableViewMatchers;

/**
 * Test the menu of Rose.
 */
class MenuTest extends GuiTest {
  /**
   * Represents T15.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testChangeLanguage() {
    clickOn("#language").clickOn("#englishLanguage");
    FxAssert.verifyThat("#project", LabeledMatchers.hasText("Project"));
    FxAssert.verifyThat("#validation", LabeledMatchers.hasText("Validation"));
    FxAssert.verifyThat("#createGroupButton", LabeledMatchers.hasText("Group from selection"));
  }

  /**
   * Represents T27.
   */
  @EnabledOnOs(OS.WINDOWS)
  @Test
  void testShortcutsDisplay() {
    clickOn("#help").clickOn("#shortcuts");
    TableView<?> tableView = lookup("#shortCutTable").query();

    assertThat(tableView, TableViewMatchers.hasNumRows(13));

    // check if some shortcuts are contained in the table
    assertThat(tableView, TableViewMatchers.containsRow("Ctrl+O", "Open Project"));
    assertThat(tableView, TableViewMatchers.containsRow("Ctrl+Z", "Undo"));
  }
}
