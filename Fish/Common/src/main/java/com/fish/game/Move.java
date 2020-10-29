package com.fish.game;

import com.fish.model.Coord;

import java.util.Objects;

/**
 * Class representing a single move in a game of Fish
 *
 * A Move contains the following information:
 *  - The starting Coord of this move
 *  - The ending Coord of this move
 *
 * This class does not validate that this move can be made. It only stores data about
 *  possible moves.
 */
public class Move {

  private Coord start;
  private Coord end;

  public Move(Coord start, Coord end) {
    this.start = start;
    this.end = end;
  }


  /**
   * Get the starting Coord of this move
   *
   * @return the starting move
   */
  public Coord getStart() {
    return this.start;
  }

  /**
   * Get the ending Coord of this move
   *
   * @return the ending move
   */
  public Coord getEnd() {
    return this.end;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Move) {
      Move other = (Move) o;

      return other.getEnd().equals(this.getEnd()) && other.getStart().equals(this.getStart());
    }
    return false;
  }

  /**
   * Creates a unique identifier for this Move object
   * @return a unique integer
   */
  @Override
  public int hashCode() {
    int code = 19;
    code = 37*code + this.getStart().hashCode();
    code = 37*code + this.getEnd().hashCode();

    return Objects.hash(this.getStart(), this.getEnd());
  }

  @Override
  public String toString() {
    Coord s = this.getStart();
    Coord e = this.getEnd();
    return s.getX() + ", " + s.getY() + " -> " + e.getX() + ", " + e.getY();
  }

}
