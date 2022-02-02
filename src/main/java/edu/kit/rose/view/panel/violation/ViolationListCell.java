package edu.kit.rose.view.panel.violation;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.view.panel.criterion.CriterionListCell;
import edu.kit.rose.view.panel.problem.MessageFactory;
import javafx.scene.control.ListCell;

/**
 * A {@link ViolationListCell} is a container that holds
 * a {@link javafx.scene.control.ListView} item layout.
 */
public class ViolationListCell extends ListCell<Violation> {

  private final PlausibilityController controller;
  private final MessageFactory messageFactory;

  /**
   * Creates a new instance of the {@link ViolationListCell}.
   *
   * @param controller     the plausibility controller
   * @param messageFactory the message Factory
   */
  public ViolationListCell(PlausibilityController controller, MessageFactory messageFactory) {
    this.controller = controller;
    this.messageFactory = messageFactory;
  }

  @Override
  public void updateItem(Violation violation, boolean empty) {
    super.updateItem(violation, empty);

    if (violation == null || empty) {
      setText(null);
      setGraphic(null);
    } else {
      ViolationHandle handle =
          new ViolationHandle(this.controller, this.messageFactory, violation);
      setGraphic(handle);
    }
  }
}
