package edu.kit.rose.view.commons;

import java.util.HashMap;
import java.util.function.Function;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 * This class encapsulates the localization of the {@link javafx.scene.control.ComboBox} class.
 */
public class ComboBoxLocalizationManager<T> {

  private final String emptyShowCellContent;
  private final Function<T, String> localizationCallback;
  private final ComboBox<T> targetComboBox;
  private final HashMap<T, ListCell<T>> comboBoxListCells = new HashMap<>();
  private ListCell<T> showCell = null;

  /**
   * Creates a new {@link ComboBoxLocalizationManager} instance.
   *
   * @param targetComboBox the {@link ComboBox} to manage the localization on
   * @param emptyShowCellContent the text, that is shown, when no value is selected
   * @param localizationCallback the callback that returns the localized text for an item
   */
  public ComboBoxLocalizationManager(ComboBox<T> targetComboBox,
                                     String emptyShowCellContent,
                                     Function<T, String> localizationCallback) {
    this.targetComboBox = targetComboBox;
    this.emptyShowCellContent = emptyShowCellContent;
    this.localizationCallback = localizationCallback;
  }

  /**
   * Registers a {@link ListCell} for a given item.
   *
   * @param item the item that belongs to the cell
   * @param cell the target {@link ListCell}
   */
  public void putCell(T item, ListCell<T> cell) {
    comboBoxListCells.put(item, cell);
  }

  /**
   * Sets the shown {@link ListCell}.
   *
   * @param cell the target {@link ListCell}
   */
  public void setShownCell(ListCell<T> cell) {
    this.showCell = cell;
  }

  /**
   * Updates all List entries and the visible part of the {@link ComboBoxLocalizationManager}s
   * {@link ComboBox}.
   */
  public void updateLocalization() {
    comboBoxListCells.forEach((item, listCell) -> {
      if (listCell != null) {
        listCell.setText(localizationCallback.apply(item));
      }
    });
    if (showCell != null) {
      if (targetComboBox.getSelectionModel().getSelectedItem() == null) {
        showCell.setText(emptyShowCellContent);
      } else {
        showCell.setText(
            localizationCallback.apply(
                targetComboBox.getSelectionModel().getSelectedItem()));
      }
    }
  }


}
