package edu.kit.rose.controller.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link RoseStorageLock}.
 */
class RoseStorageLockTest {

  @Test
  void testAcquiring() {
    RoseStorageLock storageLock = new RoseStorageLock();

    Assertions.assertFalse(storageLock.isStorageLockAcquired());

    storageLock.acquireStorageLock();
    Assertions.assertTrue(storageLock.isStorageLockAcquired());

    storageLock.releaseStorageLock();
    Assertions.assertFalse(storageLock.isStorageLockAcquired());
  }
}
