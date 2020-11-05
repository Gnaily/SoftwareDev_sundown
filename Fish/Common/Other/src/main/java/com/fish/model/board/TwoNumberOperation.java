package com.fish.model.board;

/**
 * Interface to define rules on two given integers.
 *
 * To Use: Create a class (or lambda) that implements this interface and include the performOperation
 * method that performs the desired operation.
 * Then, instantiate that class object, and call the performOperation method on it,
 * taking in the two values you wish to perform the specific operation of that class on.
 * The resulting integer is the result of performing the operation on the two integers.
 *
 * Example:
 * Create a class called "AdditionOperation" that implements TwoNumberOperation.
 * TwoNumberOperation add = new AdditionOperation();
 * int result = add.performOperation(3, 4)
 * result == 7
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
