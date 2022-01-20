package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.view.commons.FXMLContainer;

/**
 * An element view represents an element in the hierarchy view and is responsible for showing itself and it's children.
 *
 * @param <T>
 */
abstract class ElementView<T extends Element> extends FXMLContainer
    implements UnitObserver<Element> {
  private HierarchyController controller;

  private T element;

  /**
   * Creates a new element view for a given {@code element}.
   *
   * @param translator
   * @param fxmlResourceName
   * @param element
   * @param controller
   */
  protected ElementView(LocalizedTextProvider translator, String fxmlResourceName, T element,
                        HierarchyController controller) {
    super(fxmlResourceName);
    this.element = element;
    this.controller = controller;

    element.addSubscriber(this);
  }

  /**
   * Returns the controller of this element view.
   *
   * @return
   */
  protected HierarchyController getController() {
    return this.controller;
  }

  /**
   * Returns the element that this element view represents.
   *
   * @return
   */
  public T getElement() {
    return element;
  }
}
