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

public class HexReferee implements Referee {

  private List<InternalPlayer> cheaters;
  private Map<PlayerColor, PlayerInterface> colorToExternalPlayer;


  private static final int PENGUIN_SUBRACT_NUM = 6;
  private static final int BOARD_ROW = 10;
  private static final int BOARD_COLS = 6;
  private static final int TIMEOUT_SECONDS = 15;


  public HexReferee() {
    this.cheaters = new ArrayList<>();
    this.colorToExternalPlayer = new HashMap<>();
  }


  //Game.getCurrentPlayer -> find the correct player for color -> player.getNextMove();

  // TODO: how to contact each player

  public void runGame(List<PlayerInterface> players) {
    int size = players.size();

    if (size <= 1 || size > 4) {
      //TODO: handle not enough players
      return;
    }


    int minOneFish = (PENGUIN_SUBRACT_NUM - size) * size;
    GameBoard gb = this.makeGameBoard(minOneFish);

    GameState gs = new HexGameState();

    List<InternalPlayer> internalPlayers = this.makePlayersInternal(players);

    gs.initGame(gb, internalPlayers);

    List<PlayerColor> winners = this.playGame(gs);

    // TODO: do something about the winners
  }


  // ---- GAME SETUP ---- //

  private GameBoard makeGameBoard(int minOneFish) {
    //TODO: add in difficulty
    return new HexGameBoard(BOARD_ROW, BOARD_COLS, new ArrayList<>(), minOneFish);


  }

  private List<InternalPlayer> makePlayersInternal(List<PlayerInterface> players) {
    // list of external players is already ordered by age
    List<InternalPlayer> ips = new ArrayList<>();
    List<PlayerColor> colors = Arrays.asList(PlayerColor.WHITE, PlayerColor.RED, PlayerColor.BLACK, PlayerColor.BROWN);

    for (int ii = 0; ii < players.size(); ii++) {
      ips.add(makeSinglePlayerInternal(players.get(ii), colors.get(ii)));
    }

    return ips;
  }

  // this function also handles creating the link between an external player and who they are inside the
  //  game
  private InternalPlayer makeSinglePlayerInternal(PlayerInterface ep, PlayerColor color) {
    this.colorToExternalPlayer.put(color, ep);
    return new HexPlayer(color);
  }


  // ---- RUNNING GAME ---- //

  private List<PlayerColor> playGame(GameState gs) {
    // TODO: broadcast initial gamestate

    // place penguins (we know there are enough spots)
    gs = this.runPlacePenguins(gs);
    gs.startPlay();

    // play the entire movement phase

    // TODO: broadcast the game is starting
    // TODO: run moving penguins method
    gs = this.runMovingPenguins(gs);

    // TODO: report back winners
    // report back the winner
    // TODO: broadcast winners to the players

    return gs.getWinners();

  }

  // ---- PLACING PENGUINS PHASE ---- //
  private GameState runPlacePenguins(GameState gs) {
    //
    int numPlayers = gs.getPlayers().size();
    int numRounds = PENGUIN_SUBRACT_NUM - numPlayers;

    // TODO: handle the game ending while placing penguins
    for (int ii = 0; ii < numRounds; ii++) {
      for (int jj = 0; jj < numPlayers; jj++) {
        PlayerColor currentPlayer = gs.getCurrentPlayer();

        PlayerInterface ep = this.colorToExternalPlayer.get(currentPlayer);

        // TODO: add in some timeout to this / handle it - might be done
        Coord attempt;
        try {
          attempt = this.getPlayerPlacement(ep);
          gs.placePenguin(attempt, currentPlayer);
        } catch (Exception e) {
          gs.removeCurrentPlayer();
          jj--;
          this.broadcastPlayerRemoved(currentPlayer);
          continue;
        }
        this.broadcastPenguinPlacement(attempt, currentPlayer);
      }
    }

    return gs;
  }

  private Coord getPlayerPlacement(PlayerInterface pi) throws TimeoutException {
    Callable<Coord> task = pi::getPenguinPlacement;
    return communicateWithPlayer(task);
  }

  // ---- MOVING PENGUINS PHASE ---- //
  private GameState runMovingPenguins(GameState gs) {

    GameTree gt = new HexGameTree(gs);

    while (!gt.getState().isGameOver()) {
      PlayerColor currentPlayer = gs.getCurrentPlayer();

      PlayerInterface ep = this.colorToExternalPlayer.get(currentPlayer);

      // TODO: add timeout - might be done
      Move attempt;

      try {
        attempt  = this.getPlayerMove(ep);
        gt = gt.getNextGameTree(attempt);
      } catch (Exception e) {
        gs.removeCurrentPlayer();
        this.broadcastPlayerRemoved(currentPlayer);
        continue;
      }
      this.broadcastPenguinMovement(attempt, currentPlayer);
    }

    return gt.getState();
  }

  private Move getPlayerMove(PlayerInterface pi) throws TimeoutException {
    Callable<Move> task = pi::getPengiunMovement;
    return communicateWithPlayer(task);
  }


  // ---- BROADCAST MESSAGES ---- //
  private void broadcastPlayerRemoved(PlayerColor removedPlayer) {
    // TODO: implement
    for (PlayerColor color : this.colorToExternalPlayer.keySet()) {
      PlayerInterface pi = this.colorToExternalPlayer.get(color);
      pi.receivePlayerRemoved(removedPlayer);
    }
  }

  private void broadcastPenguinPlacement(Coord loc, PlayerColor color) {
    // TODO: implement
    for (PlayerColor cc : this.colorToExternalPlayer.keySet()) {
      PlayerInterface pi = this.colorToExternalPlayer.get(cc);
      pi.receivePenguinPlacement(loc, color);
    }
  }

  private void broadcastPenguinMovement(Move move, PlayerColor color) {
    // TODO: implement
    for (PlayerColor cc : this.colorToExternalPlayer.keySet()) {
      PlayerInterface pi = this.colorToExternalPlayer.get(cc);
      pi.receivePenguinMovement(move, color);
    }
  }

  // TODO: explain/figure out what this does
  static <T> T communicateWithPlayer(Callable<T> action) throws TimeoutException {
    ExecutorService executor = Executors.newCachedThreadPool();
    T result;
    Future<T> future = executor.submit(action);
    try {
      result = future.get(5, TimeUnit.SECONDS);
    } catch (Exception e) {
      throw new TimeoutException("hit it");
    } finally {
      // this will cancel execution if there is a timeout
      future.cancel(true);
    }

    return result;
  }

}
