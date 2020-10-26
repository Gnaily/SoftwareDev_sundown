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
 *   the gameBoard and Players list are set to be empty and then the referee can call
 *   initGame(board, list of players) when it is ready.
 * GameStage.PLACING_PENGUINS:
 *   The beginning moments of a game when players are placing penguins on the initial board.
 *   But first, the Referee assigns each Internal Player a color by instantiating Player objects to
 *   represent each player in the Player list.
 *   Then, the Referee assigns an order of turns by deliberately ordering the List of Players.
 *   Finally, players can validly place their penguins in the order determined by the Referee.
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
  private List<InternalPlayer> players;


  /**
   * Initiates a game of HTMF that has not been started.
   * The next step must be to called initGame() to initialize the board and players.
   */
  public HexGameState() {
    this.gameStage = GameStage.NOT_STARTED;
  }

  /**
   * Convenience constructor for generating a completely controlled GameState that represents the
   * middle of an ongoing game.
   *
   * The constructor allows for the initialization of a controlled GameBoard, such that:
   *  -- holes and num fish values are intentionally picked
   *  -- penguins are already placed on board (via placing penguins in each individual Player object's list)
   *  -- the current player is at index zero of the player list
   *
   * This allows one to construct a GameState that represents a snapshot of the middle of a game.
   * This is also used by the copyGameState method to make copies of each of the fields and generate
   * the copied GameState as it is in the middle of the ongoing game.
   * @param gameStage the current stage of the game
   * @param players the players in order of player-turn
   * @param board the GameBoard on which the game is playing
   */
  public HexGameState(GameStage gameStage, GameBoard board, List<InternalPlayer> players) {
    this.gameStage = gameStage;
    this.gameBoard = board;
    this.players = players;
  }

  ///////////////////////////////// ADVANCE TO PLACING_PENGUINS

  /**
   * Constructs the GameBoard and ordered Players list to carry out a game of HTMF and
   * moves the gameStage to the next stage, PLACING_PENGUINS,
   * which allows players to start placing their penguins in order of player turn.
   * @param board (GameBoard) the board to construct for the game
   * @param players (List of Players) the list of players involved in the game
   */
  @Override
  public void initGame(GameBoard board, List<InternalPlayer> players) {
    this.gameBoard = board;
    this.players = new ArrayList<>(players);
    this.gameStage = GameStage.PLACING_PENGUINS;
  }

  /**
   * Places a single penguin on the board at the given location on behalf of the player with the
   * given PlayerColor. Checks:
   *  - That the given PlayerColor is the player whose turn it is
   *  - That the tile to place the penguin is not currently occupied and
   *  - That the given Coord location is not a hole.
   *  Then:
   *  - Adds an element to the penguin locations list of the player
   *
   * @param loc (Coord) the coordinate location on the GameBoard
   * @param playerColor (PlayerColor) the color assigned to the HexPlayer
   * @throws IllegalArgumentException if there is no tile there to place the penguin on or if it is
   * already occupied by another penguin
   */
  @Override
  public void placePenguin(Coord loc, PlayerColor playerColor) throws IllegalArgumentException {
    if (this.gameStage == GameStage.PLACING_PENGUINS) {
      this.checkCurrentPlayer(playerColor);
      this.checkIfPengAlreadyOnTile(loc);
      this.checkIfTilePresent(loc);

      this.findPlayer(playerColor).placePenguin(loc);
      this.advanceToNextPlayer();
    }
    else {
      throw new IllegalStateException("You cannot place a new penguin during this stage of the game.");
    }
  }

  /////////////////////////////////ADVANCE TO IN_PLAY

  /**
   * Advances the game stage to IN_PLAY in order to enable game play to proceed.
   */
  @Override
  public void startPlay() {
    this.gameStage = GameStage.IN_PLAY;
    skipPlayerIfNoMoves();
  }

  /**
   * Updates the penguinLocs List of the current player to reflect movement of penguins on the board.
   * Interpretation: Moves a penguin from one location of the visual playing board to another.
   * Checks:
   *  - That the current GameStage is IN_PLAY
   *  - That there is a penguin to move from the Tile of origin(handled in Player class)
   *  - That the penguin on the Tile of origin is a penguin of the current player
   *  - That the Tile to place the penguin on is not currently occupied nor a hole
   * Then:
   *  - Adjusts the penguinLocs list of the current player to reflect the changes made
   *
   * @param origin (Coord) the tile of origin
   * @param destination (Coord) the tile to move the penguin to
   * @throws IllegalArgumentException if the from or to Coord is not present, if there is no penguin
   * to move on the from coord, or if the to Coord is already occupied by another penguin.
   * @throws IllegalStateException if the method is called before or after a game has been in play.
   */
  @Override
  public void movePenguin(Coord origin, Coord destination) throws IllegalArgumentException, IllegalStateException {

    //TODO FINISIH REFACTORING THIS
//    this.checkValidMoveForCurrentPlayer(origin, destination);
//
//
//
//    PlayerColor color = this.penguinLocs.remove(origin);
//    this.penguinLocs.put(destination, color);
//    Tile tile = this.gameBoard.removeTileAt(origin);
//    player.addToScore(tile.getNumFish());
//    this.advanceToNextPlayer();
//  }
//
//
//  //Checks that the move being made is valid for the current player
//  private InternalPlayer checkValidMoveForCurrentPlayer(Coord origin, Coord destination)
//      throws IllegalArgumentException, IllegalStateException {
//
//    if (this.gameStage != GameStage.IN_PLAY) {
//      throw new IllegalStateException(
//          "You cannot move penguins before or after a game is in play.");
//    }
//    if (this.getCurrentPlayer())
//
//
//    HexPlayer player = findPlayer(this.getCurrentPlayer());
//    this.checkCurrentPlayer(player.getColor());
//    this.checkIfTilePresent(to); //we do not need to check from because if a penguin is there, the tile is present
//    this.checkIfPengAlreadyOnTile(to);
//
//    return player;
  }

  /**
   * Advances the current player to the next player by rotating the HexPlayer list.
   * The current player is always at index 0, so remove this element and put it to the
   * back of the list.
   * Once the turn is advanced
   * -- check that this new current player has moves.
   * -- If not, skip them and keep moving on until reaching a player that can make a move.
   */
  @Override
  public void advanceToNextPlayer() {
    InternalPlayer toMove = this.players.remove(0);
    this.players.add(toMove);
    this.skipPlayerIfNoMoves(); //ensures that if a player has no more turns their turn it skipped
  }

  /**
   * Removes a player from the game by:
   *  - remove the player from the player list
   *  - remove all the player's penguins from the board
   *    (without removing the tiles they were removed from)
   *  - moving on to the next player with moves's turn
   */
  @Override
  public void removeCurrentPlayer() {
    this.players.remove(0);
    this.skipPlayerIfNoMoves();
  }

  /**
   * Constructs a hashmap of playerColor to int representing the current score of each player.
   * @return a hashmap of playerColor to score
   */
  @Override
  public Map<PlayerColor, Integer> getScoreBoard() {
    Map<PlayerColor, Integer> scoreBoard = new HashMap<>();

    for (InternalPlayer ip : this.players) {
      scoreBoard.put(ip.getColor(), ip.getScore());
    }
    return scoreBoard;
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
    List<Coord> pengCoords = new ArrayList<>(this.getPenguinLocations().keySet());
    //If the size of the players list is equal to 1 or 0, then the game ends immediately.
    if (this.players.size() > 1) {
      for (Coord c : pengCoords) {
        if (this.gameBoard.getTilesReachableFrom(c, pengCoords).size() > 0) {
          return false;
        }
      }
    }
    this.gameStage = GameStage.GAMEOVER;
    return true;
  }

  /**
   * If the game has ended, returns the list of winners.
   * The list may have one playerColor on it, or if there is a tie then the list includes all
   * tied winners.
   * @return a list of playerColor of winners
   */
  @Override
  public List<PlayerColor> getWinners() {
    List<PlayerColor> winners = new ArrayList<>();
    int highestScore = 0;

    for (InternalPlayer p: players) {
      if (p.getScore() > highestScore) {
        highestScore = p.getScore();
      }
    }
    for (InternalPlayer p: players) {
      if (p.getScore() == highestScore) {
        winners.add(p.getColor());
      }
    }
    return winners;
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
    List<InternalPlayer> playersCopy = new ArrayList<>();
    for (InternalPlayer p: this.players){
      playersCopy.add(p.getCopyPlayer());
    }
    return new HexGameState(this.gameStage, this.gameBoard.getCopyGameBoard(), playersCopy);
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
  public Map<Coord, PlayerColor> getPenguinLocations() {
    Map<Coord, PlayerColor> allPenguinLocs = new HashMap<>();

    for (InternalPlayer ip : this.players) {
      for (Coord loc : ip.getPenguinLocs()) {
        allPenguinLocs.put(loc, ip.getColor());
      }
    }
    return allPenguinLocs;
  }

  //Info about Players

  /**
   * Return the current Internal Player's color, not the HexPlayer object.
   * This stops users of this GameState from mutating the HexPlayer object.
   * The PlayerColor is unique, so it suffices to return this.
   *
   * @return (PlayerColor) the current player's color
   */
  @Override
  public PlayerColor getCurrentPlayer() {
    return this.players.get(0).getColor();
  }

  /**
   * Get the given player's score
   *
   * @param playerColor (PlayerColor) the player's color
   * @return (int) the given player's score
   */
  @Override
  public int getPlayerScore(PlayerColor playerColor) {
    InternalPlayer player = this.findPlayer(playerColor);
    return player.getScore();
  }

  /**
   * Return the array of players in this game of fish
   *
   * @return the array of players
   */
  @Override
  public List<InternalPlayer> getPlayers() {
    return new ArrayList<>(this.players);
  }

  //Returns the HexPlayer object indicated by the given PlayerColor
  //This method is private because it returns the player object, not a copy
  //If this method is to be made public, change the return line to return a copy.
  private InternalPlayer findPlayer(PlayerColor color) {
    for (InternalPlayer ip : this.players) {
      if (ip.getColor() == color) {
        return ip;
      }
    }

    throw new IllegalArgumentException("HexPlayer not found");
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
    return this.gameBoard.getTilesReachableFrom(start, new ArrayList<>(this.getPenguinLocations().keySet()));
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
    if (this.getPenguinLocations().get(loc) != null) {
      throw new IllegalArgumentException("There is already a penguin here!");
    }
  }

  //If the current player has NO valid moves, and the game is ongoing, then skip their turn.
  //This is called at every point where player turn is advancing to ensure the game proceeds
  //in a timely manner.
  private void skipPlayerIfNoMoves() {
    if (!this.currentPlayerHasMoves()) {
      this.advanceToNextPlayer();
    }
  }

  // returns a boolean representing if the current player has any valid moves
  // true == there are moves to be made
  // false == no moves
  private boolean currentPlayerHasMoves() {
    if (this.gameStage != GameStage.IN_PLAY) {
      return false;
    }
    List<Coord> penguinLocs = this.players.get(0).getPenguinLocs();

    for (Coord cc : penguinLocs) {
      if (this.getTilesReachableFrom(cc).size() > 0) {
        return true;
      }
    }
    return false;
  }


  @Override
  public boolean equals(Object o) {
    if (o instanceof HexGameState) {
      HexGameState other = (HexGameState) o;
      return this.compareGameState(other);

    }
    return false;
  }

  private boolean compareGameState(HexGameState other) {
    if (this.gameStage != other.getGameStage()) {
      return false;
    }
    Map<Coord, PlayerColor> otherPengs = other.getPenguinLocations();
    for (Coord c : this.getPenguinLocations().keySet()) {
      if (otherPengs.get(c) != this.getPenguinLocations().get(c)) {
        return false;
      }
    }
    List<InternalPlayer> otherPlayers = other.getPlayers();
    if (this.players.size() != otherPlayers.size() || this.getPenguinLocations().size() != otherPengs.size()) {
      return false;
    }
    for (int ii = 0; ii < this.players.size(); ii++) {
      if (!this.players.get(ii).equals(otherPlayers.get(ii))) {
        return false;
      }
    }
    return other.gameBoard.equals(this.gameBoard);
  }
}
