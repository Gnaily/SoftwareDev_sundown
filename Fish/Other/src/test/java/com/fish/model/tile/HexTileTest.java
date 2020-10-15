package com.fish.model.tile;
import org.junit.Before;
import static org.junit.Assert.*;

import org.junit.Test;

public class HexTileTest {
  Tile oneFishPresent;
  Tile manyFishPresent;
  Tile isNotPresent;

  @Before
  public void setUp() throws Exception {
    this.oneFishPresent = new HexTile(1);
    this.manyFishPresent = new HexTile(4);
    this.isNotPresent = new HexTile();
  }

  @Test
  public void getNumFish() {
    assertEquals(1, oneFishPresent.getNumFish());
    assertEquals(4, manyFishPresent.getNumFish());
    assertEquals(0, isNotPresent.getNumFish());
  }

  @Test
  public void isPresent() {
    assertEquals(true, oneFishPresent.isPresent());
    assertEquals(true, manyFishPresent.isPresent());
    assertEquals(false, isNotPresent.isPresent());
  }

  @Test
  public void meltTile() {
    assertEquals(1, oneFishPresent.getNumFish());
    assertEquals(true, oneFishPresent.isPresent());
    oneFishPresent.meltTile();
    assertEquals(1, oneFishPresent.getNumFish());
    assertEquals(false, oneFishPresent.isPresent());
  }
}