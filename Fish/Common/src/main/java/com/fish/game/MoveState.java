package com.fish.game;

import com.fish.model.state.GameState;

/**
 * Class for representing a Move and the resulting gamestate from making that move.
 */
public class MoveState {

  private Move move;
  private GameState gameState;

  public MoveState(Move move, GameState gameState) {
    this.move = move;
    this.gameState = gameState;
  }

  public Move getMove() {
    return this.move;
  }

  public GameState getGameState() {
    return this.gameState;
  }
}
