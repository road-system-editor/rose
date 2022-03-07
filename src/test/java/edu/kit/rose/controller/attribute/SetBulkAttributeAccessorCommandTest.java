package edu.kit.rose.controller.attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link SetBulkAttributeAccessorCommand} class.
 */
public class SetBulkAttributeAccessorCommandTest {
  private static final int INITIAL_VALUE = 1;
  private static final int SECOND_INITIAL_VALUE = 2;
  private static final int SET_VALUE = 3;

  private ReplacementLog replacementLog;
  private AttributeAccessor<Integer> accessor;
  private AttributeAccessor<Integer> accessor2;
  private Collection<Segment> segments;

  private SetBulkAttributeAccessorCommand<Integer> command;

  @BeforeEach
  void setUp() {
    Segment segment1 = new Base();
    Segment segment2 = new Exit();
    segments = List.of(segment1, segment2);

    //This cast is safe because the stream filters it by Type to ensure it is <T>.
    @SuppressWarnings("unchecked")
    AttributeAccessor<Integer> currentAccessor = (AttributeAccessor<Integer>)
        segment1.getAttributeAccessors().stream().
            filter(acc -> acc.getAttributeType() == AttributeType.LANE_COUNT)
            .findAny().orElseThrow();
    accessor = currentAccessor;

    //This cast is safe because the stream filters it by Type to ensure it is <T>.
    @SuppressWarnings("unchecked")
    AttributeAccessor<Integer> currentAccessor2 = (AttributeAccessor<Integer>)
        segment2.getAttributeAccessors().stream().
            filter(acc -> acc.getAttributeType() == AttributeType.LANE_COUNT)
            .findAny().orElseThrow();
    accessor2 = currentAccessor2;

    replacementLog = new ReplacementLog();
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
    command = new SetBulkAttributeAccessorCommand<>(replacementLog, accessor, SET_VALUE,
        segments);
    command.execute();
    assertEquals(SET_VALUE, accessor.getValue());
    assertEquals(SET_VALUE, accessor2.getValue());
    command.unexecute();
    assertEquals(INITIAL_VALUE,  accessor.getValue());
    assertEquals(INITIAL_VALUE,  accessor2.getValue());
  }

  @Test
  void testWithReplacement() {
    command = new SetBulkAttributeAccessorCommand<>(replacementLog, accessor, SET_VALUE,
        segments);
    command.execute();
    assertEquals(SET_VALUE, accessor.getValue());
    assertEquals(SET_VALUE, accessor2.getValue());
    command.unexecute();
    assertEquals(INITIAL_VALUE, accessor.getValue());
    assertEquals(INITIAL_VALUE,  accessor2.getValue());
  }

  @Test
  void testDifferentWithReplacement() {
    accessor2.setValue(SECOND_INITIAL_VALUE);
    command = new SetBulkAttributeAccessorCommand<>(replacementLog, accessor, SET_VALUE,
        segments);

    assertEquals(INITIAL_VALUE, accessor.getValue());
    assertEquals(SECOND_INITIAL_VALUE, accessor2.getValue());
    command.execute();
    assertEquals(SET_VALUE, accessor.getValue());
    assertEquals(SET_VALUE, accessor2.getValue());
    command.unexecute();
    assertEquals(INITIAL_VALUE, accessor.getValue());
    assertEquals(SECOND_INITIAL_VALUE,  accessor2.getValue());
  }
}