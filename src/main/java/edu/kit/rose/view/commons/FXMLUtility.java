package edu.kit.rose.view.commons;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

/**
 * Utility class that helps with loading FXML components.
 */
public final class FXMLUtility {
    /**
     * Loads the JavaFX nodes from the given the FXML file ({@code fxmlUrl}) into the given {@code root} object and injects the given {@code controller}.
     * @param root
     * @param controller
     * @param fxmlUrl
     * @param <T>
     * @return the loaded object hierarchy.
     */
    public static <T> T loadFXML(Object root, Object controller, URL fxmlUrl) {
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
