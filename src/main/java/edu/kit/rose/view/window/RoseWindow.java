package edu.kit.rose.view.window;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * A ROSE window manages a JavaFX window.
 * ROSE windows usually mount JavaFX panels from the {@link edu.kit.rose.view.panel} package into
 * their stage.
 */
public abstract class RoseWindow {
  /**
   * The JavaFX top-level container that belongs to the JavaFX window this ROSE window is managing.
   */
  private final Stage stage;
  /**
   * Data source for translated strings.
   */
  @Inject
  private LocalizedTextProvider translator;
  /**
   * Reference to the navigator of the application.
   */
  @Inject
  private Navigator navigator;

  private WindowState state = WindowState.INITIALIZED;

  /**
   * Creates a new window for the ROSE application.
   *
   * @param injector the dependency injector.
   */
  protected RoseWindow(Injector injector) {
    this(new Stage(), injector);
  }

  /**
   * Creates a new window for the ROSE application, using the given {@code stage}.
   * This constructor should only be if this window should be set up in the primary stage.
   *
   * @param stage the primary stage of the JavaFX application.
   * @param injector the dependency injector.
   */
  protected RoseWindow(Stage stage, Injector injector) {
    this.stage = stage;
    injector.injectMembers(this);

    stage.setOnCloseRequest(event -> {
      event.consume(); // might need to check state
      close();
    });
    stage.getIcons().add(new Image(Objects.requireNonNull(RoseWindow.class.getResourceAsStream(
        "logo.png"))));

    this.configureStage(this.stage, injector);
  }

  /**
   * Makes this window visible and puts it into focus.
   * The window can not be shown if it has already been closed.
   */
  public void show() {
    if (state == WindowState.INITIALIZED || state == WindowState.VISIBLE) {
      this.stage.show();
      state = WindowState.VISIBLE;
    }
  }

  /**
   * Closes this window; is also called when the user closes the window.
   * Closed windows can not be {@link #show()}n again.
   */
  public void close() {
    if (state == WindowState.VISIBLE) {
      stage.close();
      state = WindowState.CLOSED;
    }
  }

  /**
   * Template method that allows the implementing class to populate the {@link #stage} of this
   * window.
   *
   * @param stage the stage of this window.
   * @param injector the dependency injector.
   */
  protected abstract void configureStage(Stage stage, Injector injector);

  /**
   * Returns the state of this window.
   *
   * @return the state of this window.
   */
  public WindowState getState() {
    return this.state;
  }

  /**
   * Returns the data source for string translation.
   *
   * @return the data source for string translation.
   */
  protected LocalizedTextProvider getTranslator() {
    return translator;
  }
}
