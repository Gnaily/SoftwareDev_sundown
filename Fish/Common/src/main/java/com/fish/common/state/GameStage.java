package com.fish.common.state;

/**
 * What phase a current game of HTMF is in.
 * NOT_STARTED : Waiting for a game to begin, referee is gathering players
 *               (eg waiting for the tournament manager to send over external players)
 * PLACING_PENGUINS : putting penguins on the board one player at a time
 * IN_PLAY : the time during which the game proceeds, players may move penguins
 * GAMEOVER : when no player has any valid move available, or all players have been kicked out
 */
public enum GameStage {
  NOT_STARTED,  PLACING_PENGUINS, IN_PLAY, GAMEOVER;
}
