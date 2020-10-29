package com.player.minimax;

import com.fish.game.HexGameTree;
import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.PlayerColor;
import com.fish.model.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MinimaxStrategy {


  public static Coord findNextPenguinPlacement(GameState gs) {
    Map<Coord, PlayerColor> penguins = gs.getPenguinLocations();
    for (int ii = 0; ii < gs.getHeight(); ii++) {
      for (int jj = 0; jj < gs.getWidth(); jj++) {
        // check: if there is a penguin at the location
        // if it is a legal place to put a penguin

        Coord loc = new Coord(jj, ii);
        if (penguins.get(loc) != null) {
          continue;
        }
        Tile tile = gs.getTileAt(loc);
        if (tile.isPresent() && tile.getNumFish() == 1) {
          return loc;
        }
      }
    }

    // return value is needed for compilation, however this line should never be reached
    return null;
  }

  public static Move findCurrentPlayersBestMove(GameState gs, int nn) {
    if (nn <= 1) {
      return MinMaxAlgorithm.findBestMove(gs);
    }
    MinMaxAlgorithm algo = new MinMaxAlgorithm(nn - 1, gs.getCurrentPlayer());
    List<MoveValue> moveValues = HexGameTree.applyToAllReachableStates(
        new HexGameTree(gs),
        algo,
        new ArrayList<>());

    MoveValue bestMove = MinMaxAlgorithm.calculateBestMove(moveValues).get(0);

    return bestMove.getMove();
  }






//  public static Move findCurrentPlayersBestMove(GameState gs, int lookAhead) {
//
//    if (lookAhead <= 1) {
//      return findMoveForCurrentPosition(gs);
//    }
////    Map<Coord, PlayerColor> penguinLocations = gs.getPenguinLocations();
////    GameTree tree = new HexGameTree(gs);
////
////    Map<Move, GameState> moves = tree.getPossibleGameStates();
////    Map<Move, Integer> minimumScore = new HashMap<>();
////
////    for (Move move : moves.keySet()) {
////      minimumScore.put(move, getMinimumScore(lookAhead, move, gs));
////    }
//
//    GameTree gt = new HexGameTree(gs);
//    Map<Move, GameState> moves = gt.getPossibleGameStates();
//
//
//    Map<Move, Integer> minimumScore = getMinimumScores(moves, lookAhead - 1);
//    Move best = null;
//    int max = 0;
//    for (Move m : minimumScore.keySet()) {
//      if (minimumScore.get(m) > max) {
//        best = m;
//        max = minimumScore.get(m);
//      }
//    }
//
//    // this needs to be changed so that it does the same penguin searching stuff
//    return best;
//  }
//
//  // TEST
//  static Move findMoveForCurrentPosition(GameState gs) {
//    PlayerColor pc = gs.getCurrentPlayer();
//    List<Coord> pLocs = new ArrayList<>();
//    Map<Coord, PlayerColor> allPenguins = gs.getPenguinLocations();
//    for (Coord cc : allPenguins.keySet()) {
//      if (allPenguins.get(cc) == pc) {
//        pLocs.add(cc);
//      }
//    }
//
//
//    Coord pengiunToMove = findLowestRowCol(pLocs);
//    List<Coord> reachable = gs.getTilesReachableFrom(pengiunToMove);
//
//    Coord destination = findLowestRowCol(reachable);
//
//    return new Move(pengiunToMove, destination);
//  }
//
//
//  //TEST
//  static Coord findLowestRowCol(List<Coord> locs) {
//    Coord val = locs.get(0);
//
//    for (int ii = 1; ii < locs.size(); ii++) {
//      Coord potential = locs.get(ii);
//      if (potential.getY() < val.getY()) {
//        val = potential;
//      }
//      else if (potential.getY() == val.getY()) {
//        if (potential.getX() < val.getX()) {
//          val = potential;
//        }
//      }
//    }
//
//
//    return val;
//  }
//
//  static Map<Move, Integer> getMinimumScores(Map<Move, GameState> moves, int lookAhead) {
//    Map<Move, Integer> moveIntegerMap = new HashMap<>();
//
//    while ()
//
//
//    return moveIntegerMap;
//  }
//
//  static int getMinimumScore(int lookAhead, Move move, GameState gs) {
//    List<InternalPlayer> playersAtBeginning = gs.getPlayers();
//
//    List<PlayerColor> playerColorOrder = new ArrayList<>();
//    for (InternalPlayer p : playersAtBeginning) {
//      playerColorOrder.add(p.getColor());
//    }
//
//    int ii = 0;
//    while (ii < lookAhead) {
//
//
//
//
//      ii++;
//    }
//
//    return 0;
//  }
}
