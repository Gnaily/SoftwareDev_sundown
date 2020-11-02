package com.fish.json;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.ProtectedGameBoard;
import com.fish.model.state.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    JsonArray inputArray = XJson.processInput(scan);
    //Grab the first (and only) JSON object, which is the State represented in JSON
    JsonObject stateAsJson = inputArray.get(0).getAsJsonObject();

    //---Create the GameState from the input---//
    GameState gameState = jsonToGameState(stateAsJson);

    //Try to apply the N/NE/SE... etc algorithm on the first player's first penguin:
    GameState outputGS = attemptDirectionalAlgoOnFirstPlayerFirstPenguin(gameState);

    //Determine whether the output GameState is the same or different from the original
    boolean changeMade = !gameState.equals(outputGS);

    if (changeMade) {
      //Construct JSON data back from the State
      JsonElement outputJson = reconstructStateToJson(outputGS);
      System.out.println(outputJson);
    }
    else {
      System.out.println("False");
    }
  }

  //----Convert JSON to internal data representations----//
  /**
   * Given a JsonObject of the State, turns that json directly into a gamestate.
   * Uses tools in the XBoard class to process the board.
   *
   * @param stateAsJson the state represented as Json values
   * @return a GameState of those values
   */
  public static GameState jsonToGameState(JsonObject stateAsJson) {

    //////HANDLE "board" INPUT - turn that into an actual board using XBoard class
    GameBoard boardFromInput = XBoard.jsonToGameBoard(stateAsJson);

    //////HANDLE "players" INPUT
    List<InternalPlayer> playersFromInput = jsonToPlayer(stateAsJson);

    //Generate the gameState during the precise snapshot given in the JSON input
    return new HexGameState(GameStage.IN_PLAY, boardFromInput, playersFromInput);

  }

  /**
   * Given a JsonObject of the State, pulls the Player data from that JsonObject and
   * constructs a list of Players in a game of HTMF.
   *
   * Keeps the order of Penguins in the JSON in order in the player object's penguin locs.
   *
   * @param stateAsJson the state represented as Json
   * @return a list of Players in the game
   */
  static List<InternalPlayer> jsonToPlayer (JsonObject stateAsJson) {
    //Grab input array
    JsonArray inputPlayerArray = stateAsJson.getAsJsonArray("players");
    List<InternalPlayer> resultPlayerList = new ArrayList<>();

    //Build the list of players to initiate a gameState with
    for (JsonElement playerElem : inputPlayerArray) {
      JsonObject OnePlayerObj = playerElem.getAsJsonObject();

      //Get their Color
      String color = OnePlayerObj.getAsJsonPrimitive("color").getAsString();
      PlayerColor playerColor = getAsPlayerColor(color);

      //Create the one player and fill in their color
      InternalPlayer p = new HexPlayer(playerColor);

      //Fill in their score
      int score = OnePlayerObj.getAsJsonPrimitive("score").getAsInt();
      p.addToScore(score);

      //Fill in their penguin locations in order
      JsonArray PenguinInputArray = OnePlayerObj.getAsJsonArray("places");
      for (JsonElement posnElm : PenguinInputArray) {
        //Gets just the [row,col] pair as JsonArray
        JsonArray posnArray = posnElm.getAsJsonArray();
        //Use XBoard functionality to turn it into our interpretation of a location on board
        Coord penguinLoc = XBoard.jsonToCoord(posnArray);
        p.placePenguin(penguinLoc); //Keeps the order of penguins in the same order as the JSON
      }
      resultPlayerList.add(p);
    }
    return resultPlayerList;
  }

  //----Calculating the move----//

  /**
   * Attempts the directional algorithm on the first player's first penguin in the gameState.
   * If a move can be made, apply it to the gameState
   * If not, then return the original GameState, unaltered
   * @param gameState the gameState to apply the algorithm
   * @return either the altered gameState or the original one
   */
  static GameState attemptDirectionalAlgoOnFirstPlayerFirstPenguin(GameState gameState) {
    Coord firstPeng = getFirstPlayersFirstPenguin(gameState);

    GameState outputGS = gameState.getCopyGameState();
    //Now see if any of the tiles reachable from the first penguin's location
    //align with the directional tiles stemming from the penguin's location
    List<Coord> tilesReachable = gameState.getTilesReachableFrom(firstPeng);
    List<Coord> directionalTiles = getDirectionalTiles(firstPeng);
    for (Coord possibleMove : directionalTiles) {
      if (tilesReachable.contains(possibleMove)){
        //As soon as we find the first tile reachable in the directionals, apply the move:
        outputGS.movePenguin(firstPeng, possibleMove);
        return outputGS;
      }
    }
    //If none of the directionalTiles are available to make a move, just return the
    //original gamestate, with no changes made:
    return gameState;
  }


  /**
   * Given a gameState, returns the first(current) player's first penguin in their penguing list
   * @param gameState the GameState to pull the player data from
   * @return the Coord of the first player's first penguin
   */
  static Coord getFirstPlayersFirstPenguin(GameState gameState) {
    //Get the first player's first penguin location:
    List<ProtectedPlayer> players = gameState.getPlayers();
    ProtectedPlayer firstPlayer = players.get(0);
    return firstPlayer.getPenguinLocs().get(0);
  }


  /**
   * Builds a list of all potential Tiles that surround the Tile of origin in the following order:
   * North, NorthEast, SouthEast, South, SouthWest, NorthWest
   *
   * The resulting Coords MAY BE NEGATIVE / invalid / out of bounds - this is a static method
   * so it does not check for validity in relation to any specific board.
   * This is also desired because those invalid Coords should return implemented exceptions if sent
   * to a GameState.
   * @param origin the center Tile to calculate the directional tiles from
   * @return a list of Coords of the surrounding Tiles
   */
  static List<Coord> getDirectionalTiles(Coord origin) {
    int xx = origin.getX();
    int yy = origin.getY();

    Coord north = new Coord(xx, yy - 2);

    Coord ne = new Coord(xx + yy % 2, yy - 1);
    Coord se = new Coord(xx + yy % 2, yy + 1);

    Coord south = new Coord(xx, yy + 2);

    Coord sw = new Coord(xx - (yy + 1) % 2, yy + 1);
    Coord nw = new Coord(xx - (yy + 1) % 2, yy - 1);

    return Arrays.asList(north, ne, se, south, sw, nw);
  }


  //----Converting back to JSON----//
  /**
   * Transforms the given GameState back into a JSON object for output
   * @param gs the gameState to translate into JSON
   * @return the JsonObject
   */
  static JsonObject reconstructStateToJson(GameState gs) {
    JsonObject outputStateAsJson = new JsonObject();

    //////HANDLE PLAYERS JSON
    JsonArray playersArray = new JsonArray();
    for (ProtectedPlayer p : gs.getPlayers()) {
      playersArray.add(reconstructPlayerToJson(p));
    }

    //////HANDLE BOARD JSON
    JsonArray board = XBoard.boardToJson(gs.getGameBoard());

    outputStateAsJson.add("players", playersArray);
    outputStateAsJson.add("board", board);

    return outputStateAsJson;
  }

  /**
   * Transform a single internal player data back to Jason for output
   * @param p the internal player data
   * @return a JsonObject with the player's information
   */
  static JsonObject reconstructPlayerToJson(ProtectedPlayer p) {
    JsonObject onePlayerJsonObject = new JsonObject();

    //construct color in json
    String color = "";
    switch(p.getColor()) {
      case RED:
        color = "red";
        break;
      case WHITE:
        color = "white";
        break;
      case BLACK:
        color = "black";
        break;
      case BROWN:
        color = "brown";
        break;
    }
    onePlayerJsonObject.addProperty("color", color);

    //construct score in json
    onePlayerJsonObject.addProperty("score", p.getScore());

    //construct penguin list in json
    JsonArray pengs = new JsonArray();
    for (Coord c : p.getPenguinLocs()) {
      JsonArray singlePenguin = new JsonArray();
      singlePenguin.add(c.getY());
      singlePenguin.add(c.getX());
      pengs.add(singlePenguin);
    }
    onePlayerJsonObject.add("places", pengs);

    return onePlayerJsonObject;
  }

  //---Helper---//
  /**
   * A useful helper for converting a json string representing an avatar's color into
   * our internal representation of an avatar color, PlayerColor
   * @param color a string of the color
   * @return a PlayerColor corresponding with that color
   */
  static PlayerColor getAsPlayerColor(String color) {
    switch (color) {
      case "black":
        return PlayerColor.BLACK;
      case "brown":
        return PlayerColor.BROWN;
      case "red":
        return PlayerColor.RED;
      case "white":
        return PlayerColor.WHITE;
      default:
        throw new IllegalArgumentException("Not a valid color!");
    }
  }
}
