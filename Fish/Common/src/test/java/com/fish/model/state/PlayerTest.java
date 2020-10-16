package com.fish.model.state;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

  Player player1;
  Player player2;

  @Before
  public void setUp() {
    this.player1 = new Player(10);
    this.player2 = new Player(12);

  }

  @Test public void setColorColor() {
    assertNull(this.player1.getColor());
    this.player1.setPlayerColor(PlayerColor.BLACK);
    assertEquals(PlayerColor.BLACK, this.player1.getColor());
  }

  @Test public void getAge() {
    assertEquals(10, this.player1.getAge());
    assertEquals(12, this.player2.getAge());
  }

  @Test public void getScore() {
    assertEquals(0, this.player1.getScore());
    this.player1.addToScore(10);
    assertEquals(10, this.player1.getScore());
  }
}
