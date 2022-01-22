package edu.kit.rose.model.roadsystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link TimeSliceSetting} class.
 */
public class TimeSliceSettingTest {
  private static final int firstTestInt = 7;
  private static final int secondTestInt = 9;

  @Test
  public void testGetters() {
    TimeSliceSetting setting = new TimeSliceSetting(firstTestInt, secondTestInt);
    Assertions.assertEquals(firstTestInt, setting.getNumberOfTimeSlices());
    Assertions.assertEquals(secondTestInt, setting.getTimeSliceLength());
  }

  @Test
  public void testStandardConstructor() {
    TimeSliceSetting setting = new TimeSliceSetting();
    Assertions.assertEquals(0, setting.getNumberOfTimeSlices());
    Assertions.assertEquals(0, setting.getTimeSliceLength());
  }

  @Test
  public void testSetters() {
    TimeSliceSetting setting = new TimeSliceSetting(firstTestInt, secondTestInt);
    setting.setNumberOfTimeSlices(secondTestInt);
    setting.setTimeSliceLength(firstTestInt);
    Assertions.assertEquals(secondTestInt, setting.getNumberOfTimeSlices());
    Assertions.assertEquals(firstTestInt, setting.getTimeSliceLength());
  }
}
