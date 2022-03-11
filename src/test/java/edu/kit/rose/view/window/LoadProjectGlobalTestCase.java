package edu.kit.rose.view.window;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.ConnectionView;
import edu.kit.rose.view.commons.SegmentView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.testfx.api.FxRobotInterface;

/**
 * This GUI test automates the global test case 8.14 where a project is loaded from a file.
 */
class LoadProjectGlobalTestCase extends GuiTest {
  private static final String FILE_NAME = "LoadProjectGlobalTestCase.rose.json";
  private static final Path TMP_LOAD_PATH =
      Path.of("./build/tmp/" + FILE_NAME).toAbsolutePath().normalize();
  private static final Path RESOURCE_PATH =
      Path.of("./src/test/resources/edu/kit/rose/view/window/" + FILE_NAME)
          .toAbsolutePath().normalize();

  @BeforeEach
  void setUp() throws IOException {
    Files.copy(RESOURCE_PATH, TMP_LOAD_PATH);
  }

  @Test
  @EnabledOnOs(OS.WINDOWS)
  void testLoadProject() {
    clickOn("#project");
    FxRobotInterface robot = clickOn("#loadProject");

    assertTrue(lookup(node -> node instanceof SegmentView<?>).queryAll().isEmpty());
    FileChooserTestUtility.enterPathToFileChooser(robot, TMP_LOAD_PATH);
    assertEquals(20, lookup(node -> node instanceof SegmentView<?>).queryAll().size());
    assertEquals(17, lookup(node -> node instanceof ConnectionView).queryAll().size());
  }
}
