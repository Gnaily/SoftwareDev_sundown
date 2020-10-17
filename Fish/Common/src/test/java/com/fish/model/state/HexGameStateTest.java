package com.fish.model.state;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
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
    players.get(0).setPlayerColor(PlayerColor.BROWN);
    players.get(1).setPlayerColor(PlayerColor.BLACK);
    players.get(2).setPlayerColor(PlayerColor.WHITE);
    players.get(3).setPlayerColor(PlayerColor.RED);

    this.noHolesState = new HexGameState();
    this.noHolesState.initGame(new HexGameBoard(6, 2, new ArrayList<>(),
        5, 1), players);


    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
        new Coord(2, 2), new Coord(1, 4));
    this.holesState = new HexGameState();
    this.holesState.initGame(new HexGameBoard(8, 3, holes, 8, 1),
        players);

    List<Player> lessPlayers = new ArrayList<>(Arrays.asList(new Player(10), new Player(12)));
    lessPlayers.get(0).setPlayerColor(PlayerColor.BROWN);
    lessPlayers.get(1).setPlayerColor(PlayerColor.BLACK);
    this.constantFishNumState = new HexGameState();
    this.constantFishNumState.initGame(new HexGameBoard(4, 4, 2), lessPlayers);
  }


  @Test
  public void testInitGame() {
    GameBoard gb = new HexGameBoard(5, 5, 3);
    GameState gs = new HexGameState();
    gs.initGame(gb, new ArrayList<>(Arrays.asList(new Player(10), new Player(12))));
    assertEquals(GameStage.PLACING_PENGUINS, gs.getGameStage());
    assertEquals(3, gs.getTileAt(new Coord(0,0)).getNumFish());
  }

  /////Penguin Handling
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceInvalidNotTurn() {
    this.holesState.placePenguin(new Coord(0, 2), PlayerColor.BROWN);//advances to the next player automatically
    holesState.advanceToNextPlayer();//advance again to be out of place and trigger exception
    this.holesState.placePenguin(new Coord(1, 3), PlayerColor.BLACK);
  }


  @Test
  public void testPlacePenguin() {
    assertEquals(0, this.holesState.getPenguinLocations().size());
    this.holesState.placePenguin(new Coord(0, 2), PlayerColor.BROWN);
    this.holesState.placePenguin(new Coord(1, 3), PlayerColor.BLACK);
    Map<Coord, PlayerColor> pengs = this.holesState.getPenguinLocations();
    assertEquals(2, pengs.size());
    assertEquals(PlayerColor.BROWN, pengs.get(new Coord(0, 2)));
    assertEquals(PlayerColor.BLACK, pengs.get(new Coord(1, 3)));
  }

  @Test
  public void testGetNullPenguin() {
    assertEquals(0, this.holesState.getPenguinLocations().size());
    this.holesState.placePenguin(new Coord(0, 2), PlayerColor.BROWN);
    this.holesState.placePenguin(new Coord(1, 3), PlayerColor.BLACK);
    Map<Coord, PlayerColor> pengs = this.holesState.getPenguinLocations();
    //A tile that is a hole:
    assertNull(pengs.get(new Coord(0, 0)));
    //A tile on the board but where there is not a penguin:
    assertNull(pengs.get(new Coord(0, 1)));
  }

  @Test
  public void testPutThenMovePenguin() {
    GameBoard gb = new HexGameBoard(3, 3, 2);
    GameState gs = new HexGameState();

    List<Player> players = new ArrayList<>(Arrays.asList(new Player(10), new Player(12)));
    players.get(0).setPlayerColor(PlayerColor.WHITE);
    players.get(1).setPlayerColor(PlayerColor.RED);

    gs.initGame(gb, players);

    gs.placePenguin(new Coord(1,2), PlayerColor.WHITE);
    gs.placePenguin(new Coord(1,1), PlayerColor.RED);

    gs.startPlay();

    gs.movePenguin(new Coord(1,2), new Coord(0,0));
    gs.movePenguin(new Coord(1,1), new Coord(1,0));
    gs.movePenguin(new Coord(0,0), new Coord(0,2));
    gs.movePenguin(new Coord(1,0), new Coord(0,1));

    assertEquals(2, gs.getPenguinLocations().size());
    assertNull(gs.getPenguinLocations().get(new Coord(1, 1)));
    assertEquals(PlayerColor.WHITE, gs.getPenguinLocations().get(new Coord(0, 2)));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testPutTwoPenguinsSameLocation() {
    this.holesState.placePenguin(new Coord(1, 0), PlayerColor.BROWN);
    this.holesState.placePenguin(new Coord(1, 0), PlayerColor.BROWN);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testPutPenguinsOverOtherPenguin() {
    this.holesState.placePenguin(new Coord(1, 0), PlayerColor.BROWN);
    this.holesState.placePenguin(new Coord(1, 0), PlayerColor.BLACK);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPutPenguinNoTile() {
    this.holesState.placePenguin(new Coord(1, 1), PlayerColor.BROWN);
  }

  @Test
  public void testGetOnePlayersPenguins() {
    this.noHolesState.placePenguin(new Coord(0, 2), PlayerColor.BROWN);
    assertEquals(1, this.noHolesState.getOnePlayersPenguins(PlayerColor.BROWN).size());
    noHolesState.advanceToNextPlayer();
    noHolesState.advanceToNextPlayer();
    noHolesState.advanceToNextPlayer();
    this.noHolesState.placePenguin(new Coord(1,2), PlayerColor.BROWN);
    assertEquals(2, this.noHolesState.getOnePlayersPenguins(PlayerColor.BROWN).size());
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
    assertEquals(PlayerColor.BROWN, this.constantFishNumState.getCurrentPlayer());

    this.constantFishNumState.placePenguin(new Coord(0,0), PlayerColor.BROWN);
    this.constantFishNumState.placePenguin(new Coord(2,2), PlayerColor.BLACK);
    this.constantFishNumState.startPlay();
    this.constantFishNumState.movePenguin(new Coord(0,0), new Coord(0,1));

    assertEquals(2, this.constantFishNumState.getPlayerScore(PlayerColor.BROWN));
    //Now move across multiple tiles but make sure only the tile of origin is counted in score
    constantFishNumState.advanceToNextPlayer();//this is to skip the black penguin's move since we
    //are testing specifically the brown penguin's score.
    this.constantFishNumState.movePenguin(new Coord(0,1), new Coord(1,3));
    assertEquals(4, this.constantFishNumState.getPlayerScore(PlayerColor.BROWN));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerIsGoneAfterRemove() {
    this.holesState.removeCurrentPlayer();
    this.holesState.getPlayerScore(PlayerColor.BROWN);
  }

  @Test
  public void testRemoveCurrentPlayer() {
    this.holesState.removeCurrentPlayer();
    assertEquals(PlayerColor.BLACK, this.holesState.getCurrentPlayer());
  }

  @Test
  public void testRemoveCurrentPlayerLastIndex() {
    this.holesState.advanceToNextPlayer();
    this.holesState.advanceToNextPlayer();
    this.holesState.advanceToNextPlayer();
    assertEquals(PlayerColor.RED, holesState.getCurrentPlayer());
    this.holesState.removeCurrentPlayer();//remove red and ensure the current player index
    //restarts to zero, which would mean it is brown's turn
    assertEquals(PlayerColor.BROWN, this.holesState.getCurrentPlayer());
  }

  //////////////Testing Game Ending

  @Test
  public void testIsGameOver() {
    List<Player> players = new ArrayList<>(Arrays.asList(new Player(10), new Player(12),
        new Player(44), new Player(55)));
    players.get(0).setPlayerColor(PlayerColor.BROWN);
    players.get(1).setPlayerColor(PlayerColor.BLACK);
    players.get(2).setPlayerColor(PlayerColor.WHITE);
    players.get(3).setPlayerColor(PlayerColor.RED);

    //Create a situation wherein the game would be over
    List<Coord> holes = Arrays.asList(new Coord(0, 2), new Coord(0, 1));
    GameState gO =  new HexGameState();
    gO.initGame(new HexGameBoard(3, 2, holes, 4, 1),
        players);
    gO.placePenguin(new Coord(0,0), PlayerColor.BROWN);
    //Then call gameover
    assertEquals(true, gO.isGameOver());
    assertEquals(GameStage.GAMEOVER, gO.getGameStage());
  }

  @Test
  public void testIsGameOverWithMoves() {
    GameBoard gb = new HexGameBoard(3, 3, 2);
    GameState gs = new HexGameState();

    List<Player> players = new ArrayList<>(Arrays.asList(new Player(10), new Player(12)));
    players.get(0).setPlayerColor(PlayerColor.WHITE);
    players.get(1).setPlayerColor(PlayerColor.RED);

    gs.initGame(gb, players);

    gs.placePenguin(new Coord(1,2), PlayerColor.WHITE);
    gs.placePenguin(new Coord(1,1), PlayerColor.RED);

    gs.startPlay();

    gs.movePenguin(new Coord(1,2), new Coord(0,0));
    gs.movePenguin(new Coord(1,1), new Coord(1,0));
    gs.movePenguin(new Coord(0,0), new Coord(0,2));
    gs.movePenguin(new Coord(1,0), new Coord(0,1));

    assertEquals(true, gs.isGameOver());
    assertEquals(2, gs.getWinners().size());
  }

  @Test
  public void testGetWinners() {
    GameBoard gb = new HexGameBoard(6, 2, 2);
    GameState gs = new HexGameState();

    List<Player> players = new ArrayList<>(Arrays.asList(new Player(10), new Player(12)));
    players.get(0).setPlayerColor(PlayerColor.WHITE);
    players.get(1).setPlayerColor(PlayerColor.RED);

    gs.initGame(gb, players);

    gs.placePenguin(new Coord(0,3), PlayerColor.WHITE);
    gs.placePenguin(new Coord(1,2), PlayerColor.RED);

    gs.startPlay();

    gs.movePenguin(new Coord(0,3), new Coord(1,4));
    gs.movePenguin(new Coord(1,2), new Coord(0,0));
    gs.movePenguin(new Coord(1,4), new Coord(1,5));
    gs.movePenguin(new Coord(0,0), new Coord(0,4));
    gs.movePenguin(new Coord(1,5), new Coord(1,3));
    gs.movePenguin(new Coord(0,4), new Coord(0,5));
    gs.movePenguin(new Coord(1,3), new Coord(1,1));

    gb.removeTileAt(new Coord(1,0));

    assertEquals(true, gs.isGameOver());
    assertEquals(1, gs.getWinners().size());
    assertEquals(PlayerColor.WHITE, gs.getWinners().get(0));

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
    assertFalse(this.holesState.getTileAt(new Coord(1, 4)).isPresent());
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
