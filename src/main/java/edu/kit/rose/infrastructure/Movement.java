package edu.kit.rose.infrastructure;

/**
 * A movement in form of a 2D Vector.
 */
public class Movement {

    private int x;
    private int y;

    /**
     * Gives the x axis value of the Vector.
     * @return The x axis value of the Vector.
     */
    int getX() {return x;}

    /**
     * Gives the y axis value of the Vector.
     * @return The y axis value of the Vector.
     */
    int getY(){return y;}

    /**
     * Sets the x axis value of the Vector.
     * @param x The x axis value of the Vector.
     */
    void setX(int x){this.x = x;}

    /**
     * Sets the y axis value of the Vector.
     * @param y The x axis value of the Vector.
     */
    void setY(int y){this.y = y;}
}