package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.plausibility.PlausibilitySystem;
import edu.kit.rose.model.plausibility.RosePlausibilitySystem;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import java.nio.file.Path;

/**
 * A standard implementation for {@link Project}.
 */
class RoseProject implements Project {

  private static final int CENTER_OF_VIEW_X = 1500;
  private static final int CENTER_OF_VIEW_Y = 1500;
  private static final int ZOOM_LEVEL = 1;

  private RoadSystem roadSystem;
  private PlausibilitySystem plausibilitySystem;
  private ZoomSetting zoomSetting;
  private TimeSliceSetting timeSliceSetting;

  /**
   * Constructor.
   * It needs the criteriaManager to give to the plausibilitySystem.
   *
   * @param criteriaManager the criteriaManager to use.
   */
  public RoseProject(CriteriaManager criteriaManager) {
    this.zoomSetting = new ZoomSetting(new Position(CENTER_OF_VIEW_X, CENTER_OF_VIEW_Y),
        ZOOM_LEVEL);
    this.timeSliceSetting = new TimeSliceSetting();
    this.roadSystem = new GraphRoadSystem(criteriaManager, timeSliceSetting);
    this.plausibilitySystem = new RosePlausibilitySystem(criteriaManager, roadSystem);
  }

  @Override
  public RoadSystem getRoadSystem() {
    return this.roadSystem;
  }

  @Override
  public PlausibilitySystem getPlausibilitySystem() {
    return this.plausibilitySystem;
  }

  @Override
  public boolean exportToFile(ProjectFormat projectFormat, Path filePath) {
    //TODO: Implement
    return false;
  }

  @Override
  public void save(Path filePath) {
    //TODO: Implement
  }

  @Override
  public void load(Path filePath) {
    //TODO: Implement
  }

  @Override
  public ZoomSetting getZoomSetting() {
    return this.zoomSetting;
  }
}
