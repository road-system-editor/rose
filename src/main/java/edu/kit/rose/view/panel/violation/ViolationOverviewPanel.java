package edu.kit.rose.view.panel.violation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
  private VBox violationBox;
  @FXML
  private Label violationLabel;
  @FXML
  private Label segmentLabel;

  private Collection<ViolationHandle> violationHandles;

  /**
   * Creates a new problem overview panel.
   * Requires {@link #setController(PlausibilityController)}
   *        + {@link #setManager(ViolationManager)}
   */
  public ViolationOverviewPanel() {
    super("ViolationOverviewPanel.fxml");
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);

    violationLabel.setText(getTranslator()
        .getLocalizedText("view.panel.violation.violationOverviewPanel.violationLabel"));
    segmentLabel.setText(getTranslator()
        .getLocalizedText("view.panel.violation.violationOverviewPanel.segmentLabel"));

    project.getPlausibilitySystem().getViolationManager().getViolations();

  }

  /**
   * Sets the controller that handles plausibility input.
   *
   * @param controller controller that handles plausibility input.
   */
  public void setController(PlausibilityController controller) {
    this.controller = controller;
  }

  /**
   * Sets the {@link ViolationManager}.
   *
   * @param manager the {@link ViolationManager} instance
   */
  public void setManager(ViolationManager manager) {

  } //TODO: can this be removed?

  @Override
  public void notifyAddition(Violation unit) {

  }

  @Override
  public void notifyRemoval(Violation unit) {

  }

  @Override
  public void notifyChange(ViolationManager unit) {

  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
