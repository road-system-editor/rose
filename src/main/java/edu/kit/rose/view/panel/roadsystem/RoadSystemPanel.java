package edu.kit.rose.view.panel.roadsystem;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.ConnectionView;
import edu.kit.rose.view.commons.ConnectorView;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.SegmentView;
import edu.kit.rose.view.commons.SegmentViewFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;


/**
 * The RoadSystem panel is a view component that display a panable and zoomable editor,
 * where segment views can be placed,
 * moved and edited.
 *
 * @implNote Uses SetObserver as anonymous class
 *           to subscribe to selection events from RoadSystemController
 */
public class RoadSystemPanel extends FxmlContainer
    implements DualSetObserver<Element, Connection, RoadSystem> {

  @Inject
  private ApplicationController applicationController;
  @Inject
  private RoadSystemController roadSystemController;
  @Inject
  private AttributeController attributeController;
  @Inject
  private MeasurementController measurementController;

  /**
   * The project that contains all project specific data.
   */
  @Inject
  private Project project;

  /**
   * The navigator view that displays functionality for navigation on the road system panel.
   */
  @FXML
  private NavigatorView navigator;

  @FXML
  private ChangeButtonPanel changeButtonPanel;

  @FXML
  private ZoomableScrollPane zoomContainer;

  private Grid roadSystemGrid;

  private SegmentViewFactory segmentViewFactory;

  private final Map<Segment, SegmentView<?>> segmentViewMap = new HashMap<>();
  private final Map<Connection, ConnectionView> connectionViewMap = new HashMap<>();



  /**
   * Creates a new road system panel. Make sure to call {@link #init(Injector)} afterwards!
   */
  public RoadSystemPanel() {
    super("RoadSystemPanel.fxml");
    setupNavigatorView();
  }

  private void setupNavigatorView() {
    this.navigator.setOnUp(zoomContainer::moveUp);
    this.navigator.setOnDown(zoomContainer::moveDown);
    this.navigator.setOnLeft(zoomContainer::moveLeft);
    this.navigator.setOnRight(zoomContainer::moveRight);
    this.navigator.setOnIn(zoomContainer::zoomIn);
    this.navigator.setOnOut(zoomContainer::zoomOut);
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);
    this.zoomContainer.init(injector);
    Platform.runLater(() -> {
      project.getZoomSetting().addSubscriber(zoomContainer);
      project.getZoomSetting().notifySubscribers();
    });


    this.segmentViewFactory = new SegmentViewFactory(getTranslator(), this.roadSystemController);

    this.roadSystemGrid = this.zoomContainer.getGrid();

    this.roadSystemGrid.setOnAreaSelected((position1, position2) ->
        this.roadSystemController.selectSegmentsInRectangle(position1, position2));

    this.roadSystemController.addSubscriber(this.roadSystemGrid);

    this.project.getRoadSystem().addSubscriber(this);

    setUpEventHandlers();
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return List.of(changeButtonPanel, navigator);
  }

  @Override
  public void notifyAdditionSecond(Connection unit) {
    var connector1 = unit.getConnectors().get(0);
    var connector2 = unit.getOther(connector1);
    var segmentViews = segmentViewMap.values();

    var segment1 = getSegmentViewByConnector(segmentViews, connector1);
    var segment2 = getSegmentViewByConnector(segmentViews, connector2);

    if (segment1 == null || segment2 == null) {
      return;
    }

    var connector1Pos = segment1.getSegment().getAbsoluteConnectorPosition(connector1);
    var connector2Pos = segment2.getSegment().getAbsoluteConnectorPosition(connector2);

    var connectionView = new ConnectionView(new Point2D(connector1Pos.getX(), connector1Pos.getY()),
        new Point2D(connector2Pos.getX(), connector2Pos.getY()), unit);
    connectionViewMap.put(unit, connectionView);
    roadSystemGrid.getChildren().add(connectionView);
  }

  private SegmentView<?> getSegmentViewByConnector(
      Collection<SegmentView<?>> segmentViews, Connector connector) {
    return segmentViews.stream()
        .filter(segmentView -> segmentView.getSegment().getConnectors().contains(connector))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void notifyRemovalSecond(Connection unit) {
    var connectionView = connectionViewMap.get(unit);
    roadSystemGrid.getChildren().remove(connectionView);
    connectionViewMap.remove(unit, connectionView);
  }

  @Override
  public void notifyAddition(Element unit) {
    if (!unit.isContainer()) {
      var segment = (Segment) unit;
      var segmentView = this.segmentViewFactory.createForSegment(segment);
      roadSystemGrid.addSegmentView(segmentView);
      segmentViewMap.put(segment, segmentView);
    }
  }

  @Override
  public void notifyRemoval(Element unit) {
    if (!unit.isContainer()) {
      var segment = (Segment) unit;
      var segmentView = segmentViewMap.get(segment);
      roadSystemGrid.removeSegmentView(segmentView);
      segmentViewMap.remove(segment, segmentView);
    }
  }

  @Override
  public void notifyChange(RoadSystem unit) {

  }

  private void setUpEventHandlers() {
    setOnKeyPressed(keyEvent -> getAction(keyEvent).run());
  }

  private Runnable getAction(KeyEvent keyEvent) {
    var keyCode = keyEvent.getCode();
    return switch (keyCode) {
      case R -> roadSystemController::rotateSegment;
      case DELETE -> roadSystemController::deleteStreetSegments;
      case Z -> keyEvent.isControlDown() ? applicationController::undo : () -> {};
      case Y -> keyEvent.isControlDown() ? applicationController::redo : () -> {};
      case D -> keyEvent.isControlDown() ? roadSystemController::duplicateStreetSegment : () -> {};
      default -> () -> {};
    };
  }

  /**
   * Getter for The ZoomSetting of the project.
   *
   * @return the ZoomSetting of the project.
   */
  public ZoomSetting getZoomSetting() {
    return this.project.getZoomSetting();
  }
}
