package com.fish.model.board;

/**
 * Interface for defining a rule of when to increment values.
 */
public interface TwoNumberOperation {

  /**
   * Perform an operation on two numbers and return the result
   *
   * @param aa first input number
   * @param bb second input number
   * @return calculated value
   */
  int performOperation(int aa, int bb);

}
