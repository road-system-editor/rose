package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a data model for {@link RoadSystem} that is serializable through the Jackson library.
 */
class SerializedRoadSystem {
  @JsonIgnore
  private final RoadSystem roadSystem;

  @JsonProperty("elements")
  private List<JsonElement> elements;
  @JsonProperty("timeSliceSetting")
  private JsonTimeSliceSetting timeSliceSetting;

  public SerializedRoadSystem(RoadSystem roadSystem) {
    this.roadSystem = roadSystem;

    this.populateElements();
    this.linkElements();

    this.timeSliceSetting =
        new JsonTimeSliceSetting(this.roadSystem.getTimeSliceSetting());
  }

  private void populateElements() {
    this.elements = new ArrayList<>(this.roadSystem.getElements().getSize());
    int index = 0;
    for (var element : this.roadSystem.getElements()) {
      this.elements.add(createJsonElement(index++, element));
    }
  }

  private JsonElement createJsonElement(int index, Element element) {
    if (element.isContainer()) {
      return new JsonGroup(index, (Group) element);
    } else {
      Segment segment = (Segment) element;
      return switch (segment.getSegmentType()) {
        case BASE -> new JsonBaseSegment(index, (Base) segment);
        case ENTRANCE -> new JsonEntranceSegment(index, (Entrance) segment);
        case EXIT -> new JsonExitSegment(index, (Exit) segment);
        //noinspection UnnecessaryDefault
        default -> throw new RuntimeException("invalid segment type!");
      };
    }
  }

  private void linkElements() {
    this.elements.forEach(element -> element.link(this));
  }

  /**
   * Finds the index of a given {@code element}.
   * {@link #populateElements()} must have completed execution before calling this method.
   *
   * @return the index of the given element or {@code null} if {@code element} is {@code null}.
   * @throws java.util.NoSuchElementException if the element is not contained in this project.
   */
  private Integer getElementId(Element element) {
    return element == null ? null : this.elements.stream()
        .filter(other -> other.getElement() == element)
        .findAny()
        .orElseThrow()
        .getIndex();
  }

  /**
   * Finds the ROSE segment that is connected to {@code segment} on the given {@code connector}.
   *
   * @return the connected segment or {@code null} if this connector is not connected to another
   *     {@link Segment}.
   */
  private Segment getConnectedSegment(Segment segment, Connector connector) {
    var connection = this.roadSystem.getConnection(connector);
    if (connection == null) {
      return null;
    }
    var otherConnector = connection.getOther(connector);

    for (var adjacent : this.roadSystem.getAdjacentSegments(segment)) {
      for (var adjConnector : adjacent.getConnectors()) {
        if (adjConnector == otherConnector) {
          return adjacent;
        }
      }
    }

    throw new RuntimeException("couldn't find adjacent segment");
  }

  private abstract static class JsonElement {
    @JsonIgnore
    private final int index;
    @JsonIgnore
    private final Element element;
    @JsonProperty("name")
    private String name;
    @JsonProperty("comment")
    private String comment;

    @JsonProperty("isContainer")
    private boolean isContainer;

    protected JsonElement(int index, Element element) {
      this.index = index;
      this.element = element;
      this.populateAttributes();
    }

    public int getIndex() {
      return this.index;
    }

    public Element getElement() {
      return this.element;
    }

    private void populateAttributes() {
      this.name = this.element.getName();
      //this.comment = getAttributeValue(AttributeType.COMMENT); TODO missing in highwaysegment
      this.isContainer = this.element.isContainer();
    }

    @SuppressWarnings("unchecked")
    protected <T> T getAttributeValue(AttributeType type) {
      for (var accessor : this.element.getAttributeAccessors()) {
        if (accessor.getAttributeType() == type) {
          return (T) accessor.getValue();
        }
      }

      throw new RuntimeException("element does not have an attribute of the given type");
    }

    public abstract void link(SerializedRoadSystem serializedRoadSystem);
  }

  private static class JsonGroup extends JsonElement {
    @JsonIgnore
    private Group group;

    @JsonProperty("childrenIndices")
    private List<Integer> childrenIndices;

    JsonGroup(int index, Group group) {
      super(index, group);
      this.group = group;
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.childrenIndices = new ArrayList<>(group.getElements().getSize());

      for (var child : group.getElements()) {
        this.childrenIndices.add(serializedRoadSystem.getElementId(child));
      }
    }
  }

  private abstract static class JsonSegment extends JsonElement {
    @JsonIgnore
    private Segment segment;

    @JsonProperty("type")
    private SegmentType type;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("slope")
    private Integer slope;
    @JsonProperty("laneCount")
    private Integer laneCount;
    @JsonProperty("conurbation")
    private Boolean conurbation;
    @JsonProperty("maxSpeed")
    private Integer maxSpeed;

    JsonSegment(int index, Segment segment) {
      super(index, segment);
      this.segment = segment;

      this.populateAttributes();
    }

    private void populateAttributes() {
      this.type = this.segment.getSegmentType();
      this.length = getAttributeValue(AttributeType.LENGTH);
      this.slope = getAttributeValue(AttributeType.SLOPE);
      this.laneCount = getAttributeValue(AttributeType.LANE_COUNT);
      this.conurbation = getAttributeValue(AttributeType.CONURBATION);
      this.maxSpeed = getAttributeValue(AttributeType.MAX_SPEED);
    }
  }

  private static class JsonBaseSegment extends JsonSegment {
    @JsonIgnore
    private Base base;

    @JsonProperty("entranceConnectedSegmentId")
    private Integer entranceConnectedSegmentId;
    @JsonProperty("exitConnectedSegmentId")
    private Integer exitConnectedSegmentId;

    JsonBaseSegment(int index, Base base) {
      super(index, base);
      this.base = base;
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.entranceConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.base, this.base.getEntry()));
      this.exitConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.base, this.base.getExit()));
    }
  }

  private static class JsonEntranceSegment extends JsonSegment {
    @JsonIgnore
    private Entrance entrance;

    @JsonProperty("laneCountRamp")
    private Integer laneCountRamp;
    @JsonProperty("maxSpeedRamp")
    private Integer maxSpeedRamp;
    @JsonProperty("entranceConnectedSegmentId")
    private Integer entranceConnectedSegmentId;
    @JsonProperty("exitConnectedSegmentId")
    private Integer exitConnectedSegmentId;
    @JsonProperty("rampConnectedSegmentId")
    private Integer rampConnectedSegmentId;

    JsonEntranceSegment(int index, Entrance entrance) {
      super(index, entrance);
      this.entrance = entrance;

      this.populateAttributes();
    }

    private void populateAttributes() {
      this.laneCountRamp = getAttributeValue(AttributeType.LANE_COUNT_RAMP);
      this.maxSpeedRamp = getAttributeValue(AttributeType.MAX_SPEED_RAMP);
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.entranceConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.entrance, this.entrance.getEntry()));
      this.exitConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.entrance, this.entrance.getExit()));
      this.rampConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.entrance, this.entrance.getRamp()));
    }
  }

  private static class JsonExitSegment extends JsonSegment {
    @JsonIgnore
    private Exit exit;

    @JsonProperty("laneCountRamp")
    private Integer laneCountRamp;
    @JsonProperty("maxSpeedRamp")
    private Integer maxSpeedRamp;
    @JsonProperty("entranceConnectedSegmentId")
    private Integer entranceConnectedSegmentId;
    @JsonProperty("exitConnectedSegmentId")
    private Integer exitConnectedSegmentId;
    @JsonProperty("rampConnectedSegmentId")
    private Integer rampConnectedSegmentId;

    JsonExitSegment(int index, Exit exit) {
      super(index, exit);
      this.exit = exit;

      this.populateAttributes();
    }

    private void populateAttributes() {
      this.laneCountRamp = getAttributeValue(AttributeType.LANE_COUNT_RAMP);
      this.maxSpeedRamp = getAttributeValue(AttributeType.MAX_SPEED_RAMP);
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.entranceConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.exit, this.exit.getEntry()));
      this.exitConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.exit, this.exit.getExit()));
      this.rampConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.exit, this.exit.getRamp()));
    }
  }

  private static class JsonTimeSliceSetting {
    @JsonProperty("timeSliceLength")
    private int timeSliceLength;
    @JsonProperty("numberOfTimeSlices")
    private int numberOfTimeSlices;

    JsonTimeSliceSetting(TimeSliceSetting timeSliceSetting) {
      this.timeSliceLength = timeSliceSetting.getTimeSliceLength();
      this.numberOfTimeSlices = timeSliceSetting.getNumberOfTimeSlices();
    }
  }
}
