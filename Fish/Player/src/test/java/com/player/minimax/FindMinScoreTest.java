package com.player.minimax;

import com.fish.game.GameTree;
import com.fish.game.HexGameTree;
import com.fish.game.NodeCounter;
import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.model.state.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FindMinScoreTest {

  private GameTree gt1;

  @Before public void setUp() throws Exception {
    //GameBoard gb = new HexGameBoard(new int[][]{{2, 1, 3}, {5, 2, 2}});
    GameBoard gb = new HexGameBoard(new int[][]{{2, 1, 3,5,2,2}});
    List<InternalPlayer> players = Arrays.asList(new HexPlayer(PlayerColor.RED),
        new HexPlayer(PlayerColor.BLACK));

    GameState gs = new HexGameState();
    gs.initGame(gb, players);
//    gs.placePenguin(new Coord(1, 2), PlayerColor.RED);
//    gs.placePenguin(new Coord(1, 0), PlayerColor.BLACK);
    gs.placePenguin(new Coord(0, 5), PlayerColor.RED);
    gs.placePenguin(new Coord(0, 0), PlayerColor.BLACK);

    gs.startPlay();
    this.gt1 = new HexGameTree(gs);
  }

  @Test
  public void applyToRed() {
    FindMinScore fms = new FindMinScore(2, PlayerColor.BLACK);
    System.out.println(HexGameTree.applyToAllReachableStates(this.gt1, fms, new ArrayList<>()));
    // [3, 3, 3, 3, 1, 3, 3, 1, 1, 2, 2, 2, 1, 1, 1, 5, 5, 1, 1]
    //System.out.println(HexGameTree.applyToAllReachableStates(this.gt1, new NodeCounter(), 0));
  }
}
