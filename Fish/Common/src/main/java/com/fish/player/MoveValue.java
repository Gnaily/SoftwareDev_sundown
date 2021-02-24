package com.fish.player;


import com.fish.common.game.Move;

/**
 * POJO for storing a Move and a value as a pair. There is no
 * functionality other than storing/allowing retreival of a move
 * integer pair.
 */
public class MoveValue {

  private Move move;
  private int value;

  public MoveValue(Move move, int value) {
    this.move = move;
    this.value = value;
  }

  public Move getMove() {
    return this.move;
  }

  public int getValue() {
    return this.value;
  }
}
