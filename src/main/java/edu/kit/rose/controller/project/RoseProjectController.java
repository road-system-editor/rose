package edu.kit.rose.controller.project;

import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.ErrorType;
import edu.kit.rose.controller.navigation.FileDialogType;
import edu.kit.rose.controller.navigation.FileFormat;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ProjectFormat;
import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Provides functionality to save, load and
 * export a project.
 */
public class RoseProjectController extends Controller implements ProjectController {

  private static final String BACKUP_FOLDER_PATH = "./backups";
  private static final String BACKUP_FILENAME_TEMPLATE = "Backup%d.rose.json";
  private static final int MAX_BACKUP_COUNT = 4;
  private static final long BACKUP_DELAY_MILLISECONDS = 300000;

  private final Project project;

  private final Set<Runnable> onProjectIoActionBeginCallbacks;
  private final Set<Runnable> onProjectIoActionEndCallbacks;

  private final Timer backupTimer;

  private int lastWrittenBackupIndex = 0;

  private Path currentProjectPath;

  /**
   * Creates a new {@link RoseProjectController}.
   *
   * @param storageLock the coordinator for controller actions
   * @param navigator   the navigator for the controller
   * @param project     the model facade for project data
   */
  public RoseProjectController(StorageLock storageLock, Navigator navigator, Project project) {
    super(storageLock, navigator);

    this.project = project;
    this.onProjectIoActionBeginCallbacks = new HashSet<>();
    this.onProjectIoActionEndCallbacks = new HashSet<>();

    this.backupTimer = new Timer();
    backupTimer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        saveBackup();
      }
    }, BACKUP_DELAY_MILLISECONDS, BACKUP_DELAY_MILLISECONDS);
  }

  @Override
  public void export(ProjectFormat targetFormat) {
    if (!getStorageLock().isStorageLockAcquired()) {
      getStorageLock().acquireStorageLock();
      this.onProjectIoActionBeginCallbacks.forEach(Runnable::run);

      FileFormat targetFileFormat = toFileFormat(targetFormat);

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

  private FileFormat toFileFormat(ProjectFormat projectFormat) {
    return switch (projectFormat) {
      case ROSE -> FileFormat.ROSE;
      case YAML -> FileFormat.YAML;
      case SUMO -> FileFormat.SUMO;
    };
  }

  @Override
  public void save() {
    if (currentProjectPath == null) {
      saveAs();
    } else {
      if (!getStorageLock().isStorageLockAcquired()) {
        getStorageLock().acquireStorageLock();
        this.onProjectIoActionBeginCallbacks.forEach(Runnable::run);

        if (ensureBackupDirectoryExists()) {
          if (!project.save(currentProjectPath)) {
            getNavigator().showErrorDialog(ErrorType.SAVE_ERROR);
          }
        }

        this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
        getStorageLock().releaseStorageLock();
      }
    }
  }

  private void saveBackup() {
    if (!getStorageLock().isStorageLockAcquired()) {
      getStorageLock().acquireStorageLock();
      this.onProjectIoActionBeginCallbacks.forEach(Runnable::run);

      int currentBackupIndex = (lastWrittenBackupIndex + 1) % MAX_BACKUP_COUNT;

      if (ensureBackupDirectoryExists()) {
        Path p = Path.of(
            BACKUP_FOLDER_PATH,
            String.format(BACKUP_FILENAME_TEMPLATE, currentBackupIndex));
        if (project.save(p)) {
          lastWrittenBackupIndex = currentBackupIndex;
        }
      }

      this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
      getStorageLock().releaseStorageLock();
    }
  }

  private boolean ensureBackupDirectoryExists() {
    File f = new File(BACKUP_FOLDER_PATH);
    if (!f.exists()) {
      try {
        return f.mkdir();
      } catch (SecurityException e) {
        return false;
      }
    } else {
      return true;
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
        if (savingSucceeded) {
          currentProjectPath = targetFilePath;
        } else {
          getNavigator().showErrorDialog(ErrorType.SAVE_ERROR);
        }
      }
      this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
      getStorageLock().releaseStorageLock();
    }
  }

  @Override
  public void loadBackup(Path backUpPath) {
    if (!getStorageLock().isStorageLockAcquired()) {
      getStorageLock().acquireStorageLock();
      this.onProjectIoActionBeginCallbacks.forEach(Runnable::run);

      boolean loadingSucceeded = project.load(backUpPath);
      if (loadingSucceeded) {
        currentProjectPath = null;
      } else {
        getNavigator().showErrorDialog(ErrorType.LOAD_ERROR);
      }

      this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
      getStorageLock().releaseStorageLock();
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

  @Override
  public void shutDown() {
    this.backupTimer.cancel();
  }
}