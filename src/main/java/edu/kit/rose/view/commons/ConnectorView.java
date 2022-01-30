package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.elements.Connector;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.function.Function;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Circle;

/**
 * Represents a connector of a street segment. Part of a {@link SegmentView}.
 */
public class ConnectorView extends Circle implements UnitObserver<Connector> {

  private static final Color COLOR = Color.TRANSPARENT;
  private static final Color COLOR_HOVER = Color.RED.deriveColor(1, 1, 1, 0.5);

  /**
   * Creates new ConnectorView.
   *
   * @param connector the connector this connector view is to represent.
   */
  public ConnectorView(Connector connector,double radius) {
    super(radius, COLOR);
    connector.addSubscriber(this);
    setPosition(connector.getPosition());
    setOnMouseEntered(event -> setFill(COLOR_HOVER));
    setOnMouseExited(event -> setFill(COLOR));
  }

  @Override
  public void notifyChange(Connector unit) {
    setPosition(unit.getPosition());
  }

  private void setPosition(Position position) {
    setCenterX(position.getX());
    setCenterY(position.getY());
  }
}
