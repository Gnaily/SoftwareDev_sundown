package com.fish.game;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.model.state.GameState;
import com.fish.model.state.HexGameState;
import com.fish.model.state.Player;
import com.fish.model.state.PlayerColor;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class HexGameTreeTest {

  private GameTree threePlayerTree;
  private GameTree twoPlayerTree;

  @Before
  public void setUp() throws Exception {
    // 3 cols, 6 rows

    GameBoard gb1 = new HexGameBoard(8, 4, 3);
    List<Player> players1 = Arrays.asList(new Player(10, PlayerColor.BLACK), new Player(11, PlayerColor.BROWN),
            new Player(43, PlayerColor.RED));

    GameState gs1 = new HexGameState();
    gs1.initGame(gb1, players1);
    gs1.placePenguin(new Coord(0, 1), PlayerColor.BLACK);
    gs1.placePenguin(new Coord(1, 1), PlayerColor.BROWN);
    gs1.placePenguin(new Coord(2, 1), PlayerColor.RED);
    gs1.placePenguin(new Coord(0, 6), PlayerColor.BLACK);
    gs1.placePenguin(new Coord(3, 3), PlayerColor.BROWN);
    gs1.placePenguin(new Coord(2, 4), PlayerColor.RED);
    gs1.placePenguin(new Coord(1, 5), PlayerColor.BLACK);
    gs1.placePenguin(new Coord(3, 7), PlayerColor.BROWN);
    gs1.placePenguin(new Coord(3, 6), PlayerColor.RED);

    gs1.startPlay();

    this.threePlayerTree = new HexGameTree(gs1);


    gb1 = new HexGameBoard(5, 3, 1);
    players1 = Arrays.asList(new Player(11, PlayerColor.BROWN),
            new Player(43, PlayerColor.RED));

    gs1 = new HexGameState();
    gs1.initGame(gb1, players1);
    gs1.placePenguin(new Coord(0, 0), PlayerColor.BROWN);
    gs1.placePenguin(new Coord(0, 2), PlayerColor.RED);
    gs1.placePenguin(new Coord(0, 4), PlayerColor.BROWN);
    gs1.placePenguin(new Coord(1, 1), PlayerColor.RED);
    gs1.placePenguin(new Coord(1, 3), PlayerColor.BROWN);
    gs1.placePenguin(new Coord(2, 0), PlayerColor.RED);
    gs1.placePenguin(new Coord(2, 2), PlayerColor.BROWN);
    gs1.placePenguin(new Coord(2, 4), PlayerColor.RED);

    gs1.startPlay();

    this.twoPlayerTree = new HexGameTree(gs1);

  }

  @Test
  public void getPossibleGameStatesTwoPlayer() {

    Map<Move, GameState> moves = this.twoPlayerTree.getPossibleGameStates();

    assertEquals(9, moves.size());

    List<Move> expectedMoves = Arrays.asList(new Move(new Coord(0, 0), new Coord(0, 1)),
        new Move(new Coord(0, 0), new Coord(1, 2)),
        new Move(new Coord(0, 4), new Coord(0, 3)),
        new Move(new Coord(0, 4), new Coord(1, 2)),
        new Move(new Coord(1, 3), new Coord(0, 1)),
        new Move(new Coord(1, 3), new Coord(1, 2)),
        new Move(new Coord(1, 3), new Coord(1, 4)),
        new Move(new Coord(2, 2), new Coord(2, 1)),
        new Move(new Coord(2, 2), new Coord(2, 3)));

    for (Move move : expectedMoves) {
      assertNotNull(moves.get(move));
    }
  }

  @Test
  public void getPossibleGameStatesAfterMoveTwoPlayer() {
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 0), new Coord(1, 2)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 2), new Coord(1, 0)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 4), new Coord(0, 3)));

    Map<Move, GameState> moves = this.twoPlayerTree.getPossibleGameStates();

    assertEquals(3, moves.size());

    List<Move> expectedMoves = Arrays.asList(new Move(new Coord(2, 4), new Coord(2, 3)),
        new Move(new Coord(1, 0), new Coord(0, 1)),
        new Move(new Coord(2, 0), new Coord(2, 1)));

    for (Move move : expectedMoves) {
      assertNotNull(moves.get(move));
    }
  }

  @Test
  public void getNextGameTree() {
    Map<Move, GameState> oldMoves = this.threePlayerTree.getPossibleGameStates();
    this.threePlayerTree = this.threePlayerTree.getNextGameTree(new Move(new Coord(0, 1), new Coord(0, 0)));
    Map<Move, GameState> newMoves = this.threePlayerTree.getPossibleGameStates();

    assertNotEquals(oldMoves.size(), newMoves.size());

    assertEquals(PlayerColor.BROWN, this.threePlayerTree.getState().getCurrentPlayer());

    assertEquals(3, this.threePlayerTree.getState().getPlayerScore(PlayerColor.BLACK));

    assertEquals(1, this.threePlayerTree.getPreviousMoves().size());

    assertEquals(new Move(new Coord(0, 1), new Coord(0, 0)),
        this.threePlayerTree.getPreviousMoves().get(0).getMove());
  }

  @Test
  public void getNextGameTreePossibleStatesEnd() {
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 0), new Coord(1, 2)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 2), new Coord(1, 0)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 4), new Coord(0, 3)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(2, 4), new Coord(2, 3)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 3), new Coord(0, 1)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(2, 3), new Coord(2, 1)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(1, 3), new Coord(1, 4)));

    assertEquals(0, this.twoPlayerTree.getPossibleGameStates().size());
    assertEquals(7, this.twoPlayerTree.getPreviousMoves().size());
    assertTrue(this.twoPlayerTree.getState().isGameOver());


  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNextGameTreeWrongTurn() {
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 2), new Coord(1, 0)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNextGameTreeNoPenguin() {
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 0), new Coord(1, 3)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNextGameTreeInvalidMove() {
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 2), new Coord(1, 0)));
  }

  @Test
  public void testPreviousMoveThreePlayer() {
    this.threePlayerTree = this.threePlayerTree.getNextGameTree(new Move(new Coord(0, 1), new Coord(0, 0)));
    assertEquals(1, this.threePlayerTree.getPreviousMoves().size());

    assertEquals(new Move(new Coord(0, 1), new Coord(0, 0)), this.threePlayerTree.getPreviousMoves().get(0).getMove());
    assertEquals(PlayerColor.BLACK, this.threePlayerTree.getPreviousMoves().get(0).getGameState().getCurrentPlayer());

  }

  @Test
  public void undoPreviousMove() {
    Map<Move, GameState> oldMoves = this.twoPlayerTree.getPossibleGameStates();
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 0), new Coord(1, 2)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 2), new Coord(1, 0)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 4), new Coord(0, 3)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(2, 4), new Coord(2, 3)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 3), new Coord(0, 1)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(2, 3), new Coord(2, 1)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(1, 3), new Coord(1, 4)));
    assertEquals(0, this.twoPlayerTree.getPossibleGameStates().size());
    assertTrue(oldMoves.size() > this.twoPlayerTree.getPossibleGameStates().size());

    for (int ii = 0; ii < 7; ii++) {
      this.twoPlayerTree = this.twoPlayerTree.undoPreviousMove();
    }
    Map<Move, GameState> newMoves = this.twoPlayerTree.getPossibleGameStates();

    assertEquals(oldMoves.size(), newMoves.size());
    for(Move move : oldMoves.keySet()) {
      assertNotNull(newMoves.get(move));
    }

  }

  @Test
  public void testDoUndoMove() {
    Map<Move, GameState> oldMoves = this.threePlayerTree.getPossibleGameStates();
    this.threePlayerTree = this.threePlayerTree.getNextGameTree(new Move(new Coord(0, 1), new Coord(0, 0)));
    this.threePlayerTree = this.threePlayerTree.undoPreviousMove();
    Map<Move, GameState> newMoves = this.threePlayerTree.getPossibleGameStates();

    assertEquals(oldMoves.size(), newMoves.size());
    for(Move move : oldMoves.keySet()) {
      assertNotNull(newMoves.get(move));
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testUndoMoveStartingNode() {
    this.twoPlayerTree.undoPreviousMove();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUndoTooManyMoves() {
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 0), new Coord(1, 2)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 2), new Coord(1, 0)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 4), new Coord(0, 3)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(2, 4), new Coord(2, 3)));
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 3), new Coord(0, 1)));

    for (int ii = 0; ii < 6; ii++) {
      this.twoPlayerTree = this.twoPlayerTree.undoPreviousMove();
    }
  }

  @Test
  public void getPreviousMoves() {
    this.threePlayerTree = this.threePlayerTree.getNextGameTree(new Move(new Coord(0, 1), new Coord(0, 0)));
    this.threePlayerTree = this.threePlayerTree.getNextGameTree(new Move(new Coord(1, 1), new Coord(1, 3)));

    assertEquals(2, this.threePlayerTree.getPreviousMoves().size());

    assertEquals(new Move(new Coord(0, 1), new Coord(0, 0)),
        this.threePlayerTree.getPreviousMoves().get(0).getMove());
    assertEquals(PlayerColor.BROWN, this.threePlayerTree.getPreviousMoves().get(1).getGameState().getCurrentPlayer());
    assertEquals(PlayerColor.BLACK, this.threePlayerTree.getPreviousMoves().get(0).getGameState().getCurrentPlayer());

  }

  @Test
  public void getState() {
    GameState gs = this.twoPlayerTree.getState();

    assertEquals(2, gs.getPlayers().size());
    assertEquals(PlayerColor.BROWN, gs.getCurrentPlayer());
    assertEquals(8, gs.getPenguinLocations().size());

    List<Coord> coords = gs.getOnePlayersPenguins(PlayerColor.BROWN);
    assertEquals(4, coords.size());

    List<Coord> expectedCoords = Arrays.asList(new Coord(0, 0), new Coord(0, 4),
        new Coord(1, 3), new Coord(2, 2));

    for (Coord c : expectedCoords) {
      assertTrue(coords.contains(c));
    }

  }

  @Test
  public void getStateIsCopy() {
    GameState gs = this.twoPlayerTree.getState();
    assertEquals(PlayerColor.BROWN, gs.getCurrentPlayer());
    this.twoPlayerTree = this.twoPlayerTree.getNextGameTree(new Move(new Coord(0, 0), new Coord(1, 2)));

    assertNotNull(gs.getPenguinLocations().get(new Coord(0, 0)));
    assertNull(this.twoPlayerTree.getState().getPenguinLocations().get(new Coord(0, 0)));
    assertEquals(PlayerColor.BROWN, gs.getCurrentPlayer());
    assertEquals(PlayerColor.RED, this.twoPlayerTree.getState().getCurrentPlayer());


  }

  @Test
  public void getResultState() {

    GameState gs = HexGameTree.getResultState(this.twoPlayerTree, new Move(new Coord(0, 0), new Coord(1, 2)));

    assertEquals(1, gs.getPlayerScore(PlayerColor.BROWN));
    assertTrue(gs.getOnePlayersPenguins(PlayerColor.BROWN).contains(new Coord(1, 2)));
    assertFalse(gs.getOnePlayersPenguins(PlayerColor.BROWN).contains(new Coord(0, 0)));

  }

  @Test(expected = IllegalArgumentException.class)
  public void getResultStateInvalid() {
    HexGameTree.getResultState(this.twoPlayerTree, new Move(new Coord(0, 0), new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getResultStateWrongTurn() {
    HexGameTree.getResultState(this.twoPlayerTree, new Move(new Coord(0, 2), new Coord(0, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getResultStateAlreadyOccupied() {
    HexGameTree.getResultState(this.twoPlayerTree, new Move(new Coord(0, 0), new Coord(1, 3)));
  }

  // Function that counts how many states are reachable
  @Test
  public void applyToAllReachableStatesCount() {
    IFunc<Integer> counter = (GameTree gt, Integer yy) -> yy + 1;

    assertEquals(Integer.valueOf(9),
        HexGameTree.applyToAllReachableStates(this.twoPlayerTree, counter, 0));
  }

  // Function that maps each move to the number of fish on the tile moved to
  @Test
  public void applyToAllReachableStatesMoveFishList() {
    IFunc<Map<Move, Integer>> moveMap = (GameTree gt, Map<Move, Integer> map) -> {
      Move move = gt.getPreviousMoves().get(gt.getPreviousMoves().size() - 1).getMove();
      int numFish = gt.getState().getTileAt(move.getEnd()).getNumFish();
      map.put(move, numFish);
      return map;
    };

    Map<Move, Integer> moveIntegerMap = HexGameTree.applyToAllReachableStates(
        this.twoPlayerTree, moveMap, new HashMap<>()
    );
    assertEquals(9, moveIntegerMap.size());

    for (Move m : moveIntegerMap.keySet()) {
      assertEquals(Integer.valueOf(1), moveIntegerMap.get(m));
    }
  }

  // test a function that counts all of the nodes in the tree
  @Test
  public void testTotalNodes() {
    IFunc<Integer> func = new NodeCounter();

    assertEquals(Integer.valueOf(59844), HexGameTree.applyToAllReachableStates(this.twoPlayerTree, func, 1));
  }

  // tests a function that maps each move to the number of moves available to
  //  the next opponent
  @Test
  public void applyToAllReachableStatesMoveNumberList() {
    IFunc<Map<Move, Integer>> moveMap = (GameTree gt, Map<Move, Integer> map) -> {
      Move move = gt.getPreviousMoves().get(gt.getPreviousMoves().size() - 1).getMove();
      int numMoves = gt.getPossibleGameStates().size();
      map.put(move, numMoves);
      return map;
    };

    Map<Move, Integer> moveIntegerMap = HexGameTree.applyToAllReachableStates(
        this.twoPlayerTree, moveMap, new HashMap<>());
    assertEquals(9, moveIntegerMap.size());
    assertEquals(Integer.valueOf(7), moveIntegerMap.get(new Move(new Coord(0, 0),
        new Coord(0, 1))));
    assertEquals(Integer.valueOf(8), moveIntegerMap.get(new Move(new Coord(2, 2),
        new Coord(2, 1))));
    assertEquals(Integer.valueOf(6), moveIntegerMap.get(new Move(new Coord(0, 4),
        new Coord(0, 3))));

  }

  // test for getting all resultant trees from one round of moves
  @Test
  public void applyToAllMoveTreeMap() {
    IFunc<Map<Move, GameTree>> moveTreeMap = (GameTree gt, Map<Move, GameTree> map) -> {
      Move move = gt.getPreviousMoves().get(gt.getPreviousMoves().size() - 1).getMove();
      map.put(move, gt);
      return map;
    };

    Map<Move, GameTree> moves = HexGameTree.applyToAllReachableStates(this.twoPlayerTree, moveTreeMap, new HashMap<>());
    assertEquals(9, moves.size());
    for (Move move : moves.keySet()) {
      assertEquals(1, moves.get(move).getPreviousMoves().size());
    }

    assertEquals(7, moves.get(new Move(new Coord(0, 0), new Coord(0, 1))).getPossibleGameStates().size());
  }
}
