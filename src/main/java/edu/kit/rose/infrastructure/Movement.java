package edu.kit.rose.infrastructure;

/**
 * A movement in form of a 2D Vector.
 */
public class Movement {

  private double xvalue;
  private double yvalue;

  /**
   * Standard Constructor.
   * Sets x and y values to 0.
   */
  public Movement() {
    this.xvalue = 0;
    this.yvalue = 0;
  }

  /**
   * Constructor.
   *
   * @param xvalue the x value of the Vector.
   * @param yvalue the y value of the Vector.
   */
  public Movement(double xvalue, double yvalue) {
    this.xvalue = xvalue;
    this.yvalue = yvalue;
  }

  /**
   * Gives the x axis value of the Vector.
   *
   * @return The x axis value of the Vector.
   */
  public double getX() {
    return xvalue;
  }

  /**
   * Sets the x axis value of the Vector.
   *
   * @param x The x axis value of the Vector.
   */
  public void setX(double x) {
    this.xvalue = x;
  }

  /**
   * Gives the y axis value of the Vector.
   *
   * @return The y axis value of the Vector.
   */
  public double getY() {
    return yvalue;
  }

  /**
   * Sets the y axis value of the Vector.
   *
   * @param y The x axis value of the Vector.
   */
  public void setY(double y) {
    this.yvalue = y;
  }
}
