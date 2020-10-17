package com.fish.model.state;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.tile.Tile;
import java.util.HashMap;
import java.util.List;

/**
 * Interface for a GameState in a game of Hey Thats my Fish (HTMF)
 *
 * Interpretation:
 *  - Manages the player turns
 *  - Ability to place and move penguins on the board
 *  - Determine if the game is over
 *  - Calculate score and winning players
 */
public interface GameState {

  /**
   * Advances the gameStage to PLACING_PENGUINS, during which the players and GameBoard
   * are constructed.
   * @param board the board to construct for the game
   * @param players the list of players involved in the game
   */
  void initGame(GameBoard board, List<Player> players);

  /**
   * Places a penguin on a GameBoard in the PLACING_PENGUINS stage of a game by adding the
   * unique Coord and PlayerColor to the penguinLocs hashmap.
   * @param loc the coordinate location on the GameBoard
   * @param playerColor the color assigned to the Player
   */
  void placePenguin(Coord loc, PlayerColor playerColor);

  /**
   * Advances the game stage to IN_PLAY in order to enable game playing to proceed, including
   * moving penguins across the board.
   */
  void startPlay();

  /**
   * Updates the penguinLocs Hashmap to reflect the movement of penguins on the board.
   * Interpretation: Moves a penguin from one location of the visual playing board to another.
   * @param from the tile of origin
   * @param to the tile to move the penguin to
   */
  void movePenguin(Coord from, Coord to);

  /**
   * Advance the game to be the next player's turn
   */
  void advanceToNextPlayer();

  /**
   * Removes a player from the game by removing all of its penguins off the board and deleting
   * its score from the scoreKeeper field.
   */
  void removeCurrentPlayer();

  /**
   * Determines whether there remains any valid move for any Player in the game.
   * @return a boolean which determines if the game is over
   */
  boolean isGameOver();

  /**
   * If the game has ended, returns the list of winners.
   * The list may have one playerColor on it, or if there is a tie then the list includes all
   * tied winners.
   * @return a list of playerColor of winners
   */
  List<PlayerColor> getWinners();

  /**
   * Returns a deep copy of the GameState.
   * Every mutable field is copied so that the original is not modified.
   * @return a deep copy of the gamestate
   */
  GameState getCopyGameState();

  /**
   * Return the GameStage emuneration that represents the current stage of the game, either
   * NOT_STARTED, PLACING_PENGUINS, IN_PLAY, or GAMEOVER.
   */
  GameStage getGameStage();

  /**
   * Return the current Player's color
   *
   * @return (PlayerColor) the current player's color
   */
  PlayerColor getCurrentPlayer();

  /**
   * Retrieves the Tile located at the coordinate in the GameBoard.
   * @param loc the coordinate location of the desired Tile
   * @return the Tile object located there
   */
  Tile getTileAt(Coord loc);

  /**
   * Return a list of Coordinates of all the tiles reachable from a starting
   * Coord given the location of all the other penguins on the board
   * @param start the starting coordinate location
   * @return the list of possible tiles to move to
   */
  List<Coord> getTilesReachableFrom(Coord start);

  /**
   * Returns a HashMap of penguin locations, formatted such that the Coord is the unique
   * identifier of the location (since only one penguin can be on a tile at a time) and
   * the PlayerColor is the value, to identify which player's penguin is on that location.
   * @return a HashMap of Coord to PlayerColor values
   */
  HashMap<Coord, PlayerColor> getPenguinLocations();

  /**
   * Returns a List of all the current Coordinate locations of a single player,
   * indicated by the playerColor.
   * @param playerColor the color assigned to the player
   * @return a list of all the coordinate locations of the player's penguins
   */
  List<Coord> getOnePlayersPenguins(PlayerColor playerColor);

  /**
   * Given a specific playerColor, returns the score of that player.
   * @param playerColor the player's color
   * @return the player's score
   */
  int getPlayerScore(PlayerColor playerColor);

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
