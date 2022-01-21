package edu.kit.rose.controller.navigation;

/**
 * Contains different type of views.
 */
public enum WindowType {
  /**
   * References the part of the GUI where the {@link edu.kit.rose.model.roadsystem.RoadSystem} is
   * shown.
   */
  ROADSYSTEM,
  /**
   * References the part of the GUI where the plausibility criteria are shown.
   */
  CRITERION,
  /**
   * References the part of the GUI where an overview over all
   * {@link edu.kit.rose.model.roadsystem.measurements.Measurement}s is shown.
   */
  MEASUREMENT_OVERVIEW,
  /**
   * References the part of the GUI where
   * {@link edu.kit.rose.model.roadsystem.measurements.Measurement} values can be edited.
   */
  MEASUREMENT_EDITOR,
  /**
   * References the part of the GUI where attribute values can be edited.
   */
  Attribute
}
