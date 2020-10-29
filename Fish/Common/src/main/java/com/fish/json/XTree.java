//package com.fish.json;
//
//import com.fish.model.state.InternalPlayer;
//import com.fish.model.state.PlayerColor;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//
//import com.fish.game.GameTree;
//import com.fish.game.HexGameTree;
//import com.fish.game.IFunc;
//import com.fish.game.Move;
//import com.fish.game.MoveState;
//import com.fish.model.Coord;
//import com.fish.model.state.GameState;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//import java.util.Set;
//
//public class XTree {
//
//  public static void main(String[] args) {
//    Scanner scan = new Scanner(System.in);
//
//    JsonArray xJsonInput = XJson.processInput(scan);
//
//    JsonObject mrq = xJsonInput.get(0).getAsJsonObject();
//
//    GameState gs = createStateFromJsonWithMove(mrq);
//
//    GameTree tree = new HexGameTree(gs);
//
//    Map<Move, GameState> moves = tree.getPossibleGameStates();
//    Coord to = jsonArrayToCoord(mrq.getAsJsonArray("to"));
//    List<Coord> neighbors = XState.getDirectionalTiles(to);
//
//    Coord destination = findDestination(neighbors, moves.keySet());
//
//    if (destination == null) {
//      System.out.println("false");
//      return;
//    }
//
//    //List<Move> movesToDestination = findValidMoves(destination, moves.keySet());
//    Move finalMove = getMoveToDestination(gs, tree, destination);
//
//
//
//    assert finalMove != null;
//    JsonArray moveJson = moveToJson(finalMove);
//    System.out.println(moveJson);
//  }
//
//  static GameState createStateFromJsonWithMove(JsonObject mrq) {
//    JsonObject stateAsJson = mrq.getAsJsonObject("state");
//    GameState gs = XState.jsonToGameState(stateAsJson);
//
//    JsonArray fromJson = mrq.getAsJsonArray("from");
//    JsonArray toJson = mrq.getAsJsonArray("to");
//
//    Coord from = jsonArrayToCoord(fromJson);
//    Coord to = jsonArrayToCoord(toJson);
//    gs.movePenguin(from, to);
//
//    return gs;
//
//  }
//
//  static Move getMoveToDestination(GameState gs, GameTree tree, Coord destination) {
//    IFunc<List<Move>> moveFinder = (GameTree gt, List<Move> vals) -> {
//      List<MoveState> ms = gt.getPreviousMoves();
//      Move move = ms.get(ms.size() - 1).getMove();
//
//      if (move.getEnd().equals(destination)) {
//        vals.add(move);
//      }
//      return vals;
//    };
//
//    List<Move> movesToDestination = HexGameTree.applyToAllReachableStates(tree, moveFinder, new ArrayList<>());
//
//    return findEarliestPenguinMove(movesToDestination, gs);
//  }
//
//  /**
//   * Turn the given 2-element JsonArray into a coord in our coord representation.
//   *
//   * @param loc JsonArray in the following format: [INT, INT]
//   * @return the coord derived from the input
//   */
//  static Coord jsonArrayToCoord(JsonArray loc) {
//    return new Coord(loc.get(1).getAsInt(), loc.get(0).getAsInt());
//  }
//
//
//  /**
//   * Compares the input list to the set of moves passed in. If any of the moves reach any of the
//   *  coordinates, return that coordinate.
//   *
//   * @param neighbors ordered list of destinations to search for
//   * @param moves all valid moves for the current player
//   * @return the first coordinate that has a move
//   */
//  static Coord findDestination(List<Coord> neighbors, Set<Move> moves) {
//    for (Coord cc : neighbors) {
//      for (Move move : moves) {
//        if (move.getEnd().equals(cc)) {
//          return cc;
//        }
//      }
//    }
//    return null;
//  }
//
//  // assume there are at least two players, because otherwise the game should be over
//  static Move findEarliestPenguinMove(List<Move> movesToDestination, GameState gs) {
//    if (movesToDestination.size() == 1) {
//      return movesToDestination.get(0);
//    }
//
//    PlayerColor pc = gs.getCurrentPlayer();
//    List<Coord> penguinLocations = new ArrayList<>();
//    for (InternalPlayer p : gs.getPlayers()) {
//      if (p.getColor() == pc) {
//        penguinLocations = p.getPenguinLocs();
//      }
//    }
//    //////////////--IMPLEMENT TIE BREAKER HERE --- ////
//    for (Coord c : penguinLocations) {
//      for (Move move : movesToDestination) {
//        if (move.getStart().equals(c)) {
//          return move;
//        }
//      }
//    }
//
//    return null;
//  }
//
//  static JsonArray moveToJson(Move move) {
//    JsonArray ret = new JsonArray();
//    JsonArray start = new JsonArray();
//    JsonArray end = new JsonArray();
//
//    start.add(move.getStart().getY());
//    start.add(move.getStart().getX());
//    end.add(move.getEnd().getY());
//    end.add(move.getEnd().getX());
//
//    ret.add(start);
//    ret.add(end);
//    return ret;
//  }
//}
//
//
