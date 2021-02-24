package com.fish.common.board;

import com.fish.common.Coord;
import com.fish.common.tile.ProtectedTile;

/**
 * Interface for a GameBoard object in a game of Hey, That's my Fish! (HTMF)
 *
 * DATA DEFINITION:
 * A data structure used to maintain the state of the collection of Tile that represent the playable
 * game board in a game of HTMF.
 *
 * GameBoard extends ProtectedGameBoard, a read-only version of the GameBoard interface.
 * When communicating with external players, only Protected versions of objects are shared.
 *
 * An implementation of a GameBoard object must contain
 * --a data structure to represent the collection of tiles
 * and have mechanisms to
 * --determine which tiles are reachable from other other tiles in a valid move
 * --retrieve data about a particular Tile in a precise location,
 * --alter the Tiles as the game proceeds.
 * The location of tiles on the visible board layout is identified using Coord objects,
 * which contain one x value (column number) and one y value (row number).
 *
 * INTERPRETATION:
 * The collection of tiles that a game of HTMF is played on and their current states.
 */
public interface GameBoard extends ProtectedGameBoard {

  /**
   * Given a coordinate location within the dimensions of the game board,
   * turns that Tile into a hole by altering its isPresent boolean to False.
   * Returns the tile as a ProtectedTile so that it is immutable but can be sent to an external
   * player to know the number of fish at that tile.
   *
   * @param loc the coordinate location of the tile to remove on the board
   * @return the ProtectedTile object at that location
   * @throws IllegalArgumentException if the requested tile is out of bounds or is already a hole
   * on the board
   */
  ProtectedTile removeTileAt(Coord loc) throws IllegalArgumentException;

  /**
   * Returns a deep copy of the GameBoard
   * @return a deep copy of this gameBoard
   */
  GameBoard getCopyGameBoard();


}
