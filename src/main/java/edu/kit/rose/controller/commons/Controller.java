package edu.kit.rose.controller.commons;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.navigation.Navigator;

/**
 * Base class for all controllers.
 * Provides services for navigation handling,
 * change handling and coordination of actions between
 * multiple controllers.
 *
 * @author ROSE Team
 */
public abstract class Controller {

  protected Controller(StorageLock storageLock) {
  }

  /**
   * Initializes the controller with a navigator and a command buffer.
   *
   * @param navigator the navigator for the controller
   */
  public final void initialize(Navigator navigator) {

  }

  /**
   * Returns the navigator instance of the controller.
   *
   * @return navigator instance of the controller
   */
  protected final Navigator getNavigatior() {
    return null;
  }
}
