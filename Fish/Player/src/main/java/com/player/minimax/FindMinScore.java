package com.player.minimax;

import com.fish.game.*;
import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.InternalPlayer;
import com.fish.model.state.PlayerColor;

import java.util.*;

public class FindMinScore implements IFunc<Map<Move, Integer>> {
  private int maxMoves;
  private PlayerColor startingColor;
  private int numMoves;

  public FindMinScore(int maxMoves, PlayerColor startingColor){
    this.maxMoves = maxMoves;
    this.startingColor = startingColor;
    this.numMoves = 0;
  }

  public FindMinScore(FindMinScore fms) {
    this.maxMoves = fms.maxMoves;
    this.startingColor = fms.startingColor;
    this.numMoves = fms.numMoves;
  }



  @Override
  public Map<Move, Integer> apply(GameTree gameTree, Map<Move, Integer> scores) {

    if (gameTree.getState().getCurrentPlayer().equals(this.startingColor) || skippedPlayer(gameTree)) {
      this.numMoves += 1;
    }

    // search for max here (need to add)
    if (this.numMoves >= this.maxMoves) {
      Move m = findMoveForCurrentPosition(gameTree.getState());
      GameTree gt = gameTree.getNextGameTree(m);
      int score = gt.getState().getScoreBoard().get(this.startingColor);
      scores.put(m, score);
      return scores;
    }

    // player's turn
    else if (gameTree.getState().getCurrentPlayer().equals(this.startingColor)) {
      Map<Move, Integer> moves = HexGameTree.applyToAllReachableStates(gameTree,
          new FindMinScore(this), new HashMap<>());

      int max = 0;
      for (Move m : moves.keySet()) {
        if (moves.get(m) > max) {
          max = moves.get(m);
        }
      }

      List<Move> maxScoreMoves = new ArrayList<>();

      for (Move m : moves.keySet()) {
        if (moves.get(m) == max) {
          maxScoreMoves.add(m);
        }
      }

      Move optimal = findOptimalMove(maxScoreMoves);
      //Map<Move, Integer> bestMove = new HashMap<>();
      //bestMove.put(optimal, moves.get(optimal));
      scores.put(optimal, moves.get(optimal));
      return scores;
    }

    // someone else's turn
    else {
      Map<Move, Integer> moves = HexGameTree.applyToAllReachableStates(gameTree,
          new FindMinScore(this), new HashMap<>());

      // change
      int min = 9999;
      for (Move m : moves.keySet()) {
        if (moves.get(m) < min) {
          min = moves.get(m);
        }
      }

      List<Move> minScoreMoves = new ArrayList<>();

      for (Move m : moves.keySet()) {
        if (moves.get(m) == min) {
          minScoreMoves.add(m);
        }
      }

      Move optimal = findOptimalMove(minScoreMoves);
      scores.put(optimal, moves.get(optimal));
      return scores;
    }
  }

  static Move findOptimalMove(List<Move> moves) {
    List<Coord> starts = new ArrayList<>();

    for (Move m : moves) {
      starts.add(m.getStart());
    }

    Coord start = findLowestRowCol(starts);
    List<Coord> ends = new ArrayList<>();

    for (Move m : moves) {
      if (m.getStart().equals(start)) {
        ends.add(m.getEnd());
      }
    }

    Coord end = findLowestRowCol(ends);
    return new Move(start, end);
  }

  static Move findMoveForCurrentPosition(GameState gs) {
    PlayerColor pc = gs.getCurrentPlayer();
    List<Coord> pLocs = new ArrayList<>();
    Map<Coord, PlayerColor> allPenguins = gs.getPenguinLocations();
    for (Coord cc : allPenguins.keySet()) {
      if (allPenguins.get(cc) == pc) {
        pLocs.add(cc);
      }
    }


    Coord pengiunToMove = findLowestRowCol(pLocs);
    List<Coord> reachable = gs.getTilesReachableFrom(pengiunToMove);

    Coord destination = findLowestRowCol(reachable);

    return new Move(pengiunToMove, destination);
  }


  //TEST
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


  boolean skippedPlayer(GameTree gt) {
    List<MoveState> previousMoves = gt.getPreviousMoves();
    if (previousMoves.size() == 0) {
      return false;
    }

    // tracking color BROWN
    // RED, WHITE, BROWN, BLACK
    // red makes move
    // WHITE, BROWN, BLACK, RED
    // white makes a move, brown dosent have moves left
    // BLACK, RED, WHITE, BROWN
    GameState currentState = gt.getState();
    GameState previousState = previousMoves.get(previousMoves.size() - 1).getGameState();

    int currentIndex = 0;
    int previousIndex = 0;

    for (int ii = 0; ii < currentState.getPlayers().size(); ii++) {
      if (currentState.getPlayers().get(ii).getColor().equals(this.startingColor)) {
        currentIndex = ii;
      }
    }

    for (int ii = 0; ii < previousState.getPlayers().size(); ii++) {
      if (previousState.getPlayers().get(ii).getColor().equals(this.startingColor)) {
        previousIndex = ii;
      }
    }

    //System.out.println(previousIndex != 0 && previousIndex <= currentIndex);
    return previousIndex != 0 && previousIndex >= currentIndex;
  }
}
