package edu.kit.rose.infrastructure;

/**
 * A Position in a two Dimensional Plane.
 */
public class Position {

  private double xcoordinate;
  private double ycoordinate;

  /**
   * Standard Constructor.
   * Sets x and y Coordinate to 0.
   */
  public Position() {
    this.xcoordinate = 0;
    this.ycoordinate = 0;
  }

  /**
   * Constructor.
   *
   * @param xcoordinate the x coordinate for this Position.
   * @param ycoordinate the y coordinate for this Position.
   */
  public Position(double xcoordinate, double ycoordinate) {
    this.xcoordinate = xcoordinate;
    this.ycoordinate = ycoordinate;
  }

  /**
   * Gives the x axis value of the Position.
   *
   * @return The x axis value of the Position.
   */
  public double getX() {
    return xcoordinate;
  }

  /**
   * Sets the x axis value of the Position.
   *
   * @param x The x axis value of the Position.
   */
  public void setX(double x) {
    this.xcoordinate = x;
  }

  /**
   * Gives the y axis value of the Position.
   *
   * @return The y axis value of the Position.
   */
  public double getY() {
    return ycoordinate;
  }

  /**
   * Sets the y axis value of the Position.
   *
   * @param y The x axis value of the Position.
   */
  public void setY(double y) {
    this.ycoordinate = y;
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
   * Returns a scalar multiplied version of the current {@link Position}.
   *
   * @param scalar the scalar to apply to the current {@link Position}
   * @return the scalar multiplied {@link Position}
   */
  public Position scalarMultiply(int scalar) {
    return new Position(this.getX() * scalar, this.getY() * scalar);
  }
}
