package com.fish.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.fish.game.GameTree;
import com.fish.game.HexGameTree;
import com.fish.game.IFunc;
import com.fish.game.Move;
import com.fish.game.MoveState;
import com.fish.model.Coord;
import com.fish.model.state.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class XTree {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    JsonArray xJsonInput = XJson.processInput(scan);

    JsonObject mrq = xJsonInput.get(0).getAsJsonObject();
    JsonObject stateAsJson = mrq.getAsJsonObject("state");
    GameState gs = XState.jsonToGameState(stateAsJson);

    JsonArray fromJson = mrq.getAsJsonArray("from");
    JsonArray toJson = mrq.getAsJsonArray("to");

    Coord from = jsonArrayToCoord(fromJson);
    Coord to = jsonArrayToCoord(toJson);

    List<Coord> neighbors = XState.getDirectionalTiles(to);

    gs.movePenguin(from, to);

    GameTree tree = new HexGameTree(gs);

    Map<Move, GameState> moves = tree.getPossibleGameStates();

    /*
    Each move is (start, end)
     - make sure end is in neighbors
       - following the n/ne/... order
       - List of all Moves that satisfy the condition
       - go through and find which start is the first penguin in order
     */

    Coord destination = findDestination(neighbors, moves.keySet());

    if (destination == null) {
      System.out.println("False");
    }

    //List<Move> movesToDestination = findValidMoves(destination, moves.keySet());

    IFunc<List<Move>> moveFinder = (GameTree gt, List<Move> vals) -> {
      List<MoveState> ms = gt.getPreviousMoves();
      Move move = ms.get(ms.size() - 1).getMove();

      if (move.getEnd().equals(destination)) {
        vals.add(move);
      }
      return vals;
    };

    List<Move> movesToDestination = HexGameTree.applyToAllReachableStates(tree, moveFinder, new ArrayList<>());

    Move finalMove = findEarliestPenguinMove(movesToDestination, stateAsJson);

  }

  /**
   * Turn the given 2-element JsonArray into a coord in our coord representation.
   *
   * @param loc JsonArray in the following format: [INT, INT]
   * @return the coord derived from the input
   */
  static Coord jsonArrayToCoord(JsonArray loc) {
    return new Coord(loc.get(1).getAsInt(), loc.get(0).getAsInt());
  }


  static Coord findDestination(List<Coord> neighbors, Set<Move> moves) {
    for (Coord cc : neighbors) {
      for (Move move : moves) {
        if (move.getEnd().equals(cc)) {
          return cc;
        }
      }
    }
    return null;
  }

  static List<Move> findValidMoves(Coord destination, Set<Move> moves) {
    List<Move> validMoves = new ArrayList<>();
    for (Move move : moves) {
      if (destination == move.getEnd()) {
        validMoves.add(move);
      }
    }

    return validMoves;
  }

  // assume there are at least two players, because otherwise the game should be over
  static Move findEarliestPenguinMove(List<Move> movesToDestination, JsonObject stateAsJson) {
    // in the JsonObject, the current player is still the second player
    if (movesToDestination.size() == 1) {
      return movesToDestination.get(0);
    }

    JsonArray playerArray = stateAsJson.getAsJsonArray("players");

    JsonObject player = playerArray.get(1).getAsJsonObject();
    JsonArray places = player.getAsJsonArray("places");

    List<Coord> penguinLocations = new ArrayList<>();

    // TODO: fix after player refactor
    return null;


  }
}


