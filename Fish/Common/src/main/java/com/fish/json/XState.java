package com.fish.json;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.model.state.GameStage;
import com.fish.model.state.GameState;
import com.fish.model.state.HexGameState;
import com.fish.model.state.Player;
import com.fish.model.state.PlayerColor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class XState {

  /**
   * Main function for running the XState integration tests.
   *    1. Consumes valid JSON from Standard
   *    2. parses that into a HexGameState
   *    3. makes a move on the board and then
   *    4. returns the resulting state as JSON
   *
   * @param args Command line arguments, not used
   */
  public static void main(String[] args) {
    //grab the input from STD in
    Scanner scan = new Scanner(System.in);

    //create a JasonArray Java object whose elements are each JSON object from STD in
    JsonArray xJsonInput = XJson.processInput(scan);
    //Grab the first (and only) JSON object, which is the State represented in JSON
    JsonObject stateAsJson = xJsonInput.get(0).getAsJsonObject();

    //////HANDLE "board" INPUT
    //turn that into an actual 2D array board representation in Java
    int[][] valuesFromInput = XBoard.getTileValues(stateAsJson, "board");
    //Create the GameBoard object with the values from the JSON input
    GameBoard boardFromInput = new HexGameBoard(valuesFromInput);

    //////HANDLE "players" INPUT
    JsonArray playerArray = stateAsJson.getAsJsonArray("players");
    List<Player> players = getPlayersList(playerArray);

    //Generate the gameState during the precise snapshot given in the JSON input
    GameState gsFromInput = new HexGameState(GameStage.IN_PLAY, boardFromInput, players, 0,
        placePenguins(playerArray));

    //Make the move, OR determine that NO move can be made:
    Coord penguinStart = getFirstPlayersFirstPenguin(playerArray);
    List<Coord> tilesReachable = gsFromInput.getTilesReachableFrom(penguinStart);
    List<Coord> directionalTiles = getDirectionalTiles(penguinStart);
    Coord moveMade = null;
    for (Coord possibleMove : directionalTiles) {
      if (tilesReachable.contains(possibleMove)){
        gsFromInput.movePenguin(penguinStart, possibleMove);
        moveMade = possibleMove;
        break;
      }
    }

    //IF no move has been made, return FALSE. Else, return the new State as represented in JSON
    if (moveMade == null) {
      System.out.println("False");
    }
    else{
      JsonObject newState = adjustStateAfterMove(stateAsJson, gsFromInput, penguinStart, moveMade);
      System.out.println(newState);
    }
  }

  //////////////////////HELPERS
  //Useful helper method to generate the data representation of a given color represented as a String
  private static PlayerColor getPlayerColor(String color) {
    switch (color) {
      case "black":
        return PlayerColor.BLACK;
      case "brown":
        return PlayerColor.BROWN;
      case "red":
        return PlayerColor.RED;
      default:
        return PlayerColor.WHITE;
    }
  }

  //Returns the list of Player JSON representations
  private static List<Player> getPlayersList (JsonArray playerArray) {
    List<Player> resultPlayerList = new ArrayList<>();
    //Build the list of players to initiate a gameState with
    for (JsonElement playerElem : playerArray) {
      JsonObject playerObj = playerElem.getAsJsonObject();

      String color = playerObj.getAsJsonPrimitive("color").getAsString();
      PlayerColor pc = getPlayerColor(color);

      int score = playerObj.getAsJsonPrimitive("score").getAsInt();
      Player p = new Player(1, pc);
      p.addToScore(score);
      resultPlayerList.add(p);
    }
    return resultPlayerList;
  }

  //Returns the penguinLocs hashmap of all the players' penguins locs from the input
  private static Map<Coord, PlayerColor> placePenguins(JsonArray playerArray) {
    Map<Coord, PlayerColor> penguinLocs = new HashMap<>();

    for (JsonElement playerElem : playerArray) {
      JsonObject playerObj = playerElem.getAsJsonObject();
      int[][] penguinLocsAsArray = XBoard.getTileValues(playerObj, "places");

      String color = playerObj.getAsJsonPrimitive("color").getAsString();
      PlayerColor pc = getPlayerColor(color);

      for (int[] posn : penguinLocsAsArray) {
        penguinLocs.put(new Coord(posn[0], posn[1]), pc);
      }
    }
    return penguinLocs;
  }

  //Ensures that we are locating the first player's first penguin from the JSON input
  private static Coord getFirstPlayersFirstPenguin(JsonArray playerArray) {
    JsonObject firstPlayerObj = playerArray.get(0).getAsJsonObject();
    JsonArray firstPlayersPenguinsLocs = firstPlayerObj.getAsJsonArray("places");
    JsonArray firstPenguinLoc = firstPlayersPenguinsLocs.get(0).getAsJsonArray();
    return new Coord(firstPenguinLoc.get(0).getAsInt(), firstPenguinLoc.get(1).getAsInt());
  }

  //Get a list of all the potential tiles from the Tile of origin. They could result in negative
  //or out of bounds Coords. This is good because those results should return invalid exception
  //messages when sent to the GameState.
  private static List<Coord> getDirectionalTiles(Coord penguinStart) {
    int xx = penguinStart.getX();
    int yy = penguinStart.getY();

    Coord north = new Coord(xx, yy - 2);

    Coord ne = new Coord(xx + yy % 2, yy - 1);
    Coord se = new Coord(xx + yy % 2, yy + 1);

    Coord south = new Coord(xx, yy + 2);

    Coord sw = new Coord(xx - (yy + 1) % 2, yy + 1);
    Coord nw = new Coord(xx - (yy + 1) % 2, yy - 1);

    return Arrays.asList(north, ne, se, south, sw, nw);
  }

  //Bring together all of the information from the JSON input and APPLY it to the GameState as JSON.
  private static JsonObject adjustStateAfterMove(JsonObject stateAsJson, GameState startState,
      Coord penguinStart, Coord moveMade) {

    //Adjust board represented in stateAsJson -- change tile to hole
    JsonArray boardArray = stateAsJson.getAsJsonArray("board");
    int xx = penguinStart.getX();
    int yy = penguinStart.getY();
    JsonArray row = boardArray.get(yy).getAsJsonArray();
    //Set the location of the board in the Json representation to zero to represent a hole
    row.set(xx, new JsonPrimitive(0));

    //Adjust player score as represented in stateAsJson -- pull updated score from GameState
    JsonArray firstPlayerArray = stateAsJson.getAsJsonArray("players");
    JsonObject firstPlayer = firstPlayerArray.get(0).getAsJsonObject();
    JsonElement oldScore = firstPlayer.remove("score");
    //At this point, the move has been made, so the playerColor can be found stored in the penguin
    //locs at the destination Tile, which is moveMade.
    PlayerColor firstPlayerColor = startState.getPenguinLocations().get(moveMade);
    //This pulls the updated score directly from the GameState to ensure our score keeping is accurate
    int newScore = startState.getPlayerScore(firstPlayerColor);
    firstPlayer.addProperty("score", newScore);

    //Adjust player places as represented in stateAsJson -- move penguin location
    JsonArray originalLocs = firstPlayer.remove("places").getAsJsonArray();
    JsonArray newLoc = new JsonArray();
    newLoc.add(moveMade.getX());
    newLoc.add(moveMade.getY());
    originalLocs.set(0, newLoc);
    firstPlayer.add("places", originalLocs);

    return stateAsJson;
  }

}