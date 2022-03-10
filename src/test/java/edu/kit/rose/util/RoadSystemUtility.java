package edu.kit.rose.util;

import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.HighwaySegment;
import edu.kit.rose.model.roadsystem.elements.RampSegment;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.Objects;

/**
 * Utility methods that assist with verifying the correctness of {@link RoadSystem}s and creating
 * sensible road system sample data.
 */
public final class RoadSystemUtility {
  /**
   * Creates a base segment in the given {@code roadSystem} with sensible default attribute values.
   */
  public static Base createDefaultBase(RoadSystem roadSystem) {
    var base = roadSystem == null ? new Base() : (Base) roadSystem.createSegment(SegmentType.BASE);
    assignDefaultHighwaySegmentValues(base);
    return base;
  }

  /**
   * Creates an entrance segment in the given {@code roadSystem} with sensible default attribute
   * values.
   */
  public static Entrance createDefaultEntrance(RoadSystem roadSystem) {
    var entrance = roadSystem == null
        ? new Entrance() : (Entrance) roadSystem.createSegment(SegmentType.ENTRANCE);
    assignDefaultHighwaySegmentValues(entrance);
    assignDefaultRampValues(entrance);
    return entrance;
  }

  /**
   * Creates an exit segment in the given {@code roadSystem} with sensible default attribute
   * values.
   */
  public static Exit createDefaultExit(RoadSystem roadSystem) {
    var exit = roadSystem == null
        ? new Exit() : (Exit) roadSystem.createSegment(SegmentType.EXIT);
    assignDefaultHighwaySegmentValues(exit);
    assignDefaultRampValues(exit);
    return exit;
  }

  private static void assignDefaultHighwaySegmentValues(HighwaySegment highwaySegment) {
    highwaySegment.setName("base with default values");
    highwaySegment.setLaneCount(1);
    highwaySegment.setLength(120);
    highwaySegment.setSlope(0.0);
    highwaySegment.setMaxSpeed(SpeedLimit.NONE);
    highwaySegment.setConurbation(false);
  }

  private static void assignDefaultRampValues(RampSegment rampSegment) {
    rampSegment.setMaxSpeedRamp(SpeedLimit.T60);
    rampSegment.setLaneCountRamp(1);
    rampSegment.setJunctionName("default junction name");
  }

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
