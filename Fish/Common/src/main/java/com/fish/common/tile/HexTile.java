package com.fish.common.tile;

/**
 * Implementation of a Tile in the shape of a hexagon for a game of Hey, That's my Fish! (HTMF)
 *
 * DATA DEFINITION:
 * A Tile has:
 *  - an integer indicating how many fish are on it
 *  - a boolean indicating if it is present on the board or if it is a hole
 * A Tile object can:
 *  - 'melt' meaning mutate its internal boolean property isPresent from true to false
 *
 * INTERPRETATION:
 * A Tile represents one piece in the collection of tile pieces a game of HTMF is played on.
 * It is the place where players land their avatars in the proceedings of the game.
 * Players accumulate their score from collecting the fish of Tiles they land on.
 * Once a player moves its avatar from a Tile, it melts, meaning it becomes a hole and is removed
 * from the visible GameBoard.
 */
public class HexTile implements Tile {

  private int numFish;
  private boolean isPresent;

  /**
   * Constructor to instantiate a hexagon-shaped Tile in a game with the given number of fish
   * on it. isPresent is set to True because if a Tile is instantiated with a numFish then it
   * implies that it will start off as visibly present in a game of HTMF,
   * but that can later be changed.
   * @param numFish the number of fish on this tile
   */
  public HexTile(int numFish) {
    this.numFish = numFish;
    this.isPresent = true;
  }

  /**
   * Constructor to instantiate a Tile object that represents a hole, or a Tile that is not
   * present, in a game of HTMF. This constructor serves the purpose of a place-holder,
   * wherein the numFish is set to zero because it is not needed and isPresent is set to false
   * because once a tile is indicated as a hole it can never come back. Think of this as a null
   * Tile, without using null.
   */
  public HexTile() {
    this.numFish = 0;
    this.isPresent = false;
  }

  /**
   * Returns the number of fish on the Tile.
   * @return an int with the number of fish on the tile
   */
  @Override
  public int getNumFish() {
    return this.numFish;
  }


  /**
   * Returns whether a Tile is currently present, meaning not a hole.
   * @return a boolean determining if this Tile is present
   */
  @Override
  public boolean isPresent() {
    return this.isPresent;
  }

  /**
   * Converts a present tile into a hole by mutating the internal data field of the Tile
   * that represents whether it is a present tile or a hole.
   *
   * If called on a tile that is already a hole, this method has absolutely no affect on the Tile.
   */
  @Override
  public void meltTile() {
    this.isPresent = false;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof HexTile) {
      HexTile other = (HexTile) o;
      return this.numFish == other.getNumFish() && this.isPresent == other.isPresent();
    }
    return false;
  }

}
