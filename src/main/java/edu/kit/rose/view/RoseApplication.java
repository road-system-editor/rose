package edu.kit.rose.view;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.kit.rose.controller.ControllerFactory;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.controller.navigation.FileDialogOption;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.navigation.WindowType;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.controller.project.ProjectController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.infrastructure.language.RoseLocalizedTextProvider;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.ExportFormat;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.Project;
import edu.kit.rose.view.window.CriteriaWindow;
import edu.kit.rose.view.window.MainWindow;
import edu.kit.rose.view.window.MeasurementsWindow;
import edu.kit.rose.view.window.RoseWindow;
import edu.kit.rose.view.window.WindowState;
import java.nio.file.Path;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class is the entry point to the ROSE application.
 * It is responsible for launching the JavaFX GUI framework with the window
 * classes from the {@link edu.kit.rose.view.window} sub-package, as well as
 * initializing and connecting the {@link edu.kit.rose.controller} and
 * {@link edu.kit.rose.model} packages.
 */
public class RoseApplication extends Application implements Navigator {
  private static final Path CONFIG_PATH = Path.of(""); //TODO

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
   * The guice dependency injector.
   */
  private Injector injector;

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
  public void start(Stage primaryStage) {
    this.injector = Guice.createInjector(new RoseModule(this));

    this.mainWindow = new MainWindow(primaryStage, injector);
    this.mainWindow.show();
  }

  @Override
  public void showWindow(WindowType windowType) {
    switch (windowType) {
      case CRITERION -> {
        if (!canBeShown(this.criteriaWindow)) {
          this.criteriaWindow = new CriteriaWindow(this.injector);
        }
        this.criteriaWindow.show();
      }
      case ROADSYSTEM, Attribute, MEASUREMENT_EDITOR -> this.mainWindow.show();
      case MEASUREMENT_OVERVIEW -> {
        if (!canBeShown(measurementsWindow)) {
          measurementsWindow = new MeasurementsWindow(this.injector);
        }
        measurementsWindow.show();
      }
      default -> throw new RuntimeException("RoseApplication can't show window type " + windowType);
    }
  }

  private static boolean canBeShown(RoseWindow window) {
    return window != null && (window.getState() == WindowState.INITIALIZED
        || window.getState() == WindowState.VISIBLE);
  }

  @Override
  public Path showFileDialog(FileDialogOption option, ExportFormat format) {
    var fileChooser = new FileChooser();
    // TODO differentiate between saving and opening files
    var file = fileChooser.showOpenDialog(null);
    return file == null ? null : file.toPath();
  }

  /**
   * This is the entry point method that will be run when this application is started.
   *
   * @param args an array of command line arguments provided by the caller.
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Responsible for binding interfaces and classes in the injector.
   */
  private class RoseModule extends AbstractModule {
    private final Navigator navigator;
    private final RoseLocalizedTextProvider provider;

    private RoseModule(Navigator navigator) {
      this.navigator = navigator;
      this.provider = new RoseLocalizedTextProvider();
    }

    @Override
    protected void configure() {
      bind(LocalizedTextProvider.class).toInstance(this.provider);
      bind(Navigator.class).toInstance(this.navigator);

      this.configureModel();
      this.configureControllers();
      this.configureWindows();
    }

    private void configureModel() {
      ModelFactory factory = new ModelFactory(CONFIG_PATH);

      applicationData = factory.createApplicationDataSystem();
      bind(ApplicationDataSystem.class).toInstance(applicationData);

      project = factory.createProject();
      bind(Project.class).toInstance(project);
    }

    private void configureControllers() {
      ControllerFactory factory =
          new ControllerFactory(navigator, provider, applicationData, project);

      bind(ApplicationController.class).toInstance(factory.getApplicationController());
      bind(AttributeController.class).toInstance(factory.getAttributeController());
      bind(HierarchyController.class).toInstance(factory.getHierarchyController());
      bind(MeasurementController.class).toInstance(factory.getMeasurementController());
      bind(PlausibilityController.class).toInstance(factory.getPlausibilityController());
      bind(ProjectController.class).toInstance(factory.getProjectController());
      bind(RoadSystemController.class).toInstance(factory.getRoadSystemController());
    }

    private void configureWindows() {
      bind(MainWindow.class);
      bind(CriteriaWindow.class);
      bind(MeasurementsWindow.class);
    }
  }
}
