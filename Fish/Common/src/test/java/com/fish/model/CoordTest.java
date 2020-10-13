package com.fish.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class CoordTest {

  @Test
  public void testEquals() {
    Coord firstCoord = new Coord(1, 0);
    Coord equalCoord = new Coord(1, 0);
    Coord notEqualCoordY = new Coord(1, 8);
    Coord notEqualCoordX = new Coord(2, 0);

    assertEquals(true, firstCoord.equals(equalCoord));
    assertEquals(false, firstCoord.equals(notEqualCoordX));
    assertEquals(false, firstCoord.equals(notEqualCoordY));
  }




}