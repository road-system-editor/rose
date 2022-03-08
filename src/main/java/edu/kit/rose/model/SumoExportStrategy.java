package edu.kit.rose.model;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the export for the {@link edu.kit.rose.model.roadsystem.RoadSystem} into the
 * SUMO-Format.
 * This Format allows the Project to be reopened a Program supporting the SUMO-Format.
 */
class SumoExportStrategy extends ExportStrategy {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  boolean exportToFile(File file) {
    // TODO
    logger.error("SUMO export is not implemented!");
    return false;
  }
}
