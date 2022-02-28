package edu.kit.rose.view.commons;

import java.util.HashMap;
import java.util.function.Function;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 * This class is a {@link ComboBox} whose text fields can be localized.
 *
 * @param <T> the item type of the {@link ComboBox}
 */
public class LocalizedComboBox<T> extends ComboBox<T> {
  private String emptyShowCellContent;
  private Function<T, String> localizationCallback;
  private HashMap<T, ListCell<T>> comboBoxListCells = new HashMap<>();

  public void init(String emptyShowCellContent,
                   Function<T, String> localizationCallback) {
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
   * Updates all List entries and the visible part of the {@link LocalizedComboBox}s
   * {@link ComboBox}.
   */
  public void updateLocalization() {
    comboBoxListCells.forEach((item, listCell) -> {
      if (listCell != null) {
        listCell.setText(localizationCallback.apply(item));
      }
    });
    if (getButtonCell() != null) {
      if (this.getSelectionModel().getSelectedItem() == null) {
        getButtonCell().setText(emptyShowCellContent);
      } else {
        getButtonCell().setText(
            localizationCallback.apply(
                this.getSelectionModel().getSelectedItem()));
      }
    }
  }
}
