package com.fish.model.tile;

/**
 * Interface for a Tile object in a game of Hey, That's my Fish! (HTMF)
 * An implementation of the Tile object contains only an integer representing the number of fish
 * on the tile, and its only functionality is to return that number of fish.
 * This information is used to calculate the score of a game of HTMF.
 */
public interface Tile {

  /**
   * Returns the number of fish on the Tile.
   */
  int getNumFish();

}
