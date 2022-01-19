package edu.kit.rose.infrastructure;

/**
 * A Position in a two Dimensional Plane.
 */
public class Position {

  private int xcoordinate;
  private int ycoordinate;

  /**
   * Gives the x axis value of the Position.
   *
   * @return The x axis value of the Position.
   */
  int getX() {
    return xcoordinate;
  }

  /**
   * Sets the x axis value of the Position.
   *
   * @param x The x axis value of the Position.
   */
  void setX(int x) {
    this.xcoordinate = x;
  }

  /**
   * Gives the y axis value of the Position.
   *
   * @return The y axis value of the Position.
   */
  int getY() {
    return ycoordinate;
  }

  /**
   * Sets the y axis value of the Position.
   *
   * @param y The x axis value of the Position.
   */
  void setY(int y) {
    this.ycoordinate = y;
  }
}
