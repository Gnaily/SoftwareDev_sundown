package com.fish.game;

import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.PlayerColor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class HexGameTree implements GameTree {

  private GameTree parent;
  private GameState currentGameState;
  private List<GameState> possibleGameStates;

  public HexGameTree(GameTree parent, GameState currentGameState) {
    this.parent = parent;
    this.currentGameState = currentGameState;
    this.possibleGameStates = generatePossibleGameStates();
  }

  //Generates all possible game states that stem from this current state
  //and stores them in the possibleGameStates field.
  private List<GameState> generatePossibleGameStates() {
    List<GameState> posStates = new ArrayList<>();
    PlayerColor curPlayer = this.currentGameState.getCurrentPlayer();
    Map<Coord, PlayerColor> pengLocs = this.currentGameState.getPenguinLocations();

    //For all the penguins on the board in the color of the current player:
    for (Coord start : pengLocs.keySet()) {
      if (curPlayer == pengLocs.get(start)) {
        //Generate all the tiles reachable for that player and build a list of all pos resulting states
        for (Coord dest : this.currentGameState.getTilesReachableFrom(start)){
          GameState next = this.currentGameState.getCopyGameState();
          next.movePenguin(start, dest);
          posStates.add(next);
        }
      }
    }
    return posStates;
  }

  /**
   * Returns a copy of all possible gamestates reachable from the current state.
   * The copy only contains states reachable at the current player's turn.
   * @return a list of possible states
   */
  @Override
  public List<GameState> getPossibleGameStates() {
    return new ArrayList<>(this.possibleGameStates);
  }

  /**
   * Returns one GameState that is the retult of applying a move from start to dest on
   * the given gamestate.
   * @param gs the given gamestate
   * @param start the starting coord
   * @param dest the destination coord
   * @return the resulting GameState
   * @throws IllegalArgumentException if the move is illegal
   */
  @Override
  public GameState getResultState(GameState gs, Coord start, Coord dest) throws IllegalArgumentException{
    GameState copy = gs.getCopyGameState();
    //May throw illegal arg exceptions if the move is invalid
    copy.movePenguin(start, dest);

    return copy;
  }

  /**
   * Returns the child node stemming from this current node after making the given action,
   * which is a move from the start coord to the destination coord by the current player.
   * @param start starting coordinate of the penguin to move
   * @param dest ending coordinate of the penguin to move
   * @return new GameTree after making the given move
   * @throws IllegalArgumentException if the move is illegal
   */
  @Override
  public GameTree getNextGameTree (Coord start, Coord dest) throws IllegalArgumentException {
    GameState resultState = getResultState(this.currentGameState, start, dest);
    return new HexGameTree(this, resultState);
  }


//  @Override
//  public void applyFunctionToThisNode(Funtion f) {
//
//  }
//
//
//
//
//
//  /**
//   * Returns the parent node of this GameTree, which represents all moves taken in the game up to
//   * now. Altering the parent node does not actually alter the state.
//   * @return the parent node
//   */
//  @Override
//  public GameTree getParent() {
//    return this.parent;
//  }






}

