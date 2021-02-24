package com.fish.player;

import com.fish.common.Coord;
import com.fish.common.board.GameBoard;
import com.fish.common.board.HexGameBoard;
import com.fish.common.game.Move;
import com.fish.common.state.GameStage;
import com.fish.common.state.GameState;
import com.fish.common.state.HexGameState;
import com.fish.common.state.HexPlayer;
import com.fish.common.state.InternalPlayer;
import com.fish.common.state.PlayerColor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HousePlayerTest {

  PlayerInterface pi;
  PlayerInterface emptyPi;
  GameState gs;

  @Before
  public void setUp() throws Exception {
    this.pi = new HousePlayer(1, "kcorb");
    this.emptyPi = new HousePlayer(2, "brock");

    GameBoard gb = new HexGameBoard(new int[][] {{1, 1, 3, 4}, {2, 3, 1, 5}});

    List<InternalPlayer> players = Arrays.asList(
        new HexPlayer(PlayerColor.RED), new HexPlayer(PlayerColor.BLACK)
    );

    this.gs = new HexGameState(GameStage.PLACING_PENGUINS, gb, players);
    gs.placePenguin(new Coord(0, 0), PlayerColor.RED);
    gs.placePenguin(new Coord(1, 0), PlayerColor.BLACK);
    gs.placePenguin(new Coord(0, 3), PlayerColor.RED);
    gs.placePenguin(new Coord(1, 3), PlayerColor.BLACK);

    this.pi.receiveInitialGameState(gs);

  }

  @Test
  public void getPenguinPlacement() {
    assertEquals(new Coord(0, 1), this.pi.getPenguinPlacement());
    this.gs.placePenguin(new Coord(0, 1), PlayerColor.RED);
    assertEquals(new Coord(1, 2), this.pi.getPenguinPlacement());
    // tests that the player does not actually place a penguin, just gets the next location
    // according to the strategy
    assertNull(this.gs.getPenguinLocations().get(new Coord(1, 2)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getPengiunPlacementNoState() {
    this.emptyPi.getPenguinPlacement();
  }

  @Test
  public void getPengiunMovement() {
    assertEquals(new Move(new Coord(0, 3), new Coord(0, 1)),
        this.pi.getPengiunMovement());
    assertEquals(PlayerColor.RED, this.gs.getPenguinLocations().get(new Coord(0, 3)));
    assertNull(this.gs.getPenguinLocations().get(new Coord(0, 1)));
    this.gs.startPlay();
    this.gs.movePenguin(new Coord(0, 3), new Coord(0, 2));
    assertEquals(new Move(new Coord(1, 3), new Coord(0, 1)),
        this.pi.getPengiunMovement());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getPengiunMovementNoState() {
    this.emptyPi.getPengiunMovement();
  }

  @Test
  public void receivePlayerRemoved() {
    assertEquals(2, this.gs.getPlayers().size());
    this.pi.receivePlayerRemoved(PlayerColor.RED);
    assertEquals(1, this.gs.getPlayers().size());
    assertEquals(PlayerColor.BLACK, this.gs.getCurrentPlayer());
  }

  @Test
  public void receivePenguinPlacement() {
    assertEquals(4, this.gs.getPenguinLocations().size());
    assertNull(this.gs.getPenguinLocations().get(new Coord(1, 1)));
    this.pi.receivePenguinPlacement(new Coord(1, 1), PlayerColor.RED);
    assertEquals(PlayerColor.RED, this.gs.getPenguinLocations().get(new Coord(1, 1)));
    assertEquals(5, this.gs.getPenguinLocations().size());
    assertEquals(PlayerColor.BLACK, this.gs.getCurrentPlayer());
  }

  @Test
  public void receivePenguinMovement() {
    assertEquals(0, this.gs.getPlayers().get(0).getScore());
    assertNull(this.gs.getPenguinLocations().get(new Coord(0, 2)));
    assertEquals(PlayerColor.RED, this.gs.getPenguinLocations().get(new Coord(0, 3)));

    this.pi.receivePenguinMovement(new Move(new Coord(0, 3), new Coord(0, 2)), PlayerColor.RED);

    assertNull(this.gs.getPenguinLocations().get(new Coord(3, 0)));
    assertEquals(PlayerColor.RED, this.gs.getPenguinLocations().get(new Coord(0, 2)));
    assertEquals(PlayerColor.BLACK, this.gs.getCurrentPlayer());
  }

  @Test
  public void receiveInitialGameState() {
    try {
      this.emptyPi.getPengiunMovement();
      fail();
    }
    catch (Exception e) {
      // this means an exception was thrown, which is the desired behavior before a game is assigned
    }

    this.emptyPi.receiveInitialGameState(this.gs);

    assertEquals(new Coord(0, 1), this.emptyPi.getPenguinPlacement());
    assertEquals(new Move(new Coord(0, 0), new Coord(0, 1)),
        this.emptyPi.getPengiunMovement());


  }
}
