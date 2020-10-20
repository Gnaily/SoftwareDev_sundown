package com.fish.json;

import com.fish.model.Coord;
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
    String in1 = "{\n" + "        \"position\": [1,2],\n"
        + "        \"board\" : [[1,0],[0,0],[2,3],[0,0],[4,0]]\n" + "}\n";

    String in2 = "{\n" + "        \"position\": [1,3],\n"
        + "        \"board\" : [[2,3,4],[1,1,1],[5,5,5],[4,4,4],[3,3,3],[2,2,2],[1,1,1],[4,4,4],[3,4,2]]\n"
        + "}\n";

    String in3 = "{\n" + "        \"position\": [1,3],\n"
            + "        \"board\" : [[2,3],[1,1,1],[5,5,5],[4,4],[3,3,3],[2,2,2],[1],[4,4,4],[3,4,2,1]]\n"
            + "}\n";

    obj1 = XJson.processInput(new Scanner(in1)).get(0).getAsJsonObject();

    obj2 = XJson.processInput(new Scanner(in2)).get(0).getAsJsonObject();

    obj3 = XJson.processInput(new Scanner(in3)).get(0).getAsJsonObject();

  }

  @Test public void getTileValues() {

    int[][] nums = XBoard.getTileValues(obj1, "board");

    assertEquals(2, nums.length);
    assertEquals(5, nums[0].length);
    assertEquals(0, nums[1][1]);
    assertEquals(1, nums[0][0]);
    assertEquals(4, nums[0][4]);
    assertEquals(0, nums[1][0]);
  }

  @Test
  public void testGetTileValuesUnequalLength() {

    int[][] nums = XBoard.getTileValues(obj3, "board");

    assertEquals(4, nums.length);
    assertEquals(9, nums[0].length);

    assertEquals(0, nums[2][6]);
    assertEquals(0, nums[3][6]);
    assertEquals(1, nums[3][8]);
    assertEquals(2, nums[0][0]);

  }

  @Test public void getStartingCoordinate() {

    assertEquals(new Coord(1, 3), XBoard.getStartingCoordinate(obj2, "position"));
    assertEquals(new Coord(1, 2), XBoard.getStartingCoordinate(obj1, "position"));
  }
}
