package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;

class TreeViewUtility {

  static void addElementToElementTreeItem(Element element, ElementTreeItem targetTreeItem) {
    if (element.isContainer()) {
      SortedBox<Element> elements = ((Group) element).getElements();

      ElementTreeItem groupRootItem = new ElementTreeItem(element);
      insertTreeItemsForElementsRecursive(groupRootItem, elements);

      targetTreeItem.getInternalChildren().add(groupRootItem);
    } else {
      ElementTreeItem treeItem = new ElementTreeItem(element);
      targetTreeItem.getInternalChildren().add(treeItem);
    }
  }

  private static void insertTreeItemsForElementsRecursive(
      ElementTreeItem currentTreeItem,
      SortedBox<Element> elements) {
    for (Element element : elements) {
      if (element.isContainer()) {
        ElementTreeItem groupTreeItem = new ElementTreeItem(element);
        insertTreeItemsForElementsRecursive(groupTreeItem, ((Group) element).getElements());
        currentTreeItem.getInternalChildren().add(groupTreeItem);
      } else {
        ElementTreeItem segmentTreeItem = new ElementTreeItem(element);
        currentTreeItem.getInternalChildren().add(segmentTreeItem);
      }
    }
  }
}