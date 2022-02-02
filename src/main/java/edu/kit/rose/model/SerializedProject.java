package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
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
import java.util.Objects;
import java.util.Set;

/**
 * This is a data model for {@link Project} that is serializable through the Jackson library.
 */
class SerializedProject {
  @JsonProperty("roadSystem")
  private SerializedRoadSystem roadSystem;
  @JsonProperty("zoomSetting")
  private SerializedZoomSetting zoomSetting;

  /**
   * Serializes the given project into a data model for the ROSE format.
   *
   * @param source the project to serialize.
   */
  public SerializedProject(Project source) {
    this.roadSystem = new SerializedRoadSystem(source.getRoadSystem());
    this.zoomSetting = new SerializedZoomSetting(source.getZoomSetting());
  }

  /**
   * Empty constructor to be used by Jackson when de-serializing a file into this model.
   */
  @SuppressWarnings("unused")
  private SerializedProject() {
  }

  public void populateProject(Project target) {
    this.roadSystem.populateRoadSystem(target.getRoadSystem());
    this.zoomSetting.populateZoomSetting(target.getZoomSetting());
  }

  /**
   * Serializable data model for the {@link ZoomSetting} object.
   */
  private static class SerializedZoomSetting {
    @JsonProperty("center")
    private SerializedVector center;
    @JsonProperty("zoomLevel")
    private int zoomLevel;

    /**
     * Creates a new serialized zoom setting with the data from the given {@code source}.
     */
    public SerializedZoomSetting(ZoomSetting source) {
      Objects.requireNonNull(source);

      this.center = new SerializedVector(source.getCenterOfView());
      this.zoomLevel = source.getZoomLevel();
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    public SerializedZoomSetting() {
    }

    /**
     * Inserts this object's data into the given {@code target}.
     */
    public void populateZoomSetting(ZoomSetting target) {
      target.setCenterOfView(this.center.createPosition());
      target.setZoomLevel(this.zoomLevel);
    }
  }

  /**
   * Serializable data model for {@link Position} and {@link Movement }objects.
   */
  private static class SerializedVector {
    @JsonProperty("coordinateX")
    private int coordinateX;
    @JsonProperty("coordinateY")
    private int coordinateY;

    /**
     * Creates a new serialized position with the data from the given {@code source}.
     */
    public SerializedVector(Position source) {
      Objects.requireNonNull(source);

      this.coordinateX = source.getX();
      this.coordinateY = source.getY();
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    private SerializedVector() {
    }

    /**
     * Creates a new ROSE {@link Position} object with this object's data.
     */
    public Position createPosition() {
      return new Position(this.coordinateX, this.coordinateY);
    }

    /**
     * Creates a new ROSE {@link Movement} object with this object's data.
     */
    public Movement createMovement() {
      return new Movement(this.coordinateX, this.coordinateY);
    }
  }

  /**
   * Serializable data model for {@link RoadSystem} objects.
   */
  private static class SerializedRoadSystem {
    @JsonIgnore
    private final RoadSystem roadSystem;

    @JsonProperty("elements")
    private List<SerializedElement<? extends Element>> elements;
    @JsonProperty("timeSliceSetting")
    private JsonTimeSliceSetting timeSliceSetting;

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
    public Integer getElementId(Element element) {
      return element == null ? null : this.elements.stream()
          .filter(other -> other.getRoseElement() == element)
          .findAny()
          .orElseThrow()
          .getIndex();
    }

    /**
     * Finds a serialized element for a given {@code index}.
     *
     * @param index the index of the serialized element.
     * @return the serialized element.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    private SerializedElement<? extends Element> getElementById(int index) {
      return this.elements.get(index);
    }

    /**
     * Finds the ROSE segment that is connected to {@code segment} on the given {@code connector}.
     *
     * @return the connected segment or {@code null} if this connector is not connected to another
     *     {@link Segment}.
     */
    public Segment getConnectedSegment(Segment segment, Connector connector) {
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

  }

  /**
   * Serializable data model for {@link Element}s.
   *
   * @param <T> the concrete element type.
   */
  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.PROPERTY,
      property = "elementType"
  )
  @JsonSubTypes({
      @JsonSubTypes.Type(value = SerializedGroup.class, name = "GROUP"),
      @JsonSubTypes.Type(value = SerializedBaseSegment.class, name = "BASE_SEGMENT"),
      @JsonSubTypes.Type(value = SerializedEntranceSegment.class, name = "ENTRANCE_SEGMENT"),
      @JsonSubTypes.Type(value = SerializedExitSegment.class, name = "EXIT_SEGMENT")
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

    /**
     * Creates a new serialized element with the data of the given ROSE element.
     *
     * @param index the index of the serialized element in the {@link SerializedRoadSystem}.
     */
    protected SerializedElement(int index, T source) {
      this.index = index;
      this.roseElement = source;

      this.populateAttributes();
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
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

  /**
   * Serializable data model for {@link Group}s.
   */
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

  /**
   * Serializable data model for {@link Segment}s.
   *
   * @param <T> the concrete segment type.
   */
  private abstract static class SerializedSegment<T extends Segment> extends SerializedElement<T> {
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

    @JsonProperty("centerPosition")
    private SerializedVector centerPosition;
    @JsonProperty("rotation")
    private int rotation;

    SerializedSegment(int index, T sourceSegment) {
      super(index, sourceSegment);

      this.populateAttributes();
    }

    SerializedSegment() {
      super();
    }

    private void populateAttributes() {
      this.length = getAttributeValue(AttributeType.LENGTH);
      this.slope = getAttributeValue(AttributeType.SLOPE);
      this.laneCount = getAttributeValue(AttributeType.LANE_COUNT);
      this.conurbation = getAttributeValue(AttributeType.CONURBATION);
      this.maxSpeed = getAttributeValue(AttributeType.MAX_SPEED);

      this.centerPosition = new SerializedVector(getRoseElement().getCenter());
      this.rotation = getRoseElement().getRotation();
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

      this.getRoseElement().move(this.centerPosition.createMovement());
      this.getRoseElement().rotate(this.rotation);
    }
  }

  /**
   * Serializable data model for {@link Base} segments.
   */
  private static class SerializedBaseSegment extends SerializedSegment<Base> {
    @JsonProperty("entranceConnectedSegmentId")
    private Integer entranceConnectedSegmentId;
    @JsonProperty("exitConnectedSegmentId")
    private Integer exitConnectedSegmentId;
    @JsonProperty("entrancePosition")
    private SerializedVector entrancePosition;
    @JsonProperty("exitPosition")
    private SerializedVector exitPosition;

    /**
     * Creates a new serialized base segment with the data from the given ROSE base segment.
     *
     * @param index the index of this element in the containing {@link SerializedRoadSystem}.
     */
    public SerializedBaseSegment(int index, Base roseBase) {
      super(index, roseBase);

      this.entrancePosition = new SerializedVector(roseBase.getEntry().getPosition());
      this.exitPosition = new SerializedVector(roseBase.getExit().getPosition());
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    private SerializedBaseSegment() {
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
      /* TODO uncomment once movable connectors are merged
      this.roseElement.getEntry().move(this.entrancePosition.createMovement());
      this.roseElement.getExit().move(this.exitPosition.createMovement());*/
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

  /**
   * Serializable data model for {@link Entrance}s.
   */
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

  /**
   * Serializable data model for {@link Exit}s.
   */
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

