package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;

/**
 * A TimeSliceSetting describes the properties of the TimeSlices that
 * {@link edu.kit.rose.model.roadsystem.measurements.Measurement}s use.
 * It describes how many TimeSlices there are and how long they are (in Minutes)
 */
public class TimeSliceSetting implements UnitObservable<TimeSliceSetting> {

  private int numberOfTimeSlices;
  private int timeSlicesLength;

  /**
   * Constructor.
   *
   * @param numberOfTimeSlices the number of Time Slices to use.
   * @param timeSlicesLength   the length of the Time Slices to use.
   */
  public TimeSliceSetting(int numberOfTimeSlices, int timeSlicesLength) {
    this.numberOfTimeSlices = numberOfTimeSlices;
    this.timeSlicesLength = timeSlicesLength;
  }

  /**
   * Provides the number of TimeSlices.
   *
   * @return the number of TimeSlices.
   */
  int getNumberOfTimeSlices() {
    return this.numberOfTimeSlices;
  }

  /**
   * Sets the number of TimeSlices.
   *
   * @param numberOfTimeSlices the new number of TimeSlices.
   */
  void setNumberOfTimeSlices(int numberOfTimeSlices) {
    this.numberOfTimeSlices = numberOfTimeSlices;
  }

  /**
   * Provides the length of the TimeSlices.
   *
   * @return the length of the TimeSlices.
   */
  int getTimeSliceLength() {
    return this.timeSlicesLength;
  }

  /**
   * Sets the length of the TimeSlices.
   *
   * @param timeSlicesLength the new length of the TimeSlices.
   */
  void setTimeSliceLength(int timeSlicesLength) {
    this.timeSlicesLength = timeSlicesLength;
  }


  @Override
  public void notifySubscribers() {

  }

  @Override
  public void addSubscriber(UnitObserver<TimeSliceSetting> observer) {

  }

  @Override
  public void removeSubscriber(UnitObserver<TimeSliceSetting> observer) {

  }

}
