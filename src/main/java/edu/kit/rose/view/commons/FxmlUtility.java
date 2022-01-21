package edu.kit.rose.view.commons;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;

/**
 * Utility class that helps with loading FXML components.
 */
public final class FxmlUtility {
  /**
   * Loads the JavaFX nodes from the given the FXML file ({@code fxmlUrl}) into the given
   * {@code root} object and injects the given {@code controller}.
   *
   * @param root the root object to attach the contents of the fxml file to.
   * @param controller the JavaFX controller for the loaded components.
   * @param fxmlUrl the url of the fxml resource.
   * @param <T> the type of the root element in the fxml file.
   * @return the loaded object hierarchy.
   */
  public static <T> T loadFxml(Object root, Object controller, URL fxmlUrl) {
    FXMLLoader loader = new FXMLLoader(fxmlUrl);
    loader.setRoot(root);
    loader.setController(controller);

    try {
      return loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
