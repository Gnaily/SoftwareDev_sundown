package com.fish.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import java.util.Scanner;

/**
 * A general JSON processing class.
 * Parses/Processes JSON input by translating it into Java objects, but does not store them
 * in order for this class to be reused with diverse input.
 */
public class XJson {

  //No constructor needed because this class only serves the purpose of its static methods

  /**
   * Turn the given Scanner input into Json elements and return the Json elements as a JSonArray,
   * which is a special type of list in Java to accommodate JSON objects.
   *
   * @param scan scanner to process.
   * @return a JsonArray with the inputs
   */
  static JsonArray processInput(Scanner scan) {
    JsonArray jsonArray = new JsonArray();
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
        // Keep adding to the jsonString until it contains the entire JSON object
      }
    }
    return jsonArray;
  }

  /**
   * Format the given JsonArray for output as specified by assignment C (xJson)
   *
   * @return String containing the specified json
   */
  static String formatJson(JsonArray jsonArray) {

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

}
