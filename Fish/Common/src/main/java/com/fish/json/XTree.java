package com.fish.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.fish.game.GameTree;
import com.fish.game.HexGameTree;
import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.state.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class XTree {

  /**
   * Main function for running the XTree integration tests.
   *    1. Consumes valid JSON from Standard
   *    2. parses that into a move-response-query (mrq)
   *    3. applies the move indicated in the mrq to the given state indicated in the mrq
   *    4. utilizes GameTree query functionality to determine the move for the next player according
   *        to the algorithm given in the assignment
   *    5. returns the resulting Move as an Action in JSON
   *
   * @param args Command line arguments, not used
   */
  public static void main(String[] args) {
    //grab the input from STD in
    Scanner scan = new Scanner(System.in);
    //create a JasonArray Java object whose elements are each JSON object from STD in
    JsonArray inputArray = XJson.processInput(scan);
    //Grab the first (and only) JSON object, which is the move-response-query represented in JSON
    JsonObject mrq = inputArray.get(0).getAsJsonObject();

    //---Process the mrq and apply the move to the given GameState---//
    GameState gameState = mrqToGameState(mrq);

    //It is now the second player's turn.
    //---Gather what's needed to calculate the desired outcome and send it to the processing method---//
    GameTree tree = new HexGameTree(gameState);
    Coord destOfFirstPlayer = XBoard.jsonToCoord(mrq.getAsJsonArray("to"));

    //---Call the main processing method---//
    List<Move> validMoves = calculateValidOutcomeMoves(tree, destOfFirstPlayer);

    //If there are no valid moves, return false, otherwise, return the tie breaking move
    if (validMoves.size() == 0) {
      System.out.println("false");
    }
    else if (validMoves.size() == 1) {
      JsonArray action = moveToJson(validMoves.get(0));
      System.out.println(action);
    } else {
      Move moveMade = determineTieBreaker(validMoves);
      JsonArray action = moveToJson(moveMade);
      System.out.println(action);
    }
  }

  /**
   * Converts a mrq to a GameState with the move from the mrq applied to the gamestate
   * Note that the current player's turn advances automatically when a turn is taken
   * @param mrq the move-response-query
   * @return the GameState with the move applied
   */
  static GameState mrqToGameState(JsonObject mrq) {
    JsonObject stateAsJson = mrq.getAsJsonObject("state");
    GameState returnState = XState.jsonToGameState(stateAsJson);

    JsonArray fromJson = mrq.getAsJsonArray("from");
    JsonArray toJson = mrq.getAsJsonArray("to");
    Coord from = XBoard.jsonToCoord(fromJson);
    Coord to = XBoard.jsonToCoord(toJson);

    returnState.movePenguin(from, to);

    return returnState;
  }

  /**
   * Given a GameTree to strategize with, and
   * the Coordinate of the most recent player's move's destination,
   * Returns a list of valid moves that the current player may make that place one of their penguins
   * at a neighboring Tile of their opponent who just took a turn.
   *
   * If multiple penguins can reach the same Tile, then the tiebreaker method is called.
   *
   * @param strategyTree the GameTree to perform the query function
   * @param opponentDestination the Tile the player's opponent moved to in the opponent's previous turn
   * @return
   */
  static List<Move> calculateValidOutcomeMoves(GameTree strategyTree, Coord opponentDestination) {

    List<Move> validMoves = new ArrayList<>();
    //Grab all the neighbors of the first player's penguin in order
    List<Coord> possibleDestinations = XState
        .getDirectionalTiles(opponentDestination); //list of TO moves

    //Grab all of the current player's penguin locs
    List<Coord> possibleOrigins = strategyTree.getState().getPlayers().get(0)
        .getPenguinLocs(); //list of FROM moves

    for (Coord destination : possibleDestinations) {
      for (Coord pengToMove : possibleOrigins) {
        try {
          HexGameTree.getResultState(strategyTree, new Move(pengToMove, destination));
          validMoves.add(new Move(pengToMove, destination));
        } catch (Exception e) {
          //do nothing, keep looking for the right move
        }
        if (validMoves.size() > 0) {
          //This will break the loop once we've reached the first reachable neighbor and finished collecting
          //all the possible moves the current player can make to that neighbor.
          //but it still may have multiple elements that can reach it from the player's penguins
          return validMoves;
        }
      }
    }
    //base case, returns an empty Arraylist if we get here
    return validMoves;
  }

  /**
   * Funnels a list of valid moves into THE final move based on the given tie breaker (in the assignment)
   * Determines a tie breaker by taking in all valid moves and returning the one that
   * satisfies the tie breaking rules in the assignment
   * @param validMoves all valid moves to a neighboring spot of the original penguin
   * @return the final move made
   */
  static Move determineTieBreaker(List<Move> validMoves) {
    //assign lowest the first element's Y value as the lowest row
    int lowestRow = validMoves.get(0).getOrigin().getY();
    for (Move m : validMoves) {
      if (m.getOrigin().getY() < lowestRow) {
        lowestRow = m.getOrigin().getY();
      }
    }
    for (Move m : validMoves) {
      if (m.getOrigin().getY() != lowestRow) {
        validMoves.remove(m);
      }
    }

    //assign the first element's X value as the lowest column
    int leftMostCol = validMoves.get(0).getOrigin().getX();
    for (Move m : validMoves) {
      if (m.getOrigin().getX() < leftMostCol) {
        leftMostCol = m.getOrigin().getX();
      }
    }
    for (Move m : validMoves) {
      if (m.getOrigin().getX() != leftMostCol) {
        validMoves.remove(m);
      }
    }
    return validMoves.get(0);
  }

  /**
   * Converts a Move object into its JSON representation called Action
   * @param move Move object
   * @return a JsonArray containing the action
   */
  public static JsonArray moveToJson(Move move) {
    JsonArray ret = new JsonArray();
    JsonArray start = new JsonArray();
    JsonArray end = new JsonArray();

    start.add(move.getOrigin().getY());
    start.add(move.getOrigin().getX());
    end.add(move.getDestination().getY());
    end.add(move.getDestination().getX());

    ret.add(start);
    ret.add(end);
    return ret;
  }

}


