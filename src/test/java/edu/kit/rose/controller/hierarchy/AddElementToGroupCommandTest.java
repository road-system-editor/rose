package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.SimpleSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit Test for {@link AddElementToGroupCommand}.
 */
class AddElementToGroupCommandTest {
  private Group group;
  private Element element;

  @BeforeEach
  public void setUp() {
    this.element = new Base();
    this.group = new Group() {
      private final List<Element> elements = new ArrayList<>();

      @Override
      public SortedBox<Element> getElements() {
        return new SimpleSortedBox<>(this.elements);
      }

      @Override
      public void addElement(Element e) {
        this.elements.add(e);
      }

      @Override
      public void removeElement(Element e) {
        this.elements.remove(e);
      }
    };
  }

  @Test
  void execute() {
    ChangeCommand addElementCommand = new AddElementToGroupCommand(this.element, this.group);
    addElementCommand.execute();
    Assertions.assertTrue(this.group.getElements().contains(this.element));
  }

  @Test
  void unexecute() {
    ChangeCommand addElementCommand = new AddElementToGroupCommand(this.element, this.group);
    addElementCommand.execute();
    addElementCommand.unexecute();
    Assertions.assertFalse(this.group.getElements().contains(this.element));
  }
}