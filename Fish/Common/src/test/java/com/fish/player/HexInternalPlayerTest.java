package com.fish.player;


import com.fish.common.Coord;
import com.fish.common.state.HexPlayer;
import com.fish.common.state.InternalPlayer;
import com.fish.common.state.PlayerColor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HexInternalPlayerTest {

  private HexPlayer redPlayer;
  private HexPlayer whitePlayer;

  @Before
  public void setUp() {
    this.redPlayer = new HexPlayer(PlayerColor.RED);
    this.whitePlayer = new HexPlayer(PlayerColor.WHITE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlacePenguinInvalid() {
    redPlayer.placePenguin(new Coord(0,1));
    redPlayer.placePenguin(new Coord(0,1));
  }

  @Test
  public void testPlacePenguinValid() {
    redPlayer.placePenguin(new Coord(0,1));
    assertEquals(1, redPlayer.getPenguinLocs().size());
    assertEquals(new Coord(0,1), redPlayer.getPenguinLocs().get(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePenguinInvalidDest() {
    whitePlayer.placePenguin(new Coord(2, 5));
    whitePlayer.placePenguin(new Coord(0, 6));
    whitePlayer.movePenguin(new Coord(2, 5), new Coord(0,0));
    whitePlayer.movePenguin(new Coord(0, 6), new Coord(0,0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePenguinInvalidOrigin() {
    whitePlayer.movePenguin(new Coord(2, 5), new Coord(0,0));
  }

  @Test
  public void testMovePenguinValid() {
    whitePlayer.placePenguin(new Coord(0,1));
    whitePlayer.movePenguin(new Coord(0, 1), new Coord(2, 4));
    assertEquals(1, whitePlayer.getPenguinLocs().size());
    assertEquals(new Coord(2,4), whitePlayer.getPenguinLocs().get(0));
    assertNotEquals(new Coord(0,1), whitePlayer.getPenguinLocs().get(0));
  }

  @Test
  public void testAddToScore() {
    assertEquals(0, this.redPlayer.getScore());
    this.redPlayer.addToScore(10);
    assertEquals(10, this.redPlayer.getScore());
    this.redPlayer.addToScore(2);
    assertEquals(12, this.redPlayer.getScore());
  }

  @Test
  public void testGetPenguinLocs() {
    assertEquals(0, this.redPlayer.getPenguinLocs().size());
    redPlayer.placePenguin(new Coord(2, 6));
    redPlayer.placePenguin(new Coord(0,7));

    assertEquals(2, this.redPlayer.getPenguinLocs().size());
    assertEquals(new Coord(2,6), this.redPlayer.getPenguinLocs().get(0));
    assertEquals(new Coord(0,7), this.redPlayer.getPenguinLocs().get(1));
  }


  @Test
  public void testGetColor() {
    assertEquals(PlayerColor.RED, this.redPlayer.getColor());
    assertEquals(PlayerColor.WHITE, this.whitePlayer.getColor());
  }

  @Test
  public void testGetScore() {
    assertEquals(0, this.redPlayer.getScore());
    assertEquals(0, this.whitePlayer.getScore());
    this.redPlayer.addToScore(100);
    assertEquals(100, this.redPlayer.getScore());
    assertEquals(0, this.whitePlayer.getScore());
  }



  @Test
  public void testPlayerCopy() {
    //Add some dimensions to the red player
    redPlayer.placePenguin(new Coord(0,5));
    redPlayer.placePenguin(new Coord(2,2));
    redPlayer.addToScore(100);

    InternalPlayer redCopy = this.redPlayer.getCopyPlayer();

    //Check that the dimensions of the player got copied over properly
    assertEquals(redPlayer.getScore(), redCopy.getScore());
    assertEquals(redPlayer.getColor(), redCopy.getColor());

    //Check that modifying the copy does not modify the original
    redCopy.addToScore(100);
    assertNotEquals(redPlayer.getScore(), redCopy.getScore());
    redPlayer.movePenguin(new Coord(0,5), new Coord(0,1));
    assertNotEquals(redPlayer.getPenguinLocs(), redCopy.getPenguinLocs());
  }
}
