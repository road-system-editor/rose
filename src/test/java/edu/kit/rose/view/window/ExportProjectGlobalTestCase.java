package edu.kit.rose.view.window;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.kit.rose.model.ProjectFormat;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.GuiTest;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.segmentbox.SegmentBlueprint;
import edu.kit.rose.view.panel.segmentbox.SegmentBoxListCell;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.testfx.api.FxRobotInterface;

/**
 * This GUI test automates the global test case 8.7 where a road system is exported to the
 * FREEVAL YAML format.
 */
class ExportProjectGlobalTestCase extends GuiTest {
  private static final Path FREEVAL_EXPORT_PATH =
      Path.of("./build/tmp/gui-test-export.yml").toAbsolutePath().normalize();
  private static final Path SUMO_EXPORT_PATH =
      Path.of("./build/tmp/gui-test-export.net.xml").toAbsolutePath().normalize();

  @BeforeEach
  void setUp() throws IOException {
    ListView<SegmentType> listView = lookup("#blueprintListView").queryListView();

    // create a very basic road system
    List<SegmentBlueprint> segmentBoxListCell = from(listView)
        .lookup((Node node) -> node.getParent() instanceof SegmentBoxListCell)
        .queryAllAs(SegmentBlueprint.class)
        .stream().toList();
    doubleClickOn(segmentBoxListCell.get(0));
    SegmentView<Base> base = lookup(node -> node instanceof SegmentView<?> view
        && view.getSegment().getSegmentType() == SegmentType.BASE)
        .query();

    // make sure that the segment can be exported
    Platform.runLater(() -> {
      base.getSegment().setLength(150);
      base.getSegment().setLaneCount(3);
      base.getSegment().setSlope(2.0);
      base.getSegment().setConurbation(false);
      base.getSegment().setMaxSpeed(SpeedLimit.TUNNEL);
    });

    // make sure file does not exist yet
    Files.deleteIfExists(FREEVAL_EXPORT_PATH);
    Files.deleteIfExists(SUMO_EXPORT_PATH);
  }

  @Test
  @EnabledOnOs(OS.WINDOWS)
  void testExportProjectToFreevalYaml() throws IOException {
    clickOn("#project");
    clickOn("#exportProject");

    // we need to find the YAML export button first since it does not have an ID
    // search for the menu label
    var yamlMenuItem = findLabelByName(ProjectFormat.YAML.name());
    moveTo(yamlMenuItem); // first move to this menu item, otherwise the submenu will close
    FxRobotInterface robot = clickOn(yamlMenuItem);

    assertFalse(Files.exists(FREEVAL_EXPORT_PATH));
    FileChooserTestUtility.enterPathToFileChooser(robot, FREEVAL_EXPORT_PATH);
    assertTrue(Files.exists(FREEVAL_EXPORT_PATH));
    assertTrue(Files.readString(FREEVAL_EXPORT_PATH).contains("Segmente:\n"));
  }

  @Test
  @Disabled("the SUMO format exporter has not been implemented")
  @EnabledOnOs(OS.WINDOWS)
  void testExportProjectToSumoNetXml() throws IOException {
    clickOn("#project");
    clickOn("#exportProject");

    // we need to find the YAML export button first since it does not have an ID
    // search for the menu label
    var yamlMenuItem = findLabelByName(ProjectFormat.YAML.name());
    moveTo(yamlMenuItem); // first move to this menu item, otherwise the submenu will close
    var sumoMenuItem = findLabelByName(ProjectFormat.SUMO.name());
    moveTo(sumoMenuItem);

    var robot = clickOn(sumoMenuItem);
    assertFalse(Files.exists(SUMO_EXPORT_PATH));
    FileChooserTestUtility.enterPathToFileChooser(robot, SUMO_EXPORT_PATH);
    assertTrue(Files.exists(SUMO_EXPORT_PATH));
    assertTrue(Files.readString(SUMO_EXPORT_PATH).contains("xml"));
  }

  private Label findLabelByName(String name) {
    return lookup(node -> node instanceof Label cont
        && cont.getText().equals(name))
        .queryAs(Label.class);
  }
}
