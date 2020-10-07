package com.fish.model.tile;

/**
 * Implementatoin of a Tile in the shape of a hexagon.
 * The only functionality of the Tile implementation is to retrieve the number of fish on the tile.
 */
public class HexagonTile implements Tile {

  private int numFish;

  public HexagonTile(int numFish) {
    this.numFish = numFish;
  }

  /**
   * Returns the number of fish on this Hexagon Tile.
   * @return the number of fish on the Tile.
   */
  @Override
  public int getNumFish() {
    return this.numFish;
  }
}
