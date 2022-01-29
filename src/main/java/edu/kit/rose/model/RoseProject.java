package edu.kit.rose.model;

import edu.kit.rose.model.plausibility.PlausibilitySystem;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.RoadSystem;
import java.nio.file.Path;

/**
 * A standard implementation for {@link Project}.
 */
class RoseProject implements Project {

  /**
   * Constructor.
   * It needs the criteriaManager to give to the plausibilitySystem.
   *
   * @param criteriaManager the criteriaManager to use.
   */
  public RoseProject(CriteriaManager criteriaManager) {

  }

  @Override
  public RoadSystem getRoadSystem() {
    return null;
  }

  @Override
  public PlausibilitySystem getPlausibilitySystem() {
    return null;
  }

  @Override
  public void exportToFile(ProjectFormat projectFormat, Path filePath) {

  }

  @Override
  public void save(Path filePath) {

  }

  @Override
  public void load(Path filePath) {

  }

  @Override
  public ZoomSetting getZoomSetting() {
    return null;
  }
}
