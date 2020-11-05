package com.referee;

import com.fish.externalplayer.PlayerInterface;
import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.model.state.*;
import com.player.HousePlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class HexRefereeTest {

  HexReferee blankRef;
  HexReferee redWhiteRef;

  GameState smallStartingGs;
  List<PlayerInterface> ips;

  @Before
  public void setUp() throws Exception {
    this.blankRef = new HexReferee();
    this.redWhiteRef = new HexReferee();

    GameBoard gb = new HexGameBoard(new int[][] {{1, 1, 1, 1, 1, 3},{1, 1, 1, 1, 0, 2}});


    this.ips = Arrays.asList(new HousePlayer(1, "fred"), new HousePlayer(1, "bill"));
    this.smallStartingGs = new HexGameState(GameStage.PLACING_PENGUINS, gb, this.redWhiteRef.makePlayersInternal(this.ips));

  }

  @Test
  public void testMakeBoard() {
    GameBoard gb = this.blankRef.makeGameBoard(10);

    assertEquals(10, gb.getHeight());
    assertEquals(6, gb.getWidth());

    int ones = 0;
    for (int ii = 0; ii < gb.getWidth(); ii++) {
      for (int jj = 0; jj < gb.getHeight(); jj++) {
        if (gb.getTileAt(new Coord(ii, jj)).getNumFish() == 1) {
          ones++;
        }
      }
    }

    assertTrue(ones >= 10);
  }

  @Test
  public void testMakePlayersInternalMt() {
    List<InternalPlayer> mt = this.blankRef.makePlayersInternal(new ArrayList<>());

    assertEquals(0, mt.size());
  }

  @Test
  public void testMakePlayersInternalNotMt() {
    List<PlayerInterface> pi = Arrays.asList(new HousePlayer(10, "fred"),
        new HousePlayer(13, "jill"));

    List<InternalPlayer> players = this.blankRef.makePlayersInternal(pi);

    assertEquals(2, players.size());
    assertEquals(PlayerColor.WHITE, players.get(0).getColor());
    assertEquals(PlayerColor.RED, players.get(1).getColor());
  }

  @Test
  public void testMakeSinglePlayerInternal() {
    InternalPlayer ip = this.blankRef.makeSinglePlayerInternal(
        new HousePlayer(2, "hello"), PlayerColor.BLACK);

    assertEquals(PlayerColor.BLACK, ip.getColor());
    assertEquals(0, ip.getScore());


  }

  @Test
  public void testPlayGame() {
    List<PlayerColor> winners = this.redWhiteRef.playGame(this.smallStartingGs);

    assertEquals(1, winners.size());
    assertEquals(PlayerColor.WHITE, winners.get(0));
  }

  @Test
  public void testPlayGameWithCheater() {
    HexReferee ref = new HexReferee();
    GameBoard gb = new HexGameBoard(new int[][] {{1, 1, 1, 1, 1, 3},{1, 1, 1, 1, 0, 2}});

    List<InternalPlayer> ips = ref.makePlayersInternal(
        Arrays.asList(new Cheater(), new HousePlayer(1, "bill")));
    this.smallStartingGs = new HexGameState(GameStage.PLACING_PENGUINS, gb, ips);

    List<PlayerColor> pcs = ref.playGame(this.smallStartingGs);

    assertEquals(1, pcs.size());

    assertEquals(PlayerColor.RED, pcs.get(0));
  }

  @Test
  public void testPlayGameSameScore() {
    HexReferee ref = new HexReferee();
    GameBoard gb = new HexGameBoard(new int[][] {{1, 1, 1, 1, 1},{1, 1, 1, 1, 1}});

    List<InternalPlayer> ips = ref.makePlayersInternal(
        Arrays.asList(new HousePlayer(1, "joe"), new HousePlayer(1, "bill")));
    this.smallStartingGs = new HexGameState(GameStage.PLACING_PENGUINS, gb, ips);

    List<PlayerColor> pcs = ref.playGame(this.smallStartingGs);

    assertEquals(2, pcs.size());

    assertEquals(PlayerColor.WHITE, pcs.get(0));
    assertEquals(PlayerColor.RED, pcs.get(1));
  }

  @Test
  public void testRunPlacingPengiuns() {
    this.redWhiteRef.broadcastGameState(this.smallStartingGs);
    GameState resultState = this.redWhiteRef.runPlacePenguins(this.smallStartingGs);


    Map<Coord, PlayerColor> locs = resultState.getPenguinLocations();

    assertEquals(PlayerColor.WHITE, locs.get(new Coord(0, 0)));
    assertEquals(PlayerColor.WHITE, locs.get(new Coord(0, 1)));
    assertEquals(PlayerColor.WHITE, locs.get(new Coord(0, 2)));
    assertEquals(PlayerColor.WHITE, locs.get(new Coord(0, 3)));
    assertEquals(PlayerColor.RED, locs.get(new Coord(1, 0)));
    assertEquals(PlayerColor.RED, locs.get(new Coord(1, 1)));
    assertEquals(PlayerColor.RED, locs.get(new Coord(1, 2)));
    assertEquals(PlayerColor.RED, locs.get(new Coord(1, 3)));

  }

  @Test
  public void testPlacePenguinsCheater() {
    HexReferee ref = new HexReferee();
    GameBoard gb = new HexGameBoard(new int[][] {{1, 1, 1, 1, 1, 3},{1, 1, 1, 1, 0, 2}});

    List<InternalPlayer> ips = ref.makePlayersInternal(
        Arrays.asList(new Cheater(), new HousePlayer(1, "bill")));
    this.smallStartingGs = new HexGameState(GameStage.PLACING_PENGUINS, gb, ips);

    //List<PlayerColor> pcs = ref.playGame(this.smallStartingGs);
    ref.broadcastGameState(this.smallStartingGs);
    GameState other = ref.runPlacePenguins(this.smallStartingGs);


    assertEquals(1, other.getPlayers().size());
    assertTrue(other.isGameOver());
  }

  @Test(expected = TimeoutException.class)
  public void testGetPlacementCheater() throws TimeoutException {
    this.redWhiteRef.getPlayerPlacement(new Cheater());
  }

  @Test
  public void testGetPengiunPlacementValid() throws TimeoutException {
    HexReferee ref = new HexReferee();
    GameBoard gb = new HexGameBoard(new int[][] {{2, 2, 2, 1, 1, 3},{3, 3, 1, 1, 0, 2}});

    PlayerInterface pi = new HousePlayer(1, "bella");
    List<InternalPlayer> ips = ref.makePlayersInternal(
        Arrays.asList(pi, new HousePlayer(1, "bill")));
    this.smallStartingGs = new HexGameState(GameStage.PLACING_PENGUINS, gb, ips);

    //List<PlayerColor> pcs = ref.playGame(this.smallStartingGs);

    //assertEquals(1, pcs.size());

    //assertEquals(PlayerColor.RED, pcs.get(0));
    ref.broadcastGameState(this.smallStartingGs);
    Coord loc = ref.getPlayerPlacement(pi);

    assertEquals(new Coord(1, 2), loc);
  }

  @Test
  public void testRunMovingPengiuns() {
    this.redWhiteRef.broadcastGameState(this.smallStartingGs);
    GameState resultState = this.redWhiteRef.runPlacePenguins(this.smallStartingGs);
    resultState.startPlay();
    GameState finalState = this.redWhiteRef.runMovingPenguins(resultState);


    Map<Coord, PlayerColor> locs = finalState.getPenguinLocations();

    assertEquals(PlayerColor.WHITE, locs.get(new Coord(0, 0)));
    assertEquals(PlayerColor.WHITE, locs.get(new Coord(0, 1)));
    assertEquals(PlayerColor.WHITE, locs.get(new Coord(0, 5)));
    assertEquals(PlayerColor.WHITE, locs.get(new Coord(0, 4)));
    assertEquals(PlayerColor.RED, locs.get(new Coord(1, 0)));
    assertEquals(PlayerColor.RED, locs.get(new Coord(1, 1)));
    assertEquals(PlayerColor.RED, locs.get(new Coord(1, 5)));
    assertEquals(PlayerColor.RED, locs.get(new Coord(1, 2)));

  }

  @Test
  public void testMovePenguinsCheater() {
    HexReferee ref = new HexReferee();
    GameBoard gb = new HexGameBoard(new int[][] {{1, 1, 1, 1, 1, 3},{1, 1, 1, 1, 0, 2}});

    List<InternalPlayer> ips = ref.makePlayersInternal(
        Arrays.asList(new Cheater(), new HousePlayer(1, "bill")));
    this.smallStartingGs = new HexGameState(GameStage.PLACING_PENGUINS, gb, ips);
    this.smallStartingGs.placePenguin(new Coord(0, 0), PlayerColor.WHITE);

    this.smallStartingGs.startPlay();
    //List<PlayerColor> pcs = ref.playGame(this.smallStartingGs);
    ref.broadcastGameState(this.smallStartingGs);
    GameState other = ref.runMovingPenguins(this.smallStartingGs);


    assertEquals(1, other.getPlayers().size());
    assertEquals(PlayerColor.RED, other.getPlayers().get(0).getColor());
    assertTrue(other.isGameOver());
  }

  @Test(expected = TimeoutException.class)
  public void testGetMovementCheater() throws TimeoutException {
    this.redWhiteRef.getPlayerMove(new Cheater());
  }

  @Test
  public void testGetMovementNoCheat() throws TimeoutException {
    HexReferee ref = new HexReferee();
    GameBoard gb = new HexGameBoard(new int[][] {{2, 2, 2, 1, 1, 3},{3, 3, 1, 1, 0, 2}});

    PlayerInterface pi = new HousePlayer(1, "bella");
    List<InternalPlayer> ips = ref.makePlayersInternal(
        Arrays.asList(pi, new HousePlayer(1, "bill")));
    this.smallStartingGs = new HexGameState(GameStage.PLACING_PENGUINS, gb, ips);
    this.smallStartingGs.placePenguin(new Coord(0, 0), PlayerColor.WHITE);
    this.smallStartingGs.startPlay();
    ref.broadcastGameState(this.smallStartingGs);
    Move move = ref.getPlayerMove(pi);

    assertEquals(new Move(new Coord(0, 0), new Coord(0, 1)), move);
  }

  //TODO: test broadcast methods

  @Test
  public void testBroadastGameState() {
    try {
      this.ips.get(0).getPengiunMovement();
      fail();
    }
    catch (Exception e) {
      // means the test passed
    }
    this.redWhiteRef.broadcastGameState(this.smallStartingGs);

    assertEquals(new Coord(0, 0), this.ips.get(0).getPenguinPlacement());
    assertEquals(new Coord(0, 0), this.ips.get(1).getPenguinPlacement());
  }

  @Test
  public void testBroadcastPenguinPlacement() {
    try {
      this.ips.get(0).getPengiunMovement();
      fail();
    }
    catch (Exception e) {
      // means the test passed
    }
    this.redWhiteRef.broadcastGameState(this.smallStartingGs);
    this.redWhiteRef.broadcastPenguinPlacement(new Coord(0, 0), PlayerColor.WHITE);
    assertEquals(new Coord(1, 0), this.ips.get(0).getPenguinPlacement());
    assertEquals(new Coord(1, 0), this.ips.get(1).getPenguinPlacement());
  }

  @Test
  public void testBroadcastPenguinMovement() {
    try {
      this.ips.get(0).getPengiunMovement();
      fail();
    }
    catch (Exception e) {
      // means the test passed
    }
    this.redWhiteRef.broadcastGameState(this.smallStartingGs);
    this.redWhiteRef.broadcastPenguinPlacement(new Coord(0, 0), PlayerColor.WHITE);
    this.redWhiteRef.broadcastPenguinPlacement(new Coord(1, 0), PlayerColor.RED);

    // checks that the best move changes after the referee broadcasts a move to the players
    assertEquals(new Move(new Coord(0, 0), new Coord(0, 1)),
        this.ips.get(0).getPengiunMovement());
    this.redWhiteRef.broadcastPenguinMovement(new Move(new Coord(0, 0), new Coord(0, 2)), PlayerColor.WHITE);
    assertEquals(new Move(new Coord(1, 0), new Coord(0, 1)),
        this.ips.get(0).getPengiunMovement());
  }

  @Test
  public void testBroadcastPlayerRemoved() {
    try {
      this.ips.get(0).getPengiunMovement();
      fail();
    }
    catch (Exception e) {
      // means the test passed
    }
    this.redWhiteRef.broadcastGameState(this.smallStartingGs);
    this.redWhiteRef.broadcastPenguinPlacement(new Coord(0, 0), PlayerColor.WHITE);
    this.redWhiteRef.broadcastPenguinPlacement(new Coord(1, 0), PlayerColor.RED);

    // checks that it is the next player's turn once the starting player is removed from the game.
    // although normally this would cause the game to end (since there is one player left), since the
    // referee is asking for a move the player will still be able to give it
    this.redWhiteRef.broadcastPlayerRemoved(PlayerColor.WHITE);
    assertEquals(new Move(new Coord(1, 0), new Coord(0, 1)),this.ips.get(0).getPengiunMovement());
  }



  @Test(expected = TimeoutException.class)
  public void testCommunicateException() throws TimeoutException {
    Callable<Integer> exceptor = () -> {
      throw new IllegalArgumentException();
    };

    HexReferee.communicateWithPlayer(exceptor);
  }

  @Test
  public void testCommunicateWorking() throws TimeoutException {
    Callable<Integer> exceptor = () -> 2 * 5;

    assertEquals(Integer.valueOf(10), HexReferee.communicateWithPlayer(exceptor));
  }


  // cheater implementation that always fails to give a valid move.
  private class Cheater implements PlayerInterface {


    @Override public Coord getPenguinPlacement() {
      throw new IllegalArgumentException();
    }

    @Override public Move getPengiunMovement() {
      throw new IllegalArgumentException();
    }

    @Override public void receivePlayerRemoved(PlayerColor color) {

    }

    @Override public void receivePenguinPlacement(Coord loc, PlayerColor color) {

    }

    @Override public void receivePenguinMovement(Move move, PlayerColor color) {

    }

    @Override public void receiveInitialGameState(GameState gs) {

    }

    @Override public void receiveGameOver(List<PlayerColor> winners) {

    }
  }

}
