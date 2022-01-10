package edu.kit.rose.controller.commons;

/**
 * Provides the functionality for controllers to synchronize
 * and coordinate their actions.
 *
 * @author ROSE Team
 */
public interface StorageLock {

    /**
     * Aquires the storage lock for the caller.
     */
    void aquireStorageLock();

    /**
     * Checks if the storage lock is aquired.
     * @return true if the storage lock is aquired, false else
     */
    boolean isStorageLockAquired();

    /**
     * Releases the storage lock if the caller has already aquired it.
     */
    void releaseStorageLock();
}
