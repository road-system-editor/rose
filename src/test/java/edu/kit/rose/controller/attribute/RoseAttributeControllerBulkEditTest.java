package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Bulk edit unit tests for {@link RoseAttributeController}.
 *
 * @see RoseAttributeControllerTest
 */
public class RoseAttributeControllerBulkEditTest {
  int lane1 = 3;
  int lane2 = 2;

  double slope1 = 3.4;
  double slope2 = 3.4;

  List<Segment> elements = List.of(
      mockSegmentWithAccessors(
          new AttributeAccessor<>(AttributeType.LANE_COUNT, () -> lane1, val -> lane1 = val),
          stubAccessor(AttributeType.COMMENT, ""),
          new AttributeAccessor<>(AttributeType.SLOPE, () -> slope1, val -> slope1 = val)
      ),
      mockSegmentWithAccessors(
          stubAccessor(AttributeType.NAME, "test"),
          new AttributeAccessor<>(AttributeType.LANE_COUNT, () -> lane2, val -> lane2 = val),
          new AttributeAccessor<>(AttributeType.SLOPE, () -> slope2, val -> slope2 = val)
      )
  );

  private static <T> AttributeAccessor<T> stubAccessor(AttributeType type, T containedValue) {
    return new AttributeAccessor<>(type, () -> containedValue, newValue -> {});
  }

  private static Segment mockSegmentWithAccessors(AttributeAccessor<?>... accessors) {
    var element = Mockito.mock(Segment.class);
    SortedBox<AttributeAccessor<?>> box = new RoseSortedBox<>(List.of(accessors));
    Mockito.when(element.getAttributeAccessors()).thenReturn(box);
    return element;
  }

  @Test
  public void testGetSharedAttributeAccessors() {
    var selectionBuffer = Mockito.mock(SelectionBuffer.class);
    Mockito.when(selectionBuffer.getSelectedSegments()).thenReturn(elements);

    var controller = new RoseAttributeController(
        Mockito.mock(ChangeCommandBuffer.class),
        selectionBuffer,
        Mockito.mock(StorageLock.class),
        Mockito.mock(Navigator.class),
        Mockito.mock(Project.class),
        Mockito.mock(ApplicationDataSystem.class),
        Mockito.mock(ReplacementLog.class));

    SortedBox<AttributeAccessor<?>> shared = controller.getBulkEditAccessors();
    Assertions.assertEquals(2, shared.getSize());

    @SuppressWarnings("unchecked")
    AttributeAccessor<Integer> laneCount = (AttributeAccessor<Integer>) shared.get(0);
    Assertions.assertEquals(AttributeType.LANE_COUNT, laneCount.getAttributeType());
    Assertions.assertNull(laneCount.getValue()); // different values -> null
    laneCount.setValue(4);
    Assertions.assertEquals(4, lane1);
    Assertions.assertEquals(4, lane2);
    Assertions.assertEquals(4, laneCount.getValue()); // same values -> return value

    @SuppressWarnings("unchecked")
    AttributeAccessor<Double> slope = (AttributeAccessor<Double>) shared.get(1);
    Assertions.assertEquals(AttributeType.SLOPE, slope.getAttributeType());
    Assertions.assertEquals(3.4, slope.getValue()); // same values -> return value
    slope.setValue(4.1);
    Assertions.assertEquals(4.1, slope1);
    Assertions.assertEquals(4.1, slope2);
    Assertions.assertEquals(4.1, slope.getValue()); // same values -> return value
  }
}
