package edu.kit.rose.view;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.kit.rose.controller.ControllerFactory;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.controller.attribute.AttributeController;
import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.controller.measurement.MeasurementController;
import edu.kit.rose.controller.navigation.ErrorType;
import edu.kit.rose.controller.navigation.FileDialogType;
import edu.kit.rose.controller.navigation.FileFormat;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.navigation.WindowType;
import edu.kit.rose.controller.plausibility.PlausibilityController;
import edu.kit.rose.controller.project.ProjectController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.infrastructure.language.RoseLocalizedTextProvider;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.Project;
import edu.kit.rose.view.window.CriteriaWindow;
import edu.kit.rose.view.window.MainWindow;
import edu.kit.rose.view.window.MeasurementsWindow;
import edu.kit.rose.view.window.RoseWindow;
import edu.kit.rose.view.window.ShortCutHelpWindow;
import edu.kit.rose.view.window.WindowState;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the entry point to the ROSE application.
 * It is responsible for launching the JavaFX GUI framework with the window
 * classes from the {@link edu.kit.rose.view.window} sub-package, as well as
 * initializing and connecting the {@link edu.kit.rose.controller} and
 * {@link edu.kit.rose.model} packages.
 */
public class RoseApplication extends Application implements Navigator {
  private static final Logger LOG = LoggerFactory.getLogger(RoseApplication.class);
  private static final Path CONFIG_PATH = Path.of("./rose-config.json");

  private static final String ROSE_EXTENSION_FILTER_NAME = "ROSE";
  private static final String SUMO_EXTENSION_FILTER_NAME = "SUMO";
  private static final String YAML_EXTENSION_FILTER_NAME = "YAML";
  private static final String CRITERIA_EXTENSION_FILTER_NAME = "CRITERIA";

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
   * Contains information on available shortcuts.
   */
  private ShortCutHelpWindow shortCutHelpWindow;

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
   */
  private RoseLocalizedTextProvider translator;

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
    this.translator = new RoseLocalizedTextProvider();
    this.injector = Guice.createInjector(new RoseModule(this, this.translator));

    this.mainWindow = new MainWindow(primaryStage, injector);
    this.mainWindow.show();
  }

  @Override
  public void showWindow(WindowType windowType) {
    switch (windowType) {
      case CRITERION -> {
        if (canNotBeShown(this.criteriaWindow)) {
          this.criteriaWindow = new CriteriaWindow(this.injector);
        }
        this.criteriaWindow.show();
      }
      case ROADSYSTEM, ATTRIBUTE, MEASUREMENT_EDITOR -> this.mainWindow.show();
      case MEASUREMENT_OVERVIEW -> {
        if (canNotBeShown(measurementsWindow)) {
          measurementsWindow = new MeasurementsWindow(this.injector);
        }
        measurementsWindow.show();
      }
      case HELP -> {
        if (canNotBeShown(shortCutHelpWindow)) {
          shortCutHelpWindow = new ShortCutHelpWindow(this.injector);
        }
        shortCutHelpWindow.show();
      }

      default -> throw new RuntimeException("RoseApplication can't show window type " + windowType);
    }
  }

  private static boolean canNotBeShown(RoseWindow window) {
    return window == null || (window.getState() != WindowState.INITIALIZED
        && window.getState() != WindowState.VISIBLE);
  }

  @Override
  public Path showFileDialog(FileDialogType option, FileFormat format) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(getExtensionFilter(format));

    File file;
    switch (option) {
      case LOAD_FILE -> {
        fileChooser.setTitle(translator.getLocalizedText("fileChooser.loadTitle"));
        file = fileChooser.showOpenDialog(null);
      }
      case SAVE_FILE -> {
        fileChooser.setTitle(translator.getLocalizedText("fileChooser.saveTitle"));
        file = fileChooser.showSaveDialog(null);
      }
      default -> throw new IllegalStateException(format.toString());
    }
    return file == null ? null : file.toPath();
  }

  @Override
  public void showErrorDialog(ErrorType errorType) {
    Alert alertDialog = new Alert(Alert.AlertType.ERROR);

    switch (errorType) {
      case SAVE_ERROR -> alertDialog.setContentText(
          translator.getLocalizedText("view.roseapplication.error.save"));
      case LOAD_ERROR -> alertDialog.setContentText(
          translator.getLocalizedText("view.roseapplication.error.load"));
      case PROJECT_IMPORT_ERROR -> alertDialog.setContentText(
          translator.getLocalizedText("view.roseapplication.error.project.import"));
      case PROJECT_EXPORT_ERROR -> alertDialog.setContentText(
          translator.getLocalizedText("view.roseapplication.error.project.export"));
      case CRITERIA_IMPORT_ERROR -> alertDialog.setContentText(
          translator.getLocalizedText("view.roseapplication.error.criteria.import"));
      case CRITERIA_EXPORT_ERROR -> alertDialog.setContentText(
          translator.getLocalizedText("view.roseapplication.error.criteria.export"));
      default -> throw new IllegalStateException(errorType.toString());
    }

    alertDialog.show();
  }

  private FileChooser.ExtensionFilter getExtensionFilter(FileFormat format) {
    return switch (format) {
      case ROSE -> new FileChooser.ExtensionFilter(ROSE_EXTENSION_FILTER_NAME,
          convertBoxToList(format.getFileExtensions()));
      case SUMO -> new FileChooser.ExtensionFilter(SUMO_EXTENSION_FILTER_NAME,
          convertBoxToList(format.getFileExtensions()));
      case YAML -> new FileChooser.ExtensionFilter(YAML_EXTENSION_FILTER_NAME,
          convertBoxToList(format.getFileExtensions()));
      case CRITERIA -> new FileChooser.ExtensionFilter(CRITERIA_EXTENSION_FILTER_NAME,
          convertBoxToList(format.getFileExtensions()));
    };
  }

  private <T> List<T> convertBoxToList(Box<T> box) {
    return box.stream().toList();
  }

  /**
   * This is the entry point method that will be run when this application is started.
   *
   * @param args an array of command line arguments provided by the caller.
   */
  public static void main(String[] args) {
    Thread.setDefaultUncaughtExceptionHandler(RoseApplication::logUncaughtException);
    LOG.info("Launching ROSE...");
    launch(args);
  }

  private static void logUncaughtException(Thread thread, Throwable exception) {
    String message = String.format("Uncaught exception in thread %s", thread);
    LOG.error(message, exception);
  }

  /**
   * Responsible for binding interfaces and classes in the injector.
   */
  private class RoseModule extends AbstractModule {
    private final Navigator navigator;
    private final RoseLocalizedTextProvider provider;

    private RoseModule(Navigator navigator, RoseLocalizedTextProvider provider) {
      this.navigator = Objects.requireNonNull(navigator);
      this.provider = Objects.requireNonNull(provider);
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
