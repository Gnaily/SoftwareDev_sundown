package com.fish.common.game;

/**
 * Class for counting up all Nodes in a Tree. Recursively calls itself in
 *  the HexGameTree method until there are none left and adds one to the
 *  count each time.
 */
public class NodeCounter implements IFunc<Integer> {

  @Override
  public Integer apply(GameTree gt, Integer count) {

    return 1 + count + HexGameTree.applyToAllReachableStates(gt, this, 0);
  }
}
