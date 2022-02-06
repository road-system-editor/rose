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
public class ConnectorView extends Circle {

  private static final Color COLOR = Color.BLUE;
  private static final Color COLOR_HOVER = Color.RED.deriveColor(1, 1, 1, 0.5);

  /**
   * Creates new ConnectorView.
   *
   */
  public ConnectorView(double radius, Position position) {
    super(radius, COLOR);
    setOnMouseEntered(event -> setFill(COLOR_HOVER));
    setOnMouseExited(event -> setFill(COLOR));
    setPosition(position);
  }


  public void setPosition(Position position) {
    setCenterX(position.getX());
    setCenterY(position.getY());
  }
}
