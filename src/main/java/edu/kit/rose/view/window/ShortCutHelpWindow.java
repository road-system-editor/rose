package edu.kit.rose.view.window;

import com.google.inject.Injector;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.view.commons.FxmlUtility;
import java.util.List;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Displays all available shortcuts.
 */
public class ShortCutHelpWindow extends RoseWindow {

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


  private TableColumn<ShortcutTableEntry, String> shortCutColumn;
  private TableColumn<ShortcutTableEntry, String> descriptionColumn;


  @FXML
  private TableView<ShortcutTableEntry> shortCutTable;

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
    var scene = new Scene(tree);
    stage.setScene(scene);
    stage.setWidth(385);
    stage.setHeight(385);
    stage.setResizable(false);
    languageConsumer = language -> updateTranslatableStrings(stage);
    getTranslator().subscribeToOnLanguageChanged(languageConsumer);
    setupTable();
    stage.setOnCloseRequest(
        event -> getTranslator().unsubscribeFromOnLanguageChanged(languageConsumer));
    updateTranslatableStrings(stage);
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

  private void setupTable() {
    shortCutColumn = new TableColumn<>();
    descriptionColumn = new TableColumn<>();
    shortCutColumn.setCellValueFactory(new PropertyValueFactory<>("shortCut"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    shortCutTable.getColumns().addAll(shortCutColumn, descriptionColumn);
  }

  //TODO: fix public
  public class ShortcutTableEntry {

    private final String shortCut;
    private final String description;

    public ShortcutTableEntry(String shortCut, String description) {
      this.shortCut = shortCut;
      this.description = description;
    }

    public String getDescription() {
      return description;
    }

    public String getShortCut() {
      return shortCut;
    }
  }
}
