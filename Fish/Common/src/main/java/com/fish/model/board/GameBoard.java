package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.tile.Tile;

import java.util.List;

/**
 * Interface for a GameBoard object in a game of Hey, That's my Fish! (HTMF)
 * The GameBoard object is a representation of the collection of tiles used to play out a game of
 * HTMF. The user of this interface has control over how to generate the precise design of the
 * board, including the shape of the tiles, determining the number of fish on tiles, and where
 * holes are initiated.
 * An implementation of a GameBoard object must handle identifying the location of tiles,
 * removing tiles, placing penguins on the board, moving and removing penguins, and retrieving
 * the dimensions of the board.
 * The location of tiles on the board layout is specifically identified using Coord objects,
 * which contains one x and one y value that is used to point to the exact index of the board
 * data representation where the desired Tile lives.
 */
public interface GameBoard {

  /**
   * Given a coordinate of origin, returns a list of all possible coordinates a player can
   * make a valid move to from the origin.
   * @param start the coord of origin
   * @param penguinLocs the location of all penguins on the board
   * @return a list of Coord indicating the possible valid moves
   * @throws IllegalArgumentException if the coord of origin is out of bounds or is a hole
   */
  List<Coord> getTilesReachableFrom(Coord start, List<Coord> penguinLocs);

  /**
   * Given a coordinate location within the dimensions of the game board,
   * returns the Tile object at that coordinate location.
   * @param loc the coordinate location of the desired tile on the board
   * @return the Tile object at that location
   * @throws IllegalArgumentException if the requested tile is out of the bounds of the board
   */
  Tile getTileAt(Coord loc);

  /**
   * Given a coordinate location within the dimensions of the game board,
   * removes the tile from that coordinate location on the board, replacing it with null,
   * and returns the Tile.
   * @param loc the coordinate location of the tile to remove on the board
   * @return the Tile object at that location
   * @throws IllegalArgumentException if the requested tile is out of bounds or is already a hole
   * on the board, represented by a null value
   */
  Tile removeTileAt(Coord loc) throws IllegalArgumentException;


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
