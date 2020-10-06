package com.fish.model;

// add comments/explanations
public class Coord {

  private int xx;
  private int yy;

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

  @Override
  public boolean equals(Object o) {
    if (o instanceof Coord) {
      Coord other = (Coord) o;
      return this.xx == other.xx && this.yy == other.yy;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return xx * 67 + yy * 71;
  }
}
