package com.fish.model.state;


import com.fish.model.Coord;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of an internal player of a game of Hey, That's my Fish! (HTMF)
 *
 * Interpretation: a class to track information about a game participant's standings in a game,
 * which includes
 * --their unique assigned avatar color
 * --their score as it changes, and
 * --the location of their penguins on the board as the game progresses.
 */
public class HexPlayer implements InternalPlayer {

  private PlayerColor color;
  private int score;
  private List<Coord> penguinLocs;

  public HexPlayer(PlayerColor pc) {
    this.color = pc;
    this.score = 0;
    this.penguinLocs = new ArrayList<>();
  }

  private HexPlayer(PlayerColor pc, List<Coord> locs, int score) {
    this.color = pc;
    this.penguinLocs = new ArrayList<>(locs);
    this.score = score;
  }


  /**
   * Adds a penguin to the penguinLocs list.
   * Interpretation: adds a penguin to the board during the PLACING_PENGUINS stage.
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
   * Moves the location of a penguin avatar by removing its original Coord location from the list of
   * penguin locations and adding its new destination location to the end of the List.
   * @param origin the Tile to move from
   * @param destination the Tile to move to
   * @throws IllegalArgumentException if the origin Tile does not have a penguin on it to move,
   * or if the destination Tile already has a penguin on it, blocking the move.
   */
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
   * Add points to this player's score.
   *
   * @param points (int) the points to add
   */
  public void addToScore(int points) {
    this.score += points;
  }

  /**
   * Returns a copy of this player's current penguin locations
   * in terms of their Coord location on the GameBoard.
   *
   * @return a List of Coord of the player's penguin locations
   */
  public List<Coord> getPenguinLocs() {
    return new ArrayList<>(this.penguinLocs);
  }

  /**
   * Returns this player's assigned avatar color as a PlayerColor.
   *
   * @return (PlayerColor) the player's color
   */
  public PlayerColor getColor() {
    return this.color;
  }

  /**
   * Returns this player's current score.
   *
   * @return (int) this player's score
   */
  public int getScore() {
    return this.score;
  }

  /**
   * Returns a defensive copy of this HexPlayer, meaning any change to the resulting copy
   * will never alter the original object.
   * @return the HexPlayer
   */
  public InternalPlayer getCopyPlayer() {
    return new HexPlayer(this.color, this.getPenguinLocs(), this.score);
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
