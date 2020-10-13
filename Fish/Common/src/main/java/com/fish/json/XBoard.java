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


  // TODO: testing
  /**
   *
   * @param jsonObject
   * @return
   */
  public static int[][] getTileValues(JsonObject jsonObject) {

    JsonArray array = jsonObject.getAsJsonArray("board");

    if (array.size() == 0) {
      return new int[0][0];
    }

    int cols = array.get(0).getAsJsonArray().size();

    int[][] values = new int[cols][array.size()];

    for (int ii = 0; ii < array.size(); ii++) {
      JsonArray row = array.get(ii).getAsJsonArray();

      for (int jj = 0; jj < cols; jj++) {
        values[jj][ii] = row.get(jj).getAsInt();


      }
    }
    return values;
  }


  /**
   *
   * @param obj
   * @return
   */
  public static Coord getStartingCoordinate(JsonObject obj) {
    JsonArray array = obj.getAsJsonArray("position");

    return new Coord(
            array.get(0).getAsInt(),
            array.get(1).getAsInt());
  }
}
