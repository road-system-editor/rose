package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Unit Test for {@link Connector}.
 */
class ConnectorTest {
  private static final int X = 1;
  private static final int Y = 1;
  /**
   * This is a dummy accessor whose getters and setters don't do anything.
   */
  private static final AttributeAccessor<?> attribute = new AttributeAccessor<>(
      AttributeType.COMMENT,
      () -> null,
      newValue -> {});
  private ArrayList<AttributeAccessor<?>> accessors;
  private Connector connector;
  private MovableConnector movableConnector;

  @BeforeEach
  public void setUp() {
    this.accessors = new ArrayList<>();
    this.accessors.add(attribute);
    this.connector = new Connector(ConnectorType.ENTRY, new Position(X, Y), accessors);
    this.movableConnector =
        new MovableConnector(ConnectorType.ENTRY, new Position(X, Y), accessors);
  }

  @Test
  void getAttributeAccessors() {
    Assertions.assertIterableEquals(accessors, this.connector.getAttributeAccessors());
  }

  @Test
  void getType() {
    Assertions.assertEquals(ConnectorType.ENTRY, this.connector.getType());
  }

  @Test
  void move() {
    Movement move = new Movement(X, Y);

    Assertions.assertEquals(X, this.connector.getPosition().getX());
    Assertions.assertEquals(Y, this.connector.getPosition().getY());

    this.connector.move(move);

    Assertions.assertEquals(X, this.connector.getPosition().getX());
    Assertions.assertEquals(Y, this.connector.getPosition().getY());
  }

  @Test
  void moveMovableConnector() {
    Movement move = new Movement(X, Y);

    Assertions.assertEquals(X, this.movableConnector.getPosition().getX());
    Assertions.assertEquals(Y, this.movableConnector.getPosition().getY());

    this.movableConnector.move(move);

    Assertions.assertEquals(X + X, this.movableConnector.getPosition().getX());
    Assertions.assertEquals(Y + Y, this.movableConnector.getPosition().getY());
  }
}