package edu.kit.rose.view.panel.criterion;

import com.google.inject.Inject;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

/**
 * The criteria overview panel allows the user to view the criteria.
 */
public class CriteriaOverviewPanel extends FxmlContainer
    implements SetObserver<PlausibilityCriterion, CriteriaManager> {
  @Inject
  private PlausibilityController controller;
  @Inject
  private ApplicationDataSystem applicationDataSystem;
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
   */
  public CriteriaOverviewPanel() {
    super("CriteriaOverviewPanel.fxml");
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

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
