package com.fish.model.state;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.tile.ProtectedTile;
import com.fish.model.tile.Tile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for a GameState in a game of Hey Thats my Fish (HTMF)
 *
 * DATA DEFINITION:
 * A structure that contains the current state of one ongoing game of HTMF.
 *
 * GameState extends ProtectedGameState, a read-only version of the GameState interface.
 * When communicating with external players, only Protected versions of objects are shared.
 *
 * The GameState:
 *  - allows for control over the construction of the board to play on
 *  - Manages the player turns
 *  - Ability to place and move penguins on the board
 *  - Determine if the game is over
 *  - Calculate score and winning players
 *
 * INTERPRETATION:

 */
public interface GameState extends ProtectedGameState {

  /**
   * Constructs the GameBoard and ordered Players list to carry out a game of HTMF and
   * moves the gameStage to the next stage, PLACING_PENGUINS
   * (see gameStage explanations in class javaDoc)
   *
   * This method may be used by the referee to initialize a game once the referee has chosen
   * the board setup and received players from the tournament manager.
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
   *  - That the move is legal
   *  Then:
   *  - Adds an element to the penguinLocs map and advances the CurrentPlayerIndex by one.
   * @param loc (Coord) the coordinate location on the GameBoard
   * @param playerColor (PlayerColor) the color assigned to the HexPlayer
   * @throws IllegalArgumentException if there is no tile there to place the penguin on or if it is
   * already occupied by another penguin
   */
  void placePenguin(Coord loc, PlayerColor playerColor);

  /**
   * Converts the gameStage to IN_PLAY (see gameStage explanations in class javaDoc).
   * The enforcing of the gameStage is used to signal to the referee whether a player is taking
   * an action at the wrong time during a game, making it illegal.
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
   * Advances the current player to the next player by rotating the HexPlayer list.
   * The current player is always at index 0, so remove this element and put it to the
   * back of the list.
   * Once the turn is advanced
   * -- check that this new current player has moves.
   * -- If not, skip them and keep moving on until reaching a player that can make a move.
   */
  void advanceToNextPlayer();

  /**
   * Removes a player from the game by:
   *  - remove the player from the player list
   *  - remove all the player's penguins from the board
   *    (without removing the tiles they were removed from)
   *  - moving on to the next player with moves's turn
   */
  void removeCurrentPlayer();

  /**
   * Checks whether the game is over by:
   *  - Checking if there are any valid moves stemming from any penguin's location left on the board
   *  - Checking if there is only one player left, meaning the rest got kicked out
   *
   * If there are no more valid moves OR if there is only one player left, the game immediately ends.
   * Meaning:
   *  - the method returns false and
   *  - the GameStage is set to GAME_OVER
   *
   * @return (boolean) a boolean that determines whether the game is over
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
   * Returns a deep copy of this GameState
   * @return a deep copy of this GameState
   */
  GameState getCopyGameState();

}
