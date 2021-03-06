package edu.kit.rose.controller.navigation;

import java.nio.file.Path;

/**
 * Provides functionality to create new windows and file dialogs.
 */
public interface Navigator {
  /**
   * Creates and shows a new window of specified type.
   *
   * @param windowType the type of window to be created and shown
   */
  void showWindow(WindowType windowType);

  /**
   * Creates and shows a file dialog, that provides to user
   * the possibility to choose the path of a file
   * and return that path.
   *
   * @param option specifies whether to load or save a file
   * @param fileFormat specifies the target file format
   *
   * @return the path that the user chose
   */
  Path showFileDialog(FileDialogType option, FileFormat fileFormat);

  /**
   * Shows a Dialog, that informs the user about an error of the
   * specified {@link ErrorType}.
   *
   * @param errorType the type of the error that occured
   */
  void showErrorDialog(ErrorType errorType);
}
