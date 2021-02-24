package com.fish.player;

import com.fish.common.Coord;
import com.fish.common.board.GameBoard;
import com.fish.common.board.HexGameBoard;
import com.fish.common.game.Move;
import com.fish.common.state.GameState;
import com.fish.common.state.HexGameState;
import com.fish.common.state.HexPlayer;
import com.fish.common.state.InternalPlayer;
import com.fish.common.state.PlayerColor;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MinimaxStrategyTest {

  private GameState gs1;
  private GameState gs2;

  @Before public void setUp() throws Exception {
    int[][] board = {{2, 3, 5, 1, 2, 3}, {2, 1, 1, 5, 5, 5}, {1, 1, 5, 3, 2, 5}};
    GameBoard gb = new HexGameBoard(board);
    gs1 = new HexGameState();
    gs1.initGame(gb, Arrays.asList(new HexPlayer(PlayerColor.RED),
        new HexPlayer(PlayerColor.BLACK), new HexPlayer(PlayerColor.WHITE)));


    gb = new HexGameBoard(new int[][]{{2, 1, 3,5,2,2}});
    List<InternalPlayer> players = Arrays.asList(new HexPlayer(PlayerColor.RED),
        new HexPlayer(PlayerColor.BLACK));

    gs2 = new HexGameState();
    gs2.initGame(gb, players);
    gs2.placePenguin(new Coord(0, 5), PlayerColor.RED);
    gs2.placePenguin(new Coord(0, 0), PlayerColor.BLACK);

    gs2.startPlay();
  }

  private void setupGs1Penguins() {
    gs1.placePenguin(new Coord(1, 2), PlayerColor.RED);
    gs1.placePenguin(new Coord(1, 0), PlayerColor.BLACK);
    gs1.placePenguin(new Coord(0, 0), PlayerColor.WHITE);
    gs1.placePenguin(new Coord(0, 1), PlayerColor.RED);
    gs1.placePenguin(new Coord(2, 5), PlayerColor.BLACK);
    gs1.placePenguin(new Coord(2, 2), PlayerColor.WHITE);

    this.gs1.startPlay();
  }

  @Test public void findNextPenguinPlacement() {
    assertEquals(new Coord(2, 0), MinimaxStrategy.findNextPenguinPlacement(this.gs1));
    gs1.placePenguin(new Coord(2, 0), PlayerColor.RED);
    assertEquals(new Coord(1, 1), MinimaxStrategy.findNextPenguinPlacement(this.gs1));
    gs1.placePenguin(new Coord(1, 1), PlayerColor.BLACK);
    assertEquals(new Coord(2, 1), MinimaxStrategy.findNextPenguinPlacement(this.gs1));
    gs1.placePenguin(new Coord(2, 1), PlayerColor.WHITE);
    assertEquals(new Coord(1, 2), MinimaxStrategy.findNextPenguinPlacement(this.gs1));
    gs1.placePenguin(new Coord(1, 2), PlayerColor.RED);
    assertEquals(new Coord(0, 3), MinimaxStrategy.findNextPenguinPlacement(this.gs1));
  }

  @Test
  public void findNextPenguinPlacementNoneAvailable() {
    gs1.placePenguin(new Coord(2, 0), PlayerColor.RED);
    gs1.placePenguin(new Coord(1, 1), PlayerColor.BLACK);
    gs1.placePenguin(new Coord(2, 1), PlayerColor.WHITE);
    gs1.placePenguin(new Coord(1, 2), PlayerColor.RED);
    gs1.placePenguin(new Coord(0, 3), PlayerColor.BLACK);

    assertNull(MinimaxStrategy.findNextPenguinPlacement(this.gs1));
  }


  @Test public void findCurrentPlayersBestMoveTwoMovesRedSmallBoard() {
    Move move = MinimaxStrategy.findCurrentPlayersBestMove(this.gs2, 2);

    assertEquals(new Move(new Coord(0, 5), new Coord(0, 3)), move);
  }

  @Test
  public void testBestMoveOnePenguin() {
    Move move = MinimaxStrategy.findCurrentPlayersBestMove(this.gs2, 1);

    assertEquals(new Move(new Coord(0, 5), new Coord(0, 1)), move);
  }

  @Test
  public void testBestMoveMultiplePenguins() {
    this.setupGs1Penguins();

    Move move = MinimaxStrategy.findCurrentPlayersBestMove(this.gs1, 1);

    assertEquals(new Move(new Coord(0, 1), new Coord(0, 2)), move);

    this.gs1.movePenguin(new Coord(0, 1), new Coord(0, 2));

    move = MinimaxStrategy.findCurrentPlayersBestMove(this.gs1, 1);
    assertEquals(new Move(new Coord(2, 5), new Coord(2, 1)), move);
  }

  @Test
  public void testBestMoveMultiplePengiunsMultipleMoves() {
    this.setupGs1Penguins();
    Move move = MinimaxStrategy.findCurrentPlayersBestMove(this.gs1, 2);
    assertEquals(new Move(new Coord(0, 1), new Coord(0, 2)), move);

    move = MinimaxStrategy.findCurrentPlayersBestMove(this.gs1, 3);
    assertEquals(new Move(new Coord(1, 2), new Coord(1, 3)), move);
  }

  @Test
  public void testStrategyForHarness() {
    int[][] board = {{2, 1, 5, 0, 3, 2}, {3, 1, 5, 4, 3, 2}, {4, 1, 5, 4, 3, 2}};
    GameBoard gb = new HexGameBoard(board);
    GameState gs = new HexGameState();

    List<InternalPlayer> players = new ArrayList<>();

    players.add(new HexPlayer(PlayerColor.RED));
    players.add(new HexPlayer(PlayerColor.WHITE));

    gs.initGame(gb, players);
    players.get(0).addToScore(10);
    gs.placePenguin( new Coord(0,2), PlayerColor.RED);
    gs.placePenguin( new Coord(0,1), PlayerColor.WHITE);
    gs.placePenguin( new Coord(1,0), PlayerColor.RED);
    gs.placePenguin( new Coord(1,1), PlayerColor.WHITE);
    gs.startPlay();

    assertEquals(new Move(new Coord(1,0), new Coord(1,2)),
        MinimaxStrategy.findCurrentPlayersBestMove(gs, 2));
  }

  @Test
  public void testStrategyForHarnessExample() {
    int[][] board = {{1, 1, 0, 0, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 1}, {1, 1, 0, 0, 0, 1, 1}};
    GameBoard gb = new HexGameBoard(board);
    GameState gs = new HexGameState();

    List<InternalPlayer> players = new ArrayList<>();

    players.add(new HexPlayer(PlayerColor.RED));
    players.add(new HexPlayer(PlayerColor.WHITE));
    players.add(new HexPlayer(PlayerColor.BROWN));

    gs.initGame(gb, players);
    gs.placePenguin( new Coord(0,0), PlayerColor.RED);
    gs.placePenguin( new Coord(0,5), PlayerColor.WHITE);
    gs.placePenguin( new Coord(0,6), PlayerColor.BROWN);
    gs.placePenguin( new Coord(0,4), PlayerColor.RED);
    gs.placePenguin( new Coord(1,5), PlayerColor.WHITE);
    gs.placePenguin( new Coord(1,6), PlayerColor.BROWN);
    gs.placePenguin( new Coord(1,4), PlayerColor.RED);
    gs.placePenguin( new Coord(2,5), PlayerColor.WHITE);
    gs.placePenguin( new Coord(2,6), PlayerColor.BROWN);
    gs.startPlay();

    assertEquals(new Move(new Coord(0,0), new Coord(0,1)),
        MinimaxStrategy.findCurrentPlayersBestMove(gs, 2));
  }
}
