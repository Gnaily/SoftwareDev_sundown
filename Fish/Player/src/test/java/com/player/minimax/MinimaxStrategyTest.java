package com.player.minimax;

import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.model.state.*;
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
}
