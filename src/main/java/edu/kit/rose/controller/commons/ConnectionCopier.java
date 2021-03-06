package edu.kit.rose.controller.commons;

import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import java.util.Objects;

/**
 * Utility class that helps with copying a
 * {@link edu.kit.rose.model.roadsystem.elements.Connection}
 * to a road system while considering replacements from a {@link ReplacementLog}.
 */
public class ConnectionCopier {

  private static final String INVALID_CONNECTOR_COUNT_ERROR_MESSAGE
      = "Connection has invalid number of Connectors.";

  private final ReplacementLog replacementLog;
  private final RoadSystem target;
  private final boolean makeReplacement;

  /**
   * Constructor.
   *
   * @param replacementLog the log that stores the replacement. Can be null if the copy methods
   *                       should not make a replacement.
   * @param target         the roadSystem where the copies will be created.
   */
  public ConnectionCopier(ReplacementLog replacementLog, RoadSystem target) {
    this.replacementLog = replacementLog;
    makeReplacement = replacementLog != null;
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Copies a {@link Connection} and returns the copy.
   *
   * @param connection the {@link Connection} to copy
   * @return the copy of the {@link Connection}
   */
  public Connection copyConnection(Connection connection) {

    if (connection.getConnectors().getSize() != 2) {
      throw new IllegalArgumentException(INVALID_CONNECTOR_COUNT_ERROR_MESSAGE);
    }

    Connector segment1Connector;
    Connector segment2Connector;
    if (makeReplacement) {
      segment1Connector = this.replacementLog.getCurrentConnectorVersion(
          connection.getConnectors().get(0));
      segment2Connector = this.replacementLog.getCurrentConnectorVersion(
          connection.getConnectors().get(1));
    } else {
      segment1Connector = connection.getConnectors().get(0);
      segment2Connector = connection.getConnectors().get(1);
    }

    return this.target.connectConnectors(segment1Connector, segment2Connector);
  }
}
