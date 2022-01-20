package edu.kit.rose.view.panel.roadsystem;

import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.SegmentView;
import java.util.Collection;
import java.util.List;
import javafx.fxml.FXML;


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


  /**
   * requires {@link #setProject(Project)} +
   * {@link #setApplicationController(ApplicationController)} +
   * {@link #setRoadSystemController(RoadSystemController)} +
   * {@link #setAttributeController(AttributeController)} +
   * {@link #setMeasurementController(MeasurementController)}.
   */
  public RoadSystemPanel() {
    super("road_system_panel.fxml");
  }

  /**
   * Sets theSets project.
   *
   * @param project project to assign to the roadsystem panel
   */
  public void setProject(Project project) {
    this.project = project;
  }

  /**
   * Sets the application controller.
   *
   * @param applicationController application controller instance
   */
  public void setApplicationController(ApplicationController applicationController) {
    this.applicationController = applicationController;
  }

  /**
   * Sets the roadSystemController controller.
   *
   * @param roadSystemController roadSystemController instance
   */
  public void setRoadSystemController(RoadSystemController roadSystemController) {
    this.roadSystemController = roadSystemController;
  }

  /**
   * Sets the attributeController controller.
   *
   * @param attributeController attributeController instance
   */
  public void setAttributeController(AttributeController attributeController) {
    this.attributeController = attributeController;
  }

  /**
   * Sets the measurementController controller.
   *
   * @param measurementController measurementController instance
   */
  public void setMeasurementController(MeasurementController measurementController) {
    this.measurementController = measurementController;
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
