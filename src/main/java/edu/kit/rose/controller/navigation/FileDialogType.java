package edu.kit.rose.controller.navigation;

import edu.kit.rose.model.ProjectFormat;

/**
 * Contains options that configure a call of
 * {@link Navigator#showFileDialog(FileDialogType, ProjectFormat)}.
 */
public enum FileDialogType {
  LOAD_FILE,
  SAVE_FILE
}
