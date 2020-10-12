package com.fish.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import java.util.Scanner;

/**
 * Class from assignment C-JSON for handling json inputs
 */
public class XJson {

  // TODO: edit to work for unit testing
  // TODO: write unit testing script/main method

  private JsonArray jsonArray;

  public XJson() {
    this.jsonArray = new JsonArray();
  }

  /**
   * Format this class's JsonArray to be proper output
   *
   * @return String containing the specified json
   */
  public String formatJson() {

    JsonElement count = new JsonPrimitive(jsonArray.size());
    JsonObject output = new JsonObject();
    output.add("count", count);
    output.add("seq", jsonArray);
    JsonArray reverse = new JsonArray();
    reverse.add(count);
    for (int i = jsonArray.size(); i>0; i--) {
      reverse.add(jsonArray.get(i-1));
    }

    return output.toString() + "\n" + reverse.toString() + "\n";
  }

  /**
   * Turn the given input into Json elements
   *
   * @param scan scanner to process.
   */
  public void processInput(Scanner scan) {
    String jsonString = "";

    while (scan.hasNext()) {
      String input = scan.next();

      jsonString = jsonString + " " + input;
      try {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        jsonArray.add(jsonElement);
        jsonString = "";
      }
      catch (JsonSyntaxException e) {
        // do nothing
      }
    }
  }
}
