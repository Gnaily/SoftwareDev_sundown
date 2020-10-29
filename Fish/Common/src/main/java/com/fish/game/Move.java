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

  private Coord origin;
  private Coord destination;

  public Move(Coord origin, Coord destination) {
    this.origin = origin;
    this.destination = destination;
  }


  /**
   * Get the starting Coord of this move
   *
   * @return the starting move
   */
  public Coord getOrigin() {
    return this.origin;
  }

  /**
   * Get the ending Coord of this move
   *
   * @return the ending move
   */
  public Coord getDestination() {
    return this.destination;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Move) {
      Move other = (Move) o;

      return other.getDestination().equals(this.getDestination()) && other.getOrigin().equals(this.getOrigin());
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
    code = 37*code + this.getOrigin().hashCode();
    code = 37*code + this.getDestination().hashCode();

    return Objects.hash(this.getOrigin(), this.getDestination());
  }

  @Override
  public String toString() {
    Coord s = this.getOrigin();
    Coord e = this.getDestination();
    return s.getX() + ", " + s.getY() + " -> " + e.getX() + ", " + e.getY();
  }

}
