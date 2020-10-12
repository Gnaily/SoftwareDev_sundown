package com.fish.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class HexGameStateTest {

  private GameState noHolesState;
  private GameState holesState;
  private GameState constantFishNumBoard;

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void getTileAt() {
  }

  @Test
  public void getCurrentPlayer() {
  }

  @Test
  public void getPenguinLocations() {
  }

  @Test
  public void placePenguin() {
  }

  @Test
  public void movePenguin() {
  }

  /////Tests for Penguin Handling
  @Test
  public void testPutGetPenguin() {
    assertEquals(0, this.holesState.getPenguinLocations().size());

    this.holesState.placePenguin(new Coord(0, 2), PlayerColor.BROWN);
    this.holesState.placePenguin(new Coord(1, 3), PlayerColor.BLACK);

    Map<Coord, PlayerColor> pengs = this.holesState.getPenguinLocations();
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
    this.holesState.placePenguin(new Coord(1, 0), PlayerColor.BROWN);
    this.holesState.placePenguin(new Coord(1, 0), PlayerColor.BROWN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPutPenguinNoTile() {
    this.holesState.placePenguin(new Coord(1, 1), PlayerColor.BROWN);
  }


  @Test
  public void testPutMovePenguin() {
    assertEquals(0, this.noHolesState.getPenguinLocations().size());
    this.noHolesState.placePenguin(new Coord(1, 1), PlayerColor.BROWN);
    assertEquals(1, this.noHolesState.getPenguinLocations().size());
    this.noHolesState.movePenguin(new Coord(1, 1), new Coord(0, 0));
    assertEquals(1, this.noHolesState.getPenguinLocations().size());
    assertNull(this.noHolesState.getPenguinLocations().get(new Coord(1, 1)));
    assertEquals(PlayerColor.BROWN, this.holesState.getPenguinLocations().get(new Coord(0, 0)));
  }

  @Test
  public void getPlayerScore() {
  }

  @Test
  public void isGameOver() {
  }

  @Test
  public void getWidth() {
  }

  @Test
  public void getHeight() {
  }
}
