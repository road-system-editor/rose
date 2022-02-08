package edu.kit.rose.model;

import java.io.File;

/**
 * Implements the export for the {@link edu.kit.rose.model.roadsystem.RoadSystem} into the
 * SUMO-Format.
 * This Format allows the Project to be reopened a Program supporting the SUMO-Format.
 */
class SumoExportStrategy extends ExportStrategy {
  @Override
  boolean exportToFile(File file) {
    // TODO
    return false;
  }
}
