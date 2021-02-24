package com.fish.player;

import com.fish.common.Coord;
import com.fish.common.board.GameBoard;
import com.fish.common.board.HexGameBoard;
import com.fish.common.game.GameTree;
import com.fish.common.game.HexGameTree;
import com.fish.common.game.Move;
import com.fish.common.state.GameState;
import com.fish.common.state.HexGameState;
import com.fish.common.state.HexPlayer;
import com.fish.common.state.InternalPlayer;
import com.fish.common.state.PlayerColor;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MinMaxAlgorithmTest {

  private GameTree gt1;
  private GameTree gt2;
  private MinMaxAlgorithm fms;

  @Before public void setUp() throws Exception {
    GameBoard gb = new HexGameBoard(new int[][]{{2, 1, 3,5,2,2}});
    List<InternalPlayer> players = Arrays.asList(new HexPlayer(PlayerColor.RED),
        new HexPlayer(PlayerColor.BLACK));

    GameState gs = new HexGameState();
    gs.initGame(gb, players);
    gs.placePenguin(new Coord(0, 5), PlayerColor.RED);
    gs.placePenguin(new Coord(0, 0), PlayerColor.BLACK);

    gs.startPlay();
    this.gt1 = new HexGameTree(gs);

    gb = new HexGameBoard(new int[][]{{2, 1, 3,5,2,2}, {3, 2, 5, 2, 1, 1}});
    players = Arrays.asList(new HexPlayer(PlayerColor.RED),
        new HexPlayer(PlayerColor.BLACK));

    gs = new HexGameState();
    gs.initGame(gb, players);
    gs.placePenguin(new Coord(0, 5), PlayerColor.RED);
    gs.placePenguin(new Coord(0, 0), PlayerColor.BLACK);

    gs.startPlay();
    this.gt2 = new HexGameTree(gs);
    this.fms = new MinMaxAlgorithm(1, PlayerColor.RED);
  }

  @Test
  public void applyToRedThroughTreeOneStep() {
    List<MoveValue> moves = HexGameTree.applyToAllReachableStates(this.gt1, fms, new ArrayList<>());

    assertEquals(3, moves.size());
    Map<Move, Integer> moveMap = new HashMap<>();
    for (MoveValue mv : moves) {
      moveMap.put(mv.getMove(), mv.getValue());
    }

    assertEquals(Integer.valueOf(7), moveMap.get(new Move(new Coord(0, 5), new Coord(0, 3))));
    assertEquals(Integer.valueOf(3), moveMap.get(new Move(new Coord(0, 5), new Coord(0, 1))));
    assertEquals(Integer.valueOf(4), moveMap.get(new Move(new Coord(0, 5), new Coord(0, 4))));
  }

  @Test
  public void testApplyToRedTwoDeep() {
    this.fms = new MinMaxAlgorithm(2, PlayerColor.RED);
    List<MoveValue> moves = HexGameTree.applyToAllReachableStates(this.gt2, fms, new ArrayList<>());

    assertEquals(5, moves.size());

    MoveValue mv = MinMaxAlgorithm.calculateBestMove(moves).get(0);
    assertEquals(10, mv.getValue());
    assertEquals(new Move(new Coord(0, 5), new Coord(0, 3)), mv.getMove());
  }

  @Test
  public void testApplyToRedThreeDeep() {
    this.fms = new MinMaxAlgorithm(3, PlayerColor.RED);
    List<MoveValue> moves = HexGameTree.applyToAllReachableStates(this.gt2, fms, new ArrayList<>());

    assertEquals(5, moves.size());

    MoveValue mv = MinMaxAlgorithm.calculateBestMove(moves).get(0);
    assertEquals(13, mv.getValue());
    assertEquals(new Move(new Coord(0, 5), new Coord(0, 1)), mv.getMove());
  }

  @Test
  public void testBestValue() {

    List<MoveValue> moves = Arrays.asList(
        new MoveValue(new Move(new Coord(0, 0), new Coord(0, 3)), 4),
        new MoveValue(new Move(new Coord(0, 0), new Coord(0, 2)), 8),
        new MoveValue(new Move(new Coord(1, 0), new Coord(0, 3)), 7),
        new MoveValue(new Move(new Coord(33, 1), new Coord(0, 3)), 3));

    assertEquals(3, MinMaxAlgorithm.bestValue(moves, false));
    assertEquals(8, MinMaxAlgorithm.bestValue(moves, true));
  }

  @Test
  public void testFindLowestRowColBasic() {
    List<Coord> coords = Arrays.asList(new Coord(12, 32), new Coord(12, 33),
        new Coord(11, 33));

    assertEquals(new Coord(12, 32), MinMaxAlgorithm.findLowestRowCol(coords));

    coords = Arrays.asList(new Coord(11, 34), new Coord(12, 33),
        new Coord(11, 33));

    assertEquals(new Coord(11, 33), MinMaxAlgorithm.findLowestRowCol(coords));
  }

  @Test
  public void testFindLowestRowColAdvanced() {
    List<Coord> coords = Arrays.asList(new Coord(0, 10), new Coord(0, 9),
        new Coord(11, 2), new Coord(10, 2), new Coord(12, 2));

    assertEquals(new Coord(10, 2), MinMaxAlgorithm.findLowestRowCol(coords));
  }

  @Test
  public void testSkippedPlayerBlank() {
    assertFalse(this.fms.skippedPlayer(this.gt1));
  }

  @Test
  public void testSkippedPlayerFalse() {
    this.gt1 = this.gt1.getNextGameTree(new Move(new Coord(0, 5), new Coord(0, 3)));
    assertFalse(this.fms.skippedPlayer(this.gt1));
    this.gt1 = this.gt1.getNextGameTree(new Move(new Coord(0, 0), new Coord(0, 1)));
    assertFalse(this.fms.skippedPlayer(this.gt1));
    this.gt1 = this.gt1.getNextGameTree(new Move(new Coord(0, 3), new Coord(0, 4)));
    assertFalse(this.fms.skippedPlayer(this.gt1));
  }

  @Test
  public void testSkippedPlayerTrue() {
    this.gt1 = this.gt1.getNextGameTree(new Move(new Coord(0, 5), new Coord(0, 3)));
    this.gt1 = this.gt1.getNextGameTree(new Move(new Coord(0, 0), new Coord(0, 1)));
    MinMaxAlgorithm fms2 = new MinMaxAlgorithm(1, PlayerColor.BLACK);
    assertFalse(fms2.skippedPlayer(this.gt1));
    this.gt1 = this.gt1.getNextGameTree(new Move(new Coord(0, 3), new Coord(0, 2)));
    assertTrue(fms2.skippedPlayer(this.gt1));

  }

  @Test
  public void testCalculateBestMoveBasic() {
    List<MoveValue> moves = Arrays.asList(
        new MoveValue(new Move(new Coord(0, 0), new Coord(0, 3)), 4),
        new MoveValue(new Move(new Coord(0, 0), new Coord(0, 2)), 8),
        new MoveValue(new Move(new Coord(1, 0), new Coord(0, 3)), 7),
        new MoveValue(new Move(new Coord(33, 1), new Coord(0, 3)), 3));

    List<MoveValue> best = MinMaxAlgorithm.calculateBestMove(moves);
    assertEquals(1, best.size());
    assertEquals(8, best.get(0).getValue());
    assertEquals(new Move(new Coord(0, 0), new Coord(0 ,2)), best.get(0).getMove());
  }

  @Test
  public void testCalculateBestMoveStartSame() {
    List<MoveValue> moves = Arrays.asList(
        new MoveValue(new Move(new Coord(0, 0), new Coord(0, 3)), 4),
        new MoveValue(new Move(new Coord(0, 0), new Coord(0, 3)), 32),
        new MoveValue(new Move(new Coord(0, 0), new Coord(0, 1)), 32),
        new MoveValue(new Move(new Coord(33, 1), new Coord(0, 3)), 3));

    List<MoveValue> best = MinMaxAlgorithm.calculateBestMove(moves);
    assertEquals(1, best.size());
    assertEquals(32, best.get(0).getValue());
    assertEquals(new Move(new Coord(0, 0), new Coord(0 ,1)), best.get(0).getMove());
  }

  @Test
  public void testCalculateBestMoveAdvanced() {
    List<MoveValue> moves = Arrays.asList(
        new MoveValue(new Move(new Coord(0, 0), new Coord(0, 3)), 4),
        new MoveValue(new Move(new Coord(0, 2), new Coord(0, 3)), 32),
        new MoveValue(new Move(new Coord(0, 3), new Coord(0, 1)), 32),
        new MoveValue(new Move(new Coord(33, 1), new Coord(0, 3)), 3));

    List<MoveValue> best = MinMaxAlgorithm.calculateBestMove(moves);
    assertEquals(1, best.size());
    assertEquals(32, best.get(0).getValue());
    assertEquals(new Move(new Coord(0, 2), new Coord(0 ,3)), best.get(0).getMove());
  }

  @Test
  public void testOptimalMove() {
    List<Move> moves = Arrays.asList(
        new Move(new Coord(1, 0), new Coord(10, 22)),
        new Move(new Coord(0, 1), new Coord(22, 8)),
        new Move(new Coord(0, 2), new Coord(10, 2)),
        new Move(new Coord(1, 0), new Coord(1, 9)),
        new Move(new Coord(0, 4), new Coord(10, 22)));

    assertEquals(new Move(new Coord(1, 0), new Coord(1, 9)),
        MinMaxAlgorithm.findLowestRowColMove(moves));
  }

  @Test
  public void testOptimalMoveEndDifferent() {
    List<Move> moves = Arrays.asList(
        new Move(new Coord(1, 0), new Coord(0, 22)),
        new Move(new Coord(1, 0), new Coord(0, 8)),
        new Move(new Coord(1, 0), new Coord(10, 2)),
        new Move(new Coord(1, 0), new Coord(9, 2)),
        new Move(new Coord(1, 0), new Coord(10, 22)));

    assertEquals(new Move(new Coord(1, 0), new Coord(9, 2)),
        MinMaxAlgorithm.findLowestRowColMove(moves));
  }

  @Test
  public void testFindBestMove() {
    assertEquals(new Move(new Coord(0, 5), new Coord(0, 1)),
        MinMaxAlgorithm.findBestMove(this.gt1.getState()));

    assertEquals(new Move(new Coord(0, 5), new Coord(0, 1)),
        MinMaxAlgorithm.findBestMove(this.gt2.getState()));

    this.gt1 = this.gt1.getNextGameTree(new Move(new Coord(0, 5), new Coord(0, 1)));

    assertEquals(new Move(new Coord(0, 0), new Coord(0, 2)),
        MinMaxAlgorithm.findBestMove(this.gt1.getState()));
  }

  @Test
  public void testAddMoveMiddleTree() {
    List<MoveValue> scores = this.fms.addMoveMiddleOfTree(
        this.gt1.getNextGameTree(new Move(new Coord(0, 5), new Coord(0, 3))),
        0, new ArrayList<>());

    assertEquals(1, scores.size());
    assertEquals(new Move(new Coord(0, 5), new Coord(0, 3)), scores.get(0).getMove());
    assertEquals(7, scores.get(0).getValue());

    scores = this.fms.addMoveMiddleOfTree(
        this.gt1.getNextGameTree(new Move(new Coord(0, 5), new Coord(0, 1))),
        0, scores);

    assertEquals(2, scores.size());
    assertEquals(new Move(new Coord(0, 5), new Coord(0, 1)), scores.get(1).getMove());
    assertEquals(3, scores.get(1).getValue());
  }

  @Test
  public void addFinalMove() {
    this.gt1 = this.gt1.getNextGameTree(new Move(new Coord(0, 5), new Coord(0, 3)));
    List<MoveValue> scores = this.fms.addFinalMove(this.gt1, new ArrayList<>());

    assertEquals(1, scores.size());
    assertEquals(new Move(new Coord(0, 5), new Coord(0, 3)), scores.get(0).getMove());
    assertEquals(2, scores.get(0).getValue());
  }

  @Test
  public void testAddFinalMoveMultiplePenguins() {
    GameBoard gb = new HexGameBoard(new int[][]{{2, 1, 3,5,2,2}, {3, 2, 5, 2, 1, 1}});
    List<InternalPlayer> players = Arrays.asList(new HexPlayer(PlayerColor.RED),
        new HexPlayer(PlayerColor.BLACK));

    GameState gs = new HexGameState();
    gs.initGame(gb, players);
    gs.placePenguin(new Coord(0, 5), PlayerColor.RED);
    gs.placePenguin(new Coord(0, 0), PlayerColor.BLACK);
    gs.placePenguin(new Coord(1, 2), PlayerColor.RED);
    gs.placePenguin(new Coord(0, 3), PlayerColor.BLACK);

    gs.startPlay();
    this.gt2 = new HexGameTree(gs);
    this.gt2 = this.gt2.getNextGameTree(new Move(new Coord(0, 5), new Coord(0, 4)));

    List<MoveValue> scores = new ArrayList<>();
    scores.add(new MoveValue(new Move(new Coord(1, 1), new Coord(1, 2)), 3));

    scores = this.fms.addFinalMove(this.gt2, scores);

    assertEquals(2, scores.size());
    assertEquals(new Move(new Coord(1, 1), new Coord(1, 2)), scores.get(0).getMove());
    assertEquals(3, scores.get(0).getValue());

    assertEquals(new Move(new Coord(0, 5), new Coord(0, 4)), scores.get(1).getMove());
    assertEquals(2, scores.get(1).getValue());
  }
}
