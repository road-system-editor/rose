package edu.kit.rose.view.window;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.panel.segmentbox.SegmentBlueprint;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxListCell;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.testfx.api.FxRobotInterface;

/**
 * This GUI test automates the global test case 8.6 where a road system is saved to disk.
 */
class SaveProjectGlobalTestCase extends GuiTest {
  private static final Path TEST_SAVE_PATH =
      Path.of("./build/tmp/gui-test-save.rose.json").toAbsolutePath().normalize();

  @BeforeEach
  void setUp() throws IOException {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();

    // create a very basic road system
    List<SegmentBlueprint> segmentBoxListCell = from(listView)
        .lookup((Node node) -> node.getParent() instanceof SegmentBoxListCell)
        .queryAllAs(SegmentBlueprint.class)
        .stream().toList();
    doubleClickOn(segmentBoxListCell.get(0));

    // make sure file does not exist yet
    Files.delete(TEST_SAVE_PATH);
  }

  @Test
  @EnabledOnOs(OS.WINDOWS)
  void testSaveProject() {
    FxRobotInterface robot = clickOn("#project");
    clickOn("#saveProject");

    assertFalse(Files.exists(TEST_SAVE_PATH));
    FileChooserTestUtility.enterPathToFileChooser(robot, TEST_SAVE_PATH);
    assertTrue(Files.exists(TEST_SAVE_PATH));
  }
}
