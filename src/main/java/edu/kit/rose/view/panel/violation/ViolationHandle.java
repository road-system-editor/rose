package edu.kit.rose.view.panel.violation;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.violation.Violation;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;


/**
 * A {@link ViolationHandle} informs the user about a violation against a
 * {@link edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion}.
 */
public class ViolationHandle extends FxmlContainer implements SetObserver<SegmentType,
    PlausibilityCriterion> {
  private static final String VIOLATION_CSS_FILE = "/edu/kit/rose/view/panel/violation/Violation"
      + ".css";
  private static final int DOUBLE_CLICK = 2;

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
  private final Tooltip extendedMessage;
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

    violation.violatedCriterion().addSubscriber(this);
    setupView();

    segments.setText(messageFactory.generateShortDescription(violation));
    criterion.setText(violation.violatedCriterion().getName());
    extendedMessage = new Tooltip();
    extendedMessage.setText(messageFactory.generateDetailedDescription(violation));

    segments.setOnMouseClicked(this::handleMouseClicked);
    criterion.setOnMouseClicked(this::handleMouseClicked);

    Tooltip.install(segments, extendedMessage);
    Tooltip.install(criterion, extendedMessage);
  }

  private void setupView() {
    String violationStyleSheetUrl =
        Objects.requireNonNull(getClass().getResource(VIOLATION_CSS_FILE)).toExternalForm();
    this.getStylesheets().add(violationStyleSheetUrl);
  }

  private void handleMouseClicked(MouseEvent event) {
    if (event.getClickCount() == DOUBLE_CLICK && !event.isConsumed()) {
      event.consume();
      controller.jumpToCriterionViolation(violation);
    }
  }

  @Override
  public void notifyChange(PlausibilityCriterion unit) {
    criterion.setText(violation.violatedCriterion().getName());
  }

  @Override
  protected void updateTranslatableStrings(Language newLang) {
    segments.setText(messageFactory.generateShortDescription(violation));
    extendedMessage.setText(messageFactory.generateDetailedDescription(violation));
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }

  @Override
  public void notifyAddition(SegmentType unit) {

  }

  @Override
  public void notifyRemoval(SegmentType unit) {

  }
}
