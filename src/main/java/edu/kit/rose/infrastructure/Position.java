package edu.kit.rose.infrastructure;

/**
 * A Position in a two Dimensional Plane
 */
public class Position {

    private int x;
    private int y;

    /**
     * Gives the x axis value of the Position.
     * @return The x axis value of the Position.
     */
    int getX() {return x;}

    /**
     * Gives the y axis value of the Position.
     * @return The y axis value of the Position.
     */
    int getY(){return y;}

    /**
     * Sets the x axis value of the Position.
     * @param x The x axis value of the Position.
     */
    void setX(int x){this.x = x;}

    /**
     * Sets the y axis value of the Position.
     * @param y The x axis value of the Position.
     */
    void setY(int y){this.y = y;}
}
