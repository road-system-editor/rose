package edu.kit.rose.controller.project;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
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
   * @param changeCommandBuffer the buffer for change commands
   * @param storageLock         the coordinator for controller actions
   * @param project             the model facade for project data
   */
  protected RoseProjectController(ChangeCommandBuffer changeCommandBuffer, StorageLock storageLock,
                                  Project project) {
    super(changeCommandBuffer, storageLock);
  }

  @Override
  public void export(ExportFormat targetFormat) {

  }

  @Override
  public void save() {

  }

  @Override
  public void subscribeToProjectIOAction(Runnable onBegin, Runnable onDone) {

  }

  @Override
  public void unsubscribeFromProjectIOAction(Runnable onBegin, Runnable onEnd) {

  }
}
