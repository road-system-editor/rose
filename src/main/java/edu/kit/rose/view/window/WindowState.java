package edu.kit.rose.view.window;

/**
 * The window state describes the stages in the life cycle of a window (see section 4.3.2 of the
 * design document).
 */
public enum WindowState {
  /**
   * The window is initialized but not visible yet.
   */
  INITIALIZED,
  /**
   * The window is visible.
   * This does not specify whether the window is in focus or minimized.
   */
  VISIBLE,
  /**
   * The window has been closed and can not be re-used.
   */
  CLOSED
}
