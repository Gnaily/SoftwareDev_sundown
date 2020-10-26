package com.fish.model.state;

import com.fish.model.Coord;
import java.util.List;

/**
 * Interface for an internal player of a game of Hey, That's my Fish! (HTMF)
 *
 * Interpretation: This interface is to track the score, avatar color, and penguins of a game
 * participant, but not to track information about the human subject behind the player, such as
 * their age.
 */
public interface InternalPlayer {

  /**
   * Adds a penguin to the penguinLocs list.
   * Interpretation: adds a penguin to the board during the PLACING_PENGUINS stage.
   * @param location the coordinate location to place the penguin
   * @throws IllegalArgumentException if player places two penguins in one location
   */
  void placePenguin(Coord location);

  /**
   * Moves the location of a penguin avatar by removing its original Coord location from the list of
   * penguin locations and adding its new destination location to the end of the List.
   * @param origin the Tile to move from
   * @param destination the Tile to move to
   * @throws IllegalArgumentException if the origin Tile does not have a penguin on it to move,
   * or if the destination Tile already has a penguin on it, blocking the move.
   */
  void movePenguin(Coord origin, Coord destination);

  /**
   * Add points to this player's score.
   *
   * @param points (int) the points to add
   */
  void addToScore(int points);

  /**
   * Returns a copy of this player's current penguin locations
   * in terms of their Coord location on the GameBoard.
   *
   * @return a List of Coord of the player's penguin locations
   */
  List<Coord> getPenguinLocs();

  /**
   * Returns this player's assigned avatar color as a PlayerColor.
   *
   * @return (PlayerColor) the player's color
   */
  PlayerColor getColor();

  /**
   * Returns this player's current score.
   *
   * @return (int) this player's score
   */
  int getScore();

  /**
   * Returns a defensive copy of this HexPlayer, meaning any change to the resulting copy
   * will never alter the original object.
   * @return the HexPlayer
   */
  InternalPlayer getCopyPlayer();

}
