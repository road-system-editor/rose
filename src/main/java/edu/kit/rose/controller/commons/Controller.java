package edu.kit.rose.controller.commons;

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

  private final StorageLock storageLock;
  private Navigator navigator;

  /**
   * Creates a new instance of the {@link Controller} class.
   *
   * @param storageLock the {@link StorageLock} instance of the controller.
   */
  protected Controller(StorageLock storageLock) {
    this.storageLock = storageLock;
  }

  /**
   * Initializes the {@link Controller} with a {@link Navigator}.
   *
   * @param navigator the navigator for the controller
   */
  public final void initialize(Navigator navigator) {
    this.navigator = navigator;
  }

  /**
   * Returns the {@link Navigator} instance of the {@link Controller}.
   *
   * @return {@link Navigator} instance of the {@link Controller}
   */
  protected final Navigator getNavigator() {
    return navigator;
  }

  /**
   * Returns the {@link StorageLock} instance of the {@link Controller}.
   *
   * @return {@link StorageLock} instance of the {@link Controller}
   */
  protected final StorageLock getStorageLock() {
    return storageLock;
  }
}
