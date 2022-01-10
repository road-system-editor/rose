package edu.kit.rose.view.panel.hierarchy;

import edu.kit.rose.controller.hierarchy.HierarchyController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;

/**
 * A group view represents a {@link Group} in the hierarchy view.
 */
class GroupView extends ElementView<Group> {
    /**
     * Creates a new group view for a given {@code group}.
     * @param translator
     * @param group
     * @param controller
     */
    GroupView(LocalizedTextProvider translator, Group group, HierarchyController controller) {
        super(translator, "group_view.fxml", group, controller);
    }

    @Override
    public void notifyChange(Element unit) {

    }

    @Override
    protected void updateTranslatableStrings(Language lang) {

    }
}
