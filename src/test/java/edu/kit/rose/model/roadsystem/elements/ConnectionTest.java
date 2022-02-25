package edu.kit.rose.model.roadsystem.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import edu.kit.rose.infrastructure.Position;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Connection}.
 */
public class ConnectionTest {
  private Connector connector1;
  private Connector connector2;
  private Connector connector3;

  private Position center;
  private Connection testConnection;

  @BeforeEach
  void setup() {
    this.connector1 = new Connector(ConnectorType.ENTRY, mock(Position.class), List.of());
    this.connector2 = new Connector(ConnectorType.EXIT, mock(Position.class), List.of());
    this.connector3 = new Connector(ConnectorType.RAMP_ENTRY, mock(Position.class), List.of());
    this.center = new Position(18, 15);

    this.testConnection = new Connection(connector1, connector2, this.center);
  }

  @Test
  void testConstructorValidatesParameters() {
    assertThrows(NullPointerException.class,
        () -> new Connection(null, connector2, this.center));
    assertThrows(NullPointerException.class,
        () -> new Connection(connector1, null, this.center));
    assertThrows(NullPointerException.class,
        () -> new Connection(connector1, connector2, null));
    assertThrows(IllegalArgumentException.class,
        () -> new Connection(connector1, connector1, this.center));
  }

  @Test
  void testConstructorCopiesPosition() {
    assertNotSame(this.center, this.testConnection.getCenter());
    assertEquals(this.center, this.testConnection.getCenter());
  }

  @Test
  void testConstructorSetsConnectors() {
    Set<Connector> expectedConnectors = Set.of(connector1, connector2);
    Set<Connector> actualConnectors =
        this.testConnection.getConnectors().stream().collect(Collectors.toSet());
    assertEquals(expectedConnectors, actualConnectors);
  }

  @Test
  void testGetOther() {
    assertThrows(IllegalArgumentException.class,
        () -> testConnection.getOther(connector3));
    assertEquals(testConnection.getOther(connector1), connector2);
    assertEquals(testConnection.getOther(connector2), connector1);
  }

  @Test
  void testGetThis() {
    assertSame(testConnection, testConnection.getThis());
  }
}
