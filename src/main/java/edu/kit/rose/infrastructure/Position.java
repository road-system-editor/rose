package edu.kit.rose.infrastructure;

import java.util.Objects;

/**
 * A Position in a two Dimensional Plane.
 */
public class Position {
  private double coordinateX;
  private double coordinateY;

  /**
   * Standard Constructor.
   * Sets x and y Coordinate to 0.
   */
  public Position() {
    this.coordinateX = 0;
    this.coordinateY = 0;
  }

  /**
   * Constructor.
   *
   * @param coordinateX the x coordinate for this Position.
   * @param coordinateY the y coordinate for this Position.
   */
  public Position(double coordinateX, double coordinateY) {
    this.coordinateX = coordinateX;
    this.coordinateY = coordinateY;
  }

  /**
   * Gives the x axis value of the Position.
   *
   * @return The x axis value of the Position.
   */
  public double getX() {
    return coordinateX;
  }

  /**
   * Sets the x axis value of the Position.
   *
   * @param x The x axis value of the Position.
   */
  public void setX(double x) {
    this.coordinateX = x;
  }

  /**
   * Gives the y axis value of the Position.
   *
   * @return The y axis value of the Position.
   */
  public double getY() {
    return coordinateY;
  }

  /**
   * Sets the y axis value of the Position.
   *
   * @param y The x axis value of the Position.
   */
  public void setY(double y) {
    this.coordinateY = y;
  }

  /**
   * Returns a {@link Position} that is translated by the given {@link Movement} to
   * the current {@link Position}.
   *
   * @param translation the translation to apply to the current {@link Position}
   * @return the translated {@link Position}
   */
  public Position add(Movement translation) {
    return new Position(
        this.getX() + translation.getX(),
        this.getY() + translation.getY());
  }

  /**
   * Provides the distance between this position and a provided position.
   *
   * @param position the other position
   * @return the distance
   */
  public double distanceTo(Position position) {
    var horizontalDistance = coordinateX - position.getX();
    var verticalDistance = coordinateY - position.getY();
    return Math.sqrt(Math.pow(horizontalDistance, 2) + Math.pow(verticalDistance, 2));
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null
        && obj.getClass() == this.getClass()
        && ((Position) obj).getX() == this.getX()
        && ((Position) obj).getY() == this.getY();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getX(), getY());
  }

  @Override
  public String toString() {
    return String.format("Position [x=%f, y=%f]", getX(), getY());
  }
}
