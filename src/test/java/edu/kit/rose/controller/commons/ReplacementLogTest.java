package edu.kit.rose.controller.commons;

import static edu.kit.rose.util.AccessorUtility.findAccessorOfType;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ReplacementLog}.
 */
class ReplacementLogTest {
  private ReplacementLog replacementLog;
  private Group group;
  private Group groupReplacement;

  @BeforeEach
  void beforeEach() {
    this.replacementLog = new ReplacementLog();

    this.group = new Group();
    this.groupReplacement = new Group();

    this.replacementLog.replaceElement(this.group, this.groupReplacement);
  }

  @Test
  void testReplaceElementWithIllegalArguments() {
    assertThrows(NullPointerException.class,
        () -> replacementLog.replaceElement(null, groupReplacement));
    assertThrows(NullPointerException.class,
        () -> replacementLog.replaceElement(group, null));
    assertThrows(IllegalArgumentException.class,
        () -> replacementLog.replaceElement(group, new Base()));
  }

  @Test
  void testGetCurrentVersionWithoutReplacement() {
    this.replacementLog = new ReplacementLog();
    assertSame(this.group, this.replacementLog.getCurrentVersion(this.group));
    assertSame(this.groupReplacement, this.replacementLog.getCurrentVersion(this.groupReplacement));
  }

  @Test
  void testGetCurrentVersionSingleReplacement() {
    assertSame(groupReplacement, this.replacementLog.getCurrentVersion(this.group));
    assertSame(groupReplacement, this.replacementLog.getCurrentVersion(this.groupReplacement));
  }

  @Test
  void testGetCurrentVersionTransitiveReplacement() {
    var groupReplacementReplacement = new Group();

    this.replacementLog.replaceElement(this.groupReplacement, groupReplacementReplacement);

    assertSame(groupReplacementReplacement,
        this.replacementLog.getCurrentVersion(this.group));
    assertSame(groupReplacementReplacement,
        this.replacementLog.getCurrentVersion(this.groupReplacement));
    assertSame(groupReplacementReplacement,
        this.replacementLog.getCurrentVersion(groupReplacementReplacement));
  }

  @Test
  void testGetCurrentAccessorVersion() {
    AttributeAccessor<String> nameAccessor = findAccessorOfType(group, AttributeType.NAME);
    AttributeAccessor<String> commentAccessor = findAccessorOfType(group, AttributeType.COMMENT);

    AttributeAccessor<String> nameAccessorReplacement =
        findAccessorOfType(groupReplacement, AttributeType.NAME);
    AttributeAccessor<String> commentAccessorReplacement =
        findAccessorOfType(groupReplacement, AttributeType.COMMENT);

    assertSame(nameAccessorReplacement,
        this.replacementLog.getCurrentAccessorVersion(nameAccessor));
    assertSame(commentAccessorReplacement,
        this.replacementLog.getCurrentAccessorVersion(commentAccessor));
  }

  @Test
  void testGetCurrentAccessorVersionWithoutReplacement() {
    this.replacementLog = new ReplacementLog();
    AttributeAccessor<String> nameAccessor = findAccessorOfType(group, AttributeType.NAME);
    AttributeAccessor<String> commentAccessor = findAccessorOfType(group, AttributeType.COMMENT);

    assertSame(nameAccessor, this.replacementLog.getCurrentAccessorVersion(nameAccessor));
    assertSame(commentAccessor, this.replacementLog.getCurrentAccessorVersion(commentAccessor));
  }

  @Test
  void testGetCurrentConnectorVersion() {
    Exit original = new Exit();
    Exit replacement = new Exit();
    this.replacementLog.replaceElement(original, replacement);

    assertSame(replacement.getEntry(),
        this.replacementLog.getCurrentConnectorVersion(original.getEntry()));
    assertSame(replacement.getExit(),
        this.replacementLog.getCurrentConnectorVersion(original.getExit()));
    assertSame(replacement.getRamp(),
        this.replacementLog.getCurrentConnectorVersion(original.getRamp()));
  }

  @Test
  void testGetCurrentConnectorVersionWithoutReplacement() {
    Exit exit = new Exit();

    assertSame(exit.getEntry(),
        this.replacementLog.getCurrentConnectorVersion(exit.getEntry()));
    assertSame(exit.getExit(),
        this.replacementLog.getCurrentConnectorVersion(exit.getExit()));
    assertSame(exit.getRamp(),
        this.replacementLog.getCurrentConnectorVersion(exit.getRamp()));
  }
}
