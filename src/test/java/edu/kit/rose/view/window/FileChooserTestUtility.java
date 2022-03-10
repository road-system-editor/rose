package edu.kit.rose.view.window;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javafx.scene.input.KeyCode;
import org.testfx.api.FxRobotInterface;

/**
 * Utility class that helps with using file choosers in GUI test cases.
 */
public class FileChooserTestUtility {
  /**
   * Enters the given {@code path} into the currently open and focused OS file chooser dialog.
   */
  public static void enterPathToFileChooser(FxRobotInterface robot, String path) {
    // workaround from StackOverflow post: https://stackoverflow.com/a/55860620
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    var stringSelection = new StringSelection(path);
    clipboard.setContents(stringSelection, stringSelection);
    robot.press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
    robot.push(KeyCode.ENTER);
  }
}
