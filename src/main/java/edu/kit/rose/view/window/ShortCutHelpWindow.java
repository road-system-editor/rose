package edu.kit.rose.view.window;

import com.google.inject.Injector;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.view.commons.FxmlUtility;
import java.util.List;
import java.util.function.Consumer;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

/**
 * Displays all available shortcuts.
 */
public class ShortCutHelpWindow extends RoseWindow {

  private static final int WIDTH = 430;
  private static final int HEIGHT = 385;
  private static final List<String> shortCutLocalizationKeys =
      List.of(
          "view.window.shortCutHelp.save.",
          "view.window.shortCutHelp.saveAs.",
          "view.window.shortCutHelp.openProject.",
          "view.window.shortCutHelp.exportProject.",
          "view.window.shortCutHelp.newProject.",
          "view.window.shortCutHelp.undo.",
          "view.window.shortCutHelp.redo.",
          "view.window.shortCutHelp.duplicate.",
          "view.window.shortCutHelp.createGroup.",
          "view.window.shortCutHelp.search.",
          "view.window.shortCutHelp.delete.",
          "view.window.shortCutHelp.rotate."
      );
  private Consumer<Language> languageConsumer;

  @FXML
  private TableView<ShortcutTableEntry> shortCutTable;

  @FXML
  private TableColumn<ShortcutTableEntry, String> shortCutColumn;

  @FXML
  private TableColumn<ShortcutTableEntry, String> descriptionColumn;

  /**
   * Creates new ShortCutHelpWindow.
   *
   * @param injector the dependency injector.
   */
  public ShortCutHelpWindow(Injector injector) {
    super(injector);
  }

  @Override
  protected void configureStage(Stage stage, Injector injector) {
    Parent tree = FxmlUtility.loadFxml(null, this,
        getClass().getResource("ShortCutHelpWindow.fxml"));
    Scene scene = new Scene(tree);
    stage.setScene(scene);
    stage.setWidth(WIDTH);
    stage.setHeight(HEIGHT);
    stage.setResizable(false);

    setupTable();
    configureTranslatorSubscription(stage);
    updateTranslatableStrings(stage);
  }

  @Override
  public void close() {
    super.close();
    getTranslator().unsubscribeFromOnLanguageChanged(languageConsumer);
  }

  private void configureTranslatorSubscription(Stage stage) {
    languageConsumer = language -> updateTranslatableStrings(stage);
    getTranslator().subscribeToOnLanguageChanged(languageConsumer);
  }

  private void setupTable() {
    shortCutColumn.setCellValueFactory(columnEntry ->
        new SimpleStringProperty(columnEntry.getValue().shortCut()));

    descriptionColumn.setCellValueFactory(columnEntry ->
        new SimpleStringProperty(columnEntry.getValue().description()));

    descriptionColumn.prefWidthProperty()
        .bind(shortCutTable.widthProperty().subtract(shortCutColumn.getWidth()));
    System.out.println(descriptionColumn.getWidth());
  }

  private void updateTranslatableStrings(Stage stage) {
    stage.setTitle(getTranslator().getLocalizedText("view.window.shortCutHelp.title"));
    shortCutTable.getItems().clear();
    shortCutColumn.setText(getTranslator().getLocalizedText(
        "view.window.shortCutHelp.shortCutColumn.title"
    ));
    descriptionColumn.setText(getTranslator().getLocalizedText(
        "view.window.shortCutHelp.descriptionColumn.title"
    ));
    shortCutLocalizationKeys.forEach(key -> shortCutTable.getItems().add(new ShortcutTableEntry(
        getTranslator().getLocalizedText(key + "shortCut"),
        getTranslator().getLocalizedText(key + "description")
    )));
  }


  private record ShortcutTableEntry(String shortCut, String description) { }
}
