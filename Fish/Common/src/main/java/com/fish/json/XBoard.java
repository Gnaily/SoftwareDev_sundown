package com.fish.json;

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
    JsonArray xJson = XJson.processInput(scan);
    //Grab the first JSON object, which is the board represented in JSON
    JsonObject obj = xJson.get(0).getAsJsonObject();

    //////HANDLE "board" INPUT
    //turn that into an actual 2D array board representation in Java
    int[][] values = getTileValues(obj, "board");
    //Create the GameBoard object with the values from the JSON input
    GameBoard gb = new HexGameBoard(values);

    //////HANDLE "position" INPUT
    Coord start = getStartingCoordinate(obj, "position");
    int numTilesReachableFromPosn = gb.getTilesReachableFrom(start, new ArrayList<>()).size();

    //Print the number of tiles reachable from
    System.out.println(numTilesReachableFromPosn);
  }

  /**
   * Assumes well-formatted JSON as specified in the testing guidelines. Turns the given JSON object's
   *  "board" field into a 2D array of ints to be used for board creation
   *
   * @param jsonObject (JsonObject) The properly formatted JSON object
   * @return (int[][]) 2D array of ints representing tile values
   */
  static int[][] getTileValues(JsonObject jsonObject, String key) {

    JsonArray array = jsonObject.getAsJsonArray(key);

    if (array.size() == 0) {
      return new int[0][0];
    }

    int cols = findMaxLengthInArray(array);

    int[][] values = new int[cols][array.size()];

    for (int ii = 0; ii < array.size(); ii++) {
      JsonArray row = array.get(ii).getAsJsonArray();

      for (int jj = 0; jj < cols; jj++) {
        if (jj >= row.size()) {
          values[jj][ii] = 0;
        }
        else {
          values[jj][ii] = row.get(jj).getAsInt();
        }

      }
    }
    return values;
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
   * Assuming well-formatted JSON, parse the position field into a coordinate
   *
   * @param obj (JsonObject) JSON object containing position field to turn into a coordinate
   * @return (Coord) the coordinate found in the json object
   */
  static Coord getStartingCoordinate(JsonObject obj, String key) {
    JsonArray array = obj.getAsJsonArray(key);

    return new Coord(
            array.get(0).getAsInt(),
            array.get(1).getAsInt());
  }
}
