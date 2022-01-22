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
import javafx.scene.layout.Background;

/**
 * This class is a {@link javafx.scene.control.ListView} cell,
 * that displays a {@link Group}.
 */
public class ElementListCell extends ListCell<Element> {

  private LocalizedTextProvider translator;
  private HierarchyController hierarchyController;

  private ElementView<? extends Element> elementView;

  /**
   * Creates a new {@link ElementListCell}.
   *
   * @param hierarchyController the hierarchy controller
   * @param translator the translator
   */
  public ElementListCell(
      HierarchyController hierarchyController, LocalizedTextProvider translator) {
    this.setBackground(Background.EMPTY);
    this.hierarchyController = hierarchyController;
    this.translator = translator;
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
    if (elementView == null) {
      elementView = new GroupView(translator, (Group) element, hierarchyController);
      this.setPadding(new Insets(0, 5, 0, 0));
      elementView.setMaxWidth(this.getWidth());
      setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
      setGraphic(elementView);
    }
  }

  private void updateSegment(Element element) {
    if (elementView == null) {
      elementView = new SegmentView(translator, (Segment) element, hierarchyController);
      setGraphic(elementView);
    }
  }
}
