package com.fish.model.tile;

/**
 * Implementation of a Tile in the shape of a hexagon.
 * The only functionality of the Tile implementation is to retrieve the number of fish on the tile.
 */
public class HexTile implements Tile {

  private int numFish;

  public static final int MAX_FISH = 5;

  public HexTile(int numFish) {
    if (numFish > MAX_FISH) {
      throw new IllegalArgumentException("Too many fish on the tile!");
    }

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
