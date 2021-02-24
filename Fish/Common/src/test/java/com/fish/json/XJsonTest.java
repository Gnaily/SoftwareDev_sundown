package com.fish.integration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.*;

public class XJsonTest {

  @Test
  public void testJsonNormal() {
    String input = "{\n" + "        \"position\": [1,2],\n"
        + "        \"board\" : [[1,0],[0,3]]\n" + "}\n";

    JsonArray expectedOutput = new JsonArray();

    JsonObject jobj = new JsonObject();

    JsonArray positions = new JsonArray();
    positions.add(1);
    positions.add(2);
    jobj.add("position", positions);

    JsonArray board = new JsonArray();
    board.add(new JsonArray());
    board.add(new JsonArray());
    board.get(0).getAsJsonArray().add(1);
    board.get(0).getAsJsonArray().add(0);
    board.get(1).getAsJsonArray().add(0);
    board.get(1).getAsJsonArray().add(3);
    jobj.add("board", board);

    expectedOutput.add(jobj);

    JsonArray actualOutput = XJson.processInput(new Scanner(input));

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testJsonMt() {
    String input = "";

    JsonArray actualOutput = XJson.processInput(new Scanner(input));

    assertEquals(new JsonArray(), actualOutput);

  }

  @Test
  public void testInvalidJson() {
    String input = "hello";
    JsonArray expectedOutput = new JsonArray();
    expectedOutput.add(input);

    JsonArray actualOutput = XJson.processInput(new Scanner(input));

    assertEquals(expectedOutput, actualOutput);

  }

}
