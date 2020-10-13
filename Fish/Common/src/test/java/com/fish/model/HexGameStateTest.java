package com.fish.model;

import com.fish.model.board.HexGameBoard;
import com.fish.model.state.GameStage;
import com.fish.model.state.GameState;
import com.fish.model.state.HexGameState;
import com.fish.model.state.Player;
import com.fish.model.state.PlayerColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class HexGameStateTest {

  private GameState noHolesState;
  private GameState holesState;
  private GameState constantFishNumState;

  @Before
  public void setUp() throws Exception {

    List<Player> players = new ArrayList<>(Arrays.asList(new Player(10), new Player(12),
        new Player(44), new Player(55)));
    players.get(0).setColorColor(PlayerColor.BROWN);
    players.get(1).setColorColor(PlayerColor.BLACK);
    players.get(2).setColorColor(PlayerColor.WHITE);
    players.get(3).setColorColor(PlayerColor.RED);

    this.noHolesState = new HexGameState();
    this.noHolesState.startGame(new HexGameBoard(6, 2, new ArrayList<>(),
        5, 1), players);


    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
        new Coord(2, 2), new Coord(1, 4));
    this.holesState = new HexGameState();
    this.holesState.startGame(new HexGameBoard(8, 3, holes, 8, 1),
        players);

    this.constantFishNumState = new HexGameState();
    this.constantFishNumState.startGame(new HexGameBoard(4, 4, 2), players);
  }


  /////Tests for Tile Handling
  @Test
  public void testGetTileAtValid() {
    assertEquals(3, this.holesState.getTileAt(new Coord(0, 1)).getNumFish());
    assertEquals(1, this.holesState.getTileAt(new Coord(1, 0)).getNumFish());
    assertEquals(5, this.noHolesState.getTileAt(new Coord(1, 2)).getNumFish());
    assertEquals(1, this.noHolesState.getTileAt(new Coord(0, 5)).getNumFish());
  }

  @Test
  public void testAllTilesSameValue() {
    for (int ii = 0; ii < this.constantFishNumState.getWidth(); ii++) {
      for (int jj = 0; jj < this.constantFishNumState.getHeight(); jj++) {
        assertEquals(2, this.constantFishNumState.getTileAt(new Coord(ii, jj)).getNumFish());
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTileOutOfRange() {
    this.noHolesState.getTileAt(new Coord(10, 10));
  }

  @Test
  public void testGetTileRemoved() {
    assertEquals(null, this.holesState.getTileAt(new Coord(1, 4)));
  }

  /////Penguin Handling
  @Test
  public void testPlaceThenGetPenguin() {
    assertEquals(0, this.holesState.getPenguinLocations().size());

    this.holesState.placePenguin(new Coord(0, 2), PlayerColor.BROWN);
    this.holesState.placePenguin(new Coord(1, 3), PlayerColor.BLACK);

    Map<Coord, PlayerColor> pengs = this.holesState.getPenguinLocations();
    assertEquals(2, pengs.size());
    assertEquals(PlayerColor.BROWN, pengs.get(new Coord(0, 2)));
    assertEquals(PlayerColor.BLACK, pengs.get(new Coord(1, 3)));
  }

  @Test
  public void testPlaceThenGetNullPenguin() {
    assertEquals(0, this.holesState.getPenguinLocations().size());

    this.holesState.placePenguin(new Coord(0, 2), PlayerColor.BROWN);
    this.holesState.placePenguin(new Coord(1, 3), PlayerColor.BLACK);

    Map<Coord, PlayerColor> pengs = this.holesState.getPenguinLocations();
    //A tile that doesnt exist on the board:
    assertNull(pengs.get(new Coord(0, 0)));
    //A tile that does:
    assertNull(pengs.get(new Coord(0, 1)));
  }

  @Test
  public void testPutMovePenguin() {
    assertEquals(0, this.noHolesState.getPenguinLocations().size());
    this.noHolesState.placePenguin(new Coord(1, 1), PlayerColor.BROWN);
    assertEquals(1, this.noHolesState.getPenguinLocations().size());
    this.noHolesState.movePenguin(new Coord(1, 1), new Coord(0, 0));
    assertEquals(1, this.noHolesState.getPenguinLocations().size());
    assertNull(this.noHolesState.getPenguinLocations().get(new Coord(1, 1)));
    assertEquals(PlayerColor.BROWN, this.noHolesState.getPenguinLocations().get(new Coord(0, 0)));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testPutTwoPenguinsSameLocation() {
    this.holesState.placePenguin(new Coord(1, 0), PlayerColor.BROWN);
    this.holesState.placePenguin(new Coord(1, 0), PlayerColor.BROWN);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testPutPenguinsOverOtherPenguin() {
    this.holesState.placePenguin(new Coord(1, 0), PlayerColor.BROWN);
    this.holesState.placePenguin(new Coord(1, 0), PlayerColor.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPutPenguinNoTile() {
    this.holesState.placePenguin(new Coord(1, 1), PlayerColor.BROWN);
  }

  //////Player Handling
  @Test
  public void testGetCurrentPlayerAndAdvance() {
    assertEquals(PlayerColor.BROWN, this.holesState.getCurrentPlayer());
    this.holesState.advanceToNextPlayer();
    assertEquals(PlayerColor.BLACK, this.holesState.getCurrentPlayer());
    this.holesState.advanceToNextPlayer();
    assertEquals(PlayerColor.WHITE, this.holesState.getCurrentPlayer());
    this.holesState.advanceToNextPlayer();
    assertEquals(PlayerColor.RED, this.holesState.getCurrentPlayer());
    this.holesState.advanceToNextPlayer();
    //Test that it resets to the first player again
    assertEquals(PlayerColor.BROWN, this.holesState.getCurrentPlayer());
    this.holesState.advanceToNextPlayer();
  }


  @Test
  public void testGetPlayerScore() {
    //First move one tile over
    this.constantFishNumState.placePenguin(new Coord(0,0), PlayerColor.BLACK);
    this.constantFishNumState.movePenguin(new Coord(0,0), new Coord(0,1));
    assertEquals(2, this.constantFishNumState.getPlayerScore(PlayerColor.BLACK));
    //Now move across multiple tiles but make sure only the tile of origin is counted in score
    this.constantFishNumState.movePenguin(new Coord(0,1), new Coord(1,3));
    assertEquals(4, this.constantFishNumState.getPlayerScore(PlayerColor.BLACK));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemovePlayer() {
    this.holesState.removePlayer(PlayerColor.WHITE);
    this.holesState.getPlayerScore(PlayerColor.WHITE);
  }

  @Test
  public void testIsGameOver() {
    List<Player> players = new ArrayList<>(Arrays.asList(new Player(10), new Player(12),
        new Player(44), new Player(55)));
    players.get(0).setColorColor(PlayerColor.BROWN);
    players.get(1).setColorColor(PlayerColor.BLACK);
    players.get(2).setColorColor(PlayerColor.WHITE);
    players.get(3).setColorColor(PlayerColor.RED);

    //Create a situation wherein the game would be over
    List<Coord> holes = Arrays.asList(new Coord(0, 2), new Coord(0, 1));
    GameState gO =  new HexGameState();
    gO.startGame(new HexGameBoard(3, 2, holes, 4, 1),
        players);
    gO.placePenguin(new Coord(0,0), PlayerColor.WHITE);
    //Then call gameover
    assertEquals(true, gO.isGameOver());
    assertEquals(GameStage.GAMEOVER, gO.getGameStage());
  }

  @Test
  public void testGetWidth() {
    assertEquals(2, this.noHolesState.getWidth());
    assertEquals(4, this.constantFishNumState.getWidth());
  }

  @Test
  public void testGetHeight() {
    assertEquals(6, this.noHolesState.getHeight());
    assertEquals(4, this.constantFishNumState.getHeight());
  }
}
