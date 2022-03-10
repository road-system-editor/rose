package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.attributes.SpeedLimit;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.HighwaySegment;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is an object model for the "Deutsches FREEVAL" YAML format.
 * Use {@link YamlExportStrategy#exportToFile(File)} to run an export with this format.
 *
 * @implNote {@link SuppressWarnings} "unused" is used on lots of fields that are only accessed
 *     through Jackson.
 * @implNote {@link SuppressWarnings} "SpellCheckingInspection" is used on some
 *     {@link JsonProperty} annotations to match the format specification.
 */
class YamlProject {
  @JsonIgnore
  private final Map<SegmentType, Function<Segment, YamlSegment>> yamlSegmentCreators;

  /**
   * The road system is stored to access the source data model within the population process.
   */
  @JsonIgnore
  private final RoadSystem roadSystem;

  /**
   * This map stores a {@link YamlSegment} with an ID.
   * Use {@link #getSegmentId(Segment)} to find the ID of a ROSE segment.
   */
  @JsonProperty("Segmente")
  private Map<Integer, YamlSegment> segments;
  @SuppressWarnings("unused")
  @JsonProperty("Options")
  private YamlOptions options;

  /**
   * Creates and populates the YAML data model with the data from the given {@code project}.
   *
   * @param project the data source for this export.
   */
  YamlProject(Project project) {
    this.yamlSegmentCreators = Map.of(
        SegmentType.BASE, base -> new YamlBaseSegment(this, (Base) base),
        SegmentType.ENTRANCE, entrance -> new YamlEntranceSegment(this, (Entrance) entrance),
        SegmentType.EXIT, exit -> new YamlExitSegment(this, (Exit) exit)
    );

    this.roadSystem = project.getRoadSystem();

    this.segments = new HashMap<>();
    this.options = new YamlOptions(this.roadSystem.getTimeSliceSetting());

    this.populateSegments();
    this.populateConnections();
  }

  private void populateSegments() {
    int id = 1;
    for (var segment : this.roadSystem.getElements()) {
      if (!segment.isContainer()) {
        this.segments.put(id++, createYamlSegment((Segment) segment));
      }
    }
  }

  private void populateConnections() {
    segments.values().forEach(YamlSegment::populateConnections);
  }

  /**
   * Finds the ID for the {@link YamlSegment} that belongs to a given {@link Segment}.
   * Do not use this method before {@link #populateSegments()} has been completed!
   *
   * @param segment the ROSE segment to find the id of.
   * @return the ID of the given segment.
   * @throws java.util.NoSuchElementException if the segment is not included in this project.
   */
  private int getSegmentId(Segment segment) {
    return segments.entrySet().stream()
        .filter(entry -> entry.getValue().segment == segment)
        .map(Map.Entry::getKey)
        .findAny()
        .orElseThrow();
  }

  private YamlSegment createYamlSegment(Segment segment) {
    Function<Segment, YamlSegment> creator = yamlSegmentCreators.get(segment.getSegmentType());
    return creator.apply(segment);
  }

  /**
   * Stores a {@link Segment} and it's {@link edu.kit.rose.model.roadsystem.elements.Connection}s.
   */
  private abstract static class YamlSegment {
    /**
     * The YAML project is used to access connection and ID data within the population process.
     */
    @JsonIgnore
    private final YamlProject yamlProject;
    /**
     * The segment is stored to access the source data model within the population process.
     */
    @JsonIgnore
    private final HighwaySegment segment;

    @SuppressWarnings("unused")
    @JsonProperty("Name")
    private String name;
    @SuppressWarnings("unused")
    @JsonProperty("Typ")
    private String type;
    @SuppressWarnings({"SpellCheckingInspection", "unused"})
    @JsonProperty("Laenge")
    private int length;
    @SuppressWarnings("unused")
    @JsonProperty("Fahrstreifenanzahl")
    private int laneCount;
    @SuppressWarnings("unused")
    @JsonProperty("Steigung")
    private double slope;
    @SuppressWarnings("unused")
    @JsonProperty("Ballungsraum")
    private String conurbation;
    @SuppressWarnings("unused")
    @JsonProperty("Tempolimit")
    private String speedLimit;
    @SuppressWarnings({"SpellCheckingInspection", "unused"})
    @JsonProperty("Vorgaenger")
    private List<Integer> predecessorIds;
    @SuppressWarnings("unused")
    @JsonProperty("Nachfolger")
    private List<Integer> successorIds;

    /**
     * Creates a new YAML segment for a given {@code segment}.
     * Make sure to call {@link #populateConnections()} once IDs are available in the
     * {@code yamlProject}!
     *
     * @param type the segment type identifier of the YAML format.
     */
    protected YamlSegment(YamlProject yamlProject, HighwaySegment segment, String type) {
      this.yamlProject = yamlProject;
      this.segment = segment;

      this.type = type;
      this.populateAttributes();
    }

    private void populateAttributes() {
      this.name = segment.getName();
      this.length = segment.getLength();
      this.laneCount = segment.getLaneCount();
      this.slope = segment.getSlope();
      this.conurbation = convertConurbation(segment.getConurbation());
      this.speedLimit = convertSpeedLimit(segment.getMaxSpeed());
    }

    /**
     * Sets up connection data.
     * {@link YamlProject#populateSegments()} must have been called before using this method.
     */
    public void populateConnections() {
      this.predecessorIds = segmentListToIdList(getPredecessorConnectors());
      this.successorIds = segmentListToIdList(getSuccessorConnectors());
    }

    protected abstract List<Connector> getPredecessorConnectors();

    protected abstract List<Connector> getSuccessorConnectors();

    private List<Integer> segmentListToIdList(List<Connector> segmentList) {
      return segmentList.stream()
          .map(this::getConnectedSegment)
          .filter(Objects::nonNull)
          .map(yamlProject::getSegmentId)
          .collect(Collectors.toList());
    }

    /**
     * Finds the ROSE segment that is connected to {@link #segment} on the given {@code connector}.
     *
     * @return the connected segment or {@code null} if this connector is not connected to another
     *     {@link Segment}.
     */
    private Segment getConnectedSegment(Connector connector) {
      var connection = yamlProject.roadSystem.getConnection(connector);
      if (connection == null) {
        return null;
      }
      var otherConnector = connection.getOther(connector);

      return this.yamlProject.roadSystem.getAdjacentSegments(this.segment).stream()
          .filter(adjacent -> adjacent.getConnectors().contains(otherConnector))
          .findFirst()
          .orElseThrow();
    }

    static String convertConurbation(boolean attributeValue) {
      return attributeValue ? "innerhalb" : "außerhalb";
    }

    static String convertSpeedLimit(SpeedLimit limit) {
      return switch (limit) {
        case NONE -> "ohne";
        case TUNNEL -> "Tunnel";
        default -> limit.toString();
      };
    }
  }

  /**
   * Stores a {@link Base} segment and it's
   * {@link edu.kit.rose.model.roadsystem.elements.Connection}s.
   */
  private static class YamlBaseSegment extends YamlSegment {
    /**
     * The segment is stored to access the source data model within the population process.
     */
    @JsonIgnore
    private final Base segment;

    YamlBaseSegment(YamlProject yamlProject, Base segment) {
      super(yamlProject, segment,
          "Basissegment");
      this.segment = segment;
    }

    @Override
    protected List<Connector> getPredecessorConnectors() {
      return List.of(segment.getEntry());
    }

    @Override
    protected List<Connector> getSuccessorConnectors() {
      return List.of(segment.getExit());
    }
  }

  /**
   * Stores an {@link Exit} segment and it's
   * {@link edu.kit.rose.model.roadsystem.elements.Connection}s.
   */
  private static class YamlExitSegment extends YamlSegment {
    /**
     * The segment is stored to access the source data model within the population process.
     */
    @JsonIgnore
    private final Exit segment;

    @JsonProperty("AUS_Geschwindigkeit")
    String rampSpeed;

    YamlExitSegment(YamlProject yamlProject, Exit segment) {
      super(yamlProject, segment, "Ausfahrt");
      this.segment = segment;

      this.rampSpeed = convertSpeedLimit(segment.getMaxSpeedRamp());
    }

    @Override
    protected List<Connector> getPredecessorConnectors() {
      return List.of(this.segment.getEntry());
    }

    @Override
    protected List<Connector> getSuccessorConnectors() {
      return List.of(this.segment.getExit(), this.segment.getRamp());
    }
  }

  /**
   * Stores an {@link Entrance} segment and it's
   * {@link edu.kit.rose.model.roadsystem.elements.Connection}s.
   */
  private static class YamlEntranceSegment extends YamlSegment {
    /**
     * The segment is stored to access the source data model within the population process.
     */
    @JsonIgnore
    private final Entrance segment;

    @SuppressWarnings("unused")
    @JsonProperty("EIN_Geschwindigkeit")
    private final String rampSpeed;

    YamlEntranceSegment(YamlProject yamlProject, Entrance segment) {
      super(yamlProject, segment, "Einfahrt");
      this.segment = segment;

      this.rampSpeed = convertSpeedLimit(segment.getMaxSpeedRamp());
    }

    @Override
    protected List<Connector> getPredecessorConnectors() {
      return List.of(this.segment.getEntry(), this.segment.getRamp());
    }

    @Override
    protected List<Connector> getSuccessorConnectors() {
      return List.of(this.segment.getExit());
    }
  }

  /**
   * Stores the {@link TimeSliceSetting} object.
   */
  private static class YamlOptions {
    @JsonProperty("Zeitintervall")
    int timeSliceLength;
    @JsonProperty("Zeitschritte")
    int numberOfTimeSlices;

    YamlOptions(TimeSliceSetting timeSliceSetting) {
      this.timeSliceLength = timeSliceSetting.getTimeSliceLength();
      this.numberOfTimeSlices = timeSliceSetting.getNumberOfTimeSlices();
    }
  }
}
