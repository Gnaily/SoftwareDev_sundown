package com.fish.model.board;

/**
 * Interface to define rules on two given integers.
 * To Use: Create a class (or lambda) that implements this interface and include the performOperation
 * method that performs the desired operation.
 * Then, instantiate that class object, taking in the two values in the constructor,
 * and invoke the performOperation method on them. This returns the resulting integer.
 *
 */
public interface TwoNumberOperation {

  /**
   * Perform an operation on two numbers and return the result.
   *
   * @param aa first input number
   * @param bb second input number
   * @return calculated value
   */
  int performOperation(int aa, int bb);

}
