package com.fish.json;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.model.state.GameStage;
import com.fish.model.state.GameState;
import com.fish.model.state.HexGameState;
import com.fish.model.state.Player;
import com.fish.model.state.PlayerColor;
import com.fish.model.tile.Tile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

    Coord moveMade = findLegalMove(gsFromInput, penguinStart);

    //IF no move has been made, return FALSE. Else, return the new State as represented in JSON
    if (moveMade == null) {
      System.out.println("False");
    }
    else{
      gsFromInput.movePenguin(penguinStart, moveMade);
      adjustStateAfterMove(stateAsJson, gsFromInput, penguinStart, moveMade);
      System.out.println(stateAsJson);
    }
  }

  //////////////////////HELPERS
  //Useful helper method to generate the data representation of a given color represented as a String
  static PlayerColor getPlayerColor(String color) {
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

  //Returns the list of Player JSON representations
  static List<Player> getPlayersList (JsonArray playerArray) {
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
  static Map<Coord, PlayerColor> placePenguins(JsonArray playerArray) {
    Map<Coord, PlayerColor> penguinLocs = new HashMap<>();

    for (JsonElement playerElem : playerArray) {
      JsonObject playerObj = playerElem.getAsJsonObject();
      //System.out.println(playerObj.get("places"));
      int[][] penguinLocsAsArray = XBoard.getTileValues(playerObj, "places");
      //System.out.println(Arrays.deepToString(penguinLocsAsArray));

      String color = playerObj.getAsJsonPrimitive("color").getAsString();
      PlayerColor pc = getPlayerColor(color);


      // our other method flips the array - this flips it back
      for (int ii = 0; ii < penguinLocsAsArray[0].length; ++ii) {
        penguinLocs.put(new Coord(penguinLocsAsArray[0][ii],
            penguinLocsAsArray[1][ii]), pc);

      }
    }
    return penguinLocs;
  }

  //Ensures that we are locating the first player's first penguin from the JSON input
  static Coord getFirstPlayersFirstPenguin(JsonArray playerArray) {
    JsonObject firstPlayerObj = playerArray.get(0).getAsJsonObject();
    JsonArray firstPlayersPenguinsLocs = firstPlayerObj.getAsJsonArray("places");
    JsonArray firstPenguinLoc = firstPlayersPenguinsLocs.get(0).getAsJsonArray();
    return new Coord(firstPenguinLoc.get(0).getAsInt(), firstPenguinLoc.get(1).getAsInt());
  }

  //----Calculating the move----//

  //Get a list of all the potential tiles from the Tile of origin. They could result in negative
  //or out of bounds Coords. This is good because those results should return invalid exception
  //messages when sent to the GameState.
  static List<Coord> getDirectionalTiles(Coord penguinStart) {
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

  // Find a legal move for the given penguin as described in the testing doc
  static Coord findLegalMove(GameState gs, Coord penguinStart) {

    List<Coord> tilesReachable = gs.getTilesReachableFrom(penguinStart);
    List<Coord> directionalTiles = getDirectionalTiles(penguinStart);
    for (Coord possibleMove : directionalTiles) {
      if (tilesReachable.contains(possibleMove)){
        //gs.movePenguin(penguinStart, possibleMove);
        return possibleMove;
      }
    }

    // no move is possible - return null
    return null;
  }

  //----Functions for altering the given State as Json into the resultant state----//

  //Bring together all of the information from the JSON input and APPLY it to the GameState as JSON.
  static void adjustStateAfterMove(JsonObject stateAsJson, GameState startState,
      Coord penguinStart, Coord moveMade) {

    //Replace board represented in stateAsJson by converting the updated state back to JSON
    updateBoardPosition(stateAsJson, startState);

    //Adjust player score as represented in stateAsJson -- pull updated score from GameState
    JsonArray firstPlayerArray = stateAsJson.getAsJsonArray("players");
    JsonObject firstPlayer = firstPlayerArray.remove(0).getAsJsonObject();
    firstPlayerArray.add(firstPlayer); //cycles through the players to advance player turn

    updatePlayerScore(firstPlayer, startState, moveMade);

    //Adjust player places as represented in stateAsJson -- move penguin location
    updatePlayerPenguinPositions(firstPlayer, moveMade);
  }

  // Update the board to have a new hole in the correct place
  static void updateBoardPosition(JsonObject stateAsJson, GameState gs) {
    stateAsJson.remove("board");
    stateAsJson.add("board", createBoardJson(gs));
  }

  // update the player score with the number of fish on the removed tile
  static void updatePlayerScore(JsonObject firstPlayer,
      GameState startState, Coord moveMade) {

    firstPlayer.remove("score");
    //At this point, the move has been made, so the playerColor can be found stored in the penguin
    //locs at the destination Tile, which is moveMade.
    PlayerColor firstPlayerColor = startState.getPenguinLocations().get(moveMade);
    //This pulls the updated score directly from the GameState to ensure our score keeping is accurate
    int newScore = startState.getPlayerScore(firstPlayerColor);
    firstPlayer.addProperty("score", newScore);

  }

  // Update the player's penguin locations to have the first penguin moved
  static void updatePlayerPenguinPositions(JsonObject firstPlayer, Coord moveMade) {
    JsonArray originalLocs = firstPlayer.remove("places").getAsJsonArray();
    JsonArray newLoc = new JsonArray();
    newLoc.add(moveMade.getX());
    newLoc.add(moveMade.getY());
    originalLocs.set(0, newLoc);
    firstPlayer.add("places", originalLocs);
  }

  // Turns a given GameState into a JsonObject
  // Not used, but could be useful for future testing
  // Does not preserve the order of penguins that was passe in
  static JsonObject reconstructStateToJson(GameState gs) {
    JsonObject state = new JsonObject();

    // create players:
    JsonArray playersArray = new JsonArray();
    for (Player p : gs.getPlayers()) {
      playersArray.add(createPlayerObject(p, gs));
    }

    JsonArray board = createBoardJson(gs);
    state.add("players", playersArray);
    state.add("board", board);
    return state;
  }

  // Turns a Player into a JsonObject representing that player
  static JsonObject createPlayerObject(Player p, GameState gs) {
    JsonObject jo = new JsonObject();
    jo.addProperty("score", gs.getPlayerScore(p.getColor()));
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
    jo.addProperty("color", color);
    JsonArray pengs = new JsonArray();
    for (Coord c : gs.getOnePlayersPenguins(p.getColor())) {
      JsonArray singlePenguin = new JsonArray();
      singlePenguin.add(c.getX());
      singlePenguin.add(c.getY());
      pengs.add(singlePenguin);
    }

    jo.add("places", pengs);
    return jo;
  }

  // turn a given GameState's board into a JsonArray representing tile values
  static JsonArray createBoardJson(GameState gs) {
    JsonArray board = new JsonArray();
    for (int ii = 0; ii < gs.getHeight(); ii++) {
      JsonArray row = new JsonArray();
      for (int jj = 0; jj < gs.getWidth(); jj++) {
        Tile t = gs.getTileAt(new Coord(jj, ii));
        if (t.isPresent()) {
          row.add(t.getNumFish());
        }
        else {
          row.add(0);
        }
      }
      board.add(row);
    }

    return board;
  }

}
