package com.fish.model.state;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.model.tile.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class HexGameStateTest {

  private GameState twoPlayerGame;
  private GameState fourPlayerGame;
  private GameState constantFishNumGame;

  @Before
  public void setUp() throws Exception {

    this.twoPlayerGame = new HexGameState();
    this.fourPlayerGame = new HexGameState();
    this.constantFishNumGame = new HexGameState();

    //Set up each specific state to the point of starting a game

    // Two Players:
    List<Player> twoPlayers = new ArrayList<>(Arrays.asList(
        new Player(5, PlayerColor.WHITE), new Player(50, PlayerColor.RED)));

    //Board: 6 rows x 2 columns; no holes
    this.twoPlayerGame.initGame(new HexGameBoard(6, 2, new ArrayList<>(),
        5, 1), twoPlayers);

    //Place Penguins
    this.twoPlayerGame.placePenguin(new Coord(1, 2), PlayerColor.WHITE);
    this.twoPlayerGame.placePenguin(new Coord(0, 1), PlayerColor.RED);


    ////////////////////////////////////
    // Four Players:
    List<Player> fourPlayers = new ArrayList<>(Arrays.asList(
        new Player(10, PlayerColor.BROWN), new Player(12, PlayerColor.BLACK),
        new Player(44, PlayerColor.WHITE), new Player(55, PlayerColor.RED)));

    //Board: 8 rows x 3 columns; holes from holes list
    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
        new Coord(2, 2), new Coord(1, 4));
    this.fourPlayerGame.initGame(new HexGameBoard(8, 3, holes, 8, 1),
        fourPlayers);

    //Place Penguins
    this.fourPlayerGame.placePenguin(new Coord(0, 1), PlayerColor.BROWN);
    this.fourPlayerGame.placePenguin(new Coord(0, 2), PlayerColor.BLACK);
    this.fourPlayerGame.placePenguin(new Coord(0, 3), PlayerColor.WHITE);
    this.fourPlayerGame.placePenguin(new Coord(1, 2), PlayerColor.RED);


    ////////////////////////////////////
    //Use Two Players, but initialize the board with a constant number of fish for controlled testing
    List<Player> twoConstPlayers = new ArrayList<>(Arrays.asList(
        new Player(5, PlayerColor.BROWN), new Player(50, PlayerColor.BLACK)));

    //Board: 4 rows x 4 columns; no holes and 2 fish on each tile
    this.constantFishNumGame.initGame(new HexGameBoard(4, 4, 2), twoConstPlayers);

    //Place Penguins
    this.constantFishNumGame.placePenguin(new Coord(0, 0), PlayerColor.BROWN);
    this.constantFishNumGame.placePenguin(new Coord(1, 0), PlayerColor.BLACK);
  }


  @Test
  public void testInitGame() {
    //initGame was used in the setUp method. Here we are just checking that it set everything
    //up as intended.

    //Check for the intended gameStage
    assertEquals(GameStage.PLACING_PENGUINS, twoPlayerGame.getGameStage());
    assertEquals(GameStage.PLACING_PENGUINS, fourPlayerGame.getGameStage());
    assertEquals(GameStage.PLACING_PENGUINS, constantFishNumGame.getGameStage());

    //Check its the proper first player's turn
    assertEquals(PlayerColor.WHITE, twoPlayerGame.getCurrentPlayer());
    assertEquals(PlayerColor.BROWN, fourPlayerGame.getCurrentPlayer());
    assertEquals(PlayerColor.BROWN, constantFishNumGame.getCurrentPlayer());

    //Check that the board is properly set up
    assertEquals(4, fourPlayerGame.getTileAt(new Coord(2, 0)).getNumFish());
    assertEquals(2, constantFishNumGame.getTileAt(new Coord(3, 3)).getNumFish());
    assertFalse(fourPlayerGame.getTileAt(new Coord(0,0)).isPresent());
  }


  ////////////PENGUIN PLACING
  @Test(expected = IllegalStateException.class)
  public void testPlacePengInvalidNotPlacePengStage() {
    this.twoPlayerGame.startPlay(); //advances to the next stage
    this.twoPlayerGame.placePenguin(new Coord(1, 5), PlayerColor.RED);
    //triggers an exception if someone tries to place a penguin in the middle of a game when
    //the stage is IN_PLAY and not PLACING PENGUINS
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlacePengInvalidNotTurn() {
    //At this point, after the setup, we are still in the PLACING_PENGUINS stage, and it is
    //the white player's turn. This ensures an exception is triggered if someone tries to add
    //two penguins to the board at once
    this.twoPlayerGame.placePenguin(new Coord(1, 5), PlayerColor.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceTwoPengsSameLocation() {
    //White putting another penguin over white's existing penguin
    this.twoPlayerGame.placePenguin(new Coord(1, 2), PlayerColor.WHITE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPutPenguinsOverOtherPenguin() {
    //white putting a penguin over RED's existing penguin
    this.twoPlayerGame.placePenguin(new Coord(0, 1), PlayerColor.WHITE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPutPenguinNoTile() {
    this.fourPlayerGame.placePenguin(new Coord(1, 1), PlayerColor.BROWN);
  }

  @Test
  public void testPlacePenguinProperly() {
    assertEquals(2, this.twoPlayerGame.getPenguinLocations().size());
    this.twoPlayerGame.placePenguin(new Coord(0, 0), PlayerColor.WHITE);
    this.twoPlayerGame.placePenguin(new Coord(1, 3), PlayerColor.RED);
    Map<Coord, PlayerColor> pengs = this.twoPlayerGame.getPenguinLocations();

    //Checks that the penguins were added to the penguin tracker
    assertEquals(4, pengs.size());
    //Checks that the player turn advanced properly
    assertEquals(PlayerColor.WHITE, this.twoPlayerGame.getCurrentPlayer());
    //Checks that the penguin data tracker is 100% correct
    assertEquals(PlayerColor.WHITE, pengs.get(new Coord(1, 2)));
    assertEquals(PlayerColor.RED, pengs.get(new Coord(0, 1)));
    assertEquals(PlayerColor.WHITE, pengs.get(new Coord(0, 0)));
    assertEquals(PlayerColor.RED, pengs.get(new Coord(1, 3)));
  }

  @Test
  public void testGetNullPenguin() {
    Map<Coord, PlayerColor> pengs = this.fourPlayerGame.getPenguinLocations();
    //A tile that is a hole:
    assertNull(pengs.get(new Coord(1, 1)));
    //A tile on the board but where there is not a penguin:
    assertNull(pengs.get(new Coord(2, 1)));
  }


  ////////////PENGUIN MOVING
  @Test(expected = IllegalStateException.class)
  public void testMovePengInvalidNotMovePengStage() {
    //This would otherwise be a valid move at the valid player turn. However, the game has not advanced
    //into the IN_PLAY stage, so this is testing that the game throws an error when someone tries to
    //move a penguin when they're supposed to be placing a NEW penguin.
    this.twoPlayerGame.movePenguin(new Coord(1,2), new Coord(1,3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePengNotPresent() {
    this.twoPlayerGame.startPlay();
    //White trying to move a penguin FROM a tile with no penguin
    twoPlayerGame.movePenguin(new Coord(1,3), new Coord(1,4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePengInvalidNotTurn() {
    this.twoPlayerGame.startPlay();
    //It's white's turn, but this would move RED's penguin
    this.twoPlayerGame.movePenguin(new Coord(0,1), new Coord(0,5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePengTOHole() {
    fourPlayerGame.placePenguin(new Coord(2, 6), PlayerColor.BROWN);
    this.fourPlayerGame.startPlay();
    fourPlayerGame.movePenguin(new Coord(2,6), new Coord(2,2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePengFROMHole() {
    this.fourPlayerGame.startPlay();
    fourPlayerGame.movePenguin(new Coord(1,1), new Coord(1,7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToTileWithPengAlready() {
    fourPlayerGame.placePenguin(new Coord(2, 4), PlayerColor.BROWN);
    this.fourPlayerGame.startPlay();
    fourPlayerGame.movePenguin(new Coord(2,4), new Coord(1,2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePengToSameTile() {
    twoPlayerGame.startPlay();
    twoPlayerGame.movePenguin(new Coord(1,3), new Coord(1,3));
  }

  @Test
  public void testMovePenguinValid() {
    this.twoPlayerGame.startPlay(); // Advances the GameStage to IN_PLAY

    //Move white's penguin once, then move red's penguin once
    twoPlayerGame.movePenguin(new Coord(1,2), new Coord(1,3));
    twoPlayerGame.movePenguin(new Coord(0,1), new Coord(0,5));

    assertEquals(2, twoPlayerGame.getPenguinLocations().size());
    //Check that the original location of the white penguin is no longer in the pengLocs map
    assertNull(twoPlayerGame.getPenguinLocations().get(new Coord(1, 2)));
    //Check that the white penguin has moved
    assertEquals(PlayerColor.WHITE, twoPlayerGame.getPenguinLocations().get(new Coord(1, 3)));
    //Check that the original Tiles the penguins were on are no longer there
    assertFalse(twoPlayerGame.getTileAt(new Coord(1, 2)).isPresent());
    assertFalse(twoPlayerGame.getTileAt(new Coord(0, 1)).isPresent());
    //Check the scores of the players
    assertEquals(5, twoPlayerGame.getPlayerScore(PlayerColor.WHITE));
    assertEquals(1, twoPlayerGame.getPlayerScore(PlayerColor.RED));
    //Finally, check that the player turn advanced accordingly
    assertEquals(PlayerColor.WHITE, twoPlayerGame.getCurrentPlayer());
  }

  @Test
  public void testGetOnePlayersPenguins() {
    //Set up the expected outcome
    List<Coord> whitePengLocs = new ArrayList<>();
    whitePengLocs.add(new Coord(1, 2));
    //Check that it equals the current player's locations
    assertEquals(whitePengLocs, this.twoPlayerGame.getOnePlayersPenguins(PlayerColor.WHITE));
    //Place a new penguin
    this.twoPlayerGame.placePenguin(new Coord(1, 5), PlayerColor.WHITE);
    //Update the expected
    whitePengLocs.add(new Coord(1, 5));
    //Check that the new actual it still precise
    assertEquals(whitePengLocs, this.twoPlayerGame.getOnePlayersPenguins(PlayerColor.WHITE));
  }

  ////////////PLAYER HANDLING

  @Test
  public void testGetCurrentPlayerAndAdvance() {
    assertEquals(PlayerColor.BROWN, this.fourPlayerGame.getCurrentPlayer());
    this.fourPlayerGame.advanceToNextPlayer();
    assertEquals(PlayerColor.BLACK, this.fourPlayerGame.getCurrentPlayer());
    this.fourPlayerGame.advanceToNextPlayer();
    assertEquals(PlayerColor.WHITE, this.fourPlayerGame.getCurrentPlayer());
    this.fourPlayerGame.advanceToNextPlayer();
    assertEquals(PlayerColor.RED, this.fourPlayerGame.getCurrentPlayer());
    this.fourPlayerGame.advanceToNextPlayer();
    //Test that it resets to the first player again
    assertEquals(PlayerColor.BROWN, this.fourPlayerGame.getCurrentPlayer());
    this.fourPlayerGame.advanceToNextPlayer();
  }

  @Test
  public void testGetPlayerScore() {
    this.constantFishNumGame.startPlay();
    assertEquals(0, this.constantFishNumGame.getPlayerScore(PlayerColor.BROWN));
    this.constantFishNumGame.movePenguin(new Coord(0,0), new Coord(1, 3));
    assertEquals(2, this.constantFishNumGame.getPlayerScore(PlayerColor.BROWN));
    this.constantFishNumGame.advanceToNextPlayer(); //BACK to Brown
    this.constantFishNumGame.movePenguin(new Coord(1,3), new Coord(2, 1));
    assertEquals(4, this.constantFishNumGame.getPlayerScore(PlayerColor.BROWN));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerIsGoneAfterRemove() {
    this.twoPlayerGame.removeCurrentPlayer();
    this.twoPlayerGame.getPlayerScore(PlayerColor.WHITE);//Returns "Player not found"
  }

  @Test
  public void testRemoveCurrentPlayer() {
    fourPlayerGame.startPlay();
    this.fourPlayerGame.removeCurrentPlayer();
    assertEquals(PlayerColor.BLACK, this.fourPlayerGame.getCurrentPlayer());
    this.fourPlayerGame.removeCurrentPlayer();
    assertEquals(PlayerColor.WHITE, this.fourPlayerGame.getCurrentPlayer());
  }

  @Test
  public void testRemoveCurrentPlayerLastIndex() {
    this.fourPlayerGame.advanceToNextPlayer();
    this.fourPlayerGame.advanceToNextPlayer();
    this.fourPlayerGame.advanceToNextPlayer();
    //This advances to the last player's turn, which is RED's turn
    assertEquals(PlayerColor.RED, fourPlayerGame.getCurrentPlayer());
    this.fourPlayerGame.removeCurrentPlayer();//remove red and ensure the current player index
    //restarts to zero, which would mean it is brown's turn
    assertEquals(PlayerColor.BROWN, this.fourPlayerGame.getCurrentPlayer());
  }

  //////////////Testing Game Ending

  @Test
  public void testIsGameOverDueToOnePlayerLeftInGame() {
    //Create a situation where there is an ongoing game with 2 players, but then someone gets removed,
    //resulting in an automatic win for the other player:
    this.twoPlayerGame.startPlay();
    this.twoPlayerGame.removeCurrentPlayer();
    assertTrue(this.twoPlayerGame.isGameOver());
    assertEquals(GameStage.GAMEOVER, this.twoPlayerGame.getGameStage());
  }

  @Test
  public void testIsGameOverWithMoves() {
    //Create a simplified board for this one:
    GameBoard gb = new HexGameBoard(3, 3, 2);
    GameState gs = new HexGameState();

    List<Player> players = new ArrayList<>(Arrays.asList(
        new Player(10, PlayerColor.WHITE), new Player(12, PlayerColor.RED)));

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
    //Create a simplified board to control winners:
    GameBoard gb = new HexGameBoard(6, 2, 2);
    GameState gs = new HexGameState();

    List<Player> players = new ArrayList<>(Arrays.asList(
        new Player(10, PlayerColor.WHITE), new Player(12, PlayerColor.RED)));

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

    assertTrue(gs.isGameOver());
    assertEquals(1, gs.getWinners().size());
    assertEquals(PlayerColor.WHITE, gs.getWinners().get(0));

  }

  ///// Tests for copying the state
  @Test
  public void testCopyStateTilesConsistent() {
    GameState copy = this.twoPlayerGame.getCopyGameState();

    for (int ii = 0; ii < copy.getWidth(); ii++) {
      for (int jj = 0; jj < copy.getHeight(); jj++) {
        Tile tile = copy.getTileAt(new Coord(ii, jj));
        if (tile.isPresent()) {
          assertEquals(tile.getNumFish(), this.twoPlayerGame.getTileAt(new Coord(ii, jj)).getNumFish());
        }
        else {
          assertFalse(this.twoPlayerGame.getTileAt(new Coord(ii, jj)).isPresent());
        }
      }
    }

  }

  @Test
  public void testCopyStatePenguinsConsistent() {
    GameState copy = twoPlayerGame.getCopyGameState();

    Map<Coord, PlayerColor> original = twoPlayerGame.getPenguinLocations();
    Map<Coord, PlayerColor> penguinCopy = copy.getPenguinLocations();

    for (Coord c : original.keySet()) {
      assertEquals(original.get(c), penguinCopy.get(c));
    }

    //Make a change to the COPY and ensure the original stayed the same:
    copy.startPlay();
    copy.movePenguin(new Coord(1, 2), new Coord(1, 3));

    //Check that the original is still in PLACING_PENGUINS state, as it is at the end of setUp:
    assertEquals(GameStage.IN_PLAY, copy.getGameStage());
    assertEquals(GameStage.PLACING_PENGUINS, this.twoPlayerGame.getGameStage());

    //Check that the penguin locations of the original did not change, while the copy did:
    assertNull(copy.getPenguinLocations().get(new Coord(1,2)));
    assertNotNull(this.twoPlayerGame.getPenguinLocations().get(new Coord(1,2)));
  }


  @Test
  public void testCopyPlayersList() {
    GameState copy = twoPlayerGame.getCopyGameState();

    twoPlayerGame.advanceToNextPlayer();

    assertEquals(PlayerColor.RED, twoPlayerGame.getCurrentPlayer());
    assertEquals(PlayerColor.WHITE, copy.getCurrentPlayer());

    twoPlayerGame.removeCurrentPlayer();

    assertEquals(PlayerColor.WHITE, twoPlayerGame.getCurrentPlayer());
    assertEquals(PlayerColor.WHITE, copy.getCurrentPlayer());

    twoPlayerGame.advanceToNextPlayer();
    copy.advanceToNextPlayer();

    assertEquals(PlayerColor.WHITE, twoPlayerGame.getCurrentPlayer());
    assertEquals(PlayerColor.RED, copy.getCurrentPlayer());
  }


  /////Tests for Tile Handling
  @Test
  public void testGetTileAtValid() {
    assertEquals(3, this.fourPlayerGame.getTileAt(new Coord(0, 1)).getNumFish());
    assertEquals(1, this.fourPlayerGame.getTileAt(new Coord(1, 0)).getNumFish());
    assertEquals(5, this.twoPlayerGame.getTileAt(new Coord(1, 2)).getNumFish());
    assertEquals(1, this.twoPlayerGame.getTileAt(new Coord(0, 5)).getNumFish());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTileOutOfRange() {
    this.twoPlayerGame.getTileAt(new Coord(10, 10));
  }

  @Test
  public void testGetTileRemoved() {
    assertFalse(this.fourPlayerGame.getTileAt(new Coord(1, 4)).isPresent());
  }

  @Test
  public void testGetWidth() {
    assertEquals(2, this.twoPlayerGame.getWidth());
    assertEquals(4, this.constantFishNumGame.getWidth());
  }

  @Test
  public void testGetHeight() {
    assertEquals(6, this.twoPlayerGame.getHeight());
    assertEquals(4, this.constantFishNumGame.getHeight());
  }


}
