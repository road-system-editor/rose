package edu.kit.rose.view;

import java.nio.file.Path;

import edu.kit.rose.controller.ControllerFactory;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.navigation.WindowType;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.view.window.CriteriaWindow;
import edu.kit.rose.view.window.MainWindow;
import edu.kit.rose.view.window.MeasurementsWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class is the entry point to the ROSE application.
 * It is responsible for launching the JavaFX GUI framework with the window
 * classes from the {@link edu.kit.rose.view.window} sub-package, as well as
 * initializing and connecting the {@link edu.kit.rose.controller} and
 * {@link edu.kit.rose.model} packages.
 */
public class RoseApplication extends Application implements Navigator { // also uses edu.kit.rose.model.ModelFactory
    /**
     * Contains the main window instance of the application.
     */
    private MainWindow mainWindow;
    /**
     * Contains the criteria window instance, if it is currently shown.
     */
    private CriteriaWindow criteriaWindow;
    /**
     * Contains the measurements window instance, if it is currently shown.
     */
    private MeasurementsWindow measurementsWindow;
    /**
     * Factory to be used for creating a consistent set of MVC controllers.
     */
    private ControllerFactory controllers;
    /**
     * The project that is currently opened in the application or an empty project.
     */
    private Project project;
    /**
     * Settings and state of the ROSE application.
     */
    private ApplicationDataSystem applicationData;
    /**
     * Used to fetch text contents in the configured language.
     * Uses {@link edu.kit.rose.infrastructure.language.RoseLocalizedTextProvider}
     */
    private LocalizedTextProvider translator;

    /**
     * Instantiates a new RoseApplication object.
     * This constructor is used by the JavaFX framework and should never be called manually.
     */
    public RoseApplication() {

    }

    /**
     * Entry point template method for the JavaFX application,
     * <a href=https://openjfx.io/javadoc/17/javafx.graphics/javafx/application/Application.html#start(javafx.stage.Stage)}>inherited from Application</a>.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    /**
     * This is the entry point method that will be run when this application is started.
     * @param args an array of command line arguments provided by the caller.
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void showWindow(WindowType windowType) {
        return;
    }

    @Override
    public Path showFileDialog() {
        return null;
    }
}
