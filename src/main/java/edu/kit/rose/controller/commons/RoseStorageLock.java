package edu.kit.rose.controller.commons;

/**
 * Provides the functionality for controllers to synchronize
 * and coordinate their actions.
 *
 */
public class RoseStorageLock implements StorageLock {
    @Override
    public void aquireStorageLock() {

    }

    @Override
    public boolean isStorageLockAquired() {
        return false;
    }

    @Override
    public void releaseStorageLock() {

    }
}
