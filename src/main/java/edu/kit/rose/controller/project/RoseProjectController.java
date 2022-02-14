package edu.kit.rose.controller.project;

import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.ErrorType;
import edu.kit.rose.controller.navigation.FileDialogType;
import edu.kit.rose.controller.navigation.FileFormat;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ProjectFormat;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides functionality to save, load and
 * export a project.
 */
public class RoseProjectController extends Controller implements ProjectController {

  private final Project project;

  private final Set<Runnable> onProjectIoActionBeginCallbacks;
  private final Set<Runnable> onProjectIoActionEndCallbacks;

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
    this.onProjectIoActionBeginCallbacks = new HashSet<>();
    this.onProjectIoActionEndCallbacks = new HashSet<>();
  }

  @Override
  public void export(ProjectFormat targetFormat) {
    if (!getStorageLock().isStorageLockAcquired()) {
      getStorageLock().acquireStorageLock();
      this.onProjectIoActionBeginCallbacks.forEach(Runnable::run);
      FileFormat targetFileFormat = switch (targetFormat) {
        case ROSE -> FileFormat.ROSE;
        case YAML -> FileFormat.YAML;
        case SUMO -> FileFormat.SUMO;
      };
      Path targetFilePath
          = getNavigator().showFileDialog(FileDialogType.SAVE_FILE, targetFileFormat);

      if (targetFilePath != null) {
        boolean exportSucceeded = project.exportToFile(targetFormat, targetFilePath);
        if (!exportSucceeded) {
          getNavigator().showErrorDialog(ErrorType.EXPORT_ERROR);
        }
      }
      this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
      getStorageLock().releaseStorageLock();
    }
  }

  @Override
  public void save() {
    if (!getStorageLock().isStorageLockAcquired()) {
      getStorageLock().acquireStorageLock();
      this.onProjectIoActionBeginCallbacks.forEach(Runnable::run);

      this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
    }
  }

  @Override
  public void saveAs() {
    if (!getStorageLock().isStorageLockAcquired()) {
      getStorageLock().acquireStorageLock();
      this.onProjectIoActionBeginCallbacks.forEach(Runnable::run);
      Path targetFilePath
          = getNavigator().showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE);

      if (targetFilePath != null) {
        boolean savingSucceeded = project.save(targetFilePath);
        if (!savingSucceeded) {
          getNavigator().showErrorDialog(ErrorType.SAVE_ERROR);
        }
      }
      this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
    }
  }

  @Override
  public void loadBackup(Path backUpPath) {
    if (!getStorageLock().isStorageLockAcquired()) {
      getStorageLock().acquireStorageLock();
      this.onProjectIoActionBeginCallbacks.forEach(Runnable::run);

      boolean loadingSucceeded = project.load(backUpPath);
      if (!loadingSucceeded) {
        getNavigator().showErrorDialog(ErrorType.LOAD_ERROR);
      }

      this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
    }
  }

  @Override
  public void subscribeToProjectIoAction(Runnable onBegin, Runnable onDone) {
    this.onProjectIoActionBeginCallbacks.add(onBegin);
    this.onProjectIoActionEndCallbacks.add(onDone);
  }

  @Override
  public void unsubscribeFromProjectIoAction(Runnable onBegin, Runnable onEnd) {
    this.onProjectIoActionBeginCallbacks.remove(onBegin);
    this.onProjectIoActionEndCallbacks.remove(onEnd);
  }
}
