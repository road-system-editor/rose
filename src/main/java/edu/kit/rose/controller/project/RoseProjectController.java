package edu.kit.rose.controller.project;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.model.ExportFormat;
import edu.kit.rose.model.Project;

/**
 * Provides functionality to save, load and
 * export a project.
 */
public class RoseProjectController extends Controller implements ProjectController {

  /**
   * Creates a new {@link RoseProjectController}.
   *
   * @param storageLock         the coordinator for controller actions
   * @param navigator           the navigator for the controller
   * @param project             the model facade for project data
   */
  public RoseProjectController(StorageLock storageLock, Navigator navigator, Project project) {
    super(storageLock, navigator);
  }

  @Override
  public void export(ExportFormat targetFormat) {

  }

  @Override
  public void save() {

  }

  @Override
  public void subscribeToProjectIoAction(Runnable onBegin, Runnable onDone) {

  }

  @Override
  public void unsubscribeFromProjectIoAction(Runnable onBegin, Runnable onEnd) {

  }
}
