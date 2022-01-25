package edu.kit.rose.controller.hierarchy;

import static org.mockito.Mockito.mock;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.infrastructure.SortedBox;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Group;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link SetGroupNameCommand}.
 */
class SetGroupNameCommandTest {
  private static final String SET = "set";
  private static final String UNSET = "unset";
  private AttributeAccessor<String> nameAccessor;
  private Group group;
  private String name;

  @BeforeEach
  public void setUp() {
    this.name = UNSET;

    this.group = new Group() {
      @Override
      public SortedBox<AttributeAccessor<?>> getAttributeAccessors() {
        ArrayList<AttributeAccessor<?>> list = new ArrayList<>();
        list.add(nameAccessor);
        return new RoseSortedBox<>(list);
      }

      @Override
      public String getName() {
        return name;
      }
    };

    this.nameAccessor = new AttributeAccessor<String>(AttributeType.NAME,
            mock(Supplier.class), mock(Consumer.class)) {
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
  void testExecute() {
    ChangeCommand setGroupNameCommand = new SetGroupNameCommand(this.group, SET);
    setGroupNameCommand.execute();

    Assertions.assertEquals(SET, this.name);
  }

  @Test
  void testUnexecute() {
    ChangeCommand setGroupNameCommand = new SetGroupNameCommand(this.group, SET);
    setGroupNameCommand.execute();
    setGroupNameCommand.unexecute();

    Assertions.assertEquals(UNSET, this.name);
  }
}