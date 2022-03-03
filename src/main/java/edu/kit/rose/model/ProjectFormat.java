package edu.kit.rose.model;

/**
 * An enum holding the different Formats to which a Project can be exported.
 */
public enum ProjectFormat {
  /**
   * This is the format for the traffic simulation tool "Deutsches FREEVAL".
   */
  YAML,
  /**
   * This format is supported by the "Simulation of Urban Mobility" tool.
   */
  SUMO,
  /**
   * This is ROSE's internal format for persistently storing {@link Project}s on the disk.
   */
  ROSE
}
