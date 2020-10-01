import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.*;

public class XJsonTest {

  @Test
  public void testJsonNormal() {
    String input = "1\n" +
            "2 3 4\n" +
            "\"hello world\"\n" +
            "false\n" +
            "{ \"a\" :  1, \"b\" : 2}\n" +
            "[1, \"hello\", true]";

    XJson xjson = new XJson();
    xjson.processInput(new Scanner(input));
    String out = xjson.formatJson();

    assertEquals("{\"count\":8,\"seq\":[1,2,3,4,\"hello world\"," +
            "false,{\"a\":1,\"b\":2},[1,\"hello\",true]]}\n" +
            "[8,[1,\"hello\",true],{\"a\":1,\"b\":2},false,\"hello world\",4,3,2,1]\n",
            out);
  }

  @Test
  public void testJsonMt() {
    String input = "";

    XJson xJson = new XJson();
    xJson.processInput(new Scanner(input));
    String out = xJson.formatJson();

    assertEquals("{\"count\":0,\"seq\":[]}\n" +
            "[0]\n", out);

  }

}