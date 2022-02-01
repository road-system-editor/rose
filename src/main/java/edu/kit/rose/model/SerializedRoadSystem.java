package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
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
import java.util.Set;

/**
 * This is a data model for {@link RoadSystem} that is serializable through the Jackson library.
 */
class SerializedRoadSystem {
  @JsonIgnore
  private final RoadSystem roadSystem;

  @JsonProperty("elements")
  private List<SerializedElement<? extends Element>> elements;
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

  private SerializedElement<? extends Element> createJsonElement(int index, Element element) {
    if (element.isContainer()) {
      return new SerializedGroup(index, (Group) element);
    } else {
      Segment segment = (Segment) element;
      return switch (segment.getSegmentType()) {
        case BASE -> new SerializedBaseSegment(index, (Base) segment);
        case ENTRANCE -> new SerializedEntranceSegment(index, (Entrance) segment);
        case EXIT -> new SerializedExitSegment(index, (Exit) segment);
        //noinspection UnnecessaryDefault
        default -> throw new RuntimeException("invalid segment type!");
      };
    }
  }

  private void linkElements() {
    this.elements.forEach(element -> element.link(this));
  }

  public void populateRoadSystem(RoadSystem target) {
    this.elements.forEach(element -> element.createRoseElement(target));
    this.elements.forEach(element -> element.linkRoseElement(this, target));
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
        .filter(other -> other.getRoseElement() == element)
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
      @JsonSubTypes.Type(value = SerializedGroup.class, name = "true"),
      @JsonSubTypes.Type(value = SerializedSegment.class, name = "false")
  })
  private abstract static class SerializedElement<T extends Element> {
    @JsonIgnore
    protected T roseElement;

    @JsonProperty("index")
    private int index;
    @JsonProperty("name")
    private String name;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("isContainer")
    private boolean isContainer;

    protected SerializedElement(int index, T roseElement) {
      this.index = index;
      this.roseElement = roseElement;

      this.populateAttributes();
    }

    protected SerializedElement() {
      this.roseElement = null;
    }

    public int getIndex() {
      return this.index;
    }

    public T getRoseElement() {
      return this.roseElement;
    }

    private void populateAttributes() {
      this.name = this.roseElement.getName();
      //this.comment = getAttributeValue(AttributeType.COMMENT); TODO missing in highwaysegment
      this.isContainer = this.roseElement.isContainer();
    }

    @SuppressWarnings("unchecked")
    protected <V> V getAttributeValue(AttributeType type) {
      for (var accessor : this.roseElement.getAttributeAccessors()) {
        if (accessor.getAttributeType() == type) {
          return (V) accessor.getValue();
        }
      }

      throw new RuntimeException("element does not have an attribute of the given type");
    }

    @SuppressWarnings("unchecked")
    protected <V> void setAttributeValue(AttributeType type, V value) {
      for (var accessor : this.roseElement.getAttributeAccessors()) {
        if (accessor.getAttributeType() == type) {
          ((AttributeAccessor<V>) accessor).setValue(value);
          return;
        }
      }

      throw new RuntimeException("element does not have an attribute of the given type");
    }

    public abstract void link(SerializedRoadSystem serializedRoadSystem);

    public abstract void createRoseElement(RoadSystem target);

    public void linkRoseElement(SerializedRoadSystem source, RoadSystem target) {
      this.setAttributeValue(AttributeType.NAME, this.name);
      // TODO this.setAttributeValue(AttributeType.COMMENT, this.comment);
    }
  }

  private static class SerializedGroup extends SerializedElement<Group> {
    @JsonProperty("childrenIndices")
    private List<Integer> childrenIndices;

    SerializedGroup(int index, Group group) {
      super(index, group);
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.childrenIndices = new ArrayList<>(getRoseElement().getElements().getSize());

      for (var child : getRoseElement().getElements()) {
        this.childrenIndices.add(serializedRoadSystem.getElementId(child));
      }
    }

    @Override
    public void createRoseElement(RoadSystem target) {
      this.roseElement = target.createGroup(Set.of());
    }

    @Override
    public void linkRoseElement(SerializedRoadSystem source, RoadSystem target) {
      super.linkRoseElement(source, target);

      childrenIndices.stream()
          .map(source::getElementById)
          .forEach(child -> this.getRoseElement().addElement(child.roseElement));
    }
  }

  private SerializedElement<? extends Element> getElementById(int index) {
    return this.elements.get(index);
  }

  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.PROPERTY,
      property = "type"
  )
  @JsonSubTypes({
      @JsonSubTypes.Type(value = SerializedBaseSegment.class, name = "BASE"),
      @JsonSubTypes.Type(value = SerializedEntranceSegment.class, name = "ENTRANCE"),
      @JsonSubTypes.Type(value = SerializedExitSegment.class, name = "EXIT")
  })
  private abstract static class SerializedSegment<T extends Segment> extends SerializedElement<T> {
    @SuppressWarnings("unused")
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

    SerializedSegment(int index, T sourceSegment) {
      super(index, sourceSegment);

      this.populateAttributes();
    }

    SerializedSegment() {
      super();
    }

    private void populateAttributes() {
      this.type = this.roseElement.getSegmentType();
      this.length = getAttributeValue(AttributeType.LENGTH);
      this.slope = getAttributeValue(AttributeType.SLOPE);
      this.laneCount = getAttributeValue(AttributeType.LANE_COUNT);
      this.conurbation = getAttributeValue(AttributeType.CONURBATION);
      this.maxSpeed = getAttributeValue(AttributeType.MAX_SPEED);
    }

    public abstract Connector getConnectorForConnectionTo(int connectedIndex);

    @Override
    public void linkRoseElement(SerializedRoadSystem source, RoadSystem target) {
      super.linkRoseElement(source, target);

      this.setAttributeValue(AttributeType.LENGTH, this.length);
      this.setAttributeValue(AttributeType.SLOPE, this.slope);
      this.setAttributeValue(AttributeType.LANE_COUNT, this.laneCount);
      this.setAttributeValue(AttributeType.CONURBATION, this.conurbation);
      this.setAttributeValue(AttributeType.MAX_SPEED, this.maxSpeed);
    }
  }

  private static class SerializedBaseSegment extends SerializedSegment<Base> {
    @JsonProperty("entranceConnectedSegmentId")
    private Integer entranceConnectedSegmentId;
    @JsonProperty("exitConnectedSegmentId")
    private Integer exitConnectedSegmentId;

    SerializedBaseSegment(int index, Base roseBase) {
      super(index, roseBase);
    }

    SerializedBaseSegment() {
      super();
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.entranceConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.roseElement, this.roseElement.getEntry()));
      this.exitConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.roseElement, this.roseElement.getExit()));
    }

    @Override
    public void createRoseElement(RoadSystem target) {
      this.roseElement = (Base) target.createSegment(SegmentType.BASE);
    }

    @Override
    public void linkRoseElement(SerializedRoadSystem source, RoadSystem target) {
      super.linkRoseElement(source, target);

      if (entranceConnectedSegmentId != null) {
        SerializedSegment<? extends Segment> connectedSegment =
            (SerializedSegment<? extends Segment>)
                source.getElementById(entranceConnectedSegmentId);
        Connector connectedConnector =
            connectedSegment.getConnectorForConnectionTo(this.getIndex());

        target.connectConnectors(connectedConnector, this.roseElement.getEntry());
      }

      if (exitConnectedSegmentId != null) {
        SerializedSegment<? extends Segment> connectedSegment =
            (SerializedSegment<? extends Segment>) source.getElementById(exitConnectedSegmentId);
        Connector connectedConnector =
            connectedSegment.getConnectorForConnectionTo(this.getIndex());

        target.connectConnectors(connectedConnector, this.roseElement.getExit());
      }
    }

    @Override
    public Connector getConnectorForConnectionTo(int connectedIndex) {
      if (this.entranceConnectedSegmentId != null
          && connectedIndex == this.entranceConnectedSegmentId) {
        return this.roseElement.getEntry();
      } else if (this.exitConnectedSegmentId != null
          && connectedIndex == this.exitConnectedSegmentId) {
        return this.roseElement.getExit();
      } else {
        throw new RuntimeException("this segment is not connected to the given index");
      }
    }
  }

  private static class SerializedEntranceSegment extends SerializedSegment<Entrance> {
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

    SerializedEntranceSegment(int index, Entrance roseEntrance) {
      super(index, roseEntrance);

      this.populateAttributes();
    }

    SerializedEntranceSegment() {
      super();
    }

    private void populateAttributes() {
      this.laneCountRamp = getAttributeValue(AttributeType.LANE_COUNT_RAMP);
      this.maxSpeedRamp = getAttributeValue(AttributeType.MAX_SPEED_RAMP);
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.entranceConnectedSegmentId =
          serializedRoadSystem.getElementId(serializedRoadSystem.getConnectedSegment(
              this.roseElement, this.roseElement.getEntry()));
      this.exitConnectedSegmentId =
          serializedRoadSystem.getElementId(serializedRoadSystem.getConnectedSegment(
              this.roseElement, this.roseElement.getExit()));
      this.rampConnectedSegmentId =
          serializedRoadSystem.getElementId(serializedRoadSystem.getConnectedSegment(
              this.roseElement, this.roseElement.getRamp()));
    }

    @Override
    public void createRoseElement(RoadSystem target) {
      this.roseElement = (Entrance) target.createSegment(SegmentType.ENTRANCE);
    }

    @Override
    public void linkRoseElement(SerializedRoadSystem source, RoadSystem target) {
      super.linkRoseElement(source, target);

      this.setAttributeValue(AttributeType.LANE_COUNT_RAMP, this.laneCountRamp);
      this.setAttributeValue(AttributeType.MAX_SPEED_RAMP, this.maxSpeedRamp);

      if (entranceConnectedSegmentId != null) {
        SerializedSegment<? extends Segment> connectedSegment =
            (SerializedSegment<? extends Segment>)
                source.getElementById(entranceConnectedSegmentId);
        Connector connectedConnector =
            connectedSegment.getConnectorForConnectionTo(this.getIndex());

        target.connectConnectors(connectedConnector, this.roseElement.getEntry());
      }

      if (exitConnectedSegmentId != null) {
        SerializedSegment<? extends Segment> connectedSegment =
            (SerializedSegment<? extends Segment>) source.getElementById(exitConnectedSegmentId);
        Connector connectedConnector =
            connectedSegment.getConnectorForConnectionTo(this.getIndex());

        target.connectConnectors(connectedConnector, this.roseElement.getExit());
      }

      if (rampConnectedSegmentId != null) {
        SerializedSegment<? extends Segment> connectedSegment =
            (SerializedSegment<? extends Segment>) source.getElementById(rampConnectedSegmentId);
        Connector connectedConnector =
            connectedSegment.getConnectorForConnectionTo(this.getIndex());

        target.connectConnectors(connectedConnector, this.roseElement.getRamp());
      }
    }

    @Override
    public Connector getConnectorForConnectionTo(int connectedIndex) {
      if (this.entranceConnectedSegmentId != null
          && connectedIndex == this.entranceConnectedSegmentId) {
        return this.roseElement.getEntry();
      } else if (this.exitConnectedSegmentId != null
          && connectedIndex == this.exitConnectedSegmentId) {
        return this.roseElement.getExit();
      } else if (this.rampConnectedSegmentId != null
          && connectedIndex == this.rampConnectedSegmentId) {
        return this.roseElement.getRamp();
      } else {
        throw new RuntimeException("this segment is not connected to the given index");
      }
    }
  }

  private static class SerializedExitSegment extends SerializedSegment<Exit> {
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

    SerializedExitSegment(int index, Exit roseExit) {
      super(index, roseExit);

      this.populateAttributes();
    }

    SerializedExitSegment() {
      super();
    }

    private void populateAttributes() {
      this.laneCountRamp = getAttributeValue(AttributeType.LANE_COUNT_RAMP);
      this.maxSpeedRamp = getAttributeValue(AttributeType.MAX_SPEED_RAMP);
    }

    @Override
    public void link(SerializedRoadSystem serializedRoadSystem) {
      this.entranceConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.roseElement, this.roseElement.getEntry()));
      this.exitConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.roseElement, this.roseElement.getExit()));
      this.rampConnectedSegmentId = serializedRoadSystem.getElementId(
          serializedRoadSystem.getConnectedSegment(this.roseElement, this.roseElement.getRamp()));
    }

    @Override
    public void createRoseElement(RoadSystem target) {
      this.roseElement = (Exit) target.createSegment(SegmentType.EXIT);
    }

    @Override
    public void linkRoseElement(SerializedRoadSystem source, RoadSystem target) {
      super.linkRoseElement(source, target);

      this.setAttributeValue(AttributeType.LANE_COUNT_RAMP, this.laneCountRamp);
      this.setAttributeValue(AttributeType.MAX_SPEED_RAMP, this.maxSpeedRamp);

      if (entranceConnectedSegmentId != null) {
        SerializedSegment<? extends Segment> connectedSegment =
            (SerializedSegment<? extends Segment>)
                source.getElementById(entranceConnectedSegmentId);
        Connector connectedConnector =
            connectedSegment.getConnectorForConnectionTo(this.getIndex());

        target.connectConnectors(connectedConnector, this.roseElement.getEntry());
      }

      if (exitConnectedSegmentId != null) {
        SerializedSegment<? extends Segment> connectedSegment =
            (SerializedSegment<? extends Segment>) source.getElementById(exitConnectedSegmentId);
        Connector connectedConnector =
            connectedSegment.getConnectorForConnectionTo(this.getIndex());

        target.connectConnectors(connectedConnector, this.roseElement.getExit());
      }

      if (rampConnectedSegmentId != null) {
        SerializedSegment<? extends Segment> connectedSegment =
            (SerializedSegment<? extends Segment>) source.getElementById(rampConnectedSegmentId);
        Connector connectedConnector =
            connectedSegment.getConnectorForConnectionTo(this.getIndex());

        target.connectConnectors(connectedConnector, this.roseElement.getRamp());
      }
    }

    @Override
    public Connector getConnectorForConnectionTo(int connectedIndex) {
      if (this.entranceConnectedSegmentId != null
          && connectedIndex == this.entranceConnectedSegmentId) {
        return this.roseElement.getEntry();
      } else if (this.exitConnectedSegmentId != null
          && connectedIndex == this.exitConnectedSegmentId) {
        return this.roseElement.getExit();
      } else if (this.rampConnectedSegmentId != null
          && connectedIndex == this.rampConnectedSegmentId) {
        return this.roseElement.getRamp();
      } else {
        throw new RuntimeException("this segment is not connected to the given index");
      }
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
