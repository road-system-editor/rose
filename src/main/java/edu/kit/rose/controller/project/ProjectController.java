package edu.kit.rose.controller.project;


import edu.kit.rose.controller.navigation.FileFormat;
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
  void export(FileFormat targetFormat);


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
}

