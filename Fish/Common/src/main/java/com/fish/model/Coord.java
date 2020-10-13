package com.fish.model;

/**
 * Represents a single point on a Cartesian coordinate plane with an x and y value,
 * denoted xx and yy.
 */
public class Coord {

  private int xx;
  private int yy;

  /**
   * Constructor to instantiate a Coord object.
   * @param xx the x point
   * @param yy the y point
   */
  public Coord(int xx, int yy) {
    this.xx = xx;
    this.yy = yy;

  }

  /**
   * Returns the Coordinate point's x-value
   * @return the x value
   */
  public int getX() {
    return this.xx;
  }

  /**
   * Returns the Coordinate point's y-value
   * @return the y value
   */
  public int getY() {
    return this.yy;
  }

  /**
   * Defines whether the given Coord is equal to this coord,
   * defined by whether both the x-coordinate and the y-coordinate of the given object
   * are the same as x-coordinate and the y-coordinate of this Coord.
   * @param o
   * @return a boolean that determines if the given object is equal to this object
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Coord) {
      Coord other = (Coord) o;
      return this.xx == other.xx && this.yy == other.yy;
    }

    return false;
  }

  /**
   * Creates a unique identifier for this Coord object
   * @return a unique integer
   */
  @Override
  public int hashCode() {
    int code = 17;
    code = 31*code + xx;
    code = 31*code + yy;

    return code;
  }
}
