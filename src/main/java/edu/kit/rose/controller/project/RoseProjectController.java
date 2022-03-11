package edu.kit.rose.controller.project;

import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.ErrorType;
import edu.kit.rose.controller.navigation.FileDialogType;
import edu.kit.rose.controller.navigation.FileFormat;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ProjectFormat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides functionality to save, load and
 * export a project.
 */
public class RoseProjectController extends Controller implements ProjectController {
  private static final Path DEFAULT_BACKUP_FOLDER_PATH = Path.of(".", "backups");
  private static final String BACKUP_FILENAME_TEMPLATE = "Backup%d.rose.json";
  private static final int MAX_BACKUP_COUNT = 4;
  private static final long BACKUP_DELAY_MILLISECONDS = 300000;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Project project;
  private final ApplicationDataSystem applicationDataSystem;

  private final Set<Runnable> onProjectIoActionBeginCallbacks;
  private final Set<Runnable> onProjectIoActionEndCallbacks;

  private final Timer backupTimer;

  private int lastWrittenBackupIndex = 0;

  private final Path backupDirectoryPath;
  private Path currentProjectPath;

  /**
   * Creates a new {@link RoseProjectController}.
   *
   * @param storageLock           the coordinator for controller actions
   * @param navigator             the navigator for the controller
   * @param project               the model facade for project data
   * @param applicationDataSystem the application data system to store recently opened project
   *                              paths in.
   */
  public RoseProjectController(StorageLock storageLock, Navigator navigator, Project project,
                               ApplicationDataSystem applicationDataSystem) {
    this(storageLock, navigator, project, applicationDataSystem,
        BACKUP_DELAY_MILLISECONDS, DEFAULT_BACKUP_FOLDER_PATH);
  }

  /**
   * Creates a new {@link RoseProjectController}.
   *
   * @param storageLock           the coordinator for controller actions
   * @param navigator             the navigator for the controller
   * @param project               the model facade for project data
   * @param applicationDataSystem the application data system to store recently opened project
   *                              paths in.
   * @param backupInterval        the amount of time (in milliseconds) between backups.
   */
  public RoseProjectController(StorageLock storageLock, Navigator navigator, Project project,
                               ApplicationDataSystem applicationDataSystem, long backupInterval,
                               Path backupDirectoryPath) {
    super(storageLock, navigator);

    this.project = Objects.requireNonNull(project);
    this.applicationDataSystem = Objects.requireNonNull(applicationDataSystem);
    this.backupDirectoryPath = Objects.requireNonNull(backupDirectoryPath);
    this.onProjectIoActionBeginCallbacks = new HashSet<>();
    this.onProjectIoActionEndCallbacks = new HashSet<>();

    this.backupTimer = new Timer();
    backupTimer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        saveBackup();
      }
    }, backupInterval, backupInterval);

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
          getNavigator().showErrorDialog(ErrorType.PROJECT_EXPORT_ERROR);
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
        String fileName = String.format(BACKUP_FILENAME_TEMPLATE, currentBackupIndex);
        Path p = this.backupDirectoryPath.resolve(fileName);
        if (project.save(p)) {
          lastWrittenBackupIndex = currentBackupIndex;
        }
      }

      this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
      getStorageLock().releaseStorageLock();
    }
  }

  private boolean ensureBackupDirectoryExists() {
    if (!Files.exists(this.backupDirectoryPath)) {
      try {
        Files.createDirectories(this.backupDirectoryPath);
        return true;
      } catch (IOException e) {
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
          setCurrentProjectPath(targetFilePath);
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
        setCurrentProjectPath(null);
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

  @Override
  public void createNewProject() {
    setCurrentProjectPath(null);
    this.project.reset();
  }

  @Override
  public void loadProject() {
    if (!getStorageLock().isStorageLockAcquired()) {
      getStorageLock().acquireStorageLock();
      this.onProjectIoActionBeginCallbacks.forEach(Runnable::run);
      Path sourceFilePath
          = getNavigator().showFileDialog(FileDialogType.LOAD_FILE, FileFormat.ROSE);

      if (sourceFilePath != null) {
        boolean loadingSucceeded = project.load(sourceFilePath);
        if (loadingSucceeded) {
          setCurrentProjectPath(sourceFilePath);
        } else {
          getNavigator().showErrorDialog(ErrorType.LOAD_ERROR);
        }
      }
      this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
      getStorageLock().releaseStorageLock();
    }
  }

  @Override
  public Box<Path> getBackupPaths() {
    if (ensureBackupDirectoryExists()) {
      List<Path> backupPaths = new LinkedList<>();
      try (var stream = Files.newDirectoryStream(this.backupDirectoryPath)) {
        stream.forEach(backupPaths::add);
      } catch (IOException e) {
        logger.error("Could not create backup path list", e);
        return new RoseBox<>();
      }

      Set<String> allowedExtensions = FileFormat.ROSE.getFileExtensions().stream()
          .map(ext -> ext.substring(ext.lastIndexOf("*") + 1))
          .collect(Collectors.toSet());

      backupPaths.removeIf(
          path -> allowedExtensions.stream().noneMatch(path.getFileName().toString()::endsWith));

      return new RoseBox<>(backupPaths);
    }
    return new RoseBox<>();
  }

  @Override
  public void loadRecentProject(Path recentProjectPath) {
    if (!getStorageLock().isStorageLockAcquired()) {
      getStorageLock().acquireStorageLock();
      this.onProjectIoActionBeginCallbacks.forEach(Runnable::run);

      boolean loadingSucceeded = project.load(recentProjectPath);
      if (loadingSucceeded) {
        setCurrentProjectPath(recentProjectPath);
      } else {
        getNavigator().showErrorDialog(ErrorType.LOAD_ERROR);
      }

      this.onProjectIoActionEndCallbacks.forEach(Runnable::run);
      getStorageLock().releaseStorageLock();
    }
  }

  private void setCurrentProjectPath(Path projectPath) {
    this.currentProjectPath = projectPath;
    if (projectPath != null) {
      this.applicationDataSystem.addRecentProjectPath(projectPath);
    }
  }
}