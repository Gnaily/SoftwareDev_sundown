package com.fish.game;

import com.fish.model.Coord;

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
    int code = 17;
    code = 31*code + this.getStart().hashCode();
    code = 31*code + this.getEnd().hashCode();

    return code;
  }

}
