package com.fish.common.game;


import com.fish.common.state.GameState;
import java.util.*;

/**
 * Data representation for an entire game of Fish, starting from the point where all penguins have
 *  been placed.
 *
 * An implementation of this interface must contain the following functionality:
 *  - Get all possible GameStates from the current GameState
 *  - Make a move and advance to the next GameState
 *  - Undo the previous move and return to the previous GameState
 *  - Get all previous moves to reach the current GameState
 *  - Get the current GameState
 *  - Apply a given function to all reachable GameStates from a given GameState
 *
 */
public interface GameTree {

  /**
   * Returns a copy of all possible gamestates reachable from the current state.
   * The copy only contains states reachable at the current player's turn.
   * @return a list of possible states
   */
  Map<Move, GameState> getPossibleGameStates();

  /**
   * Returns the child node stemming from this current node after making the given action,
   * which is a move from the start coord to the destination coord by the current player.
   * @param move the move to make on the current game tree
   * @return new GameTree after making the given move
   * @throws IllegalArgumentException if the move is illegal
   */
  GameTree getNextGameTree (Move move);

  /**
   * Undo the previous move of this gametree.
   *
   * @return the previous gametree.
   */
  GameTree undoPreviousMove();

  /**
   * Get all previous moves to reach the current state
   *
   * @return the list of all moves taken
   */
  List<MoveState> getPreviousMoves();

  /**
   * Get the current state
   *
   * @return the current state of this GameTree
   */
  GameState getState();

}
