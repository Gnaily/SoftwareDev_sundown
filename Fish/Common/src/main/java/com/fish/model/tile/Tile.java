package com.fish.model.tile;

/**
 * Interface of a Tile in the shape of a hexagon for a game of Hey, That's my Fish! (HTMF)
 *
 * Interpretation: A spot on the HTMF board, which is either present (visible) or a hole.
 *
 * The implementation of a Tile object must contain properties to determine whether it is present
 * or a hole, and functionality to adjust that property from present to hole when needed.
 * It must also track the number of fish on a Tile and retrieve this information when needed.
 */
public interface Tile {

  /**
   * Returns the number of fish on the Tile.
   * @return an int with the number of fish on the tile
   */
  int getNumFish();

  /**
   * Returns whether a Tile is currently present, meaning visible.
   * @return a boolean that determines if a Tile is present
   */
  boolean isPresent();

  /**
   * Changes a Tile's isPresent boolean to false, meaning if it is currently present on the game
   * board, it becomes a hole.
   */
  void meltTile();
}