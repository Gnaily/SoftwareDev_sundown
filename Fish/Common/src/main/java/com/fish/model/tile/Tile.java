package com.fish.model.tile;

/**
 * Interface for a Tile object in a game of Hey, That's my Fish! (HTMF)
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
public interface Tile {

  /**
   * Returns the number of fish on the Tile.
   */
  int getNumFish();

  /**
   * Returns whether a Tile is currently present, meaning visible.
   */
  boolean isPresent();

  /**
   * Melts a tile, meaning changing the tile from visible in a game of HTMF to a hole.
   */
  void meltTile();
}