package edu.kit.rose.controller.measurement;

import edu.kit.rose.model.roadsystem.measurements.Measurement;

/**
 * Provides the functionality to set measurement values
 * and the settings which belong to them.
 */
public interface MeasurementController {

  /**
   * Sets the value of a given measurement to a given value.
   *
   * @param measurement the measurement to set the value on
   * @param value       the value to assign to the measurement
   * @param targetSlot  the slot into which the value will be put
   * @param <T>         the datatype of the measurement's value
   */
  <T> void setMeasurementValue(Measurement<T> measurement, T value, int targetSlot);

  /**
   * Sets the length of all measurement value's intervals
   *
   * @param intervalLength new interval length
   */
  void setIntervalLength(int intervalLength);

  /**
   * Sets the amount of measurement intervals per measurement value
   *
   * @param intervalCount new amount of intervals per measurement value
   */
  void setIntervalCount(int intervalCount);
}
