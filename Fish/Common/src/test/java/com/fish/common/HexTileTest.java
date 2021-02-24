package com.fish.common;

import com.fish.common.tile.HexTile;
import com.fish.common.tile.ProtectedTile;
import com.fish.common.tile.Tile;
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
  public void testGetNumFish() {
    assertEquals(1, oneFishPresent.getNumFish());
    assertEquals(4, manyFishPresent.getNumFish());
    assertEquals(0, isNotPresent.getNumFish());
  }

  @Test
  public void testIsPresent() {
    assertEquals(true, oneFishPresent.isPresent());
    assertEquals(true, manyFishPresent.isPresent());
    assertEquals(false, isNotPresent.isPresent());
  }

  @Test
  public void testMeltTile() {
    assertEquals(1, oneFishPresent.getNumFish());
    assertEquals(true, oneFishPresent.isPresent());
    oneFishPresent.meltTile();
    assertEquals(1, oneFishPresent.getNumFish());
    assertEquals(false, oneFishPresent.isPresent());
  }

  @Test
  public void testMutabilityOfTile() {
    Tile mutable = new HexTile(5);
    ProtectedTile immutable = new HexTile(3);

    assertTrue(mutable.isPresent());
    assertTrue(immutable.isPresent());
    mutable.meltTile();
    assertFalse(mutable.isPresent());
    //This is commented out because java will not compile if you even try to run this
    //mutation method on the immutable Tile
    //immutable.meltTile();

    //But you can run the rest of the getters on both:
    assertEquals(5, mutable.getNumFish());
    assertEquals(3, immutable.getNumFish());
  }
}