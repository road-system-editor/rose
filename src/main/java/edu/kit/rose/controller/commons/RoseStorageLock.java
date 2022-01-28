package edu.kit.rose.controller.commons;

/**
 * Provides the functionality for controllers to synchronize
 * and coordinate their actions.
 */
public class RoseStorageLock implements StorageLock {

  private final Object synchronizationObject = new Object();
  private boolean isAcquired = false;

  @Override
  public void acquireStorageLock() {
    synchronized (synchronizationObject) {
      isAcquired = true;
    }
  }

  @Override
  public boolean isStorageLockAcquired() {
    synchronized (synchronizationObject) {
      return isAcquired;
    }

  }

  @Override
  public void releaseStorageLock() {
    synchronized (synchronizationObject) {
      isAcquired = false;
    }
  }
}
