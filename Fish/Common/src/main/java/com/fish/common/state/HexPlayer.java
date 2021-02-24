package com.fish.common.state;

import com.fish.common.Coord;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of an internal player of a game of Hey, That's my Fish! (HTMF)
 *
 * DATA DEFINITION:
 * color: this player's assigned color, one of four defined in the ENUM PlayerColor, assigned by
 *      the Referee component when the Referee is building the game.
 * score: the accumulated score of this player
 * penguinLocs: A list of Coord locations that indicate the precise locations of this player's
 *      penguin avatars on the board during a game. When the player places a penguin, the Coord
 *      location of it is added to the back of this list.
 *      When a player moves its penguin, the original Coord location of that penguin is REPLACED
 *      with the new one in the same index of the list as the old one, such that the list of
 *      penguinLocs is always ordered in the same order.
 *
 * Interpretation:
 * Represents one participant in a game of HTMF that has control over their color-coded penguin avatars.
 */
public class HexPlayer implements InternalPlayer {

  private PlayerColor color;
  private int score;
  private List<Coord> penguinLocs;

  /**
   * Constructor to create a player at the beginning of a game of HTMF
   * with score of zero, no penguins placed, and the assigned PlayerColor passed in.
   * @param pc the assigned color of the player
   */
  public HexPlayer(PlayerColor pc) {
    this.color = pc;
    this.score = 0;
    this.penguinLocs = new ArrayList<>();
  }

  /**
   * Convenience constructor to create a player example from the middle of an ongoing game.
   * @param pc the assigned color of the player
   * @param penguinLocs the Coord locations of the player's penguins
   * @param score the player's current score
   */
  private HexPlayer(PlayerColor pc, List<Coord> penguinLocs, int score) {
    this.color = pc;
    this.penguinLocs = new ArrayList<>(penguinLocs);
    this.score = score;
  }


  /**
   * Adds a penguin to this player's penguin avatars and tracks the Coord location of that penguin.
   * @param location the coordinate location to place the penguin
   * @throws IllegalArgumentException if player places two penguins in one location
   */
  public void placePenguin(Coord location) throws IllegalArgumentException {
    if (this.penguinLocs.contains(location)) {
      throw new IllegalArgumentException("You cannot place two penguins in the same location.");
    }
    else {
      this.penguinLocs.add(location);
    }
  }

  /**
   * Moves the location of a penguin avatar by
   * replacing its original Coord with its new Coord
   * in the same index space on the penguinLocs list, such that the penguinLocs list is always
   * in the same order.
   * @param origin the Tile to move from
   * @param destination the Tile to move to
   * @throws IllegalArgumentException if the origin Tile does not have a penguin on it to move,
   * or if the destination Tile already has a penguin on it, blocking the move.
   */
  @Override
  public void movePenguin(Coord origin, Coord destination) throws IllegalArgumentException {
    if (this.penguinLocs.contains(destination)) {
      throw new IllegalArgumentException("You already have a penguin here; move elsewhere.");
    }
    if (!this.penguinLocs.contains(origin)) {
      throw new IllegalArgumentException("You do not have a penguin to move on the origin.");
    }
    else {
      int index = this.penguinLocs.indexOf(origin);
      this.penguinLocs.remove(origin);
      this.penguinLocs.add(index, destination);
    }
  }

  /**
   * Add the given amount of points to this player's score.
   *
   * @param points (int) the points to add
   */
  @Override
  public void addToScore(int points) {
    this.score += points;
  }

  /**
   * Returns a copy of this player's current penguin locations
   * in terms of their Coord location on the GameBoard.
   *
   * @return a List of Coord of the player's penguin locations
   */
  @Override
  public List<Coord> getPenguinLocs() {
    return new ArrayList<>(this.penguinLocs);
  }

  @Override
  public InternalPlayer getCopyPlayer() {
    return new HexPlayer(this.color, this.getPenguinLocs(), this.score);
  }

  @Override
  public PlayerColor getColor() {
    return this.color;
  }

  @Override
  public int getScore() {
    return this.score;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof HexPlayer) {
      HexPlayer other = (HexPlayer) o;
      if (this.penguinLocs.size() == other.getPenguinLocs().size()) {
        for (int ii = 0; ii < this.penguinLocs.size(); ii++) {
          if (!this.penguinLocs.get(ii).equals(other.penguinLocs.get(ii))) {
            return false;
          }
        }
      }
      else {
        return false;
      }

      return this.score == other.getScore()
          && this.color == other.getColor()
          && this.penguinLocs.equals(other.getPenguinLocs());
    }
    return false;
  }
}
