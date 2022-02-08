package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;

/**
 * A TimeSliceSetting describes the properties of the TimeSlices that
 * {@link edu.kit.rose.model.roadsystem.measurements.Measurement}s use.
 * It describes how many TimeSlices there are and how long they are (in Minutes)
 */
public class TimeSliceSetting implements UnitObservable<TimeSliceSetting> {

  private final int defaultNumberOfTimeSlices;
  private final int defaultTimeSliceLength;

  private int numberOfTimeSlices;
  private int timeSlicesLength;

  /**
   * Standard Constructor.
   * Initializes the numberOfTimeSlices and the timeSliceLength to 0.
   */
  public TimeSliceSetting() {
    this(0, 0);
  }

  /**
   * Constructor.
   *
   * @param numberOfTimeSlices the number of Time Slices to use.
   * @param timeSlicesLength   the length of the Time Slices to use.
   */
  public TimeSliceSetting(int numberOfTimeSlices, int timeSlicesLength) {
    this.numberOfTimeSlices = numberOfTimeSlices;
    this.defaultNumberOfTimeSlices = numberOfTimeSlices;
    this.timeSlicesLength = timeSlicesLength;
    this.defaultTimeSliceLength = timeSlicesLength;
  }

  /**
   * Provides the number of TimeSlices.
   *
   * @return the number of TimeSlices.
   */
  public int getNumberOfTimeSlices() {
    return this.numberOfTimeSlices;
  }

  /**
   * Sets the number of TimeSlices.
   *
   * @param numberOfTimeSlices the new number of TimeSlices.
   */
  public void setNumberOfTimeSlices(int numberOfTimeSlices) {
    this.numberOfTimeSlices = numberOfTimeSlices;
  }

  /**
   * Provides the length of the TimeSlices.
   *
   * @return the length of the TimeSlices.
   */
  public int getTimeSliceLength() {
    return this.timeSlicesLength;
  }

  /**
   * Sets the length of the TimeSlices.
   *
   * @param timeSlicesLength the new length of the TimeSlices.
   */
  public void setTimeSliceLength(int timeSlicesLength) {
    this.timeSlicesLength = timeSlicesLength;
  }

  void reset() {
    this.numberOfTimeSlices = defaultNumberOfTimeSlices;
    this.timeSlicesLength = defaultTimeSliceLength;
  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public TimeSliceSetting getThis() {
    return this;
  }

  @Override
  public void addSubscriber(UnitObserver<TimeSliceSetting> observer) {

  }

  @Override
  public void removeSubscriber(UnitObserver<TimeSliceSetting> observer) {

  }

}
