package com.player;

import com.fish.externalplayer.PlayerInterface;
import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.PlayerColor;
import com.player.minimax.MinimaxStrategy;

import java.util.List;

/**
 * Implementation of the PLayer interface that defines what actions a player should be able to make.
 * In this case, this is a local player who uses the minimax strategy to determine their moves.
 *
 * Depth to traverse the Minimax strategy can be set in the constructor.
 */
public class HousePlayer implements PlayerInterface {

  private int depth;
  private GameState gs;

  public HousePlayer(int depth) {
    this.depth = depth;
  }


  @Override
  public Coord getPenguinPlacement() {
    if (this.gs != null) {
      return MinimaxStrategy.findNextPenguinPlacement(this.gs);
    }

    throw new IllegalArgumentException("State must be set before placing penguins");
  }

  @Override
  public Move getPengiunMovement() {
    if (this.gs != null) {
      return MinimaxStrategy.findCurrentPlayersBestMove(this.gs, this.depth);
    }

    throw new IllegalArgumentException("State must be set before moving penguins");
  }

  @Override
  public void receivePlayerRemoved(PlayerColor color) {
    this.gs.removeCurrentPlayer();
  }

  @Override
  public void receivePenguinPlacement(Coord loc, PlayerColor color) {
    this.gs.placePenguin(loc, color);
  }

  @Override
  public void receivePenguinMovement(Move move, PlayerColor color) {
    this.gs.movePenguin(move.getOrigin(), move.getDestination());
  }

  @Override
  public void receiveInitialGameState(GameState gs) {
    this.gs = gs;
  }

  @Override
  public void receiveGameOver(List<PlayerColor> winners) {
    // this method does nothing for now - this player does not care or have anything to do once they
    // have received the winners.
  }
}
