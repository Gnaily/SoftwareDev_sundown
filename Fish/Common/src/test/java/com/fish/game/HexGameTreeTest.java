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
  public void getNextGameTreeEnd() {
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
  public void undoPreviousMoveThreePlayer() {
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
  }

  @Test
  public void getState() {
    GameState gs = this.twoPlayerTree.getState();


  }

  @Test
  public void getResultState() {
  }

  @Test
  public void applyToAllReachableStates() {
  }
}
