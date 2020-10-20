package com.fish.game;

import com.fish.model.Coord;

/**
 * Class representing a single move in a game of Fish
 */
public class Move {

  private Coord start;
  private Coord end;

  public Move(Coord start, Coord end) {
    this.start = start;
    this.end = end;
  }


  public Coord getStart() {
    return this.start;
  }

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
