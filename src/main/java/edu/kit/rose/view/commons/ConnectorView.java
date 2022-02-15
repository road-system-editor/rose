package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.roadsystem.elements.Connector;
import java.util.function.Consumer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents a connector of a street segment. Part of a {@link SegmentView}.
 */
public class ConnectorView extends Circle {

  private static final Color COLOR = Color.TRANSPARENT;
  private static final Color COLOR_HOVER = Color.rgb(162, 34, 35)
      .deriveColor(1, 1, 1, 0.5);
  private static final Color COLOR_CONNECT = Color.rgb(140, 182, 60)
      .deriveColor(1, 1, 1, 0.5);

  private final Connector connector;
  private boolean inDragMode = false;

  /**
   * Creates new ConnectorView.
   *
   */
  public ConnectorView(double radius, Connector connector, Consumer<ConnectorView> onDragged) {
    super(radius, COLOR);
    setOnMouseEntered(event -> setFill(COLOR_HOVER));
    setOnMouseExited(event -> setFill(COLOR));
    setPosition(connector.getPosition());
    setOnDragDetected(event -> onDragged.accept(this));
    this.connector = connector;
  }

  public void setPosition(Position position) {
    setCenterX(position.getX());
    setCenterY(position.getY());
  }

  public void setConnectMode(boolean isInConnectMode) {
    var defaultColor = this.inDragMode ? COLOR_HOVER : COLOR;
    setFill(isInConnectMode ? COLOR_CONNECT : defaultColor);
  }

  public void setDragMode(boolean isInDragMode) {
    this.inDragMode = isInDragMode;
  }

  public Connector getConnector() {
    return this.connector;
  }
}
