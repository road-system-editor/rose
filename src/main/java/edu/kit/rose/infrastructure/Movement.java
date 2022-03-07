package edu.kit.rose.infrastructure;

import java.util.Objects;

/**
 * A movement in form of a 2D Vector.
 */
public class Movement {
  private final double deltaX;
  private final double deltaY;

  /**
   * Standard Constructor.
   * Sets x and y values to 0.
   */
  public Movement() {
    this(0, 0);
  }

  /**
   * Constructor.
   *
   * @param deltaX the x value of the Vector.
   * @param deltaY the y value of the Vector.
   */
  public Movement(double deltaX, double deltaY) {
    this.deltaX = deltaX;
    this.deltaY = deltaY;
  }

  /**
   * Gives the x-axis value of the Vector.
   *
   * @return The x-axis value of the Vector.
   */
  public double getX() {
    return deltaX;
  }


  /**
   * Gives the y-axis value of the Vector.
   *
   * @return The y-axis value of the Vector.
   */
  public double getY() {
    return deltaY;
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null
        && obj.getClass() == this.getClass()
        && ((Movement) obj).getX() == getX()
        && ((Movement) obj).getY() == getY();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getX(), getY());
  }

  @Override
  public String toString() {
    return String.format("Movement [dx=%f, dy=%f]", getX(), getY());
  }
}
