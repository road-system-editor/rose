package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import java.util.List;

/**
 * An enum holding the different Formats to which a Project can be exported.
 */
public enum ExportFormat {
  YAML(List.of("yaml")),
  SUMO(List.of("sumo")),
  ROSE(List.of("rose"));

  /**
   * Creates a new value in {@link ExportFormat} enum.
   *
   * @param fileExtensions all valid file extensions for an export format
   */
  ExportFormat(List<String> fileExtensions) {
    this.fileExtensions = fileExtensions;
  }

  private List<String> fileExtensions;

  public Box<String> getFileExtensions() {
    return new RoseBox<String>(fileExtensions);
  }
}
