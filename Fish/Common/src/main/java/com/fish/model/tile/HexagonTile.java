package com.fish.model.tile;

/**
 * Tile implementation that allows for retrieving the number of fish on this tile
 */
public class HexagonTile implements Tile {

  private int numberOfFish;

  public HexagonTile(int numFish) {
    this.numberOfFish = numFish;
  }

  @Override
  public int getFishOnTile() {
    return this.numberOfFish;
  }
}
