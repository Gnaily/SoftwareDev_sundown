package com.referee;

import com.fish.externalplayer.PlayerInterface;
import com.fish.game.GameTree;
import com.fish.game.HexGameTree;
import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.model.state.GameState;
import com.fish.model.state.HexGameState;
import com.fish.model.state.HexPlayer;
import com.fish.model.state.InternalPlayer;
import com.fish.model.state.PlayerColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Implementation of the Referee class that is used by a tournament manager to run a game of Fish.
 *
 * Externally, the tournament manager knows that the runGame method can be invoked by sending the
 * referee a list of players, and the referee will return back the winners and the cheaters once
 * the game is completed.
 *
 * A player is considered a cheater if:
 *  - they disconnect/fail to respond to a move request
 *  - request a valid move/placement for their penguins
 *  - attempt to move another player's penguins
 *
 * The game is terminated once there is only one player left (everyone else cheated) or there are
 * no more moves for any players.
 *
 * To accomplish this, the Referee keeps track of:
 *  - a list of (external) players who have cheated - this starts empty
 *  - a map of color to external player - this is how the referee keeps track of in-game colors
 *     and how to contact/get responses from the corresponding player
 *
 * Each time the tournament manager would like to run a new game of fish, they should create a new
 * instance of this object.
 */
public class HexReferee implements Referee {

  private List<PlayerInterface> cheaters;
  private Map<PlayerColor, PlayerInterface> colorToExternalPlayer;


  private static final int PENGUIN_SUBRACT_NUM = 6;
  private static final int BOARD_ROW = 10;
  private static final int BOARD_COLS = 6;
  private static final int TIMEOUT_SECONDS = 60;


  /**
   * Public constructor - does not take any arguments.
   *
   * Initializes the cheaters and the map of color->player to both be empty.
   */
  public HexReferee() {
    this.cheaters = new ArrayList<>();
    this.colorToExternalPlayer = new HashMap<>();
  }

  /**
   * Run a game of Fish for the given set of Players. The players passed in are assumed to be ordered
   * by age (or whatever metric will determine turn order). The game will be run according to the
   * rules of fish implemented by the state. The referee will use these rules to determine if and
   * when a player is cheating (as described above).
   *
   * @param players an ordered list of players to run a game of fish for
   * @return the results from this game of fish, containing the winners and the cheaters.
   */
  public Results runGame(List<PlayerInterface> players) {
    int size = players.size();

    if (size <= 1 || size > 4) {
      throw new IllegalArgumentException("Not the right amount of players");
    }


    int minOneFish = (PENGUIN_SUBRACT_NUM - size) * size;
    GameBoard gb = this.makeGameBoard(minOneFish);

    GameState gs = new HexGameState();

    List<InternalPlayer> internalPlayers = this.makePlayersInternal(players);

    gs.initGame(gb, internalPlayers);

    List<PlayerColor> winners = this.playGame(gs);
    List<PlayerInterface> extWinners = new ArrayList<>();
    for (PlayerColor pc : winners) {
      extWinners.add(colorToExternalPlayer.get(pc));
    }

    return new Results(extWinners, this.cheaters);
  }


  // ---- GAME SETUP ---- //

  GameBoard makeGameBoard(int minOneFish) {
    //If desired you may add difficulty to the game here
    return new HexGameBoard(BOARD_ROW, BOARD_COLS, new ArrayList<>(), minOneFish);
  }

  List<InternalPlayer> makePlayersInternal(List<PlayerInterface> players) {
    // list of external players is already ordered by age
    List<InternalPlayer> ips = new ArrayList<>();
    List<PlayerColor> colors = Arrays.asList(PlayerColor.WHITE, PlayerColor.RED, PlayerColor.BLACK, PlayerColor.BROWN);

    for (int ii = 0; ii < players.size(); ii++) {
      ips.add(makeSinglePlayerInternal(players.get(ii), colors.get(ii)));
    }

    return ips;
  }

  /**
   * Make an internal player (to be used in a game of fish) as well as update the color->external
   * player map to include this player's mapping.
   *
   * @param ep the external player
   * @param color the assigned color to this player
   * @return an internal representation of the given external player
   */
  InternalPlayer makeSinglePlayerInternal(PlayerInterface ep, PlayerColor color) {
    this.colorToExternalPlayer.put(color, ep);
    return new HexPlayer(color);
  }


  // ---- RUNNING GAME ---- //

  /**
   * Run each of the phases of fish as well as broadcast moves to the players. This does the following:
   *  - broadcast the initial gamestate
   *  - run the placing of penguins
   *  - run the moving of penguins
   *  - return the winners of the game once it is over
   *
   * @param gs the gamestate to run a game on
   * @return the winners of this game
   */
  List<PlayerColor> playGame(GameState gs) {
    this.broadcastGameState(gs);

    // place penguins (we know there are enough spots)
    gs = this.runPlacePenguins(gs);
    gs.startPlay();

    // play the entire movement phase
    gs = this.runMovingPenguins(gs);

    return gs.getWinners();

  }

  // ---- PLACING PENGUINS PHASE ---- //
  GameState runPlacePenguins(GameState gs) {
    //
    int numPlayers = gs.getPlayers().size();
    int numRounds = PENGUIN_SUBRACT_NUM - numPlayers;

    for (int ii = 0; ii < numRounds; ii++) {
      for (int jj = 0; jj < numPlayers; jj++) {
        if (numPlayers <= 1){
          return gs;
        }
        PlayerColor currentPlayer = gs.getCurrentPlayer();

        PlayerInterface ep = this.colorToExternalPlayer.get(currentPlayer);

        Coord attempt;
        try {
          attempt = this.getPlayerPlacement(ep);
          gs.placePenguin(attempt, currentPlayer);
        } catch (Exception e) {
//          throw new IllegalArgumentException(e);
          cheaters.add(ep);
          gs.removeCurrentPlayer();
          numPlayers--;
          jj--;
          this.broadcastPlayerRemoved(currentPlayer);
          continue;
        }
        this.broadcastPenguinPlacement(attempt, currentPlayer);
      }
    }

    return gs;
  }

  Coord getPlayerPlacement(PlayerInterface pi) throws TimeoutException {
    Callable<Coord> task = pi::getPenguinPlacement;
    return communicateWithPlayer(task);
  }

  // ---- MOVING PENGUINS PHASE ---- //
  GameState runMovingPenguins(GameState gs) {

    GameTree gt = new HexGameTree(gs);

    while (!gt.getState().isGameOver()) {
      PlayerColor currentPlayer = gs.getCurrentPlayer();

      PlayerInterface ep = this.colorToExternalPlayer.get(currentPlayer);

      Move attempt;

      try {
        attempt  = this.getPlayerMove(ep);
        gt = gt.getNextGameTree(attempt);
      } catch (Exception e) {
        cheaters.add(ep);
        gs.removeCurrentPlayer();
        this.broadcastPlayerRemoved(currentPlayer);
        continue;
      }
      this.broadcastPenguinMovement(attempt, currentPlayer);
    }

    return gt.getState();
  }

  Move getPlayerMove(PlayerInterface pi) throws TimeoutException {
    Callable<Move> task = pi::getPengiunMovement;
    return communicateWithPlayer(task);
  }


  // ---- BROADCAST MESSAGES ---- //
  void broadcastGameState(GameState gs) {
    for (PlayerColor color : this.colorToExternalPlayer.keySet()) {
      PlayerInterface pi = this.colorToExternalPlayer.get(color);
      pi.receiveInitialGameState(gs.getCopyGameState());
    }
  }

  void broadcastPlayerRemoved(PlayerColor removedPlayer) {
    for (PlayerColor color : this.colorToExternalPlayer.keySet()) {
      PlayerInterface pi = this.colorToExternalPlayer.get(color);
      pi.receivePlayerRemoved(removedPlayer);
    }
  }

  void broadcastPenguinPlacement(Coord loc, PlayerColor color) {
    for (PlayerColor cc : this.colorToExternalPlayer.keySet()) {
      PlayerInterface pi = this.colorToExternalPlayer.get(cc);
      pi.receivePenguinPlacement(loc, color);
    }
  }

  void broadcastPenguinMovement(Move move, PlayerColor color) {
    for (PlayerColor cc : this.colorToExternalPlayer.keySet()) {
      PlayerInterface pi = this.colorToExternalPlayer.get(cc);
      pi.receivePenguinMovement(move, color);
    }
  }

  /**
   * This method uses threads/ futures to call upon a move from a player. It uses the Callable interface
   * (which denotes what method to call/return a value from) and creates an executor. The method then
   * allows for the amount of time specified in TIMEOUT_SECONDS for the player to respond with a move.
   * If no response is had or another exception is thrown, the referee will receive a timeout exception
   * from this method
   *
   * @param action What action to call in a separate thread and wait for a response from. This is
   *               needed so that if a player disconnects or their algorithm never terminates, we will
   *               be able to detect that without blocking the main game thread.
   * @param <T> The type that the action returns when called
   * @return The result of the action
   * @throws TimeoutException if the action fails to execute in a reasonable amount of time.
   */
  static <T> T communicateWithPlayer(Callable<T> action) throws TimeoutException {
    ExecutorService executor = Executors.newCachedThreadPool();
    T result;
    Future<T> future = executor.submit(action);
    try {
      result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    } catch (Exception e) {
      throw new TimeoutException("hit it");
    } finally {
      // this will cancel execution if there is a timeout
      future.cancel(true);
    }
    return result;
  }

}
