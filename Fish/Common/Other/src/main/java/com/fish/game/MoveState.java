package com.fish.game;

import com.fish.model.state.GameState;

/**
 * Class for representing a Move and the GameState that move was made from. This class
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
    return this.gameState.getCopyGameState();
  }


  @Override
  public boolean equals(Object o) {
    if (o instanceof MoveState) {
      MoveState other = (MoveState) o;

      return (other.getMove().equals(this.getMove()))
          && (other.getGameState().equals(this.getGameState()));
    }
    return false;
  }

  /**
   * Creates a unique identifier for this MoveState object
   * @return a unique integer
   */
  @Override
  public int hashCode() {
    int code = 17;
    code = 31*code + this.getMove().hashCode();
    code = 31*code + this.getGameState().hashCode();

    return code;
  }
}
