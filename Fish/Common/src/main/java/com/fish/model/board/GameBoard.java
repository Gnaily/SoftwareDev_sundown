package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.PlayerColor;
import com.fish.model.tile.Tile;

import java.util.HashMap;
import java.util.List;

/**
 * Interface for a GameBoard object in a game of Hey, That's my Fish! (HTMF)
 * The GameBoard object is a representation of the collection of tiles used to play out a game of
 * HTMF. The user of this interface has control over how to generate the precise design of the
 * board, including the shape of the tiles, the number of fish on tiles, and where the holes are
 * initiated.
 * An implementation of a GameBoard object must handle identifying the location of tiles,
 * removing tiles, placing penguins on the board, moving and removing penguins, and retrieving
 * the dimensions of the board.
 * 
 */
public interface GameBoard {

  /**
   * Given a coordinate of origin, returns a list of all possible coordinates a player can
   * make a valid move to from the origin.
   * @param start the coord of origin
   * @return a list of Coord indicating the possible valid moves
   * @throws IllegalArgumentException if the coord of origin is out of bounds or is a hole
   */
  List<Coord> getTilesReachableFrom(Coord start);

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
   * Returns a HashMap of penguin locations, formatted such that the Coord is the unique
   * identifier of the location (since only one penguin can be on a tile at a time) and
   * the PlayerColor is the value, to identify which player's penguin is on that location.
   * @return a HashMap of Coord to PlayerColor values
   */
  HashMap<Coord, PlayerColor> getPenguinLocations();

  /**
   * Adds a penguin to the board at the given Coord location. Stores the PlayerColor of the
   * particular player whose penguin is being placed. This method it only to be used at game
   * start-up as players may not place a penguin once gameplay has begun.
   * @param loc the coordinate location to place a penguin
   * @param playerColor the color associated with the player placing the penguin
   * @throws IllegalArgumentException if given Coord is out of bounds or there is a hole there
   */
  void placePenguin(Coord loc, PlayerColor playerColor);

  /**
   * Removes the element of the penguinLocs HashMap with the given unique Coord.
   * @param loc the coordinate location to remove the penguin
   * @return the playerColor that was at that location
   * @throws IllegalArgumentException if there is no penguin present in the given location
   */
  PlayerColor removePenguin(Coord loc);

  /**
   * Returns the width of the game board, defined by the number of columns on the visual board
   * @return an int with the width
   */
  int getWidth();

  /**
   * Returns the height of the game board, defined by the number of rows on the visual board
   * @return an int with the height
   */
  int getHeight();







}
