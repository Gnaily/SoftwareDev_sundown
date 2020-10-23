package com.fish.game;

import com.fish.model.state.GameState;

/**
 * Class for representing a Move and the resulting gamestate from making that move. This class
 *  is used to represent the history of a GameTree without storing the entire tree itself.
 *
 * This class contains the following information:
 *
 * GameState gameState : A GameState representing one point in a game of fish
 * Move move : The move taken FROM the GameState
 */
public class MoveState {

  private GameState gameState;
  private Move move;

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
