package com.fish.model.board;

import com.fish.player.Player;
import com.fish.model.tile.Tile;

import java.util.ArrayList;

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

  void removeTileAt(int xx, int yy);

  ArrayList<Player> getPlayers();

  // out of scope for this assignment

  // get the current turn player
  Player getCurrentPlayer();




}
