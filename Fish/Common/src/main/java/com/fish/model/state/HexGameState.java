package com.fish.model.state;


import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation for a GameState object in a game of Hey, That's my Fish! (HTMF)
 *
 * GameStage Interpretations:
 * GameStage.NOT_STARTED:
 *   The 'waiting room' stage wherein a GameState (or simply just a game) has been instantiated
 *   but the referee is waiting for players to gather. Therefore, when a HxGameState is instantiated
 *   the gameBoard, Players, and penguinLocs are set to be empty and then the referee can call
 *   initGame(board, list of players) when it is ready.
 * GameStage.PLACING_PENGUINS:
 *   The beginning moments of a game when the Referee assigns each Player a color (by passing in
 *   the list of Player in the initGame(board, list of players) method), and players are placing
 *   their penguins on the board in an order decided by the referee. This order is controlled in
 *   the referee class and this HexGameState class does not enforce any particular order of
 *   player turn. This class only allows the referee to change the state based on its deciding
 *   order of gameplay.
 * GameStage.IN_PLAY:
 *   During this state the actual game playing proceeds. The movePenguin method may be called.
 *   The referee will advance player turns and continuously check to see if the game is over yet.
 * GameStage.GAMEOVER:
 *   Once no player can make a valid move, the game is over.
 *
 */
public class HexGameState implements GameState {

  private GameStage gameStage;
  private Map<Coord, PlayerColor> penguinLocs;
  private int currentPlayerIndex; // int index = 0, 1, 2, 3
  private GameBoard gameBoard;
  private List<Player> players; //needs to be initialized in the order of ascending age


  /**
   * Initiates a game of HTMF that has not been started. The first stage of the game represents the
   * time that Players are gathering in for a game and therefore everything is set to be empty at
   * first and there are methods for the referee to pick the settings of the game once all players
   * are ready.
   */
  public HexGameState() {
    this.gameStage = GameStage.NOT_STARTED;
    this.penguinLocs = new HashMap<>();
    this.currentPlayerIndex = 0;
  }

  /////////////////////////////////ADVANCE TO PLACING_PENGUINS

  /**
   * Moves the gameStage to the next stage, PLACING_PENGUINS, during which the players and gameboard
   * are constructed.
   *
   * @param board (GameBoard) the board to construct for the game
   * @param players (List of Players) the list of players involved in the game
   */
  @Override
  public void initGame(GameBoard board, List<Player> players) {
    this.gameStage = GameStage.PLACING_PENGUINS;
    this.gameBoard = board;
    this.players = new ArrayList<>(players);
  }

  /**
   * Places a penguin on the board at the given location on behalf of the Player with the given
   * PlayerColor.
   *
   * @param loc (Coord) the coordinate location on the GameBoard
   * @param playerColor (PlayerColor) the color assigned to the Player
   * @throws IllegalArgumentException if there is no tile there to place the penguin on or if it is
   * already occupied by another penguin
   */
  @Override
  public void placePenguin(Coord loc, PlayerColor playerColor) throws IllegalArgumentException {
    if (!this.gameBoard.getTileAt(loc).isPresent()) {
      throw new IllegalArgumentException("There is no tile here!");
    }
    if (this.penguinLocs.containsKey(loc)) {
      throw new IllegalArgumentException("There is already a penguin on this tile.");
    }
    this.penguinLocs.put(loc, playerColor);
  }

  /////////////////////////////////ADVANCE TO IN_PLAY

  /**
   * Advances the game stage to IN_PLAY in order to enable gameplay to proceed, including moving
   * penguins across the board.
   */
  @Override
  public void startPlay() {
    this.gameStage = GameStage.IN_PLAY;
  }

  /**
   * Updates the penguinLocs Hashmap to reflect the movement of penguins on the board.
   * Interpretation: Moves a penguin from one location of the visual playing board to another.
   *
   * @param from (Coord) the tile of origin
   * @param to (Coord) the tile to move the penguin to
   * @throws IllegalArgumentException if the from or to Coord is not present, if there is no penguin
   * to move on the from coord, or if the to Coord is already occupied by another penguin.
   * @throws IllegalStateException if the method is called before or after a game has been in play.
   */
  @Override
  public void movePenguin(Coord from, Coord to)
      throws IllegalArgumentException, IllegalStateException {
    if (this.gameStage != GameStage.IN_PLAY) {
      throw new IllegalStateException(
          "You cannot move penguins before or after a game is in in play.");
    }
    if (!this.gameBoard.getTileAt(from).isPresent() || !this.gameBoard.getTileAt(to).isPresent()) {
      throw new IllegalArgumentException("You cannot move from or to a hole - please move to a tile"
          + " that is present on the board.");
    }
    if (this.penguinLocs.get(from) == null) {
      throw new IllegalArgumentException("There is no Penguin here to move.");
    }
    if (this.penguinLocs.get(to) != null) {
      throw new IllegalArgumentException("There is already a penguin here!");
    }

    Player player = findPlayer(this.penguinLocs.get(from));

    PlayerColor color = this.penguinLocs.remove(from);
    this.penguinLocs.put(to, color);

    Tile tile = this.gameBoard.removeTileAt(from);
    player.addToScore(tile.getNumFish());
  }

  /**
   * Advance the game to be the next player's turn
   */
  @Override
  public void advanceToNextPlayer() {
    this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
  }

  /**
   * Removes a player from the game by removing all of its penguins off the board and deleting its
   * score from the scoreKeeper field.
   *
   * @param color (PlayerColor) the color of the player to remove
   */
  @Override
  public void removePlayer(PlayerColor color) {
    Player toRemove = this.findPlayer(color);

    for (Coord cc : this.penguinLocs.keySet()) {
      if (this.penguinLocs.get(cc) == color) {
        this.penguinLocs.remove(cc);
      }
    }

    this.players.remove(toRemove);
    this.currentPlayerIndex %= this.players.size();
  }

  /////////////////////////////////Game Closing

  /**
   * Checks whether the game is over by checking if there are any remaining valid moves stemming
   * from any of the penguins currently located on the board, meaning in the penguinLoc hashmap. If
   * the length of the list returned by 'getTilesReachableFrom' is > 0 that implies that there are
   * tiles reachable from where a penguin currently stands and therefore the game is not yet over.
   *
   * @return (boolean) a boolean that determines whether the game is over
   */
  @Override
  public boolean isGameOver() {
    List<Coord> pengCoords = new ArrayList<>(this.penguinLocs.keySet());

    for (Coord c : this.penguinLocs.keySet()) {
      if (this.gameBoard.getTilesReachableFrom(c, pengCoords).size() > 0) {
        return false;
      }
    }
    this.gameStage = GameStage.GAMEOVER;
    return true;
  }

  /////////////////////////////////Info Retrieval

  /**
   * Return the current gameStage of this game of HTMF
   *
   * @return (GameStage) the current gameStage
   */
  @Override
  public GameStage getGameStage() {
    return this.gameStage;
  }


  /**
   * Return the current Player's color
   *
   * @return (PlayerColor) the current player's color
   */
  @Override
  public PlayerColor getCurrentPlayer() {
    return this.players.get(this.currentPlayerIndex).getColor();
  }

  //Returns the player indicated by the given PlayerColor
  private Player findPlayer(PlayerColor color) {
    for (Player pp : this.players) {
      if (pp.getColor() == color) {
        return pp;
      }
    }

    throw new IllegalArgumentException("Player not found");
  }

  /**
   * Get the tile at the given location on the board
   *
   * @param loc (Coord) the coordinate location of the desired Tile
   * @return (Tile) the tile at the given location
   */
  @Override
  public Tile getTileAt(Coord loc) {
    return this.gameBoard.getTileAt(loc);
  }

  /**
   * Get the current locations of penguins on the board
   *
   * @return (Map<Coord, PlayerColor>) the current penguin locations on the board
   */
  @Override
  public HashMap<Coord, PlayerColor> getPenguinLocations() {
    return new HashMap<>(this.penguinLocs);
  }

  /**
   * Get all of the given player's penguin locations
   *
   * @param playerColor (PlayerColor) the color assigned to the player
   * @return (List<Coord>) the player's penguin locations
   */
  @Override
  public List<Coord> getOnePlayersPenguins(PlayerColor playerColor) {
    List<Coord> pengs = new ArrayList<>();
    for (Coord cc : this.penguinLocs.keySet()) {
      if (this.penguinLocs.get(cc) == playerColor) {
        pengs.add(cc);
      }
    }
    return pengs;
  }

  /**
   * Get the given player's score
   *
   * @param playerColor (PlayerColor) the player's color
   * @return (int) the given player's score
   */
  @Override
  public int getPlayerScore(PlayerColor playerColor) {
    Player player = this.findPlayer(playerColor);

    return player.getScore();
  }

  /**
   * Get the current width of the board
   *
   * @return (int) this board's width
   */
  @Override
  public int getWidth() {
    return this.gameBoard.getWidth();
  }

  /**
   * Get the height of this game's board
   *
   * @return (int) the height of the board
   */
  @Override
  public int getHeight() {
    return this.gameBoard.getHeight();
  }

}
