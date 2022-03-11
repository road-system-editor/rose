package edu.kit.rose.controller.attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link SetAttributeAccessorCommand} class.
 */
class SetAttributeAccessorCommandTest {
  private static final int INITIAL_VALUE = 0;
  private static final int SET_VALUE = 1;

  private ReplacementLog replacementLog;
  private AttributeAccessor<Integer> accessor;
  private AttributeAccessor<Integer> replacementAccessor;
  private int testInt;
  private int replacementTestInt;

  private SetAttributeAccessorCommand<Integer> command;

  @BeforeEach
  void setUp() {
    accessor = new AttributeAccessor<>(
        AttributeType.LANE_COUNT,
        () -> testInt,
        newValue -> testInt = newValue
    );
    replacementAccessor = new AttributeAccessor<>(
        AttributeType.LANE_COUNT,
        () -> replacementTestInt,
        newValue -> replacementTestInt = newValue
    );
    testInt = INITIAL_VALUE;
    replacementTestInt = INITIAL_VALUE;

    replacementLog = mock(ReplacementLog.class);

    command = new SetAttributeAccessorCommand<>(
        replacementLog,
        accessor,
        testInt,
        SET_VALUE);
  }

  @Test
  void testConstructor() {
    assertThrows(NullPointerException.class, () -> new SetAttributeAccessorCommand<>(
        null, null, false, true));
    assertThrows(NullPointerException.class, () -> new SetAttributeAccessorCommand<>(
        this.replacementLog, null, false, true));
    assertThrows(NullPointerException.class, () -> new SetAttributeAccessorCommand<>(
        null, this.accessor, 1, 0));
  }

  @Test
  void testWithoutReplacement() {
    when(replacementLog.getCurrentAccessorVersion(accessor)).thenReturn(accessor);
    command.execute();
    assertEquals(SET_VALUE, testInt);
    command.unexecute();
    assertEquals(INITIAL_VALUE, testInt);
  }

  @Test
  void testWithReplacement() {
    when(replacementLog.getCurrentAccessorVersion(accessor)).thenReturn(replacementAccessor);
    command.execute();
    assertEquals(SET_VALUE, replacementTestInt);
    command.unexecute();
    assertEquals(INITIAL_VALUE, replacementTestInt);
  }
}
