package com.fish.common.state;

import com.fish.common.Coord;
import com.fish.common.board.*;
import com.fish.common.tile.ProtectedTile;
import java.util.List;
import java.util.Map;


/**
 * The extended, read-only interface of a GameState.
 * A ProtectedGameState cannot be mutated, and its only functionality is information retrieval.
 *
 * When communicating with external players, only Protected versions of objects are shared.
 *
 * Please see GameState data definition/interpretation for more information on the properties
 * of a GameState.
 */
public interface ProtectedGameState {

  /**
   * Returns a ProtectedGameState, which is immutable.
   * @return this GameState as a ProtectedGameState
   */
  ProtectedGameState getGameState();

  /**
   * Returns the GameStage enumeration that represents the current stage of the game, either
   * NOT_STARTED, PLACING_PENGUINS, IN_PLAY, or GAMEOVER.
   * @return the current GameStage
   */
  GameStage getGameStage();

  /**
   * Returns a copy of this GameState's current GameBoard as a ProtectedGameBoard.
   * The ProtectedGameBoard is immutable.
   * @return a ProtectedGameBoard
   */
  ProtectedGameBoard getGameBoard();

  /**
   * Returns an ordered list of ProtectedPlayer,
   * in the order of player turn,
   * where index 0 is the current player whose turn it is at the time the method is called.
   * Note that as the player turns proceed the list is rotated in order so that the above statement
   * is always true.
   * @return a list of ProtectedPlayer
   */
  List<ProtectedPlayer> getPlayers();

  /**
   * Constructs a hashmap of PlayerColor to int
   * representing the current score of each player with that avatar color
   * @return a hashmap of playerColor to score
   */
  Map<PlayerColor, Integer> getScoreBoard();

  /**
   * Returns a HashMap of all current penguin locations on the board, formatted such that
   * the Coord is the unique key (since only one penguin can be on a tile at a time) and
   * the PlayerColor is the value, to identify which player's penguin is on that location.
   * @return a HashMap of Coord to PlayerColor values
   */
  Map<Coord, PlayerColor> getPenguinLocations();

  /**
   * Given a PlayerColor, returns a list of Coord locations of all that player's penguins on the
   * board.
   * May return an empty list if:
   *  -- the player with the given player color has not placed any penguins or
   *  -- the given player color is not in this game
   *     (eg it is RED but there are no red avatars in this particular game at this time)
   *
   * @param playerColor the PlayerColor to search for penguin locations
   * @return a list of Coords representing that player's penguin locations
   */
  List<Coord> getPenguinLocationsOf(PlayerColor playerColor);

  /**
   * Return the current Internal Player's PlayerColor
   *
   * @return (PlayerColor) the current player's color
   */
  PlayerColor getCurrentPlayer();

  /**
   * Retrieves the Tile located at the coordinate in the GameBoard.
   * @param loc the coordinate location of the desired Tile
   * @return the Tile object located there
   */
  ProtectedTile getTileAt(Coord loc);

  /**
   * Return a list of Coordinates of all the tiles reachable from a starting
   * Coord given the location of all the other penguins on the board
   * @param start the starting coordinate location
   * @return the list of possible tiles to move to
   */
  List<Coord> getTilesReachableFrom(Coord start);

  /**
   * Returns the width of the game board, defined by the number of columns on the board data
   * representation.
   * @return an int with the width
   */
  int getWidth();

  /**
   * Returns the height of the game board, defined by the number of rows on the board data
   * representation.
   * @return an int with the height
   */
  int getHeight();

}
