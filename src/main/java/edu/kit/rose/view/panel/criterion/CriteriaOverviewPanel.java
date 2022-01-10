package edu.kit.rose.view.panel.criterion;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.view.commons.FXMLContainer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

/**
 * The criteria overview panel allows the user to view the criteria
 */
public class CriteriaOverviewPanel extends FXMLContainer implements SetObserver<PlausibilityCriterion, CriteriaManager> {
    private PlausibilityController controller;
    private CriteriaManager manager;
    private CriterionHandle selected;
    private Consumer<PlausibilityCriterion> selectionListener;

    @FXML
    private Button exportButton;
    @FXML
    private Button importButton;
    @FXML
    private Button deleteAllButton;
    @FXML
    private Button newButton;
    @FXML
    private ScrollPane list;
    private Collection<CriterionHandle> handles;

    /**
     * {@link #setController(PlausibilityController)} + {@link #setManager(CriteriaManager)} + {@link #setTranslator(LocalizedTextProvider)} + {@link #setSelectionListener(Consumer)} needs to be called!
     */
    public CriteriaOverviewPanel() {
        super("criteria_overview_panel.fxml");
    }

    public void setController(PlausibilityController controller) {
        this.controller = controller;
    }

    public void setManager(CriteriaManager manager) {
        this.manager = manager;
    }

    public void setSelectionListener(Consumer<PlausibilityCriterion> selectionListener) {
        this.selectionListener = selectionListener;
    }

    @Override
    public void notifyAddition(PlausibilityCriterion unit) {

    }

    @Override
    public void notifyRemoval(PlausibilityCriterion unit) {

    }

    @Override
    public void notifyChange(CriteriaManager unit) {

    }

    @Override
    protected void updateTranslatableStrings(Language lang) {

    }
}
