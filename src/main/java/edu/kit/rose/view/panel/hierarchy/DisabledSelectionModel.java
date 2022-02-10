package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.model.roadsystem.elements.Element;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;

/**
 * The {@link DisabledSelectionModel} is a {@link javafx.scene.control.MultipleSelectionModel},
 * that disables the selection functionality.
 */
public class DisabledSelectionModel extends MultipleSelectionModel<TreeItem<Element>> {


  @Override
  public ObservableList<Integer> getSelectedIndices() {
    return FXCollections.emptyObservableList();
  }

  @Override
  public ObservableList<TreeItem<Element>> getSelectedItems() {
    return FXCollections.emptyObservableList();
  }

  @Override
  public void selectIndices(int index, int... indices) {
  }

  @Override
  public void selectAll() {
  }

  @Override
  public void clearAndSelect(int index) {
  }

  @Override
  public void select(int index) {
  }

  @Override
  public void select(TreeItem<Element> obj) {
  }

  @Override
  public void clearSelection(int index) {
  }

  @Override
  public void clearSelection() {
  }

  @Override
  public boolean isSelected(int index) {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public void selectPrevious() {
  }

  @Override
  public void selectNext() {
  }

  @Override
  public void selectFirst() {
  }

  @Override
  public void selectLast() {
  }
}
