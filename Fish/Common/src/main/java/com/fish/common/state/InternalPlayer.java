package com.fish.common.state;

import com.fish.common.Coord;
import java.util.List;

/**
 * Interface for an internal player of a game of Hey, That's my Fish! (HTMF)
 *
 * DATA DEFINITION:
 * A structure that contains the current state of one internal player in an ongoing game.
 *
 * InternalPlayer extends ProtectedPlayer, a read-only version of the InternalPlayer interface.
 * When communicating with external players, only Protected versions of objects are shared.
 *
 * An InternalPlayer does NOT contain the personal information of the external player behind the
 * internal player.
 * An implementation must contain data re:
 * -- the location of penguin avatars of this player
 * -- assigned PlayerColor
 * -- player score (int)
 *
 * INTERPRETATION:
 * Represents one participant in a game of HTMF that has control over their color-coded penguin
 * avatars. The participant moves their penguins throughout the game to acquire fish from the
 * tiles they land on, which adds to their score each time they move.
 */
public interface InternalPlayer extends ProtectedPlayer {

  /**
   * Adds a penguin to this player's penguin avatars and tracks the Coord location of that penguin.
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
   * Returns a deep copy of this player
   * @return a deep copy of this Player
   */
  InternalPlayer getCopyPlayer();

}
