module edu.kit.rose.entwurf {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.guice;

    opens edu.kit.rose.view to javafx.fxml;
    exports edu.kit.rose.view;
}