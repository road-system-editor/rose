package edu.kit.rose.view.panel.criterion;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.validation.OperatorType;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * The compatibility criterion panel allows the user to configure a given compatibility criterion.
 */
class CompatibilityCriterionPanel extends CriterionPanel<CompatibilityCriterion> { // also uses VBox and ScrollPane
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
    private ComboBox<OperatorType> operatorSelector;
    @FXML
    private TextField valueField;

    public CompatibilityCriterionPanel(LocalizedTextProvider translator, PlausibilityController controller, CompatibilityCriterion criterion) {
        super(translator, "compatibility_criterion_panel.fxml", controller, criterion);
    }

    @Override
    public void notifyAddition(SegmentType unit) {

    }

    @Override
    public void notifyRemoval(SegmentType unit) {

    }


    @Override
    public void notifyChange(PlausibilityCriterion unit) {

    }

    @Override
    protected void updateTranslatableStrings(Language lang) {

    }
}