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
  PROJECT_IMPORT_ERROR,
  PROJECT_EXPORT_ERROR,
  CRITERIA_IMPORT_ERROR,
  CRITERIA_EXPORT_ERROR
}
