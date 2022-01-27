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

  @BeforeEach
  public void setUp() {
    this.accessors = new ArrayList<>();
    this.accessors.add(attribute);
    this.connector = new Connector(ConnectorType.ENTRY, new Position(X, Y), accessors);
  }

  @Test
  public void getAttributeAccessors() {
    Assertions.assertIterableEquals(accessors, this.connector.getAttributeAccessors());
  }

  @Test
  public void getType() {
    Assertions.assertEquals(ConnectorType.ENTRY, this.connector.getType());
  }

  @Test
  public void move() {
    Movement move = new Movement();
    move.setX(X);
    move.setY(Y);

    Assertions.assertEquals(X, this.connector.getPosition().getX());
    Assertions.assertEquals(Y, this.connector.getPosition().getY());

    this.connector.move(move);

    Assertions.assertEquals(X + X, this.connector.getPosition().getX());
    Assertions.assertEquals(Y + Y, this.connector.getPosition().getY());
  }
}