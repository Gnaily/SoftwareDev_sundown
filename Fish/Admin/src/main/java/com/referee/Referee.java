package com.referee;

import com.fish.externalplayer.PlayerInterface;

import java.util.List;

/**
 * Interface for A Referee who will be capable of running a game of Fish.
 */
public interface Referee {

  /**
   * Run a game of Fish for the given players.
   *
   * TODO: fill out more
   *
   * @param players
   */
  Results runGame(List<PlayerInterface> players);
}
