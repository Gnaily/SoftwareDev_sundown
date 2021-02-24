package com.fish.admin;

import com.fish.player.PlayerInterface;
import java.util.List;

/**
 *
 * Tournament Manager for a tournament of Hey, That's my Fish!
 *
 * Interpretation:
 *
 * Component for running competitive rounds of HTMF.
 * Responsible for:
 * --signing up players
 * --running an entire tournament
 * --recording and reporting results
 *
 * An implementation of this class will include these functions at a minimum.
 *
 *
 */
public interface TournamentManager {

  /**
   * Adds the given player to the ongoing tournament.
   *
   * This method should only be called when the tournament is still in the signup
   * phase. Once the tournament has begun this method should not work anymore.
   * @param playerInterface the player to sign up
   */
  void signUpPlayer(PlayerInterface playerInterface);

  /**
   * Runs a tournament for all of the players who have signed up.
   *
   * This method should handle the entire tournament by:
   *  -- creating referees for each game
   *  -- distributing players to the referees
   *  -- advance players through rounds
   */
  void runTournament();

  /**
   * Returns a list of Referee that are carrying out the current games.
   * From the Referee, one can see what is happening in the current games.
   * This method allows observers to see how the games are proceeding.
   * @return a list of Referee of ongoing games
   */
  List<Referee> reportOngoingGames();

  /**
   * Reports the final winner of the tournament.
   * This method will only work once of the rounds are completed.
   * @return the final winner
   */
  PlayerInterface reportWinner();

  /**
   * Reports players who have been completely eliminated from this tournament.
   * This could include cheating during a game, losing too many games, etc.
   * @return the eliminated players
   */
  List<PlayerInterface> reportEliminatedPlayers();


}
