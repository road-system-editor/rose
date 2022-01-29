package edu.kit.rose.controller.navigation;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.model.ProjectFormat;

/**
 * Contains file formats that can be used to
 * configure {@link Navigator#showFileDialog(FileDialogType, FileFormat)}.
 */
public enum FileFormat {
  ROSE("yaml", "yml"),
  SUMO("sumo"),
  YAML("rose"),
  CRITERIA("criteria");

  /**
   * Creates a new value in {@link ProjectFormat} enum.
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
