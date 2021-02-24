package com.fish.common;

import org.junit.Test;

import static org.junit.Assert.*;


public class CoordTest {

  @Test
  public void testEquals() {
    Coord firstCoord = new Coord(1, 0);
    Coord equalCoord = new Coord(1, 0);
    Coord notEqualCoordY = new Coord(1, 8);
    Coord notEqualCoordX = new Coord(2, 0);

    assertEquals(firstCoord, equalCoord);
    assertNotEquals(firstCoord, notEqualCoordX);
    assertNotEquals(firstCoord, notEqualCoordY);
  }




}