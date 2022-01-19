package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.view.commons.FXMLContainer;
import java.util.Collection;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

/**
 * The criteria overview panel allows the user to view the criteria.
 */
public class CriteriaOverviewPanel extends FXMLContainer
    implements SetObserver<PlausibilityCriterion, CriteriaManager> {
  private PlausibilityController controller;
  private CriteriaManager manager;
  private CriterionHandle selected;
  private Consumer<PlausibilityCriterion> selectionListener;

  @FXML
  private Button exportButton;
  @FXML
  private Button importButton;
  @FXML
  private Button deleteAllButton;
  @FXML
  private Button newButton;
  @FXML
  private ScrollPane list;
  private Collection<CriterionHandle> handles;

  /**
   * Creates a new CriteriaOverviewPanel.
   *
   * {@link #setController(PlausibilityController)} + {@link #setManager(CriteriaManager)} +
   * {@link #setTranslator(LocalizedTextProvider)} + {@link #setSelectionListener(Consumer)}
   * needs to be called!
   */
  public CriteriaOverviewPanel() {
    super("criteria_overview_panel.fxml");
  }

  /**
   * Setter for {@link PlausibilityController} held within.
   *
   * @param controller is the PlausibilityController to be set.
   */
  public void setController(PlausibilityController controller) {
    this.controller = controller;
  }

  /**
   * Setter for the {@link CriteriaManager} held within.
   *
   * @param manager is the CriteriaManager to be set.
   */
  public void setManager(CriteriaManager manager) {
    this.manager = manager;
  }

  /**
   * Setter for the selectionListener.
   *
   * @param selectionListener is the listener that is triggered by selection.
   */
  public void setSelectionListener(Consumer<PlausibilityCriterion> selectionListener) {
    this.selectionListener = selectionListener;
  }

  @Override
  public void notifyAddition(PlausibilityCriterion unit) {

  }

  @Override
  public void notifyRemoval(PlausibilityCriterion unit) {

  }

  @Override
  public void notifyChange(CriteriaManager unit) {

  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }
}
