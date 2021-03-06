package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.view.commons.FxmlContainer;

/**
 * An element view represents an element in the hierarchy view and is responsible for
 * showing itself and it's children.
 *
 * @param <T> the type of Element this ElementView uses.
 */
abstract class ElementView<T extends Element> extends FxmlContainer
    implements SetObserver<Element, Element> {

  protected static final String DELETE_BUTTON_IMAGE_URL =
      "/edu/kit/rose/view/panel/hierarchy/DeleteIcon.png";
  protected static final String ELEMENT_VIEW_STYLE_CSS_FILE =
      "/edu/kit/rose/view/panel/hierarchy/ElementViewStyle.css";
  protected static final String SELECTED_STYLE_CLASS = "elementViewSelected";
  protected static final String UNSELECTED_STYLE_CLASS = "elementViewUnselected";

  private final HierarchyController controller;
  private final T element;
  private final LocalizedTextProvider translator;

  /**
   * Creates a new element view for a given {@code element}.
   *
   * @param translator       the {@link LocalizedTextProvider} to use.
   * @param fxmlResourceName the key for the localized title (or name, etc.) of the Element.
   * @param element          the {@link Element} to use.
   * @param controller       the {@link HierarchyController} to use.
   */
  protected ElementView(LocalizedTextProvider translator, String fxmlResourceName, T element,
                        HierarchyController controller) {
    super(fxmlResourceName);
    this.translator = translator;
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

  @Override
  public LocalizedTextProvider getTranslator() {
    return this.translator;
  }

  /**
   * Provides the element that this element view represents.
   *
   * @return the element that this element view represents.
   */
  public T getElement() {
    return element;
  }

  /**
   * This method needs to be called when the {@link ElementView} is unmounted.
   */
  public void onUnmount() {
    element.removeSubscriber(this);
  }
}
