package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.PlayerColor;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
  public void testSameNumberConstructorNegativeArgument() {
    GameBoard boardr = new HexGameBoard(4, -4, 2);
    GameBoard boardc = new HexGameBoard(-4, 4, 2);
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
        if (this.noHolesBoard.getTileAt(ii, jj) != null &&
            this.noHolesBoard.getTileAt(ii, jj).getNumFish() == 1) {
          minOneFish++;
        }
      }
    }
    assertTrue(minOneFish >= 5);

    minOneFish = 0;
    for (int ii = 0; ii < this.holesBoard.getWidth(); ii++) {
      for (int jj = 0; jj < this.holesBoard.getHeight(); jj++) {
        if (this.holesBoard.getTileAt(ii, jj) != null &&
            this.holesBoard.getTileAt(ii, jj).getNumFish() == 1) {
          minOneFish++;
        }
      }
    }
    assertTrue(minOneFish >= 8);
  }


  /////Tests for Moves
  @Test
  public void getTilesReachableFrom() {
    GameBoard noHolesExpanded = new HexGameBoard(8, 3, 1);

    List<Coord> expectedMoves = new ArrayList<>(Arrays
        .asList(
            new Coord(0, 0), new Coord(0, 1), new Coord(1, 2),
            new Coord(2, 4), new Coord(2, 5),
            new Coord(1, 1), new Coord(1, 5), new Coord(1, 7),
            new Coord(2, 2), new Coord(2, 1),
            new Coord(1, 4), new Coord(0, 5), new Coord(0, 6)));

    List<Coord> actualMoves = noHolesExpanded.getTilesReachableFrom(new Coord(1, 3));

    for (Coord c : expectedMoves) {
      assertTrue(actualMoves.contains(c));
    }
    assertEquals(13, actualMoves.size());


    // remove a tile and test it still works
    noHolesExpanded.removeTileAt(1,2);
    actualMoves = noHolesExpanded.getTilesReachableFrom(new Coord(1, 3));
    expectedMoves.remove(new Coord(1, 2));
    expectedMoves.remove(new Coord(0, 1));
    expectedMoves.remove(new Coord(0, 0));
    for (Coord c : expectedMoves) {
      assertTrue(actualMoves.contains(c));
    }
    assertEquals(10, actualMoves.size());
  }

  @Test
  public void testGetTilesReachableMoreRemoved() {
    List<Coord> moves = this.holesBoard.getTilesReachableFrom(new Coord(1, 2));
    assertEquals(7, moves.size());
  }


  /////Tests for Tile Handling
  @Test
  public void getTileAtValid() {
    assertEquals(4, this.holesBoard.getTileAt(0, 1).getNumFish());
    assertEquals(1, this.holesBoard.getTileAt(1, 0).getNumFish());
    assertEquals(4, this.noHolesBoard.getTileAt(1, 2).getNumFish());
    assertEquals(3, this.noHolesBoard.getTileAt(0, 5).getNumFish());
  }

  @Test
  public void testAllTilesSameValue() {
    for (int ii = 0; ii < this.constantFishNumBoard.getWidth(); ii++) {
      for (int jj = 0; jj < this.constantFishNumBoard.getHeight(); jj++) {
        assertEquals(2, this.constantFishNumBoard.getTileAt(ii, jj).getNumFish());
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTileOutOfRange() {
    this.noHolesBoard.getTileAt(10, 10);
  }

  @Test
  public void testGetTileRemoved() {
    assertEquals(null, this.holesBoard.getTileAt(1, 4));
  }

  @Test
  public void removeTileAt() {
    assertEquals(4, this.holesBoard.removeTileAt(0, 1).getNumFish());
    assertEquals(2, this.holesBoard.removeTileAt(0, 6).getNumFish());
    assertEquals(1, this.holesBoard.removeTileAt(2, 7).getNumFish());
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileNegX() {
    this.holesBoard.removeTileAt(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileNegY() {
    this.holesBoard.removeTileAt(0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileTooBigY() {
    this.holesBoard.removeTileAt(9, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileTooBigX() {
    this.holesBoard.removeTileAt(1, 9);
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileAtAlreadyGone() {
    this.holesBoard.removeTileAt(0, 0);
  }


  /////Tests for Penguin Handling
  @Test
  public void testPutGetPenguin() {
    assertEquals(0, this.holesBoard.getPenguinLocations().size());

    this.holesBoard.placePenguin(new Coord(0, 2), PlayerColor.BROWN);
    this.holesBoard.placePenguin(new Coord(1, 3), PlayerColor.BLACK);

    HashMap<Coord, PlayerColor> pengs = this.holesBoard.getPenguinLocations();
    assertEquals(2, pengs.size());
    assertEquals(PlayerColor.BROWN, pengs.get(new Coord(0, 2)));
    assertEquals(PlayerColor.BLACK, pengs.get(new Coord(1, 3)));
    //A tile that doesnt exist on the board:
    assertNull(pengs.get(new Coord(0, 0)));
    //A tile that does:
    assertNull(pengs.get(new Coord(0, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPutTwoPenguinsSameLocation() {
    this.holesBoard.placePenguin(new Coord(1, 0), PlayerColor.BROWN);
    this.holesBoard.placePenguin(new Coord(1, 0), PlayerColor.BROWN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPutPenguinNoTile() {
    this.holesBoard.placePenguin(new Coord(1, 1), PlayerColor.BROWN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemovePenguinNoPenguin() {
    this.noHolesBoard.removePenguin(new Coord(1, 1));
  }

  @Test
  public void testPutRemovePenguin() {
    assertEquals(0, this.noHolesBoard.getPenguinLocations().size());
    this.noHolesBoard.placePenguin(new Coord(1, 1), PlayerColor.BROWN);
    assertEquals(1, this.noHolesBoard.getPenguinLocations().size());
    assertEquals(PlayerColor.BROWN, this.noHolesBoard.removePenguin(new Coord(1, 1)));
    assertEquals(0, this.noHolesBoard.getPenguinLocations().size());
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

}
