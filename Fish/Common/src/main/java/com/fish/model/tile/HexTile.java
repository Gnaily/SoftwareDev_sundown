package com.fish.model.tile;

/**
 * Implementation of a Tile in the shape of a hexagon.
 * The only functionality of the Tile implementation is to retrieve the number of fish on the tile.
 */
public class HexTile implements Tile {

  private int numFish;

  public HexTile(int numFish) {
    this.numFish = numFish;
  }

  @Override
  public int getNumFish() {
    return this.numFish;
  }
}
