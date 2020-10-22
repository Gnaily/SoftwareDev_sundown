package com.fish.model.state;


import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation for a GameState object in a game of Hey, That's my Fish! (HTMF)
 *
 * GameState Interpretations:
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
 * The Players list must be passed into initGame in the order of player turns. This means the referee
 * gets to choose if the players play in order of ascending age, or if it choses a different order
 * it may.
 *
 * The GameBoard must also be passed into initGame in order to create the collection of Tiles the
 * game is played on.
 *
 */
public class HexGameState implements GameState {

  private GameStage gameStage;
  private GameBoard gameBoard;
  private List<Player> players; //can be initialized in the order of ascending age, or another order
  private int currentPlayerIndex; // int index = 0, 1, 2, 3
  private Map<Coord, PlayerColor> penguinLocs;


  /**
   * Initiates a game of HTMF that has not been started.
   * The player index starts at zero and the penguin location hashmap is initialized as empty.
   * The next step must be to called initGame() to initialize the board and players.
   */
  public HexGameState() {
    this.gameStage = GameStage.NOT_STARTED;
    this.currentPlayerIndex = 0;
    this.penguinLocs = new HashMap<>();
  }

  /**
   * Convenience constructor for generating a completely controlled GameState that represents the
   * middle of an ongoing game.
   *
   * The constructor allows for the initialization of a controlled GameBoard, penguins already
   * on the board, players in the order of player-turn, and the player index of which player's
   * turn it is in this precise snapshot of a GameState.
   *
   * This allows one to construct a GameState that represents a snapshot of the middle of a game.
   * This is also used by the copyGameState method to make copies of each of the fields and generate
   * the copied GameState as it is in the middle of the ongoing game.
   * @param penguinLocs the location of penguins
   * @param players the players in order of player-turn
   * @param currentPlayerIndex the index in the player list to find the current player
   * @param board the GameBoard on which the game is playing
   */
  public HexGameState(GameStage gs, GameBoard board, List<Player> players,
      int currentPlayerIndex, Map<Coord, PlayerColor> penguinLocs) {
    this.gameStage = gs;
    this.gameBoard = board;
    this.players = players;
    this.currentPlayerIndex = currentPlayerIndex;
    this.penguinLocs = new HashMap<>(penguinLocs);
  }

  ///////////////////////////////// ADVANCE TO PLACING_PENGUINS

  /**
   * Constructs the GameBoard and ordered Players list to carry out a game of HTMF and
   * moves the gameStage to the next stage, PLACING_PENGUINS, which allows players to start
   * placing their penguins in order of player turn.
   * @param board (GameBoard) the board to construct for the game
   * @param players (List of Players) the list of players involved in the game
   */
  @Override
  public void initGame(GameBoard board, List<Player> players) {
    this.gameBoard = board;
    this.players = new ArrayList<>(players);
    this.gameStage = GameStage.PLACING_PENGUINS;

  }

  /**
   * Places a single penguin on the board at the given location on behalf of the Player with the
   * given PlayerColor. Checks:
   *  - That the given PlayerColor is the player whose turn it is
   *  - That the tile to place the penguin on is not currently occupied and
   *  - That the given Coord location is not a hole.
   *  Then:
   *  - Adds an element to the penguinLocs map and advances the CurrentPlayerIndex by one.
   *
   * @param loc (Coord) the coordinate location on the GameBoard
   * @param playerColor (PlayerColor) the color assigned to the Player
   * @throws IllegalArgumentException if there is no tile there to place the penguin on or if it is
   * already occupied by another penguin
   */
  @Override
  public void placePenguin(Coord loc, PlayerColor playerColor) throws IllegalArgumentException {
    if (this.gameStage == GameStage.PLACING_PENGUINS) {
      this.checkCurrentPlayer(playerColor);
      this.checkIfPengAlreadyOnTile(loc);
      this.checkIfTilePresent(loc);

      this.penguinLocs.put(loc, playerColor);
      this.advanceToNextPlayer();
    }
    else {
      throw new IllegalStateException("You cannot place a new penguin during this stage of the game.");
    }
  }

  /////////////////////////////////ADVANCE TO IN_PLAY

  /**
   * Advances the game stage to IN_PLAY in order to enable gameplay to proceed, including moving
   * penguins across the board.
   */
  @Override
  public void startPlay() {
    this.gameStage = GameStage.IN_PLAY;
    if (this.currentPlayerNoMoves()) {
      this.advanceToNextPlayer();
    }
  }

  /**
   * Updates the penguinLocs Hashmap to reflect the movement of penguins on the board.
   * Interpretation: Moves a penguin from one location of the visual playing board to another.
   * Checks:
   *  - That there is a penguin to move on the from Tile
   *  - That the penguin on the from Tile is a penguin of the current player
   *  - That the Tile to place the penguin on is not currently occupied nor a hole
   * Then:
   *  - Adjusts the penguinLocs map to reflect the changes made
   *
   * @param from (Coord) the tile of origin
   * @param to (Coord) the tile to move the penguin to
   * @throws IllegalArgumentException if the from or to Coord is not present, if there is no penguin
   * to move on the from coord, or if the to Coord is already occupied by another penguin.
   * @throws IllegalStateException if the method is called before or after a game has been in play.
   */
  @Override
  public void movePenguin(Coord from, Coord to) throws IllegalArgumentException, IllegalStateException {

    Player player = this.checkValidMove(from, to);

    PlayerColor color = this.penguinLocs.remove(from);
    this.penguinLocs.put(to, color);
    Tile tile = this.gameBoard.removeTileAt(from);
    player.addToScore(tile.getNumFish());
    this.advanceToNextPlayer();
  }

  //Checks that the move being made is valid for the current player and returns that Player
  private Player checkValidMove(Coord from, Coord to) throws IllegalArgumentException, IllegalStateException {
    if (this.gameStage != GameStage.IN_PLAY) {
      throw new IllegalStateException(
          "You cannot move penguins before or after a game is in play.");
    }
    if (this.penguinLocs.get(from) == null) {
      throw new IllegalArgumentException("There is no Penguin here to move.");
    }
    Player player = findPlayer(this.penguinLocs.get(from));
    this.checkCurrentPlayer(player.getColor());
    this.checkIfTilePresent(to); //we do not need to check from because if a penguin is there, the tile is present
    this.checkIfPengAlreadyOnTile(to);

    return player;
  }

  /**
   * Advance the currentPlayerIndex by one or cycle it back to zero once the next cycle of turns
   * begins. This enforces the player turn flow and ensures that only the proper player whose
   * turn it is can make moves.
   */
  @Override
  public void advanceToNextPlayer() {
    //Adding 1 brings us to the next player. By modding by the size of the list means that if
    //we reach the end we will loop back around to zero.
    this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
    if (this.currentPlayerNoMoves() && !this.isGameOver()) {

      this.advanceToNextPlayer();
    }
  }

  /**
   * Removes a player from the game by:
   *  - remove the player from the player list
   *  - remove all the player's penguins from the board
   *    (without removing the tiles they were removed from)
   *  - resetting the currentPlayerIndex to account for one less player
   */
  @Override
  public void removeCurrentPlayer() {
    PlayerColor playerToRemove = this.getCurrentPlayer();

    // Doing this is one foreach causes a ConcurrentModificationException
    Set<Coord> coords = this.penguinLocs.keySet();
    List<Coord> penguinsToRemove = new ArrayList<>();
    for (Coord cc : coords) {
      if (this.penguinLocs.get(cc) == playerToRemove) {
        penguinsToRemove.add(cc);
      }
    }

    for (Coord c : penguinsToRemove) {
      this.penguinLocs.remove(c);
    }

    //The mod ensures that the next currentPlayerIndex is a valid index that reflects the changes
    //made to the players list.
    this.players.remove(this.currentPlayerIndex);
    this.currentPlayerIndex %= this.players.size();
    if (this.currentPlayerNoMoves()) {
      this.advanceToNextPlayer();
    }
  }

  /////////////////////////////////Game Closing

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
  @Override
  public boolean isGameOver() {
    List<Coord> pengCoords = new ArrayList<>(this.penguinLocs.keySet());

    //If the size of the players list is equal to 1 or 0, then the game ends immediately.
    if (this.players.size() > 1) {
      for (Coord c : this.penguinLocs.keySet()) {
        if (this.gameBoard.getTilesReachableFrom(c, pengCoords).size() > 0) {
          return false;
        }
      }
    }
    this.gameStage = GameStage.GAMEOVER;
    return true;
  }

  /**
   * If the GameStage is set to GAME_OVER, then this method may be called to generate a
   * List of Player(s) that are the winner(s) of the game, defined by the player(s) with the highest
   * int in their score field, which was calculated by the number of fish collected during a game.
   *
   * If there is only one winner, the resulting list will have one element.
   * If there is a tie, the resulting list will contain all the tied players.
   *
   * @return a list of playerColor of winners
   * @throws IllegalStateException if the game is not over
   */
  @Override
  public List<PlayerColor> getWinners() throws IllegalStateException{
    if (this.gameStage == GameStage.GAMEOVER){
      List<PlayerColor> winners = new ArrayList<>();
      int highestScore = 0;

      for (Player p: players) {
        if (p.getScore() > highestScore) {
          highestScore = p.getScore();
        }
      }
      for (Player p: players) {
        if (p.getScore() == highestScore) {
          winners.add(p.getColor());
        }
      }
      return winners;
    }
    throw new IllegalStateException("The game is not over yet.");
  }

  /////////////////////////////////Info Retrieval & Helpers

  //Info about the Sate

  /**
   * Returns a deep copy of the GameState.
   * Every mutable field is copied so that the original is not modified.
   * @return a deep copy of the gamestate
   */
  @Override
  public GameState getCopyGameState() {
    List<Player> playersCopy = new ArrayList<>();
    for (Player p: this.players){
      playersCopy.add(p.getCopyPlayer());
    }
    return new HexGameState(this.gameStage, this.gameBoard.getCopyGameBoard(),
        playersCopy, this.currentPlayerIndex, this.getPenguinLocations());
  }

  /**
   * Return the current gameStage of this game of HTMF
   *
   * @return (GameStage) the current gameStage
   */
  @Override
  public GameStage getGameStage() {
    return this.gameStage;
  }


  //Info about penguins

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
   * Get the Coord locations of all the penguins of the given PlayerColor
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


  //Info about Players

  /**
   * Return the current Player's color, not the Player object.
   * This stops users of this GameState from mutating the Player object.
   * The PlayerColor is unique, so it suffices to return this.
   *
   * @return (PlayerColor) the current player's color
   */
  @Override
  public PlayerColor getCurrentPlayer() {
    return this.players.get(this.currentPlayerIndex).getColor();
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


  //Returns the Player object indicated by the given PlayerColor
  //This method is private because it returns the player object, not a copy
  //If this method is to be made public, change the return line to return a copy.
  private Player findPlayer(PlayerColor color) {
    for (Player pp : this.players) {
      if (pp.getColor() == color) {
        return pp;
      }
    }

    throw new IllegalArgumentException("Player not found");
  }


  //Info about Board

  /**
   * Get the tile at the given location on the board.
   * The getTileAt method in the board handles errors when the given Coord has negative or
   * out of bounds inputs.
   *
   * @param loc (Coord) the coordinate location of the desired Tile
   * @return (Tile) the tile at the given location
   */
  @Override
  public Tile getTileAt(Coord loc) {
    return this.gameBoard.getTileAt(loc);
  }

  /**
   * Return a list of Coordinates of all the tiles reachable from a starting
   * Coord given the location of all the other penguins on the board
   * @param start the starting coordinate location
   * @return the list of possible tiles to move to
   */
  @Override
  public List<Coord> getTilesReachableFrom(Coord start) {
    return this.gameBoard.getTilesReachableFrom(start,
        new ArrayList<>(this.penguinLocs.keySet()));
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

  /**
   * Return the array of players in this game of fish
   *
   * @return the array of players
   */
  @Override
  public List<Player> getPlayers() {
    return new ArrayList<>(this.players);
  }

  //Private Helpers

  //Checks if a player making a change to the state is the current player
  private void checkCurrentPlayer(PlayerColor pc) throws IllegalArgumentException {
    if (this.getCurrentPlayer() != pc) {
      throw new IllegalArgumentException("It's not your turn!");
    }
  }

  //Checks if the tile is present, if not (meaning its a hole) throw the exception
  //The getTileAt(loc) method handles the Illegal Arg Exception wherein the loc is negative or  out of bounds
  private void checkIfTilePresent(Coord loc) throws IllegalArgumentException {
    if (!this.gameBoard.getTileAt(loc).isPresent()) {
      throw new IllegalArgumentException("There is a hole at this location.");
    }
  }

  //Checks if another penguin is already on a tile, meaning that a current player cannot place
  //their penguing there or move to that tile
  private void checkIfPengAlreadyOnTile(Coord loc) throws IllegalArgumentException {
    if (this.penguinLocs.get(loc) != null) {
      throw new IllegalArgumentException("There is already a penguin here!");
    }
  }

  // returns a boolean representing if the current player does not have any moves
  private boolean currentPlayerNoMoves() {
    if (this.gameStage != GameStage.IN_PLAY) {
      return false;
    }
    List<Coord> pengs = this.getOnePlayersPenguins(this.getCurrentPlayer());

    for (Coord cc : pengs) {
      if (this.getTilesReachableFrom(cc).size() > 0) {
        return false;
      }
    }

    return true;
  }

}
