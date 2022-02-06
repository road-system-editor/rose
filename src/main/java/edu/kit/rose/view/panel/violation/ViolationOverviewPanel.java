package edu.kit.rose.view.panel.violation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.panel.problem.MessageFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * The problem overview panel provides an overview over
 * all current violations of the current road system against any plausibility criteria.
 * This realizes the problem overview part of specification PF11.1.2.
 */
public class ViolationOverviewPanel extends FxmlContainer
    implements SetObserver<Violation, ViolationManager> {

  @Inject
  private PlausibilityController controller;
  @Inject
  private Injector injector;

  @Inject
  private Project project;

  @FXML
  private ListView<Violation> violationList;
  @FXML
  private Label violationLabel;
  @FXML
  private Label segmentLabel;

  private MessageFactory messageFactory;

  private Collection<ViolationHandle> violationHandles;

  /**
   * Creates a new problem overview panel.
   */
  public ViolationOverviewPanel() {
    super("ViolationOverviewPanel.fxml");
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);
    this.messageFactory = new MessageFactory(getTranslator());
    initViolationBox();
  }

  private void initViolationBox() {
    violationList.setCellFactory(
        violation -> new ViolationListCell(this.controller, this.messageFactory));

    for (Violation violation : project.getPlausibilitySystem().getViolationManager()
        .getViolations()) {
      violationList.getItems().add(violation);
    }
  }

  @Override
  public void notifyAddition(Violation unit) {
    violationList.getItems().add(unit);
  }

  @Override
  public void notifyRemoval(Violation unit) {
    violationList.getItems().removeIf(item -> item == unit);
  }

  @Override
  public void notifyChange(ViolationManager unit) {
    initViolationBox();
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
    violationLabel.setText(getTranslator()
        .getLocalizedText("view.panel.violation.violationOverviewPanel.violationLabel"));
    segmentLabel.setText(getTranslator()
        .getLocalizedText("view.panel.violation.violationOverviewPanel.segmentLabel"));
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
