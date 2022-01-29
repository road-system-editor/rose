package edu.kit.rose.controller.navigation;

import edu.kit.rose.model.FileFormat;

/**
 * Contains options that configure a call of
 * {@link Navigator#showFileDialog(FileDialogType, FileFormat)}.
 */
public enum FileDialogType {
  LOAD_FILE,
  SAVE_FILE
}
