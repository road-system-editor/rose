package edu.kit.rose.view.panel.criterion;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import edu.kit.rose.view.commons.FxmlContainer;
import edu.kit.rose.view.commons.UnmountUtility;
import java.util.Collection;
import java.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;

/**
 * The applicable segments' selector allows the user to select which segment types a plausibility
 * criterion applies to.
 */
public class ApplicableSegmentsSelector extends FxmlContainer
    implements SetObserver<SegmentType, PlausibilityCriterion> {
  private static final String CRITERION_PANEL_STYLE_SHEET = "/edu/kit/rose/view/panel/criterion"
      + "/CriterionPanel.css";
  private static final int TOOLTIP_SIZE = 200;
  private static final boolean TOOLTIP_WRAP_TEXT = true;
  @FXML
  private ListView<SegmentType> typeSelector;
  @Inject
  private PlausibilityController controller;

  private CompatibilityCriterion criterion;

  private final Tooltip tooltip;

  /**
   * Creates a new ApplicableSegmentSelector. Make sure to call {@link #init(Injector)} afterwards!
   */
  public ApplicableSegmentsSelector() {
    super("ApplicableSegmentsSelector.fxml");
    setupView();

    this.tooltip = new Tooltip();
    tooltip.setPrefWidth(TOOLTIP_SIZE);
    tooltip.setWrapText(TOOLTIP_WRAP_TEXT);

    this.typeSelector.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    this.typeSelector.setCellFactory(this::createSegmentTypeSelectorCell);
    this.typeSelector.getItems().addAll(SegmentType.values());
    this.typeSelector.getSelectionModel().getSelectedItems().addListener(this::onSelectionChange);
    this.typeSelector.setTooltip(tooltip);
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);
    updateTranslatableStrings(getTranslator().getSelectedLanguage());
  }

  private void setupView() {
    String criterionStyleSheetUrl =
        Objects.requireNonNull(getClass().getResource(CRITERION_PANEL_STYLE_SHEET))
            .toExternalForm();
    this.getStylesheets().add(criterionStyleSheetUrl);
  }

  private ListCell<SegmentType> createSegmentTypeSelectorCell(ListView<SegmentType> ignored) {
    return new ListCell<>() {
      @Override
      protected void updateItem(SegmentType item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
          setText(EnumLocalizationUtility.localizeSegmentTypeTitle(getTranslator(), item));
        }
      }
    };
  }

  /**
   * Sets the criterion whose applicable segments can be configured in this panel.
   */
  public void setCriterion(CompatibilityCriterion criterion) {
    if (this.criterion != null) {
      this.criterion.removeSubscriber(this);
    }

    this.criterion = criterion;

    if (this.criterion != null) {
      this.criterion.addSubscriber(this);
      UnmountUtility.subscribeUntilUnmount(this, this, criterion);

      this.typeSelector.getSelectionModel().clearSelection();
      for (var selected : this.criterion.getSegmentTypes()) {
        this.typeSelector.getSelectionModel().select(selected);
      }
    }
  }

  private void onSelectionChange(ListChangeListener.Change<? extends SegmentType> change) {
    change.next();

    for (var removed : change.getRemoved()) {
      this.controller.removeSegmentTypeToCompatibilityCriterion(this.criterion, removed);
    }

    for (var added : change.getAddedSubList()) {
      this.controller.addSegmentTypeToCompatibilityCriterion(this.criterion, added);
    }
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
    tooltip.setText(getTranslator().getLocalizedText("view.panel.criterion"
        + ".compatibilityCriterionPanel.segmentsSelectorExplanation"));
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }

  @Override
  public void notifyAddition(SegmentType unit) {
    this.typeSelector.getSelectionModel().select(unit);
  }

  @Override
  public void notifyRemoval(SegmentType unit) {
    int index = this.typeSelector.getItems().indexOf(unit);
    this.typeSelector.getSelectionModel().clearSelection(index);
  }

  @Override
  public void notifyChange(PlausibilityCriterion unit) {

  }
}
