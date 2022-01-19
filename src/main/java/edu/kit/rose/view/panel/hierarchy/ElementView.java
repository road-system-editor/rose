package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.view.commons.FXMLContainer;

/**
 * An element view represents an element in the hierarchy view and is responsible for
 * showing itself and it's children.
 *
 * @param <T> the type of Element this ElementView uses.
 */
abstract class ElementView<T extends Element> extends FXMLContainer
    implements UnitObserver<Element> {

  private HierarchyController controller;
  private T element;

  /**
   * Creates a new element view for a given {@code element}.
   *
   * @param translator the {@link LocalizedTextProvider} to use.
   * @param fxmlResourceName
   * @param element the {@link Element} to use.
   * @param controller the {@link HierarchyController} to use.
   */
  protected ElementView(LocalizedTextProvider translator, String fxmlResourceName, T element,
                        HierarchyController controller) {
    super(fxmlResourceName);
    setTranslator(translator);
    this.element = element;
    this.controller = controller;

    element.addSubscriber(this);
  }

  /**
   * Provides the controller of this element view.
   *
   * @return the controller of this element view.
   */
  protected HierarchyController getController() {
    return this.controller;
  }

  /**
   * Provides the element that this element view represents.
   *
   * @return the element that this element view represents.
   */
  public T getElement() {
    return element;
  }
}
