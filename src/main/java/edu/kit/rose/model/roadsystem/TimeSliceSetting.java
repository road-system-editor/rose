package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;

/**
 * A TimeSliceSetting describes the properties of the TimeSlices that
 * {@link edu.kit.rose.model.roadsystem.measurements.Measurement}s use.
 * It describes how many TimeSlices there are and how long they are (in Minutes)
 */
public class TimeSliceSetting implements UnitObservable<TimeSliceSetting> {

  /**
   * Provides the number of TimeSlices.
   *
   * @return the number of TimeSlices.
   */
  int getNumberOfTimeSliceSteps() {
    return 0;
  }

  /**
   * Sets the number of TimeSlices.
   *
   * @param numberOfTimeSliceSteps the new number of TimeSlices.
   */
  void setNumberOfTimeSliceSteps(int numberOfTimeSliceSteps) {

  }

  /**
   * Provides the length of the TimeSlices.
   *
   * @return the length of the TimeSlices.
   */
  int getTimeSliceLength() {
    return 0;
  }

  /**
   * Sets the length of the TimeSlices.
   *
   * @param length the new length of the TimeSlices.
   */
  void setTimeSliceLength(int length) {

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
