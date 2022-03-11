package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import javafx.scene.control.ListCell;

/**
 * A {@link CriterionListCell} is a container that holds
 * a {@link javafx.scene.control.ListView} item layout.
 */
public class CriterionListCell extends ListCell<PlausibilityCriterion> {
  private final PlausibilityController controller;

  /**
   * Creates a new instance of the {@link CriterionListCell}.
   *
   * @param controller     the plausibility controller
   */
  public CriterionListCell(PlausibilityController controller) {
    this.controller = controller;
  }

  @Override
  public void updateItem(PlausibilityCriterion criterion, boolean empty) {
    super.updateItem(criterion, empty);

    if (criterion == null || empty) {
      setText(null);
      setGraphic(null);
    } else {
      CriterionHandle handle = new CriterionHandle(this.controller, criterion);
      setGraphic(handle);
    }
  }
}
