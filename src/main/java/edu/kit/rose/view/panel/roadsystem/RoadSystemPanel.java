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
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
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

  private Grid editorGridSurface;

  private List<SegmentView<? extends Segment>> segmentViews;

  @Inject
  private Injector injector;


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
    injector.injectMembers(this.zoomContainer);

    this.editorGridSurface = this.zoomContainer.getGrid();
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
