package edu.kit.rose.controller.commons;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.elements.Connection;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.Objects;

/**
 * Utility class that helps with copying a
 * {@link edu.kit.rose.model.roadsystem.elements.Connection}
 * to a road system while considering replacements from a {@link ReplacementLog}.
 */
public class ConnectionCopier {
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
   *
   * @param connection
   * @return
   */
  public Connection copyConnection(Connection connection) {

    if (connection.getConnectors().getSize() != 2) {
      return null;
    }

    Connector segment1Connector = null;
    Connector segment2Connector = null;
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
