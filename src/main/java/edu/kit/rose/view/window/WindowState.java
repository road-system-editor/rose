package edu.kit.rose.view.window;

public enum WindowState {
    /**
     * The window is initialized but not visible yet.
     */
    INITIALIZED,
    /**
     * The window is visible. This does not specify whether the window is in focus or minimized.
     */
    VISIBLE,
    /**
     * The window has been closed and can not be re-used.
     */
    CLOSED
}
