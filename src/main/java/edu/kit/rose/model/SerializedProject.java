package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.HighwaySegment;
import edu.kit.rose.model.roadsystem.elements.RampSegment;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    private double zoomLevel;

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
    @SuppressWarnings("unused")
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
    private double coordinateX;
    @JsonProperty("coordinateY")
    private double coordinateY;

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
    @SuppressWarnings("unused")
    private SerializedVector() {
    }

    /**
     * Creates a new ROSE {@link Position} object with this object's data.
     */
    public Position createPosition() {
      return new Position(this.coordinateX, this.coordinateY);
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
    private SerializedTimeSliceSetting timeSliceSetting;

    public SerializedRoadSystem(RoadSystem roadSystem) {
      this.roadSystem = roadSystem;

      this.populateElements();
      this.linkElements();

      this.timeSliceSetting =
          new SerializedTimeSliceSetting(this.roadSystem.getTimeSliceSetting());
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    @SuppressWarnings("unused")
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
        };
      }
    }

    private void linkElements() {
      this.elements.forEach(element -> element.link(this));
    }

    public void populateRoadSystem(RoadSystem target) {
      this.elements.forEach(element -> element.createRoseElement(target));
      this.elements.forEach(element -> element.linkRoseElement(this, target));

      this.timeSliceSetting.populateTimeSliceSetting(target.getTimeSliceSetting());
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

      return this.roadSystem.getAdjacentSegments(segment).stream()
          .filter(adjacent -> adjacent.getConnectors().contains(otherConnector))
          .findAny()
          .orElseThrow(); // this should never happen if the road system is consistent
    }
  }

  /**
   * Serializable data model for {@link Element}s.
   *
   * @param <T> the concrete element type.
   */
  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
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
      this.comment = this.roseElement.getComment();
    }

    public abstract void link(SerializedRoadSystem serializedRoadSystem);

    public abstract void createRoseElement(RoadSystem target);

    /**
     * Fills the rose element that was created through {@link #createRoseElement(RoadSystem)} with
     * the data of this object restores relations to other elements (e.g. segment connections
     * and group content).
     *
     * @param source the serialized road system to use for looking up other elements.
     * @param target the road system that the relations should be created in.
     */
    public void linkRoseElement(SerializedRoadSystem source, RoadSystem target) {
      this.roseElement.setName(this.name);
      this.roseElement.setComment(this.comment);
    }
  }

  /**
   * Serializable data model for {@link Group}s.
   */
  private static class SerializedGroup extends SerializedElement<Group> {
    @JsonProperty("childrenIndices")
    private List<Integer> childrenIndices;

    /**
     * Creates a new serialized element with the data of the given ROSE {@code group}.
     *
     * @param index the index of the serialized element in the {@link SerializedRoadSystem}.
     */
    SerializedGroup(int index, Group group) {
      super(index, group);
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    @SuppressWarnings("unused")
    private SerializedGroup() {
      super();
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
  private abstract static class SerializedSegment<T extends HighwaySegment>
      extends SerializedElement<T> {
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("slope")
    private Double slope;
    @JsonProperty("laneCount")
    private Integer laneCount;
    @JsonProperty("conurbation")
    private Boolean conurbation;
    @JsonProperty("maxSpeed")
    private SpeedLimit maxSpeed;

    @JsonProperty("centerPosition")
    private SerializedVector centerPosition;
    @JsonProperty("rotation")
    private int rotation;

    SerializedSegment(int index, T sourceSegment) {
      super(index, sourceSegment);

      this.storeHighwaySegmentData();
    }

    SerializedSegment() {
      super();
    }

    private void storeHighwaySegmentData() {
      this.length = this.roseElement.getLength();
      this.slope = this.roseElement.getSlope();
      this.laneCount = this.roseElement.getLaneCount();
      this.conurbation = this.roseElement.getConurbation();
      this.maxSpeed = this.roseElement.getMaxSpeed();

      this.centerPosition = new SerializedVector(getRoseElement().getCenter());
      this.rotation = getRoseElement().getRotation();
    }

    /**
     * Finds the connector in this segment that is connected to the segment of the given index.
     *
     * @param connectedIndex the index of the other segment that this segment is connected to.
     * @return the connector.
     * @throws NoSuchElementException if no connector of this segment is connected to the other
     *     segment.
     */
    public abstract Connector getConnectorForConnectionTo(int connectedIndex);

    @Override
    public void linkRoseElement(SerializedRoadSystem source, RoadSystem target) {
      super.linkRoseElement(source, target);

      // restore HighwaySegment attributes
      this.roseElement.setLength(this.length);
      this.roseElement.setSlope(this.slope);
      this.roseElement.setLaneCount(this.laneCount);
      this.roseElement.setConurbation(this.conurbation);
      this.roseElement.setMaxSpeed(this.maxSpeed);

      // restore positioning
      var movement = new Movement(// centerPosition - currentPosition
          this.centerPosition.coordinateX - this.roseElement.getCenter().getX(),
          this.centerPosition.coordinateY - this.roseElement.getCenter().getY());
      this.getRoseElement().move(movement);
      this.getRoseElement().rotate(this.rotation);
    }

    /**
     * Adds a {@link edu.kit.rose.model.roadsystem.elements.Connection} to the given
     * {@link RoadSystem}, connecting this segment with another segment.
     *
     * @param source the serialized road system to find the other segment in.
     * @param target the road system to create the connection in.
     * @param otherSegmentIndex the index of the segment to connect this segment with.
     * @param connector the connector from this segment that should be used for the connection.
     */
    protected void createRoseConnection(SerializedRoadSystem source, RoadSystem target,
                                        Integer otherSegmentIndex, Connector connector) {
      if (otherSegmentIndex != null) {
        SerializedSegment<? extends HighwaySegment> connectedSegment =
            (SerializedSegment<? extends HighwaySegment>) source.getElementById(otherSegmentIndex);

        Connector otherConnector = connectedSegment.getConnectorForConnectionTo(this.getIndex());

        target.connectConnectors(connector, otherConnector);
      }
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
    @SuppressWarnings("unused")
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

      // restore relative position of connectors (but not the position of the segment in general)
      var targetConnectorDifference = new Movement(// exit - entrance
          this.exitPosition.coordinateX - this.entrancePosition.coordinateX,
          this.exitPosition.coordinateY - this.entrancePosition.coordinateY);

      Position targetExitPosition = this.roseElement.getEntry()
          .getPosition().add(targetConnectorDifference); // entrance + (exit - entrance)

      Movement exitConnectorMovement = new Movement(// exitTarget - exitPos
          targetExitPosition.getX() - this.roseElement.getExit().getPosition().getX(),
          targetExitPosition.getY() - this.roseElement.getExit().getPosition().getY());

      this.roseElement.getExit().move(exitConnectorMovement);
    }

    @Override
    public void linkRoseElement(SerializedRoadSystem source, RoadSystem target) {
      super.linkRoseElement(source, target);

      createRoseConnection(source, target, entranceConnectedSegmentId, this.roseElement.getEntry());
      createRoseConnection(source, target, exitConnectedSegmentId, this.roseElement.getExit());
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
        throw new NoSuchElementException("this segment is not connected to the given index");
      }
    }
  }

  /**
   * Serializable data model for {@link RampSegment}s.
   */
  private abstract static class SerializedRampSegment<T extends RampSegment>
      extends SerializedSegment<T> {

    @JsonProperty("laneCountRamp")
    private Integer laneCountRamp;
    @JsonProperty("maxSpeedRamp")
    private SpeedLimit maxSpeedRamp;
    @JsonProperty("entranceConnectedSegmentId")
    private Integer entranceConnectedSegmentId;
    @JsonProperty("exitConnectedSegmentId")
    private Integer exitConnectedSegmentId;
    @JsonProperty("rampConnectedSegmentId")
    private Integer rampConnectedSegmentId;
    @JsonProperty("junctionName")
    private String junctionName;

    /**
     * Creates a new serialized ramp segment with the data from the given ROSE ramp segment.
     *
     * @param index the index of this element in the containing {@link SerializedRoadSystem}.
     */
    SerializedRampSegment(int index, T roseEntrance) {
      super(index, roseEntrance);

      this.storeRampAttributes();
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    @SuppressWarnings("unused")
    private SerializedRampSegment() {
      super();
    }

    private void storeRampAttributes() {
      this.laneCountRamp = this.roseElement.getLaneCountRamp();
      this.maxSpeedRamp = this.roseElement.getMaxSpeedRamp();
      this.junctionName = this.roseElement.getJunctionName();
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
    public void linkRoseElement(SerializedRoadSystem source, RoadSystem target) {
      super.linkRoseElement(source, target);

      this.roseElement.setLaneCountRamp(this.laneCountRamp);
      this.roseElement.setMaxSpeedRamp(this.maxSpeedRamp);
      this.roseElement.setJunctionName(this.junctionName);

      createRoseConnection(source, target, entranceConnectedSegmentId, roseElement.getEntry());
      createRoseConnection(source, target, exitConnectedSegmentId, roseElement.getExit());
      createRoseConnection(source, target, rampConnectedSegmentId, roseElement.getRamp());
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
        throw new NoSuchElementException("this segment is not connected to the given index");
      }
    }
  }

  /**
   * Serializable data model for {@link Entrance}s.
   */
  private static class SerializedEntranceSegment extends SerializedRampSegment<Entrance> {
    /**
     * Creates a new serialized entrance segment with the data from the given ROSE entrance segment.
     *
     * @param index the index of this element in the containing {@link SerializedRoadSystem}.
     */
    SerializedEntranceSegment(int index, Entrance roseEntrance) {
      super(index, roseEntrance);
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    @SuppressWarnings("unused")
    private SerializedEntranceSegment() {
      super();
    }

    @Override
    public void createRoseElement(RoadSystem target) {
      this.roseElement = (Entrance) target.createSegment(SegmentType.ENTRANCE);
    }
  }

  /**
   * Serializable data model for {@link Exit}s.
   */
  private static class SerializedExitSegment extends SerializedRampSegment<Exit> {
    /**
     * Creates a new serialized exit segment with the data from the given ROSE exit segment.
     *
     * @param index the index of this element in the containing {@link SerializedRoadSystem}.
     */
    SerializedExitSegment(int index, Exit roseExit) {
      super(index, roseExit);
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    @SuppressWarnings("unused")
    private SerializedExitSegment() {
      super();
    }

    @Override
    public void createRoseElement(RoadSystem target) {
      this.roseElement = (Exit) target.createSegment(SegmentType.EXIT);
    }
  }

  /**
   * Serializable data model for {@link TimeSliceSetting} objects.
   */
  private static class SerializedTimeSliceSetting {
    @JsonProperty("timeSliceLength")
    private int timeSliceLength;
    @JsonProperty("numberOfTimeSlices")
    private int numberOfTimeSlices;

    /**
     * Creates a new serialized time slice setting with data from the given {@code source}.
     */
    public SerializedTimeSliceSetting(TimeSliceSetting timeSliceSetting) {
      this.timeSliceLength = timeSliceSetting.getTimeSliceLength();
      this.numberOfTimeSlices = timeSliceSetting.getNumberOfTimeSlices();
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    @SuppressWarnings("unused")
    private SerializedTimeSliceSetting() {
    }

    /**
     * Populates the given {@code target} with the data from this object.
     */
    public void populateTimeSliceSetting(TimeSliceSetting target) {
      target.setTimeSliceLength(this.timeSliceLength);
      target.setNumberOfTimeSlices(this.numberOfTimeSlices);
    }
  }
}

