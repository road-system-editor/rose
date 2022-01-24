package edu.kit.rose.view.panel.roadsystem;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.SimpleSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.panel.segment.SegmentEditorPanel;
import java.util.Collection;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;


/**
 * The RoadSystem panel is a view component that display a panable and zoomable editor,
 * where segment views can be placed,
 * moved and edited.
 *
 * @implNote Uses SetObserver as anonymous class
 *      to subscribe to selection events from RoadsystemController
 */
public class RoadSystemPanel extends FxmlContainer
    implements DualSetObserver<Element, Connection, RoadSystem> {
  private ApplicationController applicationController;
  private RoadSystemController roadSystemController;
  private AttributeController attributeController;
  private MeasurementController measurementController;

  /**
   * The project that contains all project specific data.
   */
  private Project project;

  @FXML
  private StackPane stackPane;

  /**
   * The navigator view that displays functionality for navigation on the road system panel.
   */
  @FXML
  private NavigatorView navigator;

  @FXML
  private ChangeButtonPanel changeButtonPanel;

  @FXML
  private ZoomableScrollPane zoomContainer;

  @FXML
  private Grid editorGridSurface;

  private List<SegmentView<? extends Segment>> segmentViews;

  @Inject
  private Injector injector;


  /**
   * Creates a new road system panel. Make sure to call {@link #init(Injector)} afterwards!
   */
  public RoadSystemPanel() {
    super("RoadSystemPanel.fxml");
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);

    // remove this when implementing the road system panel, this is for testing the attribute editor
    Segment segment = new Segment() {
      private int laneCount = 2;
      private String name = "hello";

      @Override
      public SegmentType getSegmentType() {
        return null;
      }

      @Override
      public SortedBox<Measurement<?>> getMeasurements() {
        return null;
      }

      @Override
      public Box<Connector> getConnectors() {
        return null;
      }

      @Override
      public Position getCenter() {
        return null;
      }

      @Override
      public void move(Movement movement) {

      }

      @Override
      public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
        return new SimpleSortedBox<>(List.of(
            new AttributeAccessor<>(AttributeType.LANE_COUNT, () -> this.laneCount,
                newLaneCount -> {
                  this.laneCount = newLaneCount;
                  System.out.println("new lane count: " + laneCount);
                }),
            new AttributeAccessor<>(AttributeType.NAME, () -> this.name,
                newName -> {
                  this.name = newName;
                  System.out.println("new name: " + name);
                })
        ));
      }

      @Override
      public String getName() {
        return null;
      }

      @Override
      public boolean isContainer() {
        return false;
      }

      @Override
      public void addSubscriber(UnitObserver<Element> observer) {

      }

      @Override
      public void removeSubscriber(UnitObserver<Element> observer) {

      }

      @Override
      public void notifySubscribers() {

      }

      @Override
      public Element getThis() {
        return null;
      }

      @Override
      public int compareTo(Segment o) {
        return 0;
      }
    };
    SegmentEditorPanel segmentEditorPanel = new SegmentEditorPanel();
    stackPane.getChildren().add(segmentEditorPanel);
    segmentEditorPanel.init(injector);
    segmentEditorPanel.setSegment(segment);
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }

  @Override
  public void notifyAdditionSecond(Connection unit) {

  }

  @Override
  public void notifyRemovalSecond(Connection unit) {

  }

  @Override
  public void notifyAddition(Element unit) {

  }

  @Override
  public void notifyRemoval(Element unit) {

  }

  @Override
  public void notifyChange(RoadSystem unit) {

  }
}
