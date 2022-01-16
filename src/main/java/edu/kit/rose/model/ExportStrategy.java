package edu.kit.rose.model;

import java.io.File;

/**
 * Describes a Strategy for exporting a {@link edu.kit.rose.model.roadsystem.RoadSystem} or the whole Project.
 * Implements the Strategy in the Strategy design Pattern.
 */
abstract class ExportStrategy {

  /**
   * Exports the current {@link edu.kit.rose.model.roadsystem.RoadSystem} or Project into the given File.
   *
   * @param file the location to export the Project to.
   */
  void exportToFile(File file) {

  }
}
