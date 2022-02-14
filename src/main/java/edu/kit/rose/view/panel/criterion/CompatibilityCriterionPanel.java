package edu.kit.rose.view.panel.criterion;

import com.google.inject.Injector;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import edu.kit.rose.view.commons.FxmlContainer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * The compatibility criterion panel allows the user to configure a given compatibility criterion.
 */
class CompatibilityCriterionPanel
    extends CriterionPanel<CompatibilityCriterion> { // also uses VBox and ScrollPane
  private static final String VALUE_REGEX = "[0-9]{1,13}(\\.[0-9]+)?";
  private static final String VALID_VALUE_STYLE = "-fx-text-fill: black;";
  private static final String INVALID_VALUE_STYLE = "-fx-text-fill: red;";

  private final DecimalFormat valueFormat;

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
  private VBox criterionLayout;
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

    this.valueFormat = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    this.valueFormat.setMaximumFractionDigits(9);
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
      Platform.runLater(() -> {
        var caretPosition = this.nameField.getCaretPosition();
        getController().setCompatibilityCriterionName(getCriterion(), newValue);
        this.nameField.positionCaret(caretPosition);
      });
    }
  }

  private void onAttributeChange(ObservableValue<? extends AttributeType> observable,
                                 AttributeType oldValue, AttributeType newValue) {
    if (oldValue != newValue) {
      Platform.runLater(() -> {
        getController().setCompatibilityCriterionAttributeType(getCriterion(), newValue);

        clearValidationSelector();

        this.validationSelector.getItems().removeIf(type ->
            !getCriterion().getCompatibleOperatorTypes().contains(type));
        for (var validationType : getCriterion().getCompatibleOperatorTypes()) {
          if (!validationSelector.getItems().contains(validationType)) {
            this.validationSelector.getItems().add(validationType);
          }
        }
      });
    }
  }

  private void clearValidationSelector() {
    SortedBox<ValidationType> compatibleTypes = getCriterion().getCompatibleOperatorTypes();
    ValidationType selectedType = validationSelector.getSelectionModel().getSelectedItem();

    if (!compatibleTypes.contains(selectedType)) {
      this.validationSelector.getSelectionModel().clearSelection();
      setDiscrepancyFieldEnabled(false);
    }
  }

  private void onValidationChange(ObservableValue<? extends ValidationType> observable,
                                 ValidationType oldValue, ValidationType newValue) {
    if (oldValue != newValue) {
      Platform.runLater(() -> {
        getController().setCompatibilityCriterionValidationType(getCriterion(), newValue);

        if (newValue != null) {
          setDiscrepancyFieldEnabled(newValue.hasDiscrepancy());
        }
      });
    }
  }

  private void setDiscrepancyFieldEnabled(boolean enabled) {
    if (!this.criterionLayout.getChildren().contains(this.valueField) && enabled) {
      this.criterionLayout.getChildren().add(this.valueField);
    } else if (this.criterionLayout.getChildren().contains(this.valueField) && !enabled) {
      this.criterionLayout.getChildren().remove(this.valueField);
    }
  }

  private void onValueChange(ObservableValue<? extends String> observable, String oldValue,
                             String newValue) {
    if (!newValue.matches(VALUE_REGEX)) {
      this.valueField.setStyle(INVALID_VALUE_STYLE);
      return;
    }

    double value;
    try {
      value = Double.parseDouble(newValue);
    } catch (NumberFormatException e) {
      this.valueField.setStyle(INVALID_VALUE_STYLE);
      return;
    }

    if (Double.isInfinite(value) || Double.isNaN(value)) {
      this.valueField.setStyle(INVALID_VALUE_STYLE);
      return;
    }

    this.valueField.setStyle(VALID_VALUE_STYLE);

    // only trigger an update if the new value is not EXACTLY the old value
    if (this.getCriterion().getLegalDiscrepancy() != value) {
      Platform.runLater(() -> {
        var caretPosition = this.valueField.getCaretPosition();
        getController().setCompatibilityCriterionLegalDiscrepancy(getCriterion(), value);
        this.valueField.positionCaret(caretPosition);
      });
    }
  }

  @Override
  public void notifyAddition(SegmentType unit) {
    // ApplicableSegmentSelector takes care of this
  }

  @Override
  public void notifyRemoval(SegmentType unit) {
    // ApplicableSegmentSelector takes care of this
  }

  @Override
  public void notifyChange(PlausibilityCriterion unit) {
    this.nameField.setText(getCriterion().getName());
    this.attributeSelector.getSelectionModel().select(getCriterion().getAttributeType());
    this.validationSelector.getSelectionModel().select(getCriterion().getOperatorType());
    this.valueField.setText(valueFormat.format(getCriterion().getLegalDiscrepancy()));
    this.valueField.setStyle(VALID_VALUE_STYLE);
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {
    this.nameLabel.setText(getTranslator().getLocalizedText(
        "view.panel.criterion.criterionPanel.name"));
    this.applicableSegmentsLabel.setText(getTranslator().getLocalizedText(
        "view.panel.criterion.criterionPanel.applicableSegments"));
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
