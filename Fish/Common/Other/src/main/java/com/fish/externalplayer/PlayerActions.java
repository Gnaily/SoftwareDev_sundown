package com.fish.externalplayer;

import com.google.gson.JsonObject;

import com.fish.model.Coord;
import com.fish.model.state.GameStage;
import com.fish.model.state.HexPlayer;
import com.fish.model.state.PlayerColor;

import java.util.List;
import java.util.Map;

/**
 * External interface defining what actions and requests an external player may make of the Referee in a game
 *  of HTMF. It also outlines what messages the player can expect to receive from the referee.
 *
 * This a method of connecting a player to what is happening in the game they are a part of.
 *
 * This interface defines what a player must be able to do in order to adhere to the HTMF player-
 *  protocol. An implementation of this interface must be used as defined in the protocol.
 */
public interface PlayerActions {

  /// --- GETTERS --- ///

  /**
   * Returns what phase of the game (NOT_STARTED, PLACING_PENGUINS, IN_PLAY, GAME_OVER) is currently
   *  active.
   *
   * @return The current stage of the game being played.
   */
  GameStage getCurrentGameStage();

  /**
   * Return the HexPlayer whose turn it is, represented by the color of their avatars.
   *
   * @return The PlayerColor of the current HexPlayer
   */
  PlayerColor getCurrentPlayer();

  /**
   * Get and ordered list of Players in the order that they take turns. The first player in this
   *  list is the first player who took a turn in this game.
   *
   * Each HexPlayer in the list has their age, color, and current score associated with them. This
   *  method can be used to retrieve that information by invoking the following methods on one
   *  of the items in the list:
   *   - getAge() : returns the age of the player
   *   - getColor() : return the PlayerColor of the player
   *   - getScore() : return the score of the player
   *
   * @return An ordered List of HexPlayer objects participating in this game
   */
  List<HexPlayer> getPlayerList();

  /**
   * Get the current state of the board as a 2D array of integers.
   *
   * Each value will be a number between 0 and 5. If the number is 0, that represents a hole in
   *  the board. Otherwise, the number represents how many fish are on the tile.
   *
   * For example,
   *
   * the following array: [[1, 2], [3, 4], [5, 0], [2, 2]] would translate into the following board:
   *    ,--.     >--.
   *   /    \___/    \___
   *   \ 1  /   \ 2  /   \
   *    >--<  3  >--<  4 |
   *   /    \___/    \___/
   *   \ 5  /   \ 0  /   \
   *    >--<  2  >--<  2 |
   *        \___/    \___/
   *
   * @return a 2D array representing the current board state
   */
  int[][] getCurrentBoard();

  /**
   * Get a Map representing all penguins currently on the Board
   *
   * Key: A Coord, which represents an (x, y) location on the board.
   * Value: A PlayerColor, which represents what color of penguin is at that position.
   * <p>
   *
   *  The Coordinate system for a hexagonal game is used as follows:
   *
   *    ,--.     >--.
   *   /    \___/    \___
   *   \0,0 /   \1,0 /   \
   *    >--< 0,1 >--< 1,1|
   *   /    \___/    \___/
   *   \0,2 /   \1,2 /   \
   *    >--< 0,3 >--< 1,3|
   *   /    \___/    \___/
   *   \0,4 /   \1,4 /   \
   *    `--< 0,5 >--< 1,5|
   *        \___/    \___/
   *
   *   This is an example of a board with 6 rows and 2 columns. For any given coordinate (x, y) the
   *   x value represents the column number and the y value represents the row number.
   * </p>
   *
   * @return The Map of all Penguin locations
   */
  Map<Coord, PlayerColor> getPenguinLocations();

  /// --- ACTIONABLE REQUESTS --- ///

  /**
   * Attempt to place a penguin on the given location. Returns a boolean representing if the request
   *  succeeded.
   *
   * A request can fail for various reasons:
   *  - The player attempting to place a penguin is not the current player
   *  - The location given is out of bounds
   *  - The location given is a hole in the board
   *  - The location given already has a penguin on it
   *  - The game is not in the PLACING_PENGUINS gamestage
   *
   * Sending an invalid request might result in removal from the game.
   *
   * @param location Coord of where to place a penguin (see above for a description of the
   *                  coordinate system).
   * @return boolean indicated success or failure.
   */
  boolean placePenguin(Coord location);

  /**
   * Request to move a penguin from the first location to the second location. Returns a boolean
   *  indicating if the action was successful.
   *
   *  A request can fail for various reasons:
   *   - The player requesting action is not the current player
   *   - Either of the locations given are out of bounds
   *   - There is no penguin on the starting location
   *   - The penguin on the starting coordinate does not belong to the player making the request
   *   - The destination tile is not reachable from the starting tile
   *   - The destination tile already has a penguin or is a hole
   *   - the game is not in the IN_PLAY stage
   *
   *  Sending an invalid request might result in removal from the game
   *
   *  If the request is successful, the current board, player score, and penguin location will be
   *   updated to reflect the action taken.
   *
   * @param start The starting coordinate of the move
   * @param end the ending coordinate of the move
   * @return boolean representing if the move was successful
   */
  boolean movePenguin(Coord start, Coord end);

  /// --- COMMUNICATION --- ///

  /**
   * This a call that will wait for the Referee to indicate that the game has started. Once the
   *  player receives a message, the game will be in the PLACING_PENGUINS phase. The message
   *  they receive will include their assigned color and the order in which they will play.
   *
   * @return A JsonObject represnting the order of Players and the assigned color. The object will be
   * formatted as follows:
   * {
   *   "players" : [Json array of colors indicating the order],
   *   "color" : String representing this player's assigned color
   * }
   */
  JsonObject waitForGameStart();

  /**
   * This is a call that will wait for the Referee to indicate that a penguin has been placed. Once
   *  the player recieves a message, they will know where the most recent penguin has been placed on
   *  the board.
   *
   * @return A JsonObject representing where the most recent penguin has been placed. It will be
   * formatted as follows:
   * {
   *   "position" : [X, Y],
   *   "color" : String representing the color of penguin that was placed.
   * }
   *
   * Where X and Y indicate a coordinate on the board
   */
  JsonObject waitForPenguinPlacement();

  /**
   * This is a call that will wait for the Referee to indicate that a penguin has moved. Once
   *  the player receives a message, they will know where the most recent penguin movement occurred.
   *
   * @return A JsonObject indicating the most recent penguin movement. It will be formatted as follows:
   * {
   *   "start" : [X, Y],
   *   "end" : [X, Y]
   * }
   *
   * Where X and Y indicate a coordinate on the board
   */
  JsonObject waitForPenguinMovement();

  /**
   * This is a method that will wait for the referee to indicate what the final scores of the game
   *  are. Once this player receives this message, they will know where they placed in this game of
   *  Fish.
   *
   * @return A JsonObject indicating the rankings of the players. It will be formatted as follows:
   * {
   *   "winners" : [Strings representing the winning player colors]
   * }
   */
  JsonObject waitForGameResults();

}

