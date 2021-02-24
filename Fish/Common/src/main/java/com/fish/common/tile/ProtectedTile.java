package com.fish.common.tile;

/**
 * The extended, read-only interface of a Tile.
 * A ProtectedTile cannot be mutated, and its only functionality is information retrieval.
 *
 * When communicating with external players, only Protected versions of objects are shared.
 *
 * Please see Tile data definition/interpretation for more information on the properties of a Tile.
 */
public interface ProtectedTile {

  /**
   * Returns the number of fish on this Tile.
   * @return an int with the number of fish on the tile
   */
  int getNumFish();

  /**
   * Returns whether this Tile is currently present, meaning not a hole.
   * @return a boolean that determining whether this Tile is present
   */
  boolean isPresent();

}
