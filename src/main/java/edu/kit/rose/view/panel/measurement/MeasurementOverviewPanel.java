package edu.kit.rose.view.panel.measurement;

import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.measurements.MeasurementType;
import edu.kit.rose.view.commons.FXMLContainer;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class MeasurementOverviewPanel extends FXMLContainer {// also uses ScrollPane
    private MeasurementController controller;
    private RoadSystem roadSystem;
    private MeasurementType type;

    @FXML
    private TableView table;

    /**
     * Creates a new measurement overview panel for the given measurement type.
     *
     * @param translator
     * @param type
     */
    public MeasurementOverviewPanel(LocalizedTextProvider translator, MeasurementController controller, RoadSystem roadSystem, MeasurementType type) {
        super("measurement_overview_panel.fxml");
        setTranslator(translator);
        this.controller = controller;
        this.roadSystem = roadSystem;
        this.type = type;
    }

    @Override
    protected void updateTranslatableStrings(Language lang) {

    }
}
