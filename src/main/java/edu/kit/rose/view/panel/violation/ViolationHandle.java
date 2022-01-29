package edu.kit.rose.view.panel.violation;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.panel.problem.MessageFactory;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;



/**
 * A {@link ViolationHandle} informs the user about a violation against a
 * {@link edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion}.
 */
public class ViolationHandle extends FxmlContainer implements UnitObserver<Violation> {
  /**
   * The controller to use for handling navigation to the affected segments in the road system view.
   */
  private final PlausibilityController controller;
  /**
   * The violation to describe.
   */
  private final Violation violation;

  /**
   * Always-visible text with a brief description of the violation.
   */
  @FXML
  private Label criterion;
  @FXML
  private Label segments;

  /**
   * A more detailed description of the violation
   * that is only visible when this component is hovered.
   */
  @FXML
  private Tooltip extendedMessage;
  private final MessageFactory messageFactory;

  /**
   * Creates a new {@link ViolationHandle} for a given {@code violation}.
   *
   * @param controller the {@link PlausibilityController} instance
   * @param violation the {@link Violation} the {@link ViolationHandle} belongs to
   */
  public ViolationHandle(PlausibilityController controller,
                         MessageFactory messageFactory, Violation violation) {
    super("ViolationHandle.fxml");
    this.controller = controller;
    this.messageFactory = messageFactory;
    this.violation = violation;

    segments.setText(messageFactory.generateShortDescription(violation));
    criterion.setText(violation.violatedCriterion().getName());
    extendedMessage.setText(messageFactory.generateDetailedDescription(violation));

    Tooltip.install(criterion, extendedMessage);
  }

  @Override
  public void notifyChange(Violation unit) {
    updateTranslatableStrings(null);
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {
    segments.setText(messageFactory.generateShortDescription(violation));
    criterion.setText(violation.violatedCriterion().getName());
    extendedMessage.setText(messageFactory.generateDetailedDescription(violation));
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }


  Violation getViolation() {
    return this.violation;
  }
}
