package edu.kit.rose.controller.navigation;

import edu.kit.rose.controller.commons.Controller;

/**
 * The {@link ErrorType} contains types of errors that can
 * occur in the {@link Controller}s and that need to be displayed
 * to the user.
 */
public enum ErrorType {
  SAVE_ERROR,
  LOAD_ERROR,
  IMPORT_ERROR,
  EXPORT_ERROR
}
