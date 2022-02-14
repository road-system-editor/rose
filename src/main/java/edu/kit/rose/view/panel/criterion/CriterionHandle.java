package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * Criterion handles are the entries in the {@link CriteriaOverviewPanel} that each represent
 * one plausibility criterion.
 */
class CriterionHandle extends FxmlContainer
    implements SetObserver<SegmentType, PlausibilityCriterion> {

  @FXML
  private Label label;
  @FXML
  private Button deleteButton;

  /**
   * Creates a new criterion handler.
   *
   * @param controller     the controller that links to model
   * @param criterion      the criteria to be handled
   */
  public CriterionHandle(PlausibilityController controller, PlausibilityCriterion criterion) {
    super("CriterionHandle.fxml");

    this.label.setText(criterion.getName());

    //CompatibilityCriteria are the only ones that can be deleted.
    if (criterion.getType() == PlausibilityCriterionType.COMPATIBILITY) {
      this.deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
          event -> controller.deleteCompatibilityCriterion((CompatibilityCriterion) criterion));
    }

    criterion.addSubscriber(this);
  }

  @Override
  public void notifyChange(PlausibilityCriterion unit) {
    if (!unit.getName().equals(this.label.getText())) {
      this.label.setText(unit.getName());
    }
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {
    this.deleteButton.setText(getTranslator()
        .getLocalizedText("view.panel.criterion.criteriaHandle.delete"));

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }

  @Override
  public void notifyAddition(SegmentType unit) {
    // ignore, segment types are not shown in the handle
  }

  @Override
  public void notifyRemoval(SegmentType unit) {
    // ignore, segment types are not shown in the handle
  }
}
