package com.fish.integration;

import com.fish.common.Coord;
import com.fish.common.board.GameBoard;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.*;

public class XBoardTest {

  private JsonObject obj1;
  private JsonObject obj2;
  private JsonObject obj3;

  @Before
  public void setUp() {
    String in1 = "{\n" + "        \"position\": [4,0],\n"
        + "        \"board\" : [[1,0],[0,0],[2,3],[0,0],[4,0]]\n" + "}\n";

    String in2 = "{\n" + "        \"position\": [6,2],\n"
        + "        \"board\" : [[2,3,4],[1,1,1],[5,5,5],[4,4,4],[3,3,3],[2,2,2],[1,1,1],[4,4,4],[3,4,2]]\n"
        + "}\n";

    String in3 = "{\n" + "        \"position\": [0,1],\n"
            + "        \"board\" : [[2,3],[1,1,1],[5,5,5],[4,4],[3,3,3],[2,2,2],[1],[4,4,4],[3,4,2,1]]\n"
            + "}\n";

    obj1 = XJson.processInput(new Scanner(in1)).get(0).getAsJsonObject();

    obj2 = XJson.processInput(new Scanner(in2)).get(0).getAsJsonObject();

    obj3 = XJson.processInput(new Scanner(in3)).get(0).getAsJsonObject();

  }

  @Test public void tesJsonToGameBoard() {

    GameBoard example = XBoard.jsonToGameBoard(obj1);

    int[][] actualValues = example.getBoardDataRepresentation();
    int[][] expectedValues =
        {{1, 0, 2, 0, 4},
            {0, 0, 3, 0, 0}};

    assertArrayEquals(expectedValues, actualValues);

    //Check the data representation is set up properly
    assertEquals(2, actualValues.length);
    assertEquals(5, actualValues[0].length);
    //Check board physical properties are correct
    assertEquals(2, example.getWidth());
    assertEquals(5, example.getHeight());

    //Again, test Json data representation against actual board properties
    assertEquals(0, actualValues[1][1]);
    assertEquals(0, example.getTileAt(new Coord(1,1)).getNumFish());

    assertEquals(1, actualValues[0][0]);
    assertEquals(1, example.getTileAt(new Coord(0,0)).getNumFish());

    assertEquals(4, actualValues[0][4]);
    assertEquals(4, example.getTileAt(new Coord(0,4)).getNumFish());
  }

  @Test
  public void testGetTileValuesUnequalLength() {
    GameBoard example = XBoard.jsonToGameBoard(obj3);

    int[][] actualValues = example.getBoardDataRepresentation();

    assertEquals(4, actualValues.length);
    assertEquals(9, actualValues[0].length);

    //Note that actualValues[2][6] would fall into the index of [6][2] in the "board" jsonArray.
    assertEquals(0, actualValues[2][6]);
    assertEquals(0, actualValues[3][6]);
    assertEquals(1, actualValues[3][8]);
    assertEquals(2, actualValues[0][0]);

    assertFalse(example.getTileAt(new Coord(3, 6)).isPresent());
  }

  @Test public void testJsonToCoord() {
    assertEquals(new Coord(0, 4), XBoard.jsonToCoord(obj1));
    assertEquals(new Coord(2, 6), XBoard.jsonToCoord(obj2));
    assertEquals(new Coord(1, 0), XBoard.jsonToCoord(obj3));
  }

  @Test public void testTilesReachableFrom() {
    GameBoard board1 = XBoard.jsonToGameBoard(obj1);
    Coord coord1 = XBoard.jsonToCoord(obj1);

    GameBoard board2 = XBoard.jsonToGameBoard(obj2);
    Coord coord2 = XBoard.jsonToCoord(obj2);

    GameBoard board3 = XBoard.jsonToGameBoard(obj3);
    Coord coord3 = XBoard.jsonToCoord(obj3);

    assertEquals(2, XBoard.getTilesReachableFromPosn(board1, coord1));
    assertEquals(12, XBoard.getTilesReachableFromPosn(board2, coord2));
    assertEquals(6, XBoard.getTilesReachableFromPosn(board3, coord3));
  }

  @Test public void testBoardToJson() {
    GameBoard board1 = XBoard.jsonToGameBoard(obj1);
    JsonArray actualJson = XBoard.boardToJson(board1);
    JsonArray expectedJson = obj1.getAsJsonArray("board");

    assertEquals(expectedJson, actualJson);

    GameBoard board2 = XBoard.jsonToGameBoard(obj2);
    actualJson = XBoard.boardToJson(board2);
    expectedJson = obj2.getAsJsonArray("board");

    assertEquals(expectedJson, actualJson);

    //Since the third example tests the cases where some of the rows are not completely written out,
    //where we pad the shorter rows with zeros, here we reconstruct the expected outcome JSON Array
    String adjustedIn3 = "{\n" + "        \"position\": [0,1],\n"
        + "        \"board\" : [[2,3,0,0],[1,1,1,0],[5,5,5,0],[4,4,0,0],[3,3,3,0],[2,2,2,0],[1,0,0,0],[4,4,4,0],[3,4,2,1]]\n"
        + "}\n";
    JsonObject adjustedObj3 = XJson.processInput(new Scanner(adjustedIn3)).get(0).getAsJsonObject();

    GameBoard board3 = XBoard.jsonToGameBoard(obj3);//Use the shortened board for the actual outcome

    actualJson = XBoard.boardToJson(board3);
    expectedJson = adjustedObj3.getAsJsonArray("board");

    assertEquals(expectedJson, actualJson);
  }


}
