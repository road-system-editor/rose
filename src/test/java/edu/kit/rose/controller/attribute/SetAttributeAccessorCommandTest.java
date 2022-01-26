package edu.kit.rose.controller.attribute;

import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link SetAttributeAccessorCommand} class.
 */
public class SetAttributeAccessorCommandTest {

  private AttributeAccessor<Integer> accessor;
  private int testInt;
  private static final int INITIAL_VALUE = 0;
  private static final int SET_VALUE = 1;


  @BeforeEach
  void setUp() {
    accessor = new AttributeAccessor<>(
        AttributeType.LANE_COUNT,
        () -> testInt,
        newValue -> testInt = newValue
    );
    testInt = INITIAL_VALUE;
  }



  @Test
  void testConstructor() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> new SetAttributeAccessorCommand<>(null, null,
            false, true));
  }

  @Test
  void testExecute() {
    SetAttributeAccessorCommand<Integer> setAttributeAccessorCommand =
        new SetAttributeAccessorCommand<>(null, accessor, testInt, SET_VALUE);
    setAttributeAccessorCommand.execute();
    Assertions.assertEquals(SET_VALUE, testInt);
  }

  @Test
  void testUnexecute() {
    SetAttributeAccessorCommand<Integer> setAttributeAccessorCommand =
        new SetAttributeAccessorCommand<>(null, accessor, testInt, SET_VALUE);
    setAttributeAccessorCommand.execute();
    setAttributeAccessorCommand.unexecute();
    Assertions.assertEquals(testInt, INITIAL_VALUE);
  }

}
