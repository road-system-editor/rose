package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.infrastructure.SetObservable;
import edu.kit.rose.infrastructure.UnitObservable;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Provides the functionality to manage the element
 * hierarchy of the roadsystem.
 */
public interface HierarchyController {

  /**
   * Creates a new group that contains all selected segments in the element hierarchy.
   */
  void createGroup();

  /**
   * Deletes a group in the element hierarchy.
   * The segments the group contains will not be deleted.
   *
   * @param group the group to delete
   */
  void deleteGroup(Group group);

  /**
   * Adds an element to a group if the group does not already
   * contain the element.
   *
   * @param element the element to add
   * @param group   the group to add the element to
   */
  void addElementToGroup(Element element, Group group);

  /**
   * Renames a given group with a given name.
   *
   * @param group the group to rename
   * @param name  the new name of the group
   */
  void setGroupName(Group group, String name);

  /**
   * Adds a biConsumer to the consumer list.
   *
   * @param consumer to be added
   */
  void addSubscription(BiConsumer<Segment, Boolean> consumer);

  /**
   * Removes the biConsumer to the consumer list.
   *
   * @param consumer to be removed
   */
  void removeSubscription(BiConsumer<Segment, Boolean> consumer);
}
