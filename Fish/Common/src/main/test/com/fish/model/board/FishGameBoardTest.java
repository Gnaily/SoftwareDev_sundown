package com.fish.model.board;

import com.fish.model.Coord;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FishGameBoardTest {

  private FishBoard board1;
  private FishBoard board2;
  private FishBoard board3;
  private List<Coord> expectedMovesB1;

  @Before
  public void setUp() throws Exception {
    this.board1 = new FishGameBoard(8, 3, new ArrayList<Coord>(), 3, false);
    List<Coord> holes = Arrays.asList(new Coord(1, 1), new Coord(0, 0));
    this.board2 = new FishGameBoard(6, 6, holes, 8, false);
    this.expectedMovesB1 = new ArrayList<>(Arrays
        .asList(new Coord(0, 0), new Coord(0, 1), new Coord(1, 2),
            new Coord(2, 4), new Coord(2, 5),
            new Coord(1, 1), new Coord(1, 5), new Coord(1, 7),
            new Coord(2, 2), new Coord(2, 1),
            new Coord(1, 4), new Coord(0, 5), new Coord(0, 6)));

    this.board3 = new FishGameBoard(4, 4, 2);
  }

  @Test
  public void getWidth() {
    assertEquals(3, this.board1.getWidth());
    assertEquals(6, this.board2.getWidth());
  }

  @Test
  public void getHeight() {
    assertEquals(8, this.board1.getHeight());
    assertEquals(6, this.board2.getHeight());
  }

  @Test
  public void getTileAt() {
    assertEquals(1, this.board1.getTileAt(0, 0).getFishOnTile());
    assertEquals(1, this.board1.getTileAt(0, 1).getFishOnTile());
    assertEquals(1, this.board1.getTileAt(0, 2).getFishOnTile());
    assertEquals(3, this.board1.getTileAt(2, 6).getFishOnTile());
  }

  @Test
  public void testAllTilesSameValue() {
    for (int ii = 0; ii < this.board3.getWidth(); ii++) {
      for (int jj = 0; jj < this.board3.getHeight(); jj++) {
        assertEquals(2, this.board3.getTileAt(ii, jj).getFishOnTile());
      }
    }
  }

  @Test
  public void removeTileAt() {
    assertEquals(1, this.board1.removeTileAt(0, 0).getFishOnTile());
    assertEquals(1, this.board1.removeTileAt(0, 2).getFishOnTile());
    assertEquals(4, this.board1.removeTileAt(2, 7).getFishOnTile());
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileAtOOB() {
    this.board1.removeTileAt(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileAtAlreadyGone() {
    this.board2.removeTileAt(1, 1);
  }

  @Test
  public void getTilesReachableFrom() {
    List<Coord> moves = this.board1.getTilesReachableFrom(new Coord(1, 3));
    for (Coord c : expectedMovesB1) {
      assertTrue(moves.contains(c));
    }
    assertEquals(13, moves.size());

    // remove a tile and test it still works
    this.board1.removeTileAt(1, 2);
    moves = this.board1.getTilesReachableFrom(new Coord(1, 3));
    expectedMovesB1.remove(new Coord(1, 2));
    expectedMovesB1.remove(new Coord(0, 1));
    expectedMovesB1.remove(new Coord(0, 0));
    for (Coord c : expectedMovesB1) {
      assertTrue(moves.contains(c));
    }
    assertEquals(10, moves.size());
  }

  @Test
  public void testGetTilesReachableMoreRemoved() {
    this.board1.removeTileAt(0, 1);
    this.board1.removeTileAt(1, 5);
    this.board1.removeTileAt(2, 2);
    this.board1.removeTileAt(0, 5);

    List<Coord> moves = this.board1.getTilesReachableFrom(new Coord(1, 3));

    assertEquals(5, moves.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTilesOutOfRange() {
    this.board1.getTilesReachableFrom(new Coord(0, 8));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTilesRemoved() {
    this.board1.removeTileAt(1, 3);
    this.board1.getTilesReachableFrom(new Coord(1, 3));
  }

  // --- tests I think we still need (minimum) --- //
  // - getTile + exceptions
  // - constructor exceptions (both)
  // - constructor properly does tile values / holes (both)
  // - probably more reachable from tests

  // --- stuff we still need to add --- //
  // - Penguins and how they fit into this
  //   - could just have a hashmap of (coord -> playerColor) to denote locations
  //   - would then need a method for getting/placing penguins
  //     - would not need to be hooked up to a game system for now, but could be for testing

}
