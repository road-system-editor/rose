package edu.kit.rose.controller.hierarchy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.model.roadsystem.elements.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link SetGroupNameCommand}.
 */
class SetGroupNameCommandTest {
  private static final String SET = "set";
  private static final String UNSET = "unset";

  private Group group;

  private ReplacementLog replacementLog;

  private SetGroupNameCommand command;

  @BeforeEach
  void setUp() {
    this.group = new Group();
    this.group.setName(UNSET);

    this.replacementLog = new ReplacementLog();

    this.command = new SetGroupNameCommand(this.replacementLog, this.group, SET);
  }

  @Test
  void testNameChanged() {
    assumeTrue(UNSET.equals(this.group.getName()));

    command.execute();
    assertEquals(SET, this.group.getName());

    command.unexecute();
    assertEquals(UNSET, this.group.getName());
  }

  @Test
  void testConsidersReplacement() {
    var groupReplacement = new Group();
    groupReplacement.setName(UNSET);
    replacementLog.replaceElement(group, groupReplacement);

    command.execute();
    assertEquals(SET, groupReplacement.getName());

    var groupReplacementReplacement = new Group();
    groupReplacementReplacement.setName(SET);
    replacementLog.replaceElement(groupReplacement, groupReplacementReplacement);

    command.unexecute();
    assertEquals(UNSET, groupReplacementReplacement.getName());
  }
}