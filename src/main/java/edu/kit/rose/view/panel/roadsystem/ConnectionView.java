package edu.kit.rose.view.panel.roadsystem;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * Represents a connection between two {@link edu.kit.rose.view.commons.ConnectorView}s.
 */
public class ConnectionView extends Pane {

  private static final Paint COLOR = Paint.valueOf("#009682"); //KIT green
  private static final double RADIUS = 3.0;
  private static final Point2D PIVOT_POINT = new Point2D(0, RADIUS);

  private final double connectorDistance;

  /**
   * Constructs a new ConnectorView. Will connect two provided connector positions.
   *
   * @param connectorPos1 the first connector position.
   * @param connectorPos2 the second connector position.
   */
  public ConnectionView(Point2D connectorPos1, Point2D connectorPos2) {
    this.connectorDistance = connectorPos1.distance(connectorPos2);
    setupShapes();
    setupPosition(connectorPos1);
    setupRotation(calculateAngle(connectorPos1, connectorPos2));
  }

  private void setupShapes() {
    var rectangle = new Rectangle(connectorDistance, RADIUS * 2, COLOR);
    getChildren().add(rectangle);
    var circle1 = new Circle(connectorDistance, RADIUS, RADIUS, COLOR);
    getChildren().add(circle1);
    var circle2 = new Circle(0, RADIUS, RADIUS, COLOR);
    getChildren().add(circle2);
  }

  private void setupPosition(Point2D connectorPos1) {
    var layoutPos = connectorPos1.subtract(PIVOT_POINT);
    relocate(layoutPos.getX(), layoutPos.getY());
  }

  private double calculateAngle(Point2D connectorPos1, Point2D connectorPos2) {
    var thirdPoint = new Point2D(connectorPos2.getX(), connectorPos1.getY());
    var angle = connectorPos1.angle(thirdPoint, connectorPos2);
    if (connectorPos1.getY() <= connectorPos2.getY()) {
      if (connectorPos1.getX() <= connectorPos2.getX()) {
        angle = -angle;
      } else {
        angle -= 180;
      }
    } else if (connectorPos1.getX() > connectorPos2.getX()) {
      angle = 180 - angle;
    }
    return -angle;
  }

  private void setupRotation(double angle) {
    var rotation = new Rotate(angle, PIVOT_POINT.getX(), PIVOT_POINT.getY());
    getTransforms().add(rotation);
  }
}
