package com.fish.model.tile;

/**
 * Interface for a Tile object in a game of Hey, That's my Fish!
 * An implementation of the Tile object's only functionality is to return the number of fish
 * on the tile, which is used to calculate the score of a game.
 */
public interface Tile {

  /**
   * Returns the number of fish on the Tile.
   */
  int getNumFish();

}
