package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.DualSetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.view.commons.FXMLContainer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Collection;

/**
 * The hierarchy panel shows the hierarchical order of the elements contained in the road system.
 */
public class HierarchyPanel extends FXMLContainer implements DualSetObserver<Element, Connection, RoadSystem> { // also uses ScrollPane, VBox
    private HierarchyController controller;
    private RoadSystem roadSystem;

    @FXML
    private Button createGroupButton;
    private Collection<? extends ElementView<? extends Element>> rootElementViews;

    /**
     * Creates an empty hierarchy view.
     * Requires {@link #setTranslator(LocalizedTextProvider)} + {@link #setController(HierarchyController)} + {@link #setRoadSystem(RoadSystem)}
     */
    public HierarchyPanel() {
        super("hierarchy_panel.fxml");
    }

    /**
     * Sets the controller that handles interactions with this panel.
     * @param controller
     */
    public void setController(HierarchyController controller) {
        this.controller = controller;
    }

    /**
     * Sets the road system that this hierarchy panel should display.
     * @param roadSystem
     */
    public void setRoadSystem(RoadSystem roadSystem) {
        this.roadSystem = roadSystem;
    }

    @Override
    public void notifyAdditionSecond(Connection unit) {

    }

    @Override
    public void notifyRemovalSecond(Connection unit) {

    }

    @Override
    public void notifyAddition(Element unit) {

    }

    @Override
    public void notifyRemoval(Element unit) {

    }

    @Override
    public void notifyChange(RoadSystem unit) {

    }

    @Override
    protected void updateTranslatableStrings(Language lang) {

    }
}