package com.fish.game;

import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.PlayerColor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An Implementation of the GameTree interface
 * Holds the following information:
 *  - GameState currentState: The current node in the tree while traversing it
 *  - List<MoveState> history: All previous states and the moves made from them
 *  - Map<Move, GameState>: All potential gamestates stemming from the current state paired with
 *      the move required to reach that state.
 */
public class HexGameTree implements GameTree {

  private GameState currentState;
  private List<MoveState> history;
  private Map<Move, GameState> possibleMoves;

  public HexGameTree(GameState gs, List<MoveState> moves) {
    this.currentState = gs;
    this.history = moves;

    this.possibleMoves = this.generatePossibleGameStates();
  }

  public HexGameTree(GameState gs) {
    this(gs, new ArrayList<>());
  }

  private Map<Move, GameState> generatePossibleGameStates() {
    Map<Move, GameState> posStates = new HashMap<>();
    PlayerColor curPlayer = this.currentState.getCurrentPlayer();
    Map<Coord, PlayerColor> pengLocs = this.currentState.getPenguinLocations();

    //For all the penguins on the board in the color of the current player:
    for (Coord start : pengLocs.keySet()) {
      if (curPlayer == pengLocs.get(start)) {
        //Generate all the tiles reachable for that player and build a list of all pos resulting states
        for (Coord dest : this.currentState.getTilesReachableFrom(start)){
          GameState next = this.currentState.getCopyGameState();
          next.movePenguin(start, dest);
          posStates.put(new Move(start, dest), next);
        }
      }
    }
    return posStates;
  }

  /**
   * Returns a copy of all possible gamestates reachable from the current state. The copy only
   * contains states reachable at the current player's turn.
   *
   * @return a list of possible states
   */
  @Override
  public Map<Move, GameState> getPossibleGameStates() {
    return new HashMap<>(this.possibleMoves);
  }

  /**
   * Returns the child node stemming from this current node after making the given action, which is
   * a move from the start coord to the destination coord by the current player.
   *
   * @param move the move to make on the current game tree
   * @return new GameTree after making the given move
   * @throws IllegalArgumentException if the move is illegal
   */
  @Override
  public GameTree getNextGameTree(Move move) {
    GameState copy = getResultState(this.currentState, move);
    List<MoveState> newMoves = new ArrayList<>(this.history);

    newMoves.add(new MoveState(move, this.currentState.getCopyGameState()));
    return new HexGameTree(copy, newMoves);
  }

  /**
   * Undo the previous move of this gametree.
   *
   * @return the previous gametree.
   */
  @Override
  public GameTree undoPreviousMove() {
    List<MoveState> newMoves = new ArrayList<>(this.history);

    MoveState ms = newMoves.remove(newMoves.size() - 1);

    return new HexGameTree(ms.getGameState(), newMoves);
  }

  /**
   * Get all previous moves to reach the current state
   *
   * @return the list of all moves taken
   */
  @Override
  public List<MoveState> getPreviousMoves() {
    return new ArrayList<>(this.history);
  }

  /**
   * Get the current state
   *
   * @return the current state of this GameTree
   */
  @Override
  public GameState getState() {
    return this.currentState.getCopyGameState();
  }

  //Note:
  // The following methods are static because they do not need to belong to the interface -
  //  they do not define functionality any tree should have, but are useful methods
  //  for use outside of a tree.

  /**
   * A query facility that returns one GameState that is the result of applying a move from
   * start to dest on the given gamestate.
   *
   * @param gs   the given gamestate
   * @param move The move to make on the input game state
   * @return the resulting GameState
   */
  public static GameState getResultState(GameState gs, Move move) throws IllegalArgumentException {
    GameState copy = gs.getCopyGameState();
    copy.movePenguin(move.getStart(), move.getEnd());
    return copy;
  }

  /**
   * Apply the given function to all reachable states from the given gameState. Requires an initial
   * value.
   *
   * @param gameState What state to investigate
   * @param function  the function to apply
   * @param value     The base case
   * @return Whatever the function is written to return
   */
  public static <T> T applyToAllReachableStates(GameState gameState, IFunc<T> function, T value) {

    T returnVal = value;
    GameTree gt = new HexGameTree(gameState);
    Map<Move, GameState> moveGameStateMap = gt.getPossibleGameStates();
    for (Move move : moveGameStateMap.keySet()) {
      returnVal = function.apply(moveGameStateMap.get(move), returnVal);
    }

    return returnVal;
  }



//  //private GameTree parent;
//
//  private GameState currentGameState;
//  private List<GameState> possibleGameStates;
//
//  public HexGameTree(GameTree parent, GameState currentGameState) {
//    this.parent = parent;
//    this.currentGameState = currentGameState;
//    this.possibleGameStates = generatePossibleGameStates();
//  }
//
//  //Generates all possible game states that stem from this current state
//  //and stores them in the possibleGameStates field.

//
//  /**
//   * Returns a copy of all possible gamestates reachable from the current state.
//   * The copy only contains states reachable at the current player's turn.
//   * @return a list of possible states
//   */
//  @Override
//  public List<GameState> getPossibleGameStates() {
//    return new ArrayList<>(this.possibleGameStates);
//  }
//
//  /**
//   * Returns one GameState that is the retult of applying a move from start to dest on
//   * the given gamestate.
//   * @param gs the given gamestate
//   * @param start the starting coord
//   * @param dest the destination coord
//   * @return the resulting GameState
//   * @throws IllegalArgumentException if the move is illegal
//   */
//  @Override
//  public GameState getResultState(GameState gs, Coord start, Coord dest) throws IllegalArgumentException{
//    GameState copy = gs.getCopyGameState();
//    //May throw illegal arg exceptions if the move is invalid
//    copy.movePenguin(start, dest);
//
//    return copy;
//  }
//
//  /**
//   * Returns the child node stemming from this current node after making the given action,
//   * which is a move from the start coord to the destination coord by the current player.
//   * @param start starting coordinate of the penguin to move
//   * @param dest ending coordinate of the penguin to move
//   * @return new GameTree after making the given move
//   * @throws IllegalArgumentException if the move is illegal
//   */
//  @Override
//  public GameTree getNextGameTree (Coord start, Coord dest) throws IllegalArgumentException {
//    GameState resultState = getResultState(this.currentGameState, start, dest);
//    return new HexGameTree(this, resultState);
//  }


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

