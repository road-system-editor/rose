package edu.kit.rose.view.panel.criterion;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.FxmlContainer;

/**
 * The criteria panel allows the user to set up the criteria.
 *
 * @param <T> the type of PlausibilityCriterion this CriterionPanel is for.
 */
public abstract class CriterionPanel<T extends PlausibilityCriterion> extends FxmlContainer
    implements SetObserver<SegmentType, PlausibilityCriterion> {
  @Inject
  private PlausibilityController controller;

  private final T criterion;

  /**
   * Creates a new CriterionPanel.
   *
   * @param fxmlResourceName the name of fxml file that models this panel
   * @param criterion        the criteria to be set up
   */
  protected CriterionPanel(String fxmlResourceName, T criterion) {
    super(fxmlResourceName);
    this.criterion = criterion;
    this.criterion.addSubscriber(this);
  }

  /**
   * Constructs a criterion panel for a given criterion, if it is configurable.
   * For non-configurable criteria, {@code null} is returned.
   *
   * @param criterion the criterion this criterion panel is for.
   * @return the newly constructed criterion panel or null if there is no {@link CriterionPanel}
   *     implementation for the given plausibility criterion type.
   */
  public static CriterionPanel<? extends PlausibilityCriterion> forCriterion(Injector injector,
      PlausibilityCriterion criterion) {
    if (criterion.getType() == PlausibilityCriterionType.COMPATIBILITY) {
      var panel = new CompatibilityCriterionPanel((CompatibilityCriterion) criterion);
      panel.init(injector);
      return panel;
    }
    return null;
  }

  /**
   * Returns the criterion that is being set up.
   *
   * @return the criteria that is being set up
   */
  public T getCriterion() {
    return this.criterion;
  }

  /**
   * Returns the controller that handles criterion changes.
   */
  public PlausibilityController getController() {
    return this.controller;
  }
}
