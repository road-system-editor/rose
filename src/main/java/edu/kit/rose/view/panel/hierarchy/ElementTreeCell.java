package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.Map;
import javafx.application.Platform;
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

  public static TreeItem<Element> dragItem;
  public static final DataFormat DATA_FORMAT
      = new DataFormat("application/rose-hierarchy-element-drag");
  public static final String DRAG_AND_DROP_MOVE_CONTENT
      = "ElementListCell-Element";

  private final LocalizedTextProvider translator;
  private final HierarchyController hierarchyController;
  private final RoadSystemController roadSystemController;

  private Element element;

  private ElementView<? extends Element> currentGraphicElementView;

  private final Map<Element, ElementTreeCell> elementToElementTreeCellMapping;

  /**
   * Creates a new {@link ElementTreeCell}.
   *
   * @param hierarchyController the hierarchy controller
   * @param translator          the translator
   */
  public ElementTreeCell(
      RoadSystemController roadSystemController,
      HierarchyController hierarchyController, LocalizedTextProvider translator,
      Map<Element, ElementTreeCell> elementToElementTreeCellMapping) {
    this.roadSystemController = roadSystemController;
    this.hierarchyController = hierarchyController;
    this.translator = translator;
    this.elementToElementTreeCellMapping = elementToElementTreeCellMapping;

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

  private void updateGroup(Element element) {
    if (this.element != null) {
      this.element.removeSubscriber(this);
    }
    this.element = element;
    this.element.addSubscriber(this);

    //Necessary, because javaFx reuses TreeCells
    removeOldElementTreeCellSubscriberFromElement(element);

    if (this.currentGraphicElementView != null) {
      this.currentGraphicElementView.onUnmount();
    }

    this.currentGraphicElementView
            = new GroupView(translator, (Group) element, hierarchyController);
    setGraphic(this.currentGraphicElementView);
  }

  private void removeOldElementTreeCellSubscriberFromElement(Element element) {
    ElementTreeCell currentlyMappedToElement
        = elementToElementTreeCellMapping.getOrDefault(element, null);
    if (currentlyMappedToElement != this) {
      element.removeSubscriber(currentlyMappedToElement);
      elementToElementTreeCellMapping.put(element, this);
    }
  }

  private void updateSegment(Element element) {
    if (this.currentGraphicElementView != null) {
      this.currentGraphicElementView.onUnmount();
    }

    this.currentGraphicElementView = new SegmentView(
        translator, (Segment) element, hierarchyController, roadSystemController);
    setGraphic(this.currentGraphicElementView);
  }

  private void onDragDetected(MouseEvent mouseEvent) {
    dragItem = getTreeItem();

    if (dragItem == null) {
      return;
    }

    Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);

    ClipboardContent content = new ClipboardContent();
    content.put(DATA_FORMAT, DRAG_AND_DROP_MOVE_CONTENT);

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

    if (itemToPlaceOn.getValue() != null && itemToPlaceOn.getValue().isContainer()) {
      this.hierarchyController.addElementToGroup(
          dragItem.getValue(),
          (Group) itemToPlaceOn.getValue());
    }

    dragItem = null;

    dragEvent.setDropCompleted(true);
    dragEvent.consume();
  }

  private void onDragDone(DragEvent dragEvent) {

  }

  @Override
  public void notifyAddition(Element unit) {
    Platform.runLater(() -> {
      ElementTreeItem itemToPlaceOn = (ElementTreeItem) getTreeItem();
      if (itemToPlaceOn != null) {
        TreeViewUtility.addElementToElementTreeItem(unit, itemToPlaceOn);
      }
    });
  }

  @Override
  public void notifyRemoval(Element unit) {
    Platform.runLater(() -> {
      ElementTreeItem treeItem = (ElementTreeItem) getTreeItem();
      if (treeItem != null) {
        treeItem.getInternalChildren().removeIf(child -> child.getValue() == unit);
      }
    });
  }

  @Override
  public void notifyChange(Element unit) {

  }
}