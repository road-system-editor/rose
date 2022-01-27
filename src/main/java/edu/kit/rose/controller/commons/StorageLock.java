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
   *
   * @return true if the lock is acquired and false otherwise
   */
  boolean acquireStorageLock();

  /**
   * Releases the storage lock if the caller has already acquired it.
   */
  void releaseStorageLock();
}
