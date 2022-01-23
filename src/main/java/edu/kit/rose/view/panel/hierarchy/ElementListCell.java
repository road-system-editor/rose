package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;

/**
 * This class is a {@link javafx.scene.control.ListView} cell,
 * that displays a {@link Group}.
 */
public class ElementListCell extends TreeCell<Element> {

  private final LocalizedTextProvider translator;
  private final HierarchyController hierarchyController;

  private ElementView<? extends Element> elementView;

  /**
   * Creates a new {@link ElementListCell}.
   *
   * @param hierarchyController the hierarchy controller
   * @param translator the translator
   */
  public ElementListCell(
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

  private void updateGroup(Element element) {
    setGraphic(new GroupView(translator, (Group) element, hierarchyController));
  }

  private void updateSegment(Element element) {
    setGraphic(new SegmentView(translator, (Segment) element, hierarchyController));
  }

  private void onDragDetected(MouseEvent mouseEvent) {
  }

  private void onDragOver(DragEvent dragEvent) {

  }

  private void onDragDropped(DragEvent dragEvent) {

  }

  private void onDragDone(DragEvent dragEvent) {

  }
}
