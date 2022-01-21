module edu.kit.rose.entwurf {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.guice;

    opens edu.kit.rose.view to javafx.fxml, com.google.guice;
    opens edu.kit.rose.view.commons to javafx.fxml, com.google.guice;
    opens edu.kit.rose.view.panel.hierarchy to javafx.fxml, com.google.guice;
    opens edu.kit.rose.view.window to com.google.guice;
    exports edu.kit.rose.view;
}