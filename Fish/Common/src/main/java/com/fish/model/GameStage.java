package com.fish.model;

/**
 * What phase a current game of HTMF is in.
 * NOT_STARTED : Waiting for a game to begin
 * PLACING_PENGUINS : putting penguins on the board one player at a time
 * IN_PLAY : the time during which the game proceeds
 * GAMEOVER : when no player has any valid move available
 */
public enum GameStage {
  NOT_STARTED,  PLACING_PENGUINS, IN_PLAY, GAMEOVER;
}
