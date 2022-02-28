package edu.kit.rose.controller.project;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.ErrorType;
import edu.kit.rose.controller.navigation.FileDialogType;
import edu.kit.rose.controller.navigation.FileFormat;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseProjectController}.
 */
public class RoseProjectControllerTest {
  private static final Path INVALID_PATH =
      Path.of("build", "tmp", "invalid-directory", "invalid-file.rose.json");

  private Navigator navigator;
  private RoseProjectController controller;

  @BeforeEach
  void beforeEach() {
    StorageLock storageLock = mock(StorageLock.class);
    this.navigator = mock(Navigator.class);
    Project project = mock(Project.class);
    ApplicationDataSystem applicationDataSystem = mock(ApplicationDataSystem.class);

    this.controller = new RoseProjectController(
        storageLock,
        this.navigator,
        project,
        applicationDataSystem
    );
  }

  @Test
  void testSaveShowsErrorOnFailure() {
    when(this.navigator.showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE))
        .thenReturn(INVALID_PATH);

    this.controller.save();
    verify(this.navigator, times(1)).showErrorDialog(ErrorType.SAVE_ERROR);
  }

  @Test
  void testLoadShowsErrorOnFailure() {
    when(this.navigator.showFileDialog(FileDialogType.LOAD_FILE, FileFormat.ROSE))
        .thenReturn(INVALID_PATH);

    this.controller.loadProject();
    verify(this.navigator, times(1)).showErrorDialog(ErrorType.LOAD_ERROR);
  }
}
