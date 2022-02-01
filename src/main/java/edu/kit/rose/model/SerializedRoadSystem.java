package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

  /**
   * Serializes the given {@code roadSystem} into a data model for the ROSE format.
   *
   * @param roadSystem the road system to serialize.
   */
  public SerializedRoadSystem(RoadSystem roadSystem) {
    this.roadSystem = roadSystem;

    this.populateElements();
    this.linkElements();

    this.timeSliceSetting =
        new JsonTimeSliceSetting(this.roadSystem.getTimeSliceSetting());
  }

  /**
   * Empty constructor to be used by Jackson when de-serializing a file into this model.
   */
  public SerializedRoadSystem() {
    this.roadSystem = null;
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

  public void populateRoadSystem(RoadSystem target) {
    System.out.println(elements.size());
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
        .filter(other -> other.getSourceElement() == element)
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

  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.PROPERTY,
      property = "isContainer"
  )
  @JsonSubTypes({
      @JsonSubTypes.Type(value = JsonGroup.class, name = "true"),
      @JsonSubTypes.Type(value = JsonSegment.class, name = "false")
  })
  private abstract static class JsonElement {
    @JsonIgnore
    private final int index;
    @JsonIgnore
    private final Element sourceElement;
    @JsonProperty("name")
    private String name;
    @JsonProperty("comment")
    private String comment;

    @JsonProperty("isContainer")
    private boolean isContainer;

    protected JsonElement(int index, Element sourceElement) {
      this.index = index;
      this.sourceElement = sourceElement;

      this.populateAttributes();
    }

    protected JsonElement() {
      this.index = -1;
      this.sourceElement = null;
    }

    public int getIndex() {
      return this.index;
    }

    public Element getSourceElement() {
      return this.sourceElement;
    }

    private void populateAttributes() {
      this.name = this.sourceElement.getName();
      //this.comment = getAttributeValue(AttributeType.COMMENT); TODO missing in highwaysegment
      this.isContainer = this.sourceElement.isContainer();
    }

    @SuppressWarnings("unchecked")
    protected <T> T getAttributeValue(AttributeType type) {
      for (var accessor : this.sourceElement.getAttributeAccessors()) {
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

  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.PROPERTY,
      property = "type"
  )
  @JsonSubTypes({
      @JsonSubTypes.Type(value = JsonBaseSegment.class, name = "BASE"),
      @JsonSubTypes.Type(value = JsonEntranceSegment.class, name = "ENTRANCE"),
      @JsonSubTypes.Type(value = JsonExitSegment.class, name = "EXIT")
  })
  private /* TODO abstract*/ static class JsonSegment extends JsonElement {
    @JsonIgnore
    private final Segment sourceSegment;

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

    JsonSegment(int index, Segment sourceSegment) {
      super(index, sourceSegment);
      this.sourceSegment = sourceSegment;

      this.populateAttributes();
    }

    JsonSegment() {
      super();
      this.sourceSegment = null;
    }

    private void populateAttributes() {
      this.type = this.sourceSegment.getSegmentType();
      this.length = getAttributeValue(AttributeType.LENGTH);
      this.slope = getAttributeValue(AttributeType.SLOPE);
      this.laneCount = getAttributeValue(AttributeType.LANE_COUNT);
      this.conurbation = getAttributeValue(AttributeType.CONURBATION);
      this.maxSpeed = getAttributeValue(AttributeType.MAX_SPEED);
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      //TODO remove
    }
  }

  private static class JsonBaseSegment extends JsonSegment {
    @JsonIgnore
    private Base sourceBase;

    @JsonProperty("entranceConnectedSegmentId")
    private Integer entranceConnectedSegmentId;
    @JsonProperty("exitConnectedSegmentId")
    private Integer exitConnectedSegmentId;

    JsonBaseSegment(int index, Base sourceBase) {
      super(index, sourceBase);
      this.sourceBase = sourceBase;
    }

    JsonBaseSegment() {
      super();

      this.sourceBase = null;
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.entranceConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.sourceBase, this.sourceBase.getEntry()));
      this.exitConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.sourceBase, this.sourceBase.getExit()));
    }
  }

  private static class JsonEntranceSegment extends JsonSegment {
    @JsonIgnore
    private final Entrance sourceEntrance;

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

    JsonEntranceSegment(int index, Entrance sourceEntrance) {
      super(index, sourceEntrance);
      this.sourceEntrance = sourceEntrance;

      this.populateAttributes();
    }

    JsonEntranceSegment() {
      super();

      this.sourceEntrance = null;
    }

    private void populateAttributes() {
      this.laneCountRamp = getAttributeValue(AttributeType.LANE_COUNT_RAMP);
      this.maxSpeedRamp = getAttributeValue(AttributeType.MAX_SPEED_RAMP);
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.entranceConnectedSegmentId =
          serializedRoadSystem.getElementId(serializedRoadSystem.getConnectedSegment(
              this.sourceEntrance, this.sourceEntrance.getEntry()));
      this.exitConnectedSegmentId =
          serializedRoadSystem.getElementId(serializedRoadSystem.getConnectedSegment(
              this.sourceEntrance, this.sourceEntrance.getExit()));
      this.rampConnectedSegmentId =
          serializedRoadSystem.getElementId(serializedRoadSystem.getConnectedSegment(
              this.sourceEntrance, this.sourceEntrance.getRamp()));
    }
  }

  private static class JsonExitSegment extends JsonSegment {
    @JsonIgnore
    private final Exit sourceExit;

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

    JsonExitSegment(int index, Exit sourceExit) {
      super(index, sourceExit);
      this.sourceExit = sourceExit;

      this.populateAttributes();
    }

    JsonExitSegment() {
      super();

      this.sourceExit = null;
    }

    private void populateAttributes() {
      this.laneCountRamp = getAttributeValue(AttributeType.LANE_COUNT_RAMP);
      this.maxSpeedRamp = getAttributeValue(AttributeType.MAX_SPEED_RAMP);
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.entranceConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.sourceExit, this.sourceExit.getEntry()));
      this.exitConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.sourceExit, this.sourceExit.getExit()));
      this.rampConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.sourceExit, this.sourceExit.getRamp()));
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

    JsonTimeSliceSetting() {
      // nothing...
    }
  }
}
