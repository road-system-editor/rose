package edu.kit.rose.controller.commons;

import edu.kit.rose.controller.navigation.Navigator;
import java.util.Objects;

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
   * @param navigator the navigator for the controller.
   */
  protected Controller(StorageLock storageLock, Navigator navigator) {
    this.storageLock = Objects.requireNonNull(storageLock, "storage lock may not be null");
    this.navigator = Objects.requireNonNull(navigator, "navigator may not be null");
  }

  /**
   * Initializes the {@link Controller} with a {@link Navigator}.
   *
   * @param navigator the navigator for the controller.
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
