package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Encapsulates the functionality of creating a new group
 * and makes it changeable.
 */
public class CreateGroupCommand implements ChangeCommand {
  private final Project project;
  private final List<Element> elements;
  private Group group;
  private final HashMap<Group, ArrayList<Element>> parentMap;

  /**
   * Creates a {@link CreateGroupCommand} that creates a group out of a list of segments.
   *
   * @param project  the model facade to execute the {@link CreateGroupCommand} on
   * @param elements the elements that will be in the group
   */
  public CreateGroupCommand(Project project, List<Element> elements) {
    this.project = project;
    this.elements = new ArrayList<>(elements);
    this.parentMap = new HashMap<>();
  }

  @Override
  public void execute() {
    this.storeParentsForElements();
    this.removeChildrenFromParents();
    this.group = this.project.getRoadSystem().createGroup(this.elements);
  }

  @Override
  public void unexecute() {
    this.addElementsBackToParent();
    this.project.getRoadSystem().removeElement(this.group);
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
      for (Element roadElement : this.project.getRoadSystem().getElements()) {
        if (roadElement.isContainer()) {
          Group auxGroup = (Group) roadElement;
          ArrayList<Element> child = new ArrayList<>();
          for (Element element : this.elements) {
            if (auxGroup.getElements().contains(element)) {
              child.add(element);
            }
          }
          if (!child.isEmpty()) {
            this.parentMap.put(auxGroup, new ArrayList<>(child));
          }
        }
      }
    }
  }

  /**
   * Goes Throw the parentMap and adds elements back
   * to its parents.
   */
  private void addElementsBackToParent() {
    for (var entry : this.parentMap.entrySet()) {
      for (Element element : entry.getValue()) {
        entry.getKey().addElement(element);
      }
    }
  }

  /**
   * Goes Throw the parentMap and removes elements back
   * to its parents.
   */
  private void removeChildrenFromParents() {
    for (var entry : this.parentMap.entrySet()) {
      for (Element element : entry.getValue()) {
        entry.getKey().removeElement(element);
      }
    }
  }
}
