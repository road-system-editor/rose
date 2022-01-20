package edu.kit.rose.view.window;

import com.google.inject.Injector;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import javafx.stage.Stage;

/**
 * A ROSE window manages a JavaFX window.
 * ROSE windows usually mount JavaFX panels from the {@link edu.kit.rose.view.panel} package into their stage.
 */
public abstract class RoseWindow {
  /**
   * The JavaFX top-level container that belongs to the JavaFX window this ROSE window is managing.
   */
  private Stage stage;
  /**
   * Data source for translated strings.
   */
  private LocalizedTextProvider translator;
  /**
   * Reference to the navigator of the applciation.
   */
  private Navigator navigator;

  private WindowState state = WindowState.INITIALIZED;

  private Injector injector;

  /**
   * Creates a new window for the ROSE application.
   *
   * @param translator
   * @param injector
   */
  protected RoseWindow(LocalizedTextProvider translator, Injector injector) {
    this.translator = translator;
  }

  /**
   * Creates a new window for the ROSE application, using the given {@code stage}.
   * This constructor should only be if this window should be set up in the primary stage.
   *
   * @param translator
   * @param stage
   * @param injector
   */
  protected RoseWindow(LocalizedTextProvider translator, Stage stage, Injector injector) {
    this(translator, injector);
    this.stage = stage;
    stage.setOnCloseRequest(event -> {
      event.consume(); // might need to check state
      close();
    });
  }

  /**
   * Makes this window visible and puts it into focus.
   */
  public void show() {

  }

  /**
   * Closes this window; is also called when the user closes the window.
   * Closed windows can not be {@link #show()}n again.
   */
  public void close() {

  }

  /**
   * Template method that allows the implementing class to populate the {@link #stage} of this window.
   *
   * @param stage
   */
  protected abstract void configureStage(Stage stage);

  public WindowState getState() {
    return this.state;
  }

  protected LocalizedTextProvider getTranslator() {
    return translator;
  }
}
