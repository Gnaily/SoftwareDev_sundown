package com.fish.game;

import com.fish.model.state.GameState;

/**
 * Function interface for interacting with GameStates. Has a single method that takes in a GameState
 *  and a value and applies its function to those inputs.
 * @param <T> What type of data this function object should return
 */
public interface IFunc<T> {

  /**
   * Apply a function to the given GameState and value
   * @param gameState the given GameState to apply a function to
   * @param value the previous value
   * @return The calculated value
   */
  T apply(GameState gameState, T value);

  /*

  Map: [LoX] [X -> Y]
  (map lox func)

  Foldr: [LoX] Y [X Y -> Y]
  (foldr lox '() (lambda (x y) (cons (func x) y))
  (foldr LoN 0 +)
   */
}
