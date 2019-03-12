package edu.kit.informatik.gamecontroll;

/**
 * a Point that is build of an x and y coordinat
 */
public class Coordinat {
    /** the x proportion of the coordinat */
    private int x;
    /** the y proportion of the coordinat */
    private int y;

    /**
     * empty constructor sets x and y on 0
     */
    public Coordinat() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * empty constructor sets x and y on 0
     * @param y the y coordinat
     * @param x the x coordinat
     */
    public Coordinat(int y, int x) {
        this.x = x;
        this.y = y;
    }

    /**
     * setter for the x coordinat
     * @param x the new x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * getter for the x coordinat
     * @return the x coordinat of the point
     */
    public int getX() {
        return this.x;
    }

    /**
     * setter for the y coordinat
     * @param y the new y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * getter for the y coordinat
     * @return the y coordinat of the point
     */
    public int getY() {
        return this.y;
    }

    /**
     * checks if two coordinates are directly above each other
     * @param coordinat the reference coordinat
     * @return false if there are not identical or directly vertial above each other
     */
    public boolean verticalConnection(Coordinat coordinat) {
        return coordinat.x == this.x && Math.abs(coordinat.y - this.y) <= 1;
    }

    /**
     * checks if two coordinates are above each other
     * 
     * @param coordinat the reference coordinat
     * @return false if there are not identical or directly vertial above each other
     */
    public boolean verticalInfinty(Coordinat coordinat) {
        return coordinat.x == this.x;
    }

    /**
     * checks if two coordinates are directly near each other horizontally
     * @param coordinat the reference coordinat
     * @return false if there are not identical or horizontal near each other
     */
    public boolean horizontalConnection(Coordinat coordinat) {
        return coordinat.y == this.y && Math.abs(coordinat.x - this.x) <= 1;
    }

    /**
     * checks if two coordinates are directly near each other horizontally
     * 
     * @param coordinat the reference coordinat
     * @return false if there are not identical or horizontal near each
     *         other
     */
    public boolean horizontalInfinity(Coordinat coordinat) {
        return coordinat.y == this.y;
    }

    /**
     * calculates the horizontal distance between two coordinats
     * @param coordinat the other coordinat
     * @return the horizontal distance between them
     */
    public int distanceHorizontal(Coordinat coordinat) {
        return Math.abs(coordinat.x - this.x);
    }

    /**
     * calculates the vertical distance between two coordinats
     * 
     * @param coordinat the other coordinat
     * @return the vertical distance between them
     */
    public int distanceVertical(Coordinat coordinat) {
        return Math.abs(coordinat.y - this.y);
    }

    /**
     * checks if two objects of the Coordinat class are equal
     * @param referenceCoordinat  the other coordinat
     * @return true if they are the same otherwise false
     */
    public boolean equalsCoordinat(Coordinat referenceCoordinat) {
        if (referenceCoordinat == this) {
            return true;
        }
        return referenceCoordinat.x == this.x && referenceCoordinat.y == this.y;
    }

    @Override
    public String toString() {
        return "y = " + this.y + " |x = " + this.x;
    }
}