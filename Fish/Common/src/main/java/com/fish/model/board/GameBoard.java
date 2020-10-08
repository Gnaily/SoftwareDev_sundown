package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.PlayerColor;
import com.fish.model.tile.Tile;

import java.util.HashMap;
import java.util.List;

/**
 * Interface for a GameBoard object in a game of Hey, That's my Fish!
 * An implementation of the GameBoard object's functionality must handle a few required features:
 * -- How many holes to initiate the board with
 * -- where to put those holes
 * -- minimum number of 1-fish tiles to start out with (more 1-fish tiles means more challenge)
 */
public interface GameBoard {

  List<Coord> getTilesReachableFrom(Coord start);

  Tile getTileAt(Coord loc);

  Tile removeTileAt(Coord loc) throws IllegalArgumentException;

  PlayerColor removePenguin(Coord loc);

  void placePenguin(Coord loc, PlayerColor playerColor);

  HashMap<Coord, PlayerColor> getPenguinLocations();

  int getWidth();

  int getHeight();







}
