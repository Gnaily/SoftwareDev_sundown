package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.tile.Tile;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HexGameBoardTest {

  private GameBoard noHolesBoard;
  private GameBoard holesBoard;
  private GameBoard constantFishNumBoard;

  @Before
  public void setUp() throws Exception {
    this.noHolesBoard = new HexGameBoard(6, 2, new ArrayList<>(),
        5, 1);

    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
        new Coord(2, 2), new Coord(1, 4));
    this.holesBoard = new HexGameBoard(8, 3, holes,
        8, 1);

    this.constantFishNumBoard = new HexGameBoard(4, 4, 2);
  }

  /////Tests for Constructors
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeArgument() {
    GameBoard boardr = new HexGameBoard(-3, 2, new ArrayList<>(),
        5, 1);
    GameBoard boardc = new HexGameBoard(3, -2, new ArrayList<>(),
        5, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstantConstructorNegativeArgument() {
    GameBoard boardr = new HexGameBoard(4, -4, 2);
    GameBoard boardc = new HexGameBoard(-4, 4, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstantConstructorTooManyFish() {
    GameBoard tooManyFish = new HexGameBoard(4, 4, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNotEnoughSpacesForOneFishTiles() {
    GameBoard board = new HexGameBoard(3, 2, new ArrayList<>(),
        20, 1);
  }

  @Test
  public void testMinimumNumberOneFish() {
    int minOneFish = 0;
    for (int ii = 0; ii < this.noHolesBoard.getWidth(); ii++) {
      for (int jj = 0; jj < this.noHolesBoard.getHeight(); jj++) {
        if (this.noHolesBoard.getTileAt(new Coord(ii, jj)).isPresent() &&
            this.noHolesBoard.getTileAt(new Coord(ii, jj)).getNumFish() == 1) {
          minOneFish++;
        }
      }
    }
    assertTrue(minOneFish >= 5);

    minOneFish = 0;
    for (int ii = 0; ii < this.holesBoard.getWidth(); ii++) {
      for (int jj = 0; jj < this.holesBoard.getHeight(); jj++) {
        if (this.holesBoard.getTileAt(new Coord(ii, jj)) != null &&
            this.holesBoard.getTileAt(new Coord(ii, jj)).getNumFish() == 1) {
          minOneFish++;
        }
      }
    }
    assertTrue(minOneFish >= 8);
  }

  // xBoard constructor tests
  @Test(expected = IllegalArgumentException.class)
  public void testXBoardBadInput() {
    GameBoard board = new HexGameBoard(new int[0][1]);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testXBoardBadInputRow() {
    GameBoard board = new HexGameBoard(new int[2][0]);

  }

  @Test
  public void testXBoardConstructor() {
    int[][] nums = {{1, 1},{0, 2}};
    GameBoard board = new HexGameBoard(nums);

    assertEquals(2, board.getHeight());
    assertFalse(board.getTileAt(new Coord(1, 0)).isPresent());
    assertEquals(1, board.getTileAt(new Coord(0, 0)).getNumFish());
  }


  /////Tests for Moves
  @Test
  public void getTilesReachableFrom() {
    GameBoard noHolesExpanded = new HexGameBoard(8, 3, 1);

    List<Coord> expectedMoves = new ArrayList<>(Arrays.asList(
            new Coord(0, 0), new Coord(0, 1), new Coord(1, 2),
            new Coord(2, 4), new Coord(2, 5),
            new Coord(1, 1), new Coord(1, 5), new Coord(1, 7),
            new Coord(2, 2), new Coord(2, 1),
            new Coord(1, 4), new Coord(0, 5), new Coord(0, 6)));

    List<Coord> actualMoves = noHolesExpanded.getTilesReachableFrom(new Coord(1, 3), new ArrayList<>());


    assertEquals(13, actualMoves.size());
    for (Coord c : expectedMoves) {
      assertTrue(actualMoves.contains(c));
    }


    // remove a tile and test it still works
    noHolesExpanded.removeTileAt(new Coord(1,2));
    actualMoves = noHolesExpanded.getTilesReachableFrom(new Coord(1, 3), new ArrayList<>());
    expectedMoves.remove(new Coord(1, 2));
    expectedMoves.remove(new Coord(0, 1));
    expectedMoves.remove(new Coord(0, 0));
    for (Coord c : expectedMoves) {
      assertTrue(actualMoves.contains(c));
    }
    assertEquals(10, actualMoves.size());
  }

  @Test
  public void getTilesReachableFromWithPenguin() {
    GameBoard noHolesExpanded = new HexGameBoard(8, 3, 1);

    List<Coord> expectedMoves = new ArrayList<>(Arrays.asList(
            new Coord(2, 4), new Coord(2, 5),
            new Coord(1, 1), new Coord(1, 5), new Coord(1, 7),
            new Coord(2, 2), new Coord(2, 1),
            new Coord(1, 4), new Coord(0, 5), new Coord(0, 6)));

    List<Coord> actualMoves = noHolesExpanded.getTilesReachableFrom(new Coord(1, 3),
        Collections.singletonList(new Coord(1, 2)));

    for (Coord c : expectedMoves) {
      assertTrue(actualMoves.contains(c));
    }
    assertEquals(10, actualMoves.size());
  }

  @Test
  public void testGetTilesReachableMoreRemoved() {
    List<Coord> moves = this.holesBoard.getTilesReachableFrom(new Coord(1, 2), new ArrayList<>());
    assertEquals(7, moves.size());
  }


  /////Tests for Tile Handling
  @Test
  public void getTileAtValid() {
    assertEquals(3, this.holesBoard.getTileAt(new Coord(0, 1)).getNumFish());
    assertTrue(this.holesBoard.getTileAt(new Coord(0, 1)).isPresent());
    assertEquals(1, this.holesBoard.getTileAt(new Coord(1, 0)).getNumFish());
    assertTrue(this.holesBoard.getTileAt(new Coord(1, 0)).isPresent());

    assertEquals(1, this.noHolesBoard.getTileAt(new Coord(0, 5)).getNumFish());
    noHolesBoard.removeTileAt(new Coord(0,5));
    assertEquals(1, this.noHolesBoard.getTileAt(new Coord(0, 5)).getNumFish());
    assertFalse(this.noHolesBoard.getTileAt(new Coord(0, 5)).isPresent());
  }

  @Test
  public void testAllTilesSameValue() {
    for (int ii = 0; ii < this.constantFishNumBoard.getWidth(); ii++) {
      for (int jj = 0; jj < this.constantFishNumBoard.getHeight(); jj++) {
        assertEquals(2, this.constantFishNumBoard.getTileAt(new Coord(ii, jj)).getNumFish());
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTileOutOfRange() {
    this.noHolesBoard.getTileAt(new Coord(10, 10));
  }

  @Test
  public void testGetTileRemoved() {
    assertFalse(this.holesBoard.getTileAt(new Coord(1, 4)).isPresent());
  }

  @Test
  public void removeTileAt() {
    assertEquals(3, this.holesBoard.removeTileAt(new Coord(0, 1)).getNumFish());
    assertEquals(5, this.holesBoard.removeTileAt(new Coord(0, 6)).getNumFish());
    assertEquals(3, this.holesBoard.removeTileAt(new Coord(2, 7)).getNumFish());
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileNegX() {
    this.holesBoard.removeTileAt(new Coord(-1, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileNegY() {
    this.holesBoard.removeTileAt(new Coord(0, -1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileTooBigY() {
    this.holesBoard.removeTileAt(new Coord(9, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileTooBigX() {
    this.holesBoard.removeTileAt(new Coord(1, 9));
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileAtAlreadyGone() {
    this.holesBoard.removeTileAt(new Coord(0, 0));
  }



  /////Tests for basic getters and helper methods

  @Test
  public void testGetWidth() {
    assertEquals(2, this.noHolesBoard.getWidth());
    assertEquals(4, this.constantFishNumBoard.getWidth());
  }

  @Test
  public void testGetHeight() {
    assertEquals(6, this.noHolesBoard.getHeight());
    assertEquals(4, this.constantFishNumBoard.getHeight());
  }


  //// Tests for copying a board
  @Test
  public void testGetCopyBoardTilesConsistent() {
    GameBoard copy = this.noHolesBoard.getCopyGameBoard();

    for (int ii = 0; ii < copy.getWidth(); ii++) {
      for (int jj = 0; jj < copy.getHeight(); jj++) {
        Tile tile = copy.getTileAt(new Coord(ii, jj));
        if (tile.isPresent()) {
          assertEquals(tile.getNumFish(), this.noHolesBoard.getTileAt(new Coord(ii, jj)).getNumFish());
        }
        else {
          assertFalse(this.noHolesBoard.getTileAt(new Coord(ii, jj)).isPresent());
        }
      }

    }

  }

  @Test
  public void testGetCopyAlterBoard() {
    GameBoard copy = this.holesBoard.getCopyGameBoard();
    Coord cc = new Coord(0, 4);
    assertTrue(this.holesBoard.getTileAt(cc).isPresent());
    assertTrue(copy.getTileAt(cc).isPresent());

    this.holesBoard.removeTileAt(cc);
    assertTrue(copy.getTileAt(cc).isPresent());
    assertFalse(this.holesBoard.getTileAt(cc).isPresent());

    cc = new Coord(0, 2);
    copy.removeTileAt(cc);
    assertFalse(copy.getTileAt(cc).isPresent());
    assertTrue(this.holesBoard.getTileAt(cc).isPresent());
  }

  @Test
  public void testEqualsCopy() {
    GameBoard copy = this.holesBoard.getCopyGameBoard();
    assertEquals(this.holesBoard, copy);
  }

  @Test
  public void testEqualsNotCopy() {
    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
        new Coord(2, 2), new Coord(1, 4));
    GameBoard gb = new HexGameBoard(8, 3, holes,
        8, 1);
    assertEquals(gb, this.holesBoard);

    gb.removeTileAt(new Coord(0, 1));
    assertNotEquals(gb, this.holesBoard);
    this.holesBoard.removeTileAt(new Coord(0, 1));
    assertEquals(gb, this.holesBoard);
  }


}
