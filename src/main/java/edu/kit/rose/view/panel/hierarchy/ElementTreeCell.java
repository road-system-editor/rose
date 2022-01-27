package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 * This class is a {@link javafx.scene.control.ListView} cell,
 * that displays a {@link Group}.
 */
public class ElementTreeCell extends TreeCell<Element>
                              implements SetObserver<Element, Element> {

  private final LocalizedTextProvider translator;
  private final HierarchyController hierarchyController;

  private ElementView<? extends Element> elementView;

  /**
   * Creates a new {@link ElementTreeCell}.
   *
   * @param hierarchyController the hierarchy controller
   * @param translator          the translator
   */
  public ElementTreeCell(
      HierarchyController hierarchyController, LocalizedTextProvider translator) {
    this.hierarchyController = hierarchyController;
    this.translator = translator;

    this.setOnDragDetected(this::onDragDetected);
    this.setOnDragOver(this::onDragOver);
    this.setOnDragDropped(this::onDragDropped);
    this.setOnDragDone(this::onDragDone);
  }

  @Override
  protected void updateItem(Element element, boolean empty) {
    super.updateItem(element, empty);

    if (empty || element == null) {
      setText(null);
      setGraphic(null);
    } else {
      if (element.isContainer()) {
        updateGroup(element);
      } else {
        updateSegment(element);
      }
    }
  }

  private Element element;

  private void updateGroup(Element element) {
    if (this.element == null) {
      this.element = element;
      setGraphic(new GroupView(translator, (Group) element, hierarchyController));
      this.element.addSubscriber(this);
    } else if (this.element != element) {
      this.element.removeSubscriber(this);
      this.element = element;
      this.element.addSubscriber(this);
      setGraphic(new GroupView(translator, (Group) element, hierarchyController));
    }
  }

  private void updateSegment(Element element) {
    setGraphic(new SegmentView(translator, (Segment) element, hierarchyController));
  }

  public static TreeItem<Element> dragItem;
  public static final DataFormat DATA_FORMAT
      = new DataFormat("application/rose-hierarchy-element-drag");


  private void onDragDetected(MouseEvent mouseEvent) {
    dragItem = getTreeItem();

    if (dragItem == null) {
      return;
    }

    Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);

    ClipboardContent content = new ClipboardContent();
    content.put(DATA_FORMAT, "ElementListCell-Element");

    dragboard.setContent(content);
    dragboard.setDragView(snapshot(null, null));

    mouseEvent.consume();
  }

  private void onDragOver(DragEvent dragEvent) {
    if (!dragEvent.getDragboard()
        .hasContent(DATA_FORMAT) || getItem() == null) {
      return;
    }

    TreeItem<Element> itemToPlaceOn = getTreeItem();

    if (itemToPlaceOn == null || itemToPlaceOn == dragItem) {
      return;
    }

    if (dragItem.getParent() == null) {
      return;
    }

    dragEvent.acceptTransferModes(TransferMode.MOVE);
  }

  private void onDragDropped(DragEvent dragEvent) {
    if (!dragEvent.getDragboard()
        .hasContent(DATA_FORMAT) || getItem() == null) {
      return;
    }

    TreeItem<Element> itemToPlaceOn = getTreeItem();

    this.hierarchyController.addElementToGroup(
        dragItem.getValue(),
        (Group) itemToPlaceOn.getValue());

    dragEvent.setDropCompleted(true);
    dragEvent.consume();
  }

  private void onDragDone(DragEvent dragEvent) {

  }

  @Override
  public void notifyAddition(Element unit) {
    TreeItem<Element> itemToPlaceOn = getTreeItem();
    if (itemToPlaceOn != null) {
      itemToPlaceOn.getChildren().add(dragItem);
    }
  }

  @Override
  public void notifyRemoval(Element unit) {
    TreeItem<Element> treeItem = getTreeItem().getParent();
    if (treeItem != null) {
      treeItem.getChildren().removeIf(child -> child.getValue() == unit);
      unit.removeSubscriber(this);
    }
  }

  @Override
  public void notifyChange(Element unit) {

  }
}
