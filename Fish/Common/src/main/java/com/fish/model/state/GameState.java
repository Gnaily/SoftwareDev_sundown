package com.fish.model.state;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.tile.Tile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
   * Constructs the GameBoard and ordered Players list to carry out a game of HTMF and
   * moves the gameStage to the next stage, PLACING_PENGUINS, which allows players to start
   * placing their penguins in order of player turn.
   * @param board (GameBoard) the board to construct for the game
   * @param players (List of Players) the list of players involved in the game
   */
  void initGame(GameBoard board, List<InternalPlayer> players);

  /**
   * Places a single penguin on the board at the given location on behalf of the HexPlayer with the
   * given PlayerColor. Checks:
   *  - That the given PlayerColor is the player whose turn it is
   *  - That the tile to place the penguin on is not currently occupied and
   *  - That the given Coord location is not a hole.
   *  Then:
   *  - Adds an element to the penguinLocs map and advances the CurrentPlayerIndex by one.
   * @param loc (Coord) the coordinate location on the GameBoard
   * @param playerColor (PlayerColor) the color assigned to the HexPlayer
   * @throws IllegalArgumentException if there is no tile there to place the penguin on or if it is
   * already occupied by another penguin
   */
  void placePenguin(Coord loc, PlayerColor playerColor);

  /**
   * Advances the game stage to IN_PLAY in order to enable game playing to proceed, including
   * moving penguins across the board.
   */
  void startPlay();

  /**
   * Moves a penguin from one location of the visual playing board to another.
   * Checks:
   *  - That there is a penguin to move on the from Tile
   *  - That the penguin on the from Tile is a penguin of the current player
   *  - That the Tile to place the penguin on is not currently occupied nor a hole
   * Then:
   *  - Adjusts the penguinLocs to reflect the changes made
   * @param from (Coord) the tile of origin
   * @param to (Coord) the tile to move the penguin to
   * @throws IllegalArgumentException if the from or to Coord is not present, if there is no penguin
   * to move on the from coord, or if the to Coord is already occupied by another penguin.
   * @throws IllegalStateException if the method is called before or after a game has been in play.
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
   * Constructs a hashmap of playerColor to int representing the current score of each player.
   * @return a hashmap of playerColor to score
   */
  Map<PlayerColor, Integer> getScoreBoard();

  /**
   * Determines whether there remains any valid move for any HexPlayer in the game.
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
   * Returns a HashMap of penguin locations, formatted such that
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
   * Return the current Internal Player's color
   *
   * @return (PlayerColor) the current player's color
   */
  PlayerColor getCurrentPlayer();

  /**
   * Return the players (in order) of this game
   *
   * @return the list of players
   */
  List<InternalPlayer> getPlayers();

  /**
   * Returns a copy of this GameState's current Gameboard
   * @return a GameBoard object of the current collection of tiles in the playable game
   */
  GameBoard getGameBoard();

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
