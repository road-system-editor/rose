package edu.kit.rose.model.roadsystem.elements;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.RoseBox;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


/**
 * Tests {@link Connection}.
 */
public class ConnectionTest {

  private static final Connector CONNECTOR1 = new Connector(ConnectorType.ENTRY,
      Mockito.mock(Position.class), List.of());
  private static final Connector CONNECTOR2 = new Connector(ConnectorType.EXIT,
      Mockito.mock(Position.class), List.of());
  private static final Connector CONNECTOR3 = new Connector(ConnectorType.RAMP_ENTRY,
      Mockito.mock(Position.class), List.of());

  private static Connection testConnection;

  @BeforeEach
  void setup() {
    testConnection = new Connection(CONNECTOR1, CONNECTOR2);
  }

  @Test
  void testGetConnectors() {
    Assertions.assertEquals(2, testConnection.getConnectors().getSize());
    testConnection.getConnectors().forEach(
        c -> Assertions.assertTrue(c == CONNECTOR1 || c == CONNECTOR2));
  }

  @Test
  void testGetOther() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> testConnection.getOther(CONNECTOR3));
    Assertions.assertEquals(testConnection.getOther(CONNECTOR1), CONNECTOR2);
    Assertions.assertEquals(testConnection.getOther(CONNECTOR2), CONNECTOR1);
  }

}
