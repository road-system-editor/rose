package edu.kit.rose.view.panel.problem;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.plausibility.violation.ViolationManager;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;

/**
 * The problem overview panel provides an overview over
 * all current violations of the current road system against any plausibility criteria.
 * This realizes the problem overview part of specification PF11.1.2.
 */
public class ProblemOverviewPanel extends FxmlContainer
    implements SetObserver<Violation, ViolationManager> {
  /**
   * The controller to use for handling navigation to the affected segments in the road system view.
   */
  private PlausibilityController controller;
  /**
   * The violation manager to fetch violation data from.
   */
  private ViolationManager manager;
  /**
   * Contains a view for each violation reported by the violation manager.
   */
  private Collection<Problem> problems;

  /**
   * Creates a new problem overview panel.
   * Requires {@link #setTranslator(LocalizedTextProvider)}
   *        + {@link #setController(PlausibilityController)}
   *        + {@link #setManager(ViolationManager)}
   */
  public ProblemOverviewPanel() {
    super("problem_overview_panel.fxml");
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
    this.manager = manager;
  }

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
