package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.tile.ProtectedTile;
import java.util.List;

/**
 * The extended, read-only interface of a GameBoard.
 * A ProtectedGameBoard cannot be mutated, and its only functionality is information retrieval.
 *
 * When communicating with external players, only Protected versions of objects are shared.
 *
 * Please see GameBoard data definition/interpretation for more information on the properties of a
 * GameBoard.
 */
public interface ProtectedGameBoard {

  /**
   * Given a coordinate of origin, returns a list of all possible Coords a player can
   * make a valid move to from the origin.
   * (Note: the name is getTiles not getCoords but this returns a list of Coord used to locate the Tiles)
   * A valid move is defined by landing on any Coord location in a straight line from the
   * Coord of origin in either the up, down, up-left, up-right, down-left, or down-right directions
   * on the visible board.
   * The coordinates of each direction are calculated by observing the change in x and y values
   * as the coordinates move away from the Coord of origin.
   * 'Reachable' also means that there is no hole or penguin in the way to get there in a straight line.
   * @param origin the coord of origin
   * @param penguinLocs the locations of all penguins on this board
   * @return a list of Coord indicating the possible valid moves
   * @throws IllegalArgumentException if the coord of origin is out of bounds or is a hole
   */
  List<Coord> getTilesReachableFrom(Coord origin, List<Coord> penguinLocs);

  /**
   * Given a coordinate location within the dimensions of the game board,
   * returns the ProtectedTile object at that coordinate location.
   * The result of this method may be sent to an external player to evaluate the number of points
   * they may receive from passing over the tile. For this reason, the return tile must be
   * protected so that they cannot mutate the tile or turn it into a hole.
   * @param loc the coordinate location of the desired tile on the board
   * @return the ProtectedTile object at that location
   * @throws IllegalArgumentException if the requested tile is out of the bounds of the board
   */
  ProtectedTile getTileAt(Coord loc);

  /**
   * Constructs a 2dArray of integers that represent the number of fish on the Tile at location
   * Coord(jj, ii), where ii and jj directly correlate to the indices of a 2dArray[ii][jj]
   * @return a 2dArray of integers
   */
  int[][] getBoardDataRepresentation();

  /**
   * Returns the width of the game board, defined by the number of columns on the visual board.
   * @return an int with the width
   */
  int getWidth();

  /**
   * Returns the height of the game board, defined by the number of rows on the visual board.
   * @return an int with the height
   */
  int getHeight();


}
