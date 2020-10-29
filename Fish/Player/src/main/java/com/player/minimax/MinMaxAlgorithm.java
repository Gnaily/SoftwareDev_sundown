package com.player.minimax;

import com.fish.game.*;
import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.PlayerColor;

import java.util.*;

/**
 * Implementation of a Function Object that operates on trees.
 *
 * Given a Player to maximize the score for and a number of levels to dive into the tree,
 *  this algorithm outputs the potential moves from the starting state and their minimax values.
 *
 * This algorithm functions by recursively calling itself and generating more nodes until the number
 *  of desired moves has been taken by the player (or the given player's turn has been skipped that
 *  many times if they do not have any moves). Once that has occurred, it evalueates each node as
 *  follows:
 *   - if this is representing the player running the algorithm's turn, take the move that
 *        results in the maximum number of points from all children states
 *   - if this is representing an opponent, take the move that results in the minimum number of
 *        points for the given player.
 *
 */
public class MinMaxAlgorithm implements IFunc<List<MoveValue>> {
  private final int maxMoves;
  private final PlayerColor startingColor;
  private final int numMoves;

  /**
   * Public constructor for this algorithm. Takes in the Player to maximize score for and the
   *  depth to search the tree.
   * @param maxMoves the depth to search this tree
   * @param startingColor the player to count moves for
   */
  public MinMaxAlgorithm(int maxMoves, PlayerColor startingColor){
    this.maxMoves = maxMoves;
    this.startingColor = startingColor;
    this.numMoves = 0;
  }

  private MinMaxAlgorithm(int numMoves, int maxMoves, PlayerColor startingColor) {
    this.maxMoves = maxMoves;
    this.startingColor = startingColor;
    this.numMoves = numMoves;
  }


  /**
   * Apply the MiniMax algorithm to the given GameTree. The algorithm terminates either when the
   *  GameTree has no child nodes or the desired number of moves has been reached. Desired moves are
   *  tracked by the number of times the tracked player makes a move or is skipped.
   *
   * @param gameTree the given GameTree to apply this minimax algorithm to
   * @param scores The List of Moves (paired with their associated values) for this level of the tree
   * @return The input list with the addition of this node's move/value pair if there is one.
   */
  @Override
  public List<MoveValue> apply(GameTree gameTree, List<MoveValue> scores) {
    // this avoids a data race where multiple versions of this object are modifying this.numMoves
    int numberMoves = this.numMoves;
    if (gameTree.getState().getCurrentPlayer().equals(this.startingColor) || skippedPlayer(gameTree)) {
      numberMoves += 1;
    }

    if (numberMoves >= this.maxMoves && gameTree.getPreviousMoves().size() != 0) {
      return this.addFinalMove(gameTree, scores);
    }

    return this.addMoveMiddleOfTree(gameTree, numberMoves, scores);

  }

  /**
   * Find the ideal move for the current node. This functions by adding a single value to the
   * list passed in. Parent nodes use the result of this function to calculate their decision.
   *
   * @param gameTree The current game tree
   * @param numberMoves The number of Moves that have already been taken by the tracked player in
   *                    this tree
   * @param scores the other valid moves the append this function call's results to
   * @return An updated list of MoveValues with the move that reached this node and it's corresponding
   *  value.
   */
  List<MoveValue> addMoveMiddleOfTree(GameTree gameTree, int numberMoves, List<MoveValue> scores) {
    List<MoveValue> nextScores =
        HexGameTree.applyToAllReachableStates(gameTree,
            new MinMaxAlgorithm(numberMoves, this.maxMoves, this.startingColor), new ArrayList<>());


    int val;
    if (nextScores.size() == 0) {
      val = gameTree.getState().getScoreBoard().get(this.startingColor);
    }
    else {
      val = bestValue(nextScores, gameTree.getState().getCurrentPlayer().equals(this.startingColor));
    }

    //Move previousMove = gameTree.getPreviousMoves().get(0).getMove();
    Move previousMove = gameTree.getPreviousMoves().get(gameTree.getPreviousMoves().size() - 1).getMove();
    scores.add(new MoveValue(previousMove, val));
    return scores;
  }

  /**
   * Return the final move in this search. This function can be achieved by always taking the move that
   *  results in the maximum number of fish for the tracked player. This works because:
   *  - if it is the player's turn, they would like to get the most points
   *  - if it is not the player's turn, that means the player has been skipped and their point value
   *   will remain constant for the rest of the game.
   * @param gameTree the game tree to find the final move for
   * @param scores the other moves calculated at this level of the tree
   * @return the input list with the current node's value added to it
   */
  List<MoveValue> addFinalMove(GameTree gameTree, List<MoveValue> scores) {
    if (gameTree.getPossibleGameStates().size() == 0) {
      return scores;
    }

    Move m = findBestMove(gameTree.getState());

    GameTree gt = gameTree.getNextGameTree(m);
    int score = gt.getState().getScoreBoard().get(this.startingColor);
    scores.add(new MoveValue(m, score));
    return scores;
  }


  /**
   * Helper for finding the base case to our algorithm. Our implementation of
   *  fold in our GameTree requires calculating the base case by ourselves.
   *
   * @param gs gamestate to find the best move for
   * @return the best move for the current player at the current gamestate
   */
  static Move findBestMove(GameState gs) {
    GameTree gt = new HexGameTree(gs);
    List<MoveValue> moveValues = new ArrayList<>();
    for (Move m: gt.getPossibleGameStates().keySet()) {
      GameState next = gt.getNextGameTree(m).getState();
      int score = next.getScoreBoard().get(gt.getState().getCurrentPlayer());
      moveValues.add(new MoveValue(m, score));
    }
    return MinMaxAlgorithm.calculateBestMove(moveValues).get(0).getMove();
  }

  /**
   * Find the maximum or minimum score from the input list of Move/value pairings
   *
   * @param moves the list of move/value pairs to iterate over
   * @param isMax whether the function should search for the minimum or maximum value
   * @return the min or max in this list of moves.
   */
  static int bestValue(List<MoveValue> moves, boolean isMax) {

    int value = moves.get(0).getValue();
    for (MoveValue mv : moves) {
      if (isMax && mv.getValue() > value) {
        value = mv.getValue();
      }
      else if (!isMax && mv.getValue() < value) {
        value = mv.getValue();
      }
    }

    return value;
  }

  /**
   * Find the best possible move from the input list. Best move is determined as follows:
   *  - moves with higher values associated with them are considered better
   *  - if there is a tie, the move with the lowest Y start location
   *  - if there is still a tie, the move with the lowest X start location
   *  - if there is still a tie, the move with the lowest Y destination
   *  - if there is still a tie, the move with the lowest X destination
   *
   * @param possibilities The list of moves to find the best move out of
   * @return the best move as described above
   */
  static List<MoveValue> calculateBestMove(List<MoveValue> possibilities) {
    int max = bestValue(possibilities, true);
    List<Move> maxPointMoves = new ArrayList<>();
    for (MoveValue mv : possibilities) {
      if (mv.getValue() == max) {
        maxPointMoves.add(mv.getMove());
      }
    }

    MoveValue best = null;
    Move bestMove = findLowestRowColMove(maxPointMoves);
    for (MoveValue mv : possibilities) {
      if (mv.getMove().equals(bestMove)) {
        best = mv;
        break;
      }
    }

    List<MoveValue> finalValue = new ArrayList<>();
    finalValue.add(best);
    return finalValue;
  }

  /**
   * Find the move with the lowest row/column value. Precednece is as follows:
   *  start row < start column < end row < end column.
   *
   * @param moves The moves to search through
   * @return the move with the smallest row/column value of all moves
   */
  static Move findLowestRowColMove(List<Move> moves) {
    List<Coord> starts = new ArrayList<>();

    for (Move m : moves) {
      starts.add(m.getOrigin());
    }

    Coord start = findLowestRowCol(starts);
    List<Coord> ends = new ArrayList<>();

    for (Move m : moves) {
      if (m.getOrigin().equals(start)) {
        ends.add(m.getDestination());
      }
    }

    Coord end = findLowestRowCol(ends);
    return new Move(start, end);
  }

  /**
   * Find the lowest Row/Column Coordinate out of the given list. Row takes precedence over column,
   * ie a coord with a low row but high column is considered lower than a coord with a high row
   * and low column. This function should only be called when there is at least one coordinate
   * int the list.
   *
   * @param locs Coordinates to search for the lowest value in.
   * @return the lowest coordinate in the list.
   */
  static Coord findLowestRowCol(List<Coord> locs) {
    Coord val = locs.get(0);

    for (int ii = 1; ii < locs.size(); ii++) {
      Coord potential = locs.get(ii);
      if (potential.getY() < val.getY()) {
        val = potential;
      } else if (potential.getY() == val.getY()) {
        if (potential.getX() < val.getX()) {
          val = potential;
        }
      }
    }
    return val;
  }

  /**
   * Determine if the tracked player has been skipped by the previous move. This is done through
   * calculating the current index of the player in the turn order and comparing it to the index of
   * the previous turn.
   *
   * If the previousIndex is 0, that means the player just made a move and was not skipped. However,
   * if a move was made and the tracked player's index stayed the same or increased, that means they
   * were skipped.
   *
   * @param gt the current game tree
   * @return if the tracked player was skipped by the most recent move.
   */
  boolean skippedPlayer(GameTree gt) {
    List<MoveState> previousMoves = gt.getPreviousMoves();
    if (previousMoves.size() == 0) {
      return false;
    }

    GameState currentState = gt.getState();
    GameState previousState = previousMoves.get(previousMoves.size() - 1).getGameState();

    int currentIndex = this.getIndex(currentState);
    int previousIndex = this.getIndex(previousState);


    return previousIndex != 0 && previousIndex <= currentIndex;
  }

  // Find the index of the player being tracked in the given gamestate.
  private int getIndex(GameState gs) {
    for (int ii = 0; ii < gs.getPlayers().size(); ii++) {
      if (gs.getPlayers().get(ii).getColor().equals(this.startingColor)) {
        return ii;
      }
    }
    return 0;
  }
}
