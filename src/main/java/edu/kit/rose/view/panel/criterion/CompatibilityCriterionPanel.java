package edu.kit.rose.view.panel.criterion;

import com.google.inject.Injector;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * The compatibility criterion panel allows the user to configure a given compatibility criterion.
 */
class CompatibilityCriterionPanel
    extends CriterionPanel<CompatibilityCriterion> { // also uses VBox and ScrollPane
  @FXML
  private Label nameLabel;
  @FXML
  private TextField nameField;
  @FXML
  private Label applicableSegmentsLabel;
  @FXML
  private ApplicableSegmentsSelector applicableSegmentsSelector;
  @FXML
  private Label criterionLabel;
  @FXML
  private ComboBox<AttributeType> attributeSelector;
  @FXML
  private ComboBox<ValidationType> validationSelector;
  @FXML
  private TextField valueField;

  /**
   * Creates a new CompatibilityCriterionPanel.
   *
   * @param criterion compatibility criteria that will be configured.
   */
  public CompatibilityCriterionPanel(CompatibilityCriterion criterion) {
    super("CompatibilityCriterionPanel.fxml", criterion);
  }

  @Override
  public void init(Injector injector) {
    super.init(injector);

    this.nameField.textProperty().addListener(this::onNameChange);

    this.applicableSegmentsSelector.setCriterion(getCriterion());

    this.attributeSelector.setCellFactory(this::createAttributeSelectorCell);
    this.attributeSelector.setButtonCell(this.createAttributeSelectorCell(null));
    this.attributeSelector.getItems().addAll(AttributeType.values());
    this.attributeSelector.getSelectionModel().selectedItemProperty()
        .addListener(this::onAttributeChange);

    this.validationSelector.setCellFactory(this::createValidationSelectorCell);
    this.validationSelector.setButtonCell(this.createValidationSelectorCell(null));
    this.validationSelector.getItems().addAll(ValidationType.values());
    this.validationSelector.getSelectionModel().selectedItemProperty()
        .addListener(this::onValidationChange);

    this.valueField.textProperty().addListener(this::onValueChange);

    this.notifyChange(getCriterion());
  }

  private void onNameChange(ObservableValue<? extends String> observable, String oldValue,
                            String newValue) {
    if (!Objects.equals(oldValue, newValue)) {
      getController().setCompatibilityCriterionName(getCriterion(), newValue);
    }
  }

  private void onAttributeChange(ObservableValue<? extends AttributeType> observable,
                                 AttributeType oldValue, AttributeType newValue) {
    if (oldValue != newValue) {
      getController().setCompatibilityCriterionAttributeType(getCriterion(), newValue);
    }
  }

  private void onValidationChange(ObservableValue<? extends ValidationType> observable,
                                 ValidationType oldValue, ValidationType newValue) {
    if (oldValue != newValue) {
      getController().setCompatibilityCriterionValidationType(getCriterion(), newValue);
    }
  }

  private void onValueChange(ObservableValue<? extends String> observable, String oldValue,
                             String newValue) {
    if (!Objects.equals(oldValue, newValue)) {
      double value;
      try {
        value = Double.parseDouble(newValue);
      } catch (NumberFormatException e) {
        this.valueField.setText(oldValue);
        return;
      }

      getController().setCompatibilityCriterionLegalDiscrepancy(getCriterion(), value);
    }
  }

  @Override
  public void notifyAddition(SegmentType unit) {

  }

  @Override
  public void notifyRemoval(SegmentType unit) {

  }


  @Override
  public void notifyChange(PlausibilityCriterion unit) {
    this.nameField.setText(getCriterion().getName());
    this.attributeSelector.getSelectionModel().select(getCriterion().getAttributeType());
    this.validationSelector.getSelectionModel().select(getCriterion().getOperatorType());
    this.valueField.setText(String.valueOf(getCriterion().getLegalDiscrepancy()));
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
    this.nameLabel.setText(getTranslator().getLocalizedText(
        "view.panel.criterion.criterionPanel.name"));
    this.criterionLabel.setText(getTranslator().getLocalizedText(
        "view.panel.criterion.criterionPanel.criterion"));
    this.attributeSelector.setPromptText(getTranslator().getLocalizedText(
        "view.panel.criterion.compatibilityCriterionPanel.attributePrompt"));
    this.validationSelector.setPromptText(getTranslator().getLocalizedText(
        "view.panel.criterion.compatibilityCriterionPanel.validationPrompt"));
    this.valueField.setPromptText(getTranslator().getLocalizedText(
        "view.panel.criterion.compatibilityCriterionPanel.valuePrompt"));
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return List.of(this.applicableSegmentsSelector);
  }

  private ListCell<AttributeType> createAttributeSelectorCell(ListView<AttributeType> ignored) {
    return new ListCell<>() {
      @Override
      protected void updateItem(AttributeType item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
          setText(EnumLocalizationUtility.localizeAttributeTypeTitle(getTranslator(), item));
        }
      }
    };
  }

  private ListCell<ValidationType> createValidationSelectorCell(ListView<ValidationType> ignored) {
    return new ListCell<>() {
      @Override
      protected void updateItem(ValidationType item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
          setText(EnumLocalizationUtility.localizeValidationTypeTitle(getTranslator(), item));
        }
      }
    };
  }
}
