package com.player.minimax;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.model.state.GameState;
import com.fish.model.state.HexGameState;
import com.fish.model.state.HexPlayer;
import com.fish.model.state.PlayerColor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MinimaxStrategyTest {

  private GameState gs1;

  @Before public void setUp() throws Exception {
    int[][] board = {{2, 3, 5, 1}, {2, 1, 1, 5}, {1, 1, 5, 0}};
    GameBoard gb = new HexGameBoard(board);
    gs1 = new HexGameState();
    gs1.initGame(gb, Arrays.asList(new HexPlayer(PlayerColor.BLACK),
        new HexPlayer(PlayerColor.RED)));
  }

  @Test public void findNextPenguinPlacement() {
    assertEquals(new Coord(2, 0), MinimaxStrategy.findNextPenguinPlacement(this.gs1));
    gs1.placePenguin(new Coord(2, 0), PlayerColor.BLACK);
    assertEquals(new Coord(1, 1), MinimaxStrategy.findNextPenguinPlacement(this.gs1));
    gs1.placePenguin(new Coord(1, 1), PlayerColor.RED);
    assertEquals(new Coord(2, 1), MinimaxStrategy.findNextPenguinPlacement(this.gs1));
    gs1.placePenguin(new Coord(2, 1), PlayerColor.BLACK);
    assertEquals(new Coord(1, 2), MinimaxStrategy.findNextPenguinPlacement(this.gs1));
    gs1.placePenguin(new Coord(1, 2), PlayerColor.RED);
    assertEquals(new Coord(0, 3), MinimaxStrategy.findNextPenguinPlacement(this.gs1));
  }

  @Test
  public void findNextPenguinPlacementNoneAvailable() {
    gs1.placePenguin(new Coord(2, 0), PlayerColor.BLACK);
    gs1.placePenguin(new Coord(1, 1), PlayerColor.RED);
    gs1.placePenguin(new Coord(2, 1), PlayerColor.BLACK);
    gs1.placePenguin(new Coord(1, 2), PlayerColor.RED);
    gs1.placePenguin(new Coord(0, 3), PlayerColor.BLACK);

    assertNull(MinimaxStrategy.findNextPenguinPlacement(this.gs1));
  }



  @Test public void findCurrentPlayersBestMove() {
  }
}
