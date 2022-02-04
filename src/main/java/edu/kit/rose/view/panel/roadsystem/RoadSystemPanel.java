package edu.kit.rose.view.panel.roadsystem;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.model.roadsystem.measurements.Measurement;
import edu.kit.rose.view.commons.BaseSegmentView;
import edu.kit.rose.view.commons.ExitSegmentView;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.SegmentView;
import java.util.Collection;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


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

    this.roadSystemGrid = this.zoomContainer.getGrid();

    this.roadSystemGrid.setOnAreaSelected((position1, position2) ->
        this.roadSystemController.selectSegmentsInRectangle(position1, position2));

    /*var exit = new Exit();
    exit.move(new Movement(300, 300));
    var exitView = new ExitSegmentView(exit, roadSystemController, getTranslator());
    var circle = new Circle(2, Color.GREEN);
    circle.setLayoutX(exit.getCenter().getX());
    circle.setLayoutY(exit.getCenter().getY());
    roadSystemGrid.getChildren().add(exitView);
    exitView.setOnMouseMoved(event -> {
      if (event.isAltDown()) {
        exit.rotate(2);
      }
    });*/

    /*var rectangle = new Rectangle(exitView.getBoundsInParent().getMinX(),
        exitView.getBoundsInParent().getMinY(), exitView.getBoundsInParent().getWidth(),
        exitView.getBoundsInParent().getHeight());
    rectangle.setFill(Color.RED.deriveColor(1, 1, 1, 0.2));
    roadSystemGrid.getChildren().add(rectangle);
    rectangle.setOnMouseMoved(event -> {
      exit.rotate(2);
      rectangle.setX(exitView.getBoundsInParent().getMinX());
      rectangle.setY(exitView.getBoundsInParent().getMinY());
      rectangle.setWidth(exitView.getBoundsInParent().getWidth());
      rectangle.setHeight(exitView.getBoundsInParent().getHeight());
      System.out.println(exitView.getBoundsInParent());
    });

    roadSystemGrid.getChildren().add(circle);*/
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);
    injector.injectMembers(this.zoomContainer);

    Base b = new Base();
    b.move(new Movement(600, 600));
    var baseView = new BaseSegmentView(b, this.roadSystemController, this.getTranslator());
    this.roadSystemGrid.addSegmentView(baseView);
    /*var rectangle = new Rectangle(baseView.getBoundsInParent().getMinX(),
        baseView.getBoundsInParent().getMinY(), baseView.getBoundsInParent().getWidth(),
        baseView.getBoundsInParent().getHeight());
    rectangle.setFill(Color.RED.deriveColor(1, 1, 1, 0.2));
    roadSystemGrid.getChildren().add(rectangle);
    baseView.setOnMouseMoved(event -> {
      rectangle.setX(baseView.getBoundsInParent().getMinX());
      rectangle.setY(baseView.getBoundsInParent().getMinY());
      rectangle.setWidth(baseView.getBoundsInParent().getWidth());
      rectangle.setHeight(baseView.getBoundsInParent().getHeight());
      System.out.println(baseView.getBoundsInParent());
    });*/
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
