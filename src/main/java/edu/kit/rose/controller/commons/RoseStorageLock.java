package edu.kit.rose.controller.commons;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides the functionality for controllers to synchronize
 * and coordinate their actions.
 */
public class RoseStorageLock implements StorageLock {

  ReentrantLock lock;

  @Override
  public boolean acquireStorageLock() {
    return lock.tryLock();
  }


  @Override
  public void releaseStorageLock() {
    lock.unlock();
  }
}
