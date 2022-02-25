package edu.kit.rose.util;

import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Objects;

/**
 * Utility methods that assist with verifying the correctness of {@link RoadSystem}s.
 */
public final class RoadSystemUtility {
  /**
   * Finds any group in a road system.
   *
   * @return any group from the road system or {@code null} if there is none.
   */
  public static Group findAnyGroup(RoadSystem roadSystem) {
    for (var element : roadSystem.getElements()) {
      if (element.isContainer()) {
        return (Group) element;
      }
    }
    return null;
  }

  /**
   * Finds an element in a given road system by exactly matching name.
   *
   * @return any element with the given name from the given road system or {@code null} if such
   *     an element does not exist.
   */
  public static Element findElementByName(RoadSystem roadSystem, String name) {
    for (var element : roadSystem.getElements()) {
      if (Objects.equals(name, element.getName())) {
        return element;
      }
    }
    return null;
  }

  /**
   * Finds any segment of the given {@code type} in a road system.
   *
   * @param <T> the Java type to cast the found segment to, must match {@code type}.
   * @return any segment of the given {@code type} from the road system or {@code null} if there
   *     is none.
   * @throws ClassCastException if the Java type parameter and the SegmentType parameter do not
   *     match.
   */
  @SuppressWarnings("unchecked")
  public static <T> T findAnySegmentOfType(RoadSystem roadSystem, SegmentType type) {
    for (var element : roadSystem.getElements()) {
      if (!element.isContainer()) {
        Segment segment = (Segment) element;
        if (segment.getSegmentType() == type) {
          return (T) segment;
        }
      }
    }

    return null;
  }
}