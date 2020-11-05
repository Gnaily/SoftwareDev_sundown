package com.fish.model.state;

import com.fish.model.Coord;
import java.util.List;

/**
 * The extended, read-only interface of an InternalPlayer.
 * A ProtectedPlayer cannot be mutated, and its only functionality is information retrieval.
 *
 * When communicating with external players, only Protected versions of objects are shared.
 *
 * Please see InternalPlayer data definition/interpretation for more information on the properties
 * of an InternalPlauer.
 */
public interface ProtectedPlayer {

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

}
