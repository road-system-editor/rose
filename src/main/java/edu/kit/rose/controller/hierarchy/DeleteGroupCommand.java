package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Encapsulates the functionality of deleting a group
 * and makes it changeable.
 */
public class DeleteGroupCommand implements ChangeCommand {
  private final Project project;
  private Group group;
  private SortedBox<AttributeAccessor<?>> accessors;
  private SortedBox<Element> elements;

  /**
   * Creates a {@link DeleteGroupCommand} that deletes the given group.
   *
   * @param project the model facade to execute the {@link DeleteGroupCommand} on
   * @param group   the group to be deleted
   */
  public DeleteGroupCommand(Project project, Group group) {
    this.project = project;
    this.group = group;
  }

  @Override
  public void execute() {
    this.accessors = this.group.getAttributeAccessors();
    this.elements = this.group.getElements();
    this.project.getRoadSystem().removeElement(this.group);
  }

  @Override
  public void unexecute() {
    ArrayList<Element> list = new ArrayList<>();

    for (Element element : this.elements) {
      list.add(element);
    }
    Group group = this.project.getRoadSystem().createGroup(list);
    SortedBox<AttributeAccessor<?>> accessors = group.getAttributeAccessors();
    Iterator<AttributeAccessor<?>> iteratorTo = accessors.iterator();
    Iterator<AttributeAccessor<?>> iteratorFrom = this.accessors.iterator();

    while (iteratorTo.hasNext()) {
      setAttributeValue(iteratorTo.next(), iteratorFrom.next());
    }
    this.group = group;
  }

  /**
   * Sets the value of attribute 2 to attribute 1.
   * Attribute 1 and 2 must have same data type.
   *
   * @param attribute1 to to be set the value.
   * @param attribute2 from to be set the value.
   */
  private void setAttributeValue(AttributeAccessor attribute1, AttributeAccessor attribute2) {
    switch (attribute1.getAttributeType().getDataType()) {
      case STRING:
        AttributeAccessor<String> auxAttribute1 =
              (AttributeAccessor<String>) attribute1;
        AttributeAccessor<String> auxAttribute2 = (AttributeAccessor<String>) attribute2;
        auxAttribute1.setValue(auxAttribute2.getValue());
        break;

      case BOOLEAN:
        AttributeAccessor<Boolean> auxAttribute3 =
                (AttributeAccessor<Boolean>) attribute1;
        AttributeAccessor<Boolean> auxAttribute4 = (AttributeAccessor<Boolean>) attribute2;
        auxAttribute3.setValue(auxAttribute4.getValue());
        break;

      case INTEGER:
        AttributeAccessor<Integer> auxAttribute5 =
                (AttributeAccessor<Integer>) attribute1;
        AttributeAccessor<Integer> auxAttribute6 = (AttributeAccessor<Integer>) attribute2;
        auxAttribute5.setValue(auxAttribute6.getValue());
        break;

      case FRACTIONAL:
        AttributeAccessor<Double> auxAttribute7 =
                (AttributeAccessor<Double>) attribute1;
        AttributeAccessor<Double> auxAttribute8 = (AttributeAccessor<Double>) attribute2;
        auxAttribute7.setValue(auxAttribute8.getValue());
        break;

      default:      break;
    }
  }
}
