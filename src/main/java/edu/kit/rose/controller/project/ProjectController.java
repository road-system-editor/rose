package edu.kit.rose.controller.project;


import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.model.ProjectFormat;
import java.nio.file.Path;

/**
 * Provides functionality to save, load and
 * export a project.
 */
public interface ProjectController {

  /**
   * Exports the roadsystem into a specified target format.
   *
   * @param targetFormat format in which the roadsystem will be exported
   */
  void export(ProjectFormat targetFormat);


  /**
   * Saves the project to a file.
   */
  void save();

  /**
   * Lets the user pick a file and saves the project to that file.
   */
  void saveAs();

  /**
   * Loads the newest backup version of the currently opened project.
   *
   * @param backUpPath the path where the backup file is stored
   */
  void loadBackup(Path backUpPath);

  /**
   * Registers a runnable that gets called before the controller executes a loading, saving or
   * an export and a runnable that runs when it is done.
   *
   * @param onBegin called before the import or export
   * @param onDone  called after the import or export
   */
  void subscribeToProjectIoAction(Runnable onBegin, Runnable onDone);

  /**
   * Unregisters a runnable that gets called before the controller executes a loading, saving or
   * an export and a runnable that runs when it is done.
   *
   * @param onBegin called before the import or export
   * @param onDone  called after the import or export
   */
  void unsubscribeFromProjectIoAction(Runnable onBegin, Runnable onDone);

  /**
   * Shuts down the {@link ProjectController} and stops its automatic backups.
   */
  void shutDown();

  /**
   * Creates a new project in this editor.
   * This will clear all project data.
   */
  void createNewProject();

  /**
   * Lets the user pick a file to load.
   */
  void loadProject();

  /**
   * Returns the paths of all backed up files.
   */
  Box<Path> getBackupPaths();

  /**
   * Load recent project from the given path.
   */
  void loadRecentProject(Path recentProjectPath);
}

