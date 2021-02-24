package com.fish.admin;

import com.fish.player.PlayerInterface;
import java.util.List;

/**
 * Interface for A Referee who will be capable of running a game of Fish.
 *
 * The only thing a tournament manager needs to be able to do are call upon the referee to run a
 * game for a given set of players and learn the results of the game once it is completed.
 */
public interface Referee {

  /**
   * Run a game of Fish for the given players.
   *
   * Once the game is completed and a winner can be declared, the referee will return a Results object,
   * which contains the following information:
   * - the list of winners of the game
   * - the list of cheaters who were eliminated from the game
   *
   * Interactions with the players (such as requesting and recieving moves) will be handled by the
   * interactions defined in the PlayerInterface interface.
   *
   * @param players the players (sorted by age/turn order) to run a game of HTMF for.
   */
  Results runGame(List<PlayerInterface> players);
}
