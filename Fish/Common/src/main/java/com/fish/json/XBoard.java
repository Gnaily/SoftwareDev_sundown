package com.fish.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;

import java.util.ArrayList;
import java.util.Scanner;

public class XBoard {

  /**
   * Main function for running the XBoard integration tests. Consumes valid JSON from Standard,
   *   parses that into a HexGameBoard, and then prints how many tiles are reachable from the
   *   given position.
   *
   * @param args Command line arguments, not used
   */
  public static void main(String[] args) {

    Scanner scan = new Scanner(System.in);

    XJson xJson = new XJson();
    xJson.processInput(scan);

    JsonObject obj = xJson.getJsonArray().get(0).getAsJsonObject();

    int[][] values = getTileValues(obj);

    GameBoard gb = new HexGameBoard(values);

    Coord start = getStartingCoordinate(obj);
    int size = gb.getTilesReachableFrom(start, new ArrayList<>()).size();

    System.out.println(size);
  }


  /**
   * Assumes well-formatted JSON as specific in the testing guidelines. Turns the given JSON object's
   *  "board" field into a 2D array of ints to be used for board creation
   *
   * @param jsonObject (JsonObject) The properly formatted JSON object
   * @return (int[][]) 2D array of ints representing tile values
   */
  public static int[][] getTileValues(JsonObject jsonObject) {

    JsonArray array = jsonObject.getAsJsonArray("board");

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

  public static int findMaxLengthInArray(JsonArray array) {

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
  public static Coord getStartingCoordinate(JsonObject obj) {
    JsonArray array = obj.getAsJsonArray("position");

    return new Coord(
            array.get(0).getAsInt(),
            array.get(1).getAsInt());
  }
}
