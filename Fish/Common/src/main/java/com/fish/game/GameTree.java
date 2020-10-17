package com.fish.game;

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
}
