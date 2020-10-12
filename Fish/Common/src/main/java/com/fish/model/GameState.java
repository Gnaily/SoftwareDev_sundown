package com.fish.model;

import com.fish.model.board.GameBoard;
import com.fish.model.tile.Tile;
import java.util.HashMap;
import java.util.List;

/**
 * Add Later
 */
public interface GameState {

  // TODO: Fill out comments

  /**
   *
   * @param board
   * @param players
   */
  void startGame(GameBoard board, List<Player> players);

  /**
   * Retrieves the Tile located at the coordinate in the GameBoard.
   * @param loc the coordinate location of the desired Tile
   * @return the Tile object located there
   */
  Tile getTileAt(Coord loc);

  /**
   * Returns the Player whose current turn it is.
   * @return the PlayerColor assigned to that Player
   */
  PlayerColor getCurrentPlayer();

  /**
   * Places a penguin on a GameBoard in the PLACING_PENGUINS stage of a game of HTMF
   * @param loc the coordinate location on the GameBoard
   * @param playerColor the color assigned to the Player
   */
  void placePenguin(Coord loc, PlayerColor playerColor);

  /**
   * Returns a HashMap of penguin locations, formatted such that the Coord is the unique
   * identifier of the location (since only one penguin can be on a tile at a time) and
   * the PlayerColor is the value, to identify which player's penguin is on that location.
   * @return a HashMap of Coord to PlayerColor values
   */
  HashMap<Coord, PlayerColor> getPenguinLocations();

  /**
   * Move a penguin from a coordinate location on a GameBoard to another coordinate location
   * during a game of HTMF.
   * @param from the tile of origin
   * @param to the tile to move the penguin to
   */
  void movePenguin(Coord from, Coord to);

  /**
   * Given a specific playerColor, returns the score of that player.
   * @param playerColor the player's color
   * @return the player's score
   */
  int getPlayerScore(PlayerColor playerColor);

  /**
   * Determines whether there remains any valid move for any Player in the game.
   * @return a boolean which determines if the game is over
   */
  boolean isGameOver();

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

  /**
   *
   */
  void advanceToNextPlayer();

  void removePlayer(PlayerColor color);


}
