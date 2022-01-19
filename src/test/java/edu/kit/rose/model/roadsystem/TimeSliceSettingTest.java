package edu.kit.rose.model.roadsystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeSliceSettingTest {
  private static final int firstTestInt = 7;
  private static final int secondTestInt = 9;

  @Test
  public void TestGetters() {
    TimeSliceSetting setting = new TimeSliceSetting(firstTestInt, secondTestInt);
    Assertions.assertEquals(firstTestInt, setting.getNumberOfTimeSlices());
    Assertions.assertEquals(secondTestInt, setting.getTimeSliceLength());
  }

  @Test
  public void TestSetters() {
    TimeSliceSetting setting = new TimeSliceSetting(firstTestInt, secondTestInt);
    setting.setNumberOfTimeSlices(secondTestInt);
    setting.setTimeSliceLength(firstTestInt);
    Assertions.assertEquals(secondTestInt, setting.getNumberOfTimeSlices());
    Assertions.assertEquals(firstTestInt, setting.getTimeSliceLength());
  }
}
