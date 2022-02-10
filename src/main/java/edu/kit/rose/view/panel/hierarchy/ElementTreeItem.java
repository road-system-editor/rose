package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.model.roadsystem.elements.Element;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TreeItem;

/**
 * A {@link ElementTreeItem} is an {@link TreeItem} that holds
 * an {@link Element} and that allows filtering in a {@link javafx.scene.control.TreeView}.
 */
public class ElementTreeItem extends TreeItem<Element> {

  private final ObservableList<TreeItem<Element>> sourceList = FXCollections.observableArrayList();
  private final FilteredList<TreeItem<Element>> filteredList = new FilteredList<>(this.sourceList);

  private boolean preserveElement = true;

  /**
   * Creates a new instance of the {@link ElementTreeItem}.
   *
   * @param element the {@link Element} to display
   */
  public ElementTreeItem(Element element) {
    super(element);

    Bindings.bindContent(getChildren(), this.filteredList);
  }

  /**
   * Updates the filter status of children of this {@link ElementTreeItem}.
   *
   * @param filterText the text to use as filter
   */
  public void updateFilter(String filterText) {
    sourceList.forEach(elementTreeItem ->
        ((ElementTreeItem) elementTreeItem).updateFilter(filterText));

    if (getValue() != null) { // Don't update preserveElement in root
      preserveElement = getValue() != null
          && (getValue().getName().startsWith(filterText) || this.hasPreservedChildren());
    }

    filteredList.setPredicate((childElementTreeItem) -> {
      if (filterText == null || filterText.isEmpty()
          || childElementTreeItem.getValue() == null) {
        return true;
      } else {
        ElementTreeItem child = (ElementTreeItem) childElementTreeItem;
        return child.preserveElement;
      }
    });
  }

  private boolean hasPreservedChildren() {
    return sourceList.stream().anyMatch(child -> ((ElementTreeItem) child).preserveElement);
  }

  public final ObservableList<TreeItem<Element>> getInternalChildren() {
    return this.sourceList;
  }
}
