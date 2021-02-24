package com.fish.common;

/**
 * DATA: A single point on a Cartesian coordinate plane, indicated by an x and y value.
 *
 * INTERPRETATION/In relation to HTMF:
 * The location of a Tile in the collection of tiles that make up the board the game is played on.
 * The 0,0 Coord is in the upper left hand corner of the board.
 * xx represents the column number, and xx increases the further right the column is.
 * yy represents the row number, and yy increases the further down the row is.
 */
public class Coord {

  private int xx;
  private int yy;

  /**
   * Only constructor to create a Coord object.
   * @param xx the x point, or column
   * @param yy the y point, or row
   */
  public Coord(int xx, int yy) {
    this.xx = xx;
    this.yy = yy;

  }

  public int getX() {
    return this.xx;
  }

  public int getY() {
    return this.yy;
  }

  /**
   * Defines whether the given Coord is equal to this Coord,
   * defined by whether both the x-coordinate and the y-coordinate of the given object
   * are the same integer value as x-coordinate and the y-coordinate of this Coord.
   * @param o the object to compare to this Coord
   * @return a boolean determining if the given object is equal to this object(t) or not(f)
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
   * Creates a unique identifier for this Coord object.
   * @return a unique integer
   */
  @Override
  public int hashCode() {
    int code = 17;
    code = 31*code + xx;
    code = 31*code + yy;

    return code;
  }

  @Override
  public String toString() {
    return this.xx + " : " + this.yy;
  }
}
