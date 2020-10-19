package com.fish.game;

import com.fish.model.Coord;
import com.fish.model.state.GameState;
import java.util.List;

/**
 *
 */
public interface GameTree {

  /**
   * Returns a copy of all possible gamestates reachable from the current state.
   * The copy only contains states reachable at the current player's turn.
   * @return a list of possible states
   */
  List<GameState> getPossibleGameStates();

  /**
   * Returns one GameState that is the retult of applying a move from start to dest on
   * the given gamestate.
   * @param gs the given gamestate
   * @param start the starting coord
   * @param dest the destination coord
   * @return the resulting GameState
   */
  GameState getResultState(GameState gs, Coord start, Coord dest) throws IllegalArgumentException;

  /**
   * Returns the child node stemming from this current node after making the given action,
   * which is a move from the start coord to the destination coord by the current player.
   * @param start starting coordinate of the penguin to move
   * @param dest ending coordinate of the penguin to move
   * @return new GameTree after making the given move
   * @throws IllegalArgumentException if the move is illegal
   */
  GameTree getNextGameTree (Coord start, Coord dest);
}
