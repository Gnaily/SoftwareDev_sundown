package com.fish.model.tile;

/**
 * Interface for a Tile object in a game of Hey, That's my Fish! (HTMF)
 *
 * DATA DEFINITION:
 * Used to construct and adjust the internal data structure of a board in a game of HTMF.
 *
 * Tile extends ProtectedTile, a read-only version of the Tile interface.
 * When communicating with external players, only Protected versions of objects are shared.
 *
 * An implementation of Tile must contain properties to determine whether one is
 * a tile present on the board or a hole on the board,
 * the number of fish on that tile,
 * functionality to convert a tile into a hole, and
 * functionality to retrieve the number of fish on the tile.
 *
 * INTERPRETATION:
 * A Tile represents one piece in the collection of tile pieces a game of HTMF is played on.
 * It is the place where players land their avatars in the proceedings of the game.
 * A Tile is either present (still in the playable collection of tiles) or a hole. A present
 * tile becomes a hole when a player moves their avatar from it.
 *
 */
public interface Tile extends ProtectedTile {

  /**
   * Converts a present tile into a hole by mutating the internal data field of the Tile
   * that represents whether it is a present tile or a hole.
   *
   * If called on a tile that is already a hole, this method has absolutely no affect on the Tile.
   */
  void meltTile();
}