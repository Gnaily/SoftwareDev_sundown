package com.fish.model.state;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

  private Player redPlayer;
  private Player whitePlayer;

  @Before
  public void setUp() {
    this.redPlayer = new Player(10, PlayerColor.RED);
    this.whitePlayer = new Player(12, PlayerColor.WHITE);
  }

  @Test
  public void testGetAge() {
    assertEquals(10, this.redPlayer.getAge());
    assertEquals(12, this.whitePlayer.getAge());
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
  public void testAddToScore() {
    assertEquals(0, this.redPlayer.getScore());
    this.redPlayer.addToScore(10);
    assertEquals(10, this.redPlayer.getScore());
    this.redPlayer.addToScore(2);
    assertEquals(12, this.redPlayer.getScore());
  }


  @Test
  public void testPlayerCopy() {
    Player redCopy = this.redPlayer.getCopyPlayer();

    //Check that the dimensions of the player got copied over properly
    assertEquals(redPlayer.getAge(), redCopy.getAge());
    assertEquals(redPlayer.getScore(), redCopy.getScore());
    assertEquals(redPlayer.getColor(), redCopy.getColor());

    //Check that modifying the copy does not modify the original
    redCopy.addToScore(100);
    assertNotEquals(redPlayer.getScore(), redCopy.getScore());
  }
}
