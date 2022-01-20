package edu.kit.rose.controller.project;


import edu.kit.rose.model.ExportFormat;

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
  void export(ExportFormat targetFormat);


  /**
   * Saves the project to a file.
   */
  void save();

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

