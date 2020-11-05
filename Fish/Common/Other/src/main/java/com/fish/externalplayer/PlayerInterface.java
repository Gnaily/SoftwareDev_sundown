package com.fish.externalplayer;

import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.PlayerColor;

import java.util.List;

/**
 * Player interface for what methods a player of a game of fish is expected to be able to perform
 * in order for them to interact with the referee. A player is responsible for implementing a
 * decision making process for determining moves (only when it is their turn), as well as updating
 * their copy of the game tree or game state whenever they recieve an update that another player
 * made a move or that their move was accepted by the referee.
 */
public interface PlayerInterface {

  // REQUESTS //

  /**
   * Get a penguin placement from the player this method is being called upon. The referee will
   * expect this method to return a valid coordinate for the current gamestate. If the returned
   * coordinate is not valid, the player is at risk of being removed from the game.
   *
   * @return a valid coordinate for the given player to place a penguin.
   */
  Coord getPenguinPlacement();

  /**
   * Get a penguin movement from the player this method is being called upon. The referee will
   * expect this method to return a valid move (two coordinates) for the current gamestate. If the
   * returned move is not valid, the player will be at risk of elimination
   *
   * @return a valid movement for the current gamestate
   */
  Move getPengiunMovement();


  // MESSAGES //

  /**
   * Recieve that the given player has been removed from the game. The player will have to update their
   * local gamestate accordingly
   *
   * @param color the color of the player that was removed
   */
  void receivePlayerRemoved(PlayerColor color);

  /**
   * Receive that the given color placed a penguin on the given location. The player will have to update
   * their local gamestate accordingly
   *
   * @param loc the location the penguin was added
   * @param color the color that added the penguin
   */
  void receivePenguinPlacement(Coord loc, PlayerColor color);

  /**
   * Receive a movement for a penguin. The class implementing this interface is responsible for
   * updating their local state.
   *
   * @param move the movement that was made
   * @param color the color who moved their pengiun
   */
  void receivePenguinMovement(Move move, PlayerColor color);

  /**
   * Recieve the inital gamestate from the referee for this game of fish. The player will be responsible
   * for updating their copy of the game according the penguin placements and movements after it
   * recieves this initial version.
   *
   * @param gs the starting gamestate (with no pengiuns) passed to the player by the referee.
   */
  void receiveInitialGameState(GameState gs);

  /**
   * Recieve a message indicating who won the game. The player can do whatever they want with this
   * information since it will only be sent when the game is completed.
   *
   * @param winners the winners of the game of fish being played.
   */
  void receiveGameOver(List<PlayerColor> winners);

}
