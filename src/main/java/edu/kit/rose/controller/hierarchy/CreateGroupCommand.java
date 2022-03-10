package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Encapsulates the functionality of creating a new group
 * and makes it changeable.
 */
public class CreateGroupCommand implements ChangeCommand {

  private static final String GROUP_NAME_TEMPLATE = "Group %s";
  private static int CREATED_GROUP_COUNT = 0;

  private final ReplacementLog replacementLog;
  private final Project project;
  private final Set<Element> elements;
  private Group group;
  private final HashMap<Group, ArrayList<Element>> parentMap;

  /**
   * Creates a {@link CreateGroupCommand} that creates a group out of a list of segments.
   *
   * @param replacementLog the log that stores the replacements of elements
   * @param project        the model facade to execute the {@link CreateGroupCommand} on
   * @param elements       the elements that will be in the group
   */
  public CreateGroupCommand(ReplacementLog replacementLog,
                            Project project,
                            List<Segment> elements) {
    this.replacementLog = Objects.requireNonNull(replacementLog);
    this.project = Objects.requireNonNull(project);
    this.elements = new HashSet<>(Objects.requireNonNull(elements));
    this.parentMap = new HashMap<>();
  }

  @Override
  public void execute() {
    this.storeParentsForElements();
    this.removeChildrenFromParents();
    var oldGroup = this.group;
    var newElements = this.replacementLog.getCurrentVersions(this.elements);
    this.group = this.project.getRoadSystem().createGroup(newElements);
    if (oldGroup != null) {
      this.replacementLog.replaceElement(oldGroup, this.group);
      this.group.setName(oldGroup.getName());
    } else {
      CREATED_GROUP_COUNT++;
      this.group.setName(String.format(GROUP_NAME_TEMPLATE, CREATED_GROUP_COUNT));
    }
  }

  @Override
  public void unexecute() {
    this.addElementsBackToParent();
    var currentGroup = this.replacementLog.getCurrentVersion(this.group);
    this.project.getRoadSystem().removeElement(currentGroup);
  }

  /**
   * Searches throw all the elements of the road system and
   * checks if any given element in this constructor has a group
   * that stores that element. If that's the case this method
   * puts the parent and child elements is this parentMap
   */
  private void storeParentsForElements() {
    Box<Element> auxElements = this.project.getRoadSystem().getElements();
    if (auxElements != null) {
      for (Element roadElement : auxElements) {
        if (roadElement.isContainer()) {
          Group auxGroup = (Group) roadElement;
          addGroupToChildrenMapping(auxGroup);
        }
      }
    }

    Group auxGroup = this.project.getRoadSystem().getRootGroup();
    addGroupToChildrenMapping(auxGroup);
  }

  private void addGroupToChildrenMapping(Group auxGroup) {
    ArrayList<Element> children = new ArrayList<>();
    for (Element element : this.elements) {
      if (auxGroup.getElements().contains(element)) {
        children.add(element);
      }
    }
    if (!children.isEmpty()) {
      this.parentMap.put(auxGroup, new ArrayList<>(children));
    }
  }


  /**
   * Goes Throw the parentMap and adds elements back
   * to its parents.
   */
  private void addElementsBackToParent() {
    for (var entry : this.parentMap.entrySet()) {
      Group parent = this.replacementLog.getCurrentVersion(entry.getKey());

      for (Element element : entry.getValue()) {
        Element child = this.replacementLog.getCurrentVersion(element);
        parent.addElement(child);
      }
    }
  }

  /**
   * Goes Throw the parentMap and removes elements back
   * to its parents.
   */
  private void removeChildrenFromParents() {
    for (var entry : this.parentMap.entrySet()) {
      Group parent = this.replacementLog.getCurrentVersion(entry.getKey());
      for (Element element : entry.getValue()) {
        Element child = this.replacementLog.getCurrentVersion(element);
        parent.removeElement(child);
      }
    }
  }
}
