package com.fish.game;

/**
 * Function interface for interacting with GameTrees. Has a single method that takes in a GameTree
 *  and a value and applies its function to those inputs.
 * @param <T> What type of data this function object should return
 */
public interface IFunc<T> {

  /**
   * Apply a function to the given GameTree and value
   * @param gameTree the given GameTree to apply a function to
   * @param value the previous value
   * @return The calculated value
   */
  T apply(GameTree gameTree, T value);
}
