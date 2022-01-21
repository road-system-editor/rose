package edu.kit.rose.controller.commons;

/**
 * Provides the functionality for controllers to synchronize
 * and coordinate their actions.
 *
 * @author ROSE Team
 */
public interface StorageLock {

  /**
   * Acquires the storage lock for the caller.
   */
  void acquireStorageLock();

  /**
   * Checks if the storage lock is acquired.
   *
   * @return true if the storage lock is acquired, false else
   */
  boolean isStorageLockAcquired();

  /**
   * Releases the storage lock if the caller has already acquired it.
   */
  void releaseStorageLock();
}
