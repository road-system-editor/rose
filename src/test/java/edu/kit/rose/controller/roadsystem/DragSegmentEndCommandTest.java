package edu.kit.rose.controller.roadsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import edu.kit.rose.controller.commons.ReplacementLog;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.MovableConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 * Tests the {@link DragSegmentEndCommand} class.
 */
class DragSegmentEndCommandTest {
  private static final Position ORIGINAL_POSITION = new Position(10, 20);
  private static final Position TARGET_POSITION = new Position(20, 30);
  private static final Movement MOVEMENT = new Movement(10, 10);

  private ReplacementLog replacementLog;
  private Base segment;
  private MovableConnector connector;

  private DragSegmentEndCommand command;

  /**
   * Sets up all mock objects.
   */
  @BeforeEach
  void setUp() {
    this.segment = new Base();

    connector = this.segment.getEntry();
    connector.move(new Movement(ORIGINAL_POSITION.getX(), ORIGINAL_POSITION.getY()));

    RoadSystem roadSystem = Mockito.mock(RoadSystem.class);
    Mockito.when(roadSystem.connectConnectors(ArgumentMatchers.any(), ArgumentMatchers.any()))
        .thenReturn(Mockito.mock(Connection.class));

    this.replacementLog = new ReplacementLog();
    this.command = new DragSegmentEndCommand(roadSystem,
        this.replacementLog, this.connector, MOVEMENT);
  }

  @Test
  void testPositionChanges() {
    assumeTrue(ORIGINAL_POSITION.equals(connector.getPosition()));
    command.execute();
    assertEquals(TARGET_POSITION, connector.getPosition());
    command.unexecute();
    assertEquals(ORIGINAL_POSITION, connector.getPosition());
  }

  @Disabled("need to adjust to relative coordinates")
  @Test
  void testConsidersReplacements() {
    // simulate first replacement before execution
    Base firstReplacement = new Base();
    MovableConnector firstReplacementConnector = firstReplacement.getEntry();
    firstReplacementConnector.move(new Movement(connector.getPosition().getX(),
        connector.getPosition().getY()));
    this.replacementLog.replaceElement(segment, firstReplacement);

    command.execute();
    assertEquals(TARGET_POSITION, firstReplacement.getEntry().getPosition());

    // simulate second replacement after execution
    Base secondReplacement = new Base();
    MovableConnector secondReplacementConnector = secondReplacement.getEntry();
    secondReplacementConnector.move(new Movement(firstReplacementConnector.getPosition().getX(),
        firstReplacementConnector.getPosition().getY()));
    this.replacementLog.replaceElement(firstReplacement, secondReplacement);

    command.unexecute();
    assertEquals(ORIGINAL_POSITION, secondReplacement.getEntry().getPosition());
  }
}
