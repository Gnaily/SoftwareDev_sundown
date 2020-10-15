package com.fish.view;

/**
 * Interface for drawing the GUI a game of Hey, that's my fish! (HTMF)
 * A user of this interface has control over the design of the particular game visual,
 * including the shape and size of tiles in the game. A user must implement a visual
 * for each game piece needed to play a game:
 * - a tile
 * - a collection of tiles
 * - multiple fish on a tile
 * - a penguin on a tile, one of four colors
 */
public interface GameView {

  /**
   * Draws each game piece needed in a game of HTMF and brings together the layout of the game.
   */
  void drawGame();

}
