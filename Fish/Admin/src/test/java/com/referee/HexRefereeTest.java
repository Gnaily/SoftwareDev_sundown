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

import static org.junit.Assert.*;

public class HexRefereeTest {

  HexReferee blankRef;
  HexReferee redWhiteRef;

  GameState smallStartingGs;

  @Before
  public void setUp() throws Exception {
    this.blankRef = new HexReferee();
    this.redWhiteRef = new HexReferee();

    GameBoard gb = new HexGameBoard(new int[][] {{1, 1, 1, 1, 1, 3},{1, 1, 1, 1, 0, 2}});

    List<InternalPlayer> ips = this.redWhiteRef.makePlayersInternal(
        Arrays.asList(new HousePlayer(1, "fred"), new HousePlayer(1, "bill")));
    this.smallStartingGs = new HexGameState(GameStage.PLACING_PENGUINS, gb, ips);

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




  // cheater implementation that always fails to give a valid move.
  private class Cheater implements PlayerInterface {


    @Override public Coord getPenguinPlacement() {
      return new Coord(-1, -1);
    }

    @Override public Move getPengiunMovement() {
      return new Move(new Coord(-1, -1), new Coord(0, 0));
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
