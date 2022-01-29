package edu.kit.rose.controller.project;

import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.FileDialogType;
import edu.kit.rose.controller.navigation.FileFormat;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ProjectFormat;
import java.nio.file.Path;

/**
 * Provides functionality to save, load and
 * export a project.
 */
public class RoseProjectController extends Controller implements ProjectController {

  private final Project project;

  /**
   * Creates a new {@link RoseProjectController}.
   *
   * @param storageLock         the coordinator for controller actions
   * @param navigator           the navigator for the controller
   * @param project             the model facade for project data
   */
  public RoseProjectController(StorageLock storageLock, Navigator navigator, Project project) {
    super(storageLock, navigator);

    this.project = project;
  }

  @Override
  public void export(ProjectFormat targetFormat) {

  }

  @Override
  public void save() {

  }

  @Override
  public void saveAs() {
    Path targetPath = getNavigator().showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE);
    if (targetPath != null) {
      project.save(targetPath);
    }
  }

  @Override
  public void loadBackup(Path backUpPath) {

  }

  @Override
  public void subscribeToProjectIoAction(Runnable onBegin, Runnable onDone) {

  }

  @Override
  public void unsubscribeFromProjectIoAction(Runnable onBegin, Runnable onEnd) {

  }
}
