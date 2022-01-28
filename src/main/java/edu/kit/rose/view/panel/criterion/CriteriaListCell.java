package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import java.util.function.Consumer;
import javafx.scene.control.ListCell;

/**
 * A {@link CriteriaListCell} is a container that holds
 * a {@link javafx.scene.control.ListView} item layout.
 */
public class CriteriaListCell extends ListCell<PlausibilityCriterion> {

  private final PlausibilityController controller;
  private final Consumer<PlausibilityCriterion> selectListener;

  /**
   * Creates a new instance of the {@link CriteriaListCell}.
   *
   * @param controller     the plausibility controller
   * @param selectListener the selection listener
   */
  public CriteriaListCell(PlausibilityController controller,
                          Consumer<PlausibilityCriterion> selectListener) {
    this.controller = controller;
    this.selectListener = selectListener;
  }

  @Override
  public void updateItem(PlausibilityCriterion criterion, boolean empty) {
    super.updateItem(criterion, empty);

    if (criterion == null || empty) {
      setText(null);
      setGraphic(null);
    } else {
      CriterionHandle handle =
          new CriterionHandle(this.controller, criterion, this.selectListener);
      setGraphic(handle);
    }
  }
}
