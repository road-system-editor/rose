package edu.kit.rose.controller.measurement;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.measurements.Measurement;

/**
 * Provides the functionality to set measurement values
 * and the settings which belong to them.
 */
public class RoseMeasurementController extends Controller implements MeasurementController {

  /**
   * Creates a new {@link RoseMeasurementController}.
   *
   * @param storageLock         the coordinator for controller actions
   * @param project             the model facade for project data
   */
  public RoseMeasurementController(ChangeCommandBuffer changeCommandBuffer, StorageLock storageLock,
                                   Project project) {
    super(storageLock);
  }

  @Override
  public <T> void setMeasurementValue(Measurement<T> measurement, T value, int targetSlot) {

  }

  @Override
  public void setIntervalLength(int intervalLength) {

  }

  @Override
  public void setIntervalCount(int intervalCount) {

  }
}
