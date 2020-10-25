package com.fish.model.tile;

/**
 * Implementation of a Tile in the shape of a hexagon for a game of Hey, That's my Fish! (HTMF)
 *
 * Interpretation: A spot on the HTMF board, which is either present (visible) or a hole.
 * A Tile has:
 *  - a number indicating how many fish are on it
 *  - a boolean indicating if it is present on the board or if it is a hole
 *
 * Players accumulate their score from collecting the fish of Tiles they land on.
 * Once a player moves its avatar from a Tile, it melts, meaning it becomes a hole and is removed
 * from the visible GameBoard.
 */
public class HexTile implements Tile {

  private int numFish;
  private boolean isPresent;

  /**
   * Constructor to instantiate a hexagon-shaped Tile in a game. isPresent is set to True because
   * if a Tile is instantiated with a numFish then it implies that it will start off as visibly
   * present in a game of HTMF, but that can later be changed.
   * @param numFish the number of fish
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
   * Returns whether a Tile is currently present, meaning visible.
   * @return a boolean that determines if a Tile is present
   */
  @Override
  public boolean isPresent() {
    return this.isPresent;
  }

  /**
   * Changes a Tile's isPresent boolean to false, meaning if it is currently present on the game
   * board, it becomes a hole.
   */
  @Override
  public void meltTile() {
    this.isPresent = false;
  }


}
