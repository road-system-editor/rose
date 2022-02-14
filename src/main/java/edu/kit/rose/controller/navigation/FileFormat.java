package edu.kit.rose.controller.navigation;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.model.ProjectFormat;

/**
 * Contains file formats that can be used to
 * configure {@link Navigator#showFileDialog(FileDialogType, FileFormat)}.
 */
public enum FileFormat {
  ROSE("rose.json"),
  SUMO("sumo"),
  YAML("yaml", "yml"),
  CRITERIA("criteria");

  /**
   * Creates a new value in {@link FileFormat} enum.
   *
   * @param fileExtensions all valid file extensions for an export format
   */
  FileFormat(String ... fileExtensions) {
    this.fileExtensions = new RoseBox<String>(fileExtensions);
  }

  private final Box<String> fileExtensions;

  public Box<String> getFileExtensions() {
    return fileExtensions;
  }
}
