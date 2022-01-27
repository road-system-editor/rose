package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * Criterion handles are the entries in the {@link CriteriaOverviewPanel} that each represent
 * one plausibility criterion.
 */
class CriterionHandle extends FxmlContainer
    implements UnitObserver<PlausibilityCriterion> { // TODO maybe as a listcell implementation?
  private PlausibilityController controller;
  private PlausibilityCriterion criterion;
  private boolean selected = false;

  @FXML
  private Label label;
  @FXML
  private Button deleteButton;

  /**
   * Creates a new criterion handler.
   *
   * @param controller     the controller that links to model
   * @param criterion      the criteria to be handled
   * @param selectListener will be called when this handle is clicked
   */
  public CriterionHandle(PlausibilityController controller, PlausibilityCriterion criterion,
                         Consumer<PlausibilityCriterion> selectListener) {
    super("CriterionHandle.fxml");
    this.controller = controller;
    this.criterion = criterion;
    this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> selectListener.accept(criterion));
    this.label.setText(criterion.getName());
    this.deleteButton.setText("delete");
  }

  /**
   * selected must be true if the criteria is selected and false otherwise.
   *
   * @return true if the criteria is selected and false otherwise.
   */
  public boolean isSelected() {
    return this.selected;
  }

  /**
   * returns true if the criteria is selected and false otherwise.
   *
   * @param selected true if the criteria is selected and false otherwise.
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  @Override
  public void notifyChange(PlausibilityCriterion unit) {

  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
