package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.SimpleSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link SetGroupNameCommand}.
 */
class SetGroupNameCommandTest {
  private static final String SETED = "seted";
  private static final String UNSETED = "unseted";
  private AttributeAccessor<String> nameAccessor;
  private Group group;
  private String name;

  @BeforeEach
  public void setUp() {
    this.name = UNSETED;

    this.group = new Group() {
      @Override
      public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
        ArrayList<AttributeAccessor<?>> list = new ArrayList<>();
        list.add(nameAccessor);
        return new SimpleSortedBox<>(list);
      }

      @Override
      public String getName() {
        return name;
      }
    };

    this.nameAccessor = new AttributeAccessor<>() {
      @Override
      public AttributeType getAttributeType() {
        return AttributeType.NAME;
      }

      @Override
      public void setValue(String str) {
        name = str;
      }

      public String getValue() {
        return name;
      }
    };
  }

  @Test
  void execute() {
    ChangeCommand setGroupNameCommand = new SetGroupNameCommand(this.group, SETED);
    setGroupNameCommand.execute();

    Assertions.assertEquals(SETED, this.name);
  }

  @Test
  void unexecute() {
    ChangeCommand setGroupNameCommand = new SetGroupNameCommand(this.group, SETED);
    setGroupNameCommand.execute();
    setGroupNameCommand.unexecute();

    Assertions.assertEquals(UNSETED, this.name);
  }
}