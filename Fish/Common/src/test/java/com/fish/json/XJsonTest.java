package com.fish.json;

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

    String out = XJson.formatJson(XJson.processInput(new Scanner(input)));

    assertEquals("{\"count\":8,\"seq\":[1,2,3,4,\"hello world\"," +
            "false,{\"a\":1,\"b\":2},[1,\"hello\",true]]}\n" +
            "[8,[1,\"hello\",true],{\"a\":1,\"b\":2},false,\"hello world\",4,3,2,1]\n",
        out);
  }

  @Test
  public void testJsonMt() {
    String input = "";

    String out = XJson.formatJson(XJson.processInput(new Scanner(input)));

    assertEquals("{\"count\":0,\"seq\":[]}\n" +
        "[0]\n", out);

  }

}
