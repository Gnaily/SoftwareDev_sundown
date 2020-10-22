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
    gs1.placePenguin(new Coord(2, 1), PlayerColor.RED);
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
    gs1.placePenguin(new Coord(1, 3), PlayerColor.BLACK);
    gs1.placePenguin(new Coord(2, 0), PlayerColor.RED);
    gs1.placePenguin(new Coord(2, 2), PlayerColor.BLACK);
    gs1.placePenguin(new Coord(2, 4), PlayerColor.RED);

    gs1.startPlay();

    this.twoPlayerTree = new HexGameTree(gs1);



  }

  @Test
  public void getPossibleGameStates() {
  }

  @Test
  public void getNextGameTree() {
  }

  @Test
  public void undoPreviousMove() {
  }

  @Test
  public void getPreviousMoves() {
  }

  @Test
  public void getState() {
  }

  @Test
  public void getResultState() {
  }

  @Test
  public void applyToAllReachableStates() {
  }
}