package com.fish.json;

import com.fish.model.board.ProtectedGameBoard;
import com.fish.model.state.GameState;
import com.fish.model.tile.ProtectedTile;
import com.fish.model.tile.Tile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class XBoard {

  /**
   * Main method for running the XBoard integration tests. Consumes valid JSON from Standard,
   *   parses that into a HexGameBoard, and then prints how many tiles are reachable from the
   *   given position.
   *
   * @param args Command line arguments, not used
   */
  public static void main(String[] args) {

    //grab the input from STD in
    Scanner scan = new Scanner(System.in);
    //create a JasonArray Java object whose elements are each JSON object from STD in
    JsonArray inputArray = XJson.processInput(scan);
    //Grab the first (and only) JSON object, which is the board represented in JSON
    JsonObject obj = inputArray.get(0).getAsJsonObject();

    //////HANDLE "board" INPUT
    //Turn the Json representation of a board INTO a GameBoard object
    GameBoard gameBoard = jsonToGameBoard(obj);

    //////HANDLE "position" INPUT
    //Turn the Json representation of a position INTO a Coord object (our position structure)
    Coord origin = jsonToCoord(obj);

    //Now calculate the desired output, and do not consider that there are any penguins on the board,
    //(hence the empty array of penguin locs passed into getTilesReachableFrom)
    int numTilesReachableFromPosn = getTilesReachableFromPosn(gameBoard, origin);

    //Print the number of tiles reachable from the given position
    System.out.println(numTilesReachableFromPosn);
  }

  /**
   * Quickly calculate the number of tiles reachable from the input posn
   * Separated out here in order to test this functionality
   *
   * @param gameBoard the gameboard to test the reachable tiles from
   * @param origin the Coordinate point of origin
   * @return the number of tiles reachable from the origin in valid moves
   */
  static int getTilesReachableFromPosn(GameBoard gameBoard, Coord origin) {
    return gameBoard.getTilesReachableFrom(origin, new ArrayList<>()).size();
  }

  /**
   * Assumes well-formatted JSON as specified in the testing guidelines.
   * Turns the given JSON object's "board" field into a GameBoard object.
   *
   * @param boardAsJsonObject (JsonObject) The properly formatted JSON object
   * @return (int[][]) 2D array of ints representing tile values
   */
  static GameBoard jsonToGameBoard(JsonObject boardAsJsonObject) {
    JsonArray boardInputArray = boardAsJsonObject.getAsJsonArray("board");

    //Base case - an empty board
    if (boardInputArray.size() == 0) {
      return new HexGameBoard(new int[0][0]); //will return a the desired exception that this input is too small
    }

    //Make up for holes that are omitted from the json representation
    int cols = findMaxLengthInArray(boardInputArray);
    //Create the 2-d Array of values, initialized like OUR Coord-based representation of the board.
    int[][] coordValuesArray = new int[cols][boardInputArray.size()];

    //Note that our Coord-based system is inverted compared to the testing harness's system.
    //Here, we do the inverting in order to set up our GameBoard object.
    //Their representation == boardArray
    //Our representation == valuesArray
    for (int ii = 0; ii < boardInputArray.size(); ii++) {
      JsonArray row = boardInputArray.get(ii).getAsJsonArray();

      for (int jj = 0; jj < cols; jj++) {
        if (jj >= row.size()) {
          //invert:
          coordValuesArray[jj][ii] = 0;
        }
        else {
          //invert:
          coordValuesArray[jj][ii] = row.get(jj).getAsInt();
        }

      }
    }
    return new HexGameBoard(coordValuesArray);
  }

  /**
   * Handles the possibility that the board's JSON representation does not have even dimensions.
   * Finds the maximum length of any row in the full board representation.
   * @param array
   * @return
   */
  private static int findMaxLengthInArray(JsonArray array) {

    int max = 0;
    for (int ii = 0; ii < array.size(); ii++) {
      if (array.get(ii).getAsJsonArray().size() > max) {
        max = array.get(ii).getAsJsonArray().size();
      }
    }

    return max;
  }


  /**
   * Assuming well-formatted JSON, parse the position field into a Coord object.
   * Note again that the given json input structure is the inverse of ours.
   * Eg:
   * INPUT "position": [1,5] corresponds to
   * COORD Coord(5,1)
   *
   * For this reason, the second element of the positionInputArray is the x value of the Coord
   * and the first element of the positionInputArray is the y value.
   *
   * @param obj (JsonObject) JSON object containing position field to turn into a coordinate
   * @return (Coord) the coordinate found in the json object
   */
  static Coord jsonToCoord(JsonObject obj) {
    JsonArray positionInputArray = obj.getAsJsonArray("position");

    return new Coord(
        positionInputArray.get(1).getAsInt(),
        positionInputArray.get(0).getAsInt());
  }

  /**
   * Assuming well-formatted JSON, parse the position field into a Coord object.
   * Note again that the given json input structure is the inverse of ours.
   * Eg:
   * INPUT "position": [1,5] corresponds to
   * COORD Coord(5,1)
   *
   * For this reason, the second element of the positionInputArray is the x value of the Coord
   * and the first element of the positionInputArray is the y value.
   *
   * Note this method is different from the previous in that it takes in a JsonArray,
   * instead of a JsonObject.
   *
   * @param jsonArray (JsonArray) JSON array containing position field to turn into a coordinate
   * @return (Coord) the coordinate found in the json object
   */
  static Coord jsonToCoord(JsonArray jsonArray) {
    return new Coord(
        jsonArray.get(1).getAsInt(),
        jsonArray.get(0).getAsInt());
  }

  /**
   * Transforms a given gameBoard into a JSON representtion
   * @param gameBoard the gameboard to transform
   * @return a JSON Array of the board
   */
  static JsonArray boardToJson(ProtectedGameBoard gameBoard) {
    JsonArray board = new JsonArray();
    for (int ii = 0; ii < gameBoard.getHeight(); ii++) {
      JsonArray row = new JsonArray();
      for (int jj = 0; jj < gameBoard.getWidth(); jj++) {
        ProtectedTile t = gameBoard.getTileAt(new Coord(jj, ii));
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
