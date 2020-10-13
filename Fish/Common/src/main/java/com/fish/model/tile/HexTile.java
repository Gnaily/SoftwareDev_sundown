package com.fish.model.tile;

/**
 * Implementation of a Tile in the shape of a hexagon for a game of Hey, That's my Fish! (HTMF)
 *
 * A Tile in the game is a spot on the board that a player may land one of their avatars on.
 * A Tile has a number of fish on it, and players earn the fish from Tiles they land on as
 * an accumulation of their score. Once a player moves its avatar from a Tile, it melts, meaning
 * it disappears/is removed from from the game board, becoming a hole.
 *
 * An implementation of the Tile object contains an integer of the number of fish on the Tile, and
 * a boolean determining whether or not the tile isPresent on the board.
 * Its only functionality is to retrieve the number of fish on it, to determine whether it is
 * currently present, and the functionality to 'melt' the Tile, meaning to change its internal
 * state from present to a hole.
 */
public class HexTile implements Tile {

  private int numFish;
  private boolean isPresent;

  /**
   * Constructor to instantiate a hexagon-shaped tile in a game. isPresent is set to True because
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
   * Melts a tile, meaning changing the tile from visible in a game of HTMF to a hole.
   */
  @Override
  public void meltTile() {
    this.isPresent = false;
  }


}
