package edu.kit.rose.model.roadsystem.measurements;

import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;

/**
 * A Measurement stores values for a given number of time slices.
 * These TimeSlices are to observe the {@link TimeSliceSetting} of the Project to configure
 * themselves.
 * (See Pflichtenheft : "Messwert" and "Zeitschritt")
 */
public abstract class Measurement<T>
    implements UnitObserver<TimeSliceSetting>, UnitObservable<Measurement<T>> {

  /**
   * Constructor.
   * This creates a Measurement with decoy values for both number of time slices and their length.
   * Needs to be subscribed to a {@link TimeSliceSetting}o be consistent with other Measurements using the same.
   * Notify after subscription.
   */
  public Measurement() {

  }

  /**
   * Constructor.
   * The TimeSliceSetting provides the number of timeSlices as well as their length.
   *
   * @param timeSliceSetting the TimeSliceSetting to use.
   */
  public Measurement(TimeSliceSetting timeSliceSetting) {

  }


  @Override
  public void addSubscriber(UnitObserver<Measurement<T>> observer) {

  }

  @Override
  public void removeSubscriber(UnitObserver<Measurement<T>> observer) {

  }

  /**
   * Returns the Measurement value at the given time slice.
   *
   * @param timeSlot The int describing the timeSlot.
   * @return The value of the Measurement at the given time slice.
   */
  public T getValue(int timeSlot) {
    return null;
  }

  /**
   * Sets the given value at the given time slice.
   *
   * @param value    the value to set.
   * @param timeSlot an int describing the time slice.
   */
  public void setValue(T value, int timeSlot) {

  }

  @Override
  public void notifyChange(TimeSliceSetting timeSliceSetting) {

  }

  /**
   * Provides the {@link MeasurementType} of this Measurement.
   *
   * @return the MeasurementType of this Measurement
   */
  public MeasurementType getMeasurementType() {
    return null;
  }
}
