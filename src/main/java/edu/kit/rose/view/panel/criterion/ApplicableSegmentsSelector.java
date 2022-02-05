package edu.kit.rose.view.panel.criterion;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * The applicable segments' selector allows the user to select which segment types a plausibility
 * criterion applies to.
 */
public class ApplicableSegmentsSelector extends FxmlContainer
    implements SetObserver<SegmentType, PlausibilityCriterion> {
  @FXML
  private ListView<SegmentType> typeSelector;
  @Inject
  private PlausibilityController controller;

  private CompatibilityCriterion criterion;

  /**
   * Creates a new ApplicableSegmentSelector. Make sure to call {@link #init(Injector)} afterwards!
   */
  public ApplicableSegmentsSelector() {
    super("ApplicableSegmentsSelector.fxml");

    this.typeSelector.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    this.typeSelector.setCellFactory(this::createSegmentTypeSelectorCell);
    this.typeSelector.getItems().addAll(SegmentType.values());
    this.typeSelector.getSelectionModel().getSelectedItems().addListener(this::onSelectionChange);
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
    /*
    this.selectAllCheckBox.setText(getTranslator().getLocalizedText(
        "view.panel.criterion.criterionPanel.selectAllSegmentTypes"));
    */
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
