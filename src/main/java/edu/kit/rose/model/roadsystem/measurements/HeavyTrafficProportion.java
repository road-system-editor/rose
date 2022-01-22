package edu.kit.rose.model.roadsystem.measurements;

import edu.kit.rose.model.roadsystem.TimeSliceSetting;

/**
 * A {@link Measurement} describing the Heavy Traffic Proportion of a
 * {@link edu.kit.rose.model.roadsystem.elements.Segment}.
 */
class HeavyTrafficProportion extends Measurement<Double> {

  /**
   * Constructor.
   * The TimeSliceSetting provides the number of timeSlices as well as their length.
   *
   * @param timeSliceSetting the TimeSliceSetting to use.
   */
  public HeavyTrafficProportion(TimeSliceSetting timeSliceSetting) {
    super(timeSliceSetting);
  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public Measurement<Double> getThis() {
    return this;
  }
}
