package com.json;

import com.fish.game.GameTree;
import com.fish.game.HexGameTree;
import com.fish.game.Move;
import com.fish.json.XJson;
import com.fish.json.XState;
import com.fish.json.XTree;
import com.fish.model.state.GameState;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.player.minimax.MinimaxStrategy;
import java.util.Scanner;

public class XStrategy {

  /**
   * Main function for running the XStrategy integration tests.
   *
   * @param args Command line arguments, not used
   */
  public static void main(String[] args) {
    //grab the input from STD in
    Scanner scan = new Scanner(System.in);
    //create a JasonArray Java object whose elements are each JSON object from STD in
    JsonArray inputArray = XJson.processInput(scan);
    //Grab the depth-state json
    JsonArray ds = inputArray.get(0).getAsJsonArray();

    int depth = ds.get(0).getAsInt();
    JsonObject state = ds.get(1).getAsJsonObject();

    GameState inputGS = XState.jsonToGameState(state);

    GameTree gt = new HexGameTree(inputGS);
    if (gt.getPossibleGameStates().size() == 0) {
      System.out.println("false");
      return;
    }

    Move m = MinimaxStrategy.findCurrentPlayersBestMove(inputGS, depth);

    JsonArray output = XTree.moveToJson(m);

    System.out.println(output);
  }

}
