package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.tile.Tile;

import java.util.List;

/**
 * Interface for a Board of Fish
 */
public interface FishBoard {

  // what does a board need
  // dimension (with tiles)
  //  - tiles contain their own information of fish
  //  - hole vs tile
  //     - representation of hole

  // board should have maxnumber of fish on a tile;

  int getWidth();

  int getHeight();

  Tile getTileAt(int xx, int yy);

  Tile removeTileAt(int xx, int yy) throws IllegalArgumentException;

  List<Coord> getTilesReachableFrom(Coord start);





}
