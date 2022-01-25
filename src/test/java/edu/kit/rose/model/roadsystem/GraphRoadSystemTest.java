package edu.kit.rose.model.roadsystem;

import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Element;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link GraphRoadSystem}.
 */
public class GraphRoadSystemTest {
  int lane1 = 3;
  int lane2 = 2;

  double slope1 = 3.4;
  double slope2 = 3.4;

  private static <T> AttributeAccessor<T> stubAccessor(AttributeType type, T containedValue) {
    return new AttributeAccessor<>(type, () -> containedValue, newValue -> {});
  }

  private static Element mockElementWithAccessors(AttributeAccessor<?>... accessors) {
    var element = Mockito.mock(Element.class);
    SortedBox<AttributeAccessor<?>> box = new RoseSortedBox<>(List.of(accessors));
    Mockito.when(element.getAttributeAccessors()).thenReturn(box);
    return element;
  }

  @Test
  public void testGetSharedAttributeAccessors() {
    var roadSystem = new GraphRoadSystem(
        Mockito.mock(CriteriaManager.class),
        Mockito.mock(TimeSliceSetting.class)
    );

    var elements = List.of(
        mockElementWithAccessors(
            new AttributeAccessor<>(AttributeType.LANE_COUNT, () -> lane1, val -> lane1 = val),
            stubAccessor(AttributeType.COMMENT, ""),
            new AttributeAccessor<>(AttributeType.SLOPE, () -> slope1, val -> slope1 = val)
        ),
        mockElementWithAccessors(
            stubAccessor(AttributeType.NAME, "test"),
            new AttributeAccessor<>(AttributeType.LANE_COUNT, () -> lane2, val -> lane2 = val),
            new AttributeAccessor<>(AttributeType.SLOPE, () -> slope2, val -> slope2 = val)
        )
    );

    SortedBox<AttributeAccessor<?>> shared = roadSystem.getSharedAttributeAccessors(elements);
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
