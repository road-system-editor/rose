package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.FXMLContainer;
import edu.kit.rose.view.commons.UnmountUtility;

/**
 * The criteria panel allows the user to set up the criteria.
 *
 * @param <T> the type of PlausibilityCriterion this CriterionPanel is for.
 */
public abstract class CriterionPanel<T extends PlausibilityCriterion> extends FXMLContainer
    implements SetObserver<SegmentType, PlausibilityCriterion> {
  private PlausibilityController controller;
  private T criterion;

  /**
   * Creates a new CriterionPanel.
   *
   * @param translator       data that will be used for {@link FXMLContainer} constructor
   * @param fxmlResourceName the name of fxml file that models this panel
   * @param criterion        the criteria to be set up
   */
  protected CriterionPanel(LocalizedTextProvider translator, String fxmlResourceName,
                           PlausibilityController controller, T criterion) {
    super(fxmlResourceName);
    setTranslator(translator);
    this.controller = controller;
    this.criterion = criterion;
    this.criterion.addSubscriber(this);

    UnmountUtility.runOnUnmount(this, () -> this.criterion.removeSubscriber(this));
  }

  /**
   * Constructs a criterion panel for a given criterion, if it is configurable.
   * For non-configurable criteria, {@code null} is returned.
   *
   * @param translator the translator that is to be used.
   * @param criterion the criterion this criterion panel is for.
   * @return the newly constructed criterion panel.
   */
  public static CriterionPanel<? extends PlausibilityCriterion> forCriterion(
      LocalizedTextProvider translator, PlausibilityController controller,
      PlausibilityCriterion criterion) {
    switch (criterion.getType()) {
      case COMPATIBILITY:
        return new CompatibilityCriterionPanel(translator, controller,
            (CompatibilityCriterion) criterion);
      default:
        return null; //TODO?
    }
  }

  /**
   * Returns the criterion that is being set up.
   *
   * @return the criteria that is being set up
   */
  public T getCriterion() {
    return this.criterion;
  }
}
