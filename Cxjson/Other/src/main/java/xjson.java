import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import java.util.*;

public class xjson {

  public static void main (String [] args){

    JsonArray jsonArray = new JsonArray();
    String jsonString = "";


    Scanner sc = new Scanner(System.in);
    while(sc.hasNext()){
      String input = sc.next();

      jsonString = jsonString + " " + input;
      try {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        jsonArray.add(jsonElement);
        jsonString = "";
      }
      catch (JsonSyntaxException e){

      }

    }

    JsonElement count = new JsonPrimitive(jsonArray.size());
    JsonObject output = new JsonObject();
    output.add("count", count);
    output.add("seq", jsonArray);

    JsonArray reverse = new JsonArray();
    reverse.add(count);
    for (int i = jsonArray.size(); i>0; i--) {
      reverse.add(jsonArray.get(i-1));
    }

    System.out.println(output);
    System.out.println(reverse);


  }

}
