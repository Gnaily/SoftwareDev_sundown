package com.fish.model.tile;


/**
 * Implementation of a Tile in the shape of a hexagon for a game of Hey, That's my Fish! (HTMF)
 * An implementation of the Tile object contains only an integer representing the number of fish
 * on the tile, and its only functionality is to return that number of fish.
 * This information is used to calculate the score of a game of HTMF.
 */
public class HexTile implements Tile {

  private int numFish;

  /**
   * Constructor to instantiate a hexagon-shaped tile in a game.
   * @param numFish the number of fish
   */
  public HexTile(int numFish) {
    this.numFish = numFish;
  }

  /**
   * Returns the number of fish on the Tile.
   * @return an int with the number of fish on the tile
   */
  @Override
  public int getNumFish() {
    return this.numFish;
  }
}
