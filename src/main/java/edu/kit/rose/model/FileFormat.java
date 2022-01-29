package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;

/**
 * An enum holding the different Formats to which a Project can be exported.
 */
public enum FileFormat {
  YAML("yaml", "yml"),
  SUMO("sumo"),
  ROSE("rose");

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
