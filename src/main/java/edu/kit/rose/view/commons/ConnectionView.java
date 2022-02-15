package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.model.roadsystem.elements.Connection;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * Represents a connection between two {@link edu.kit.rose.view.commons.ConnectorView}s.
 */
public class ConnectionView extends Pane implements UnitObserver<Connection> {

  private static final Paint COLOR = Paint.valueOf("#009682"); //KIT green
  private static final double RADIUS = 3.0;
  private static final Point2D PIVOT_POINT = new Point2D(0, RADIUS);

  private final Connection connection;
  private final double connectorDistance;

  private Position center;

  /**
   * Constructs a new ConnectorView. Will connect two provided connector positions.
   *
   * @param connectorPos1 the first connector position.
   * @param connectorPos2 the second connector position.
   */
  public ConnectionView(Point2D connectorPos1, Point2D connectorPos2, Connection connection) {
    this.connection = connection;
    this.connectorDistance = connectorPos1.distance(connectorPos2);
    this.center = connection.getCenter();
    setupShapes();
    setupPosition(connectorPos1);
    setupRotation(calculateAngle(connectorPos1, connectorPos2));
    this.connection.addSubscriber(this);
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

  /**
   * calculates the angle needed for the rotation of the connection view.
   *              A--------------C
   *                \ @           |
   *                  \           |
   *                    \         |
   *                      \       |
   *                        \     |
   *                          \   |
   *                            \ |
   *                              B
   * the angle is transformed afterwards as the angle is needed for rotation.
   * javafx rotates counter clock wise thus the angle needs to undergo the following transformation
   * before being used   (dependent on the position of point B):
   *                    |
   *            180 - @ |  no transformation
   *                    |
   *            --------A--------
   *              -180  |   *(-1)
   *                    |
   *
   * @param connectorPos1 the point A
   * @param connectorPos2 the point B
   * @return the angle @ (after transformation)
   */
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

  @Override
  public void notifyChange(Connection unit) {
    if (unit != this.connection) {
      throw new IllegalArgumentException(
          "Connection views might only observe one connection at a time.");
    }
    var newCenter = unit.getCenter();
    var movement = new Movement(
        newCenter.getX() - this.center.getX(),
        newCenter.getY() - this.center.getY()
    );
    relocate(getLayoutX() + movement.getX(), getLayoutY() + movement.getY());
    this.center = newCenter;
  }
}
