package com.fish.json;

import com.fish.game.GameTree;
import com.fish.game.HexGameTree;
import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class XTreeTest {

  private JsonObject falseMRQ;
  private GameState falseGameState;

  private JsonObject regularMRQ;
  private GameState regularGameState;

  private JsonObject tieMRQ;
  private GameState tieGameState;

  @Before
  public void setUp() {
    //A case that returns False
    String falseCase = "{\n"
        + "  \"state\" : {\n"
        + "    \"players\" : [\n" + "      {\n"
        + "        \"color\" : \"red\",\n"
        + "        \"score\" : 10,\n"
        + "        \"places\" : [[2,0],[0,1]]\n" + "      },\n"
        + "      {\n"
        + "        \"color\" : \"white\",\n"
        + "        \"score\" : 0,\n"
        + "        \"places\" : [[1,0],[1,1]]\n" + "      }],\n"
        + "      \"board\" : [[2,3,4],[1,1,1],[5,5,5],[0,4,4],[3,3,3],[2,2,2]]\n" + "  },\n" + "\n"
        + "  \"from\" : [2, 0],\n"
        + "  \"to\" : [4, 0]\n" + "}";

    JsonArray falseArray = XJson.processInput(new Scanner(falseCase));
    this.falseMRQ = falseArray.get(0).getAsJsonObject();
    this.falseGameState = XTree.mrqToGameState(this.falseMRQ);

    //Just a regular case with one possible outcome
    String regularCase = "{\n"
        + "  \"state\" : {\n"
        + "    \"players\" : [\n" + "      {\n"
        + "        \"color\" : \"red\",\n"
        + "        \"score\" : 10,\n"
        + "        \"places\" : [[0,0],[0,1]]\n" + "      },\n"
        + "      {\n"
        + "        \"color\" : \"white\",\n"
        + "        \"score\" : 0,\n"
        + "        \"places\" : [[1,0],[1,1]]\n" + "      }],\n"
        + "      \"board\" : [[2,3,4],[1,1,1],[5,5,5],[4,4,4],[3,3,3],[2,2,2]]\n" + "  },\n" + "\n"
        + "  \"from\" : [0, 0],\n"
        + "  \"to\" : [4, 0]\n" + "}";

    JsonArray regularArray = XJson.processInput(new Scanner(regularCase));
    this.regularMRQ = regularArray.get(0).getAsJsonObject();
    this.regularGameState = XTree.mrqToGameState(this.regularMRQ);

    //A case that involves a tie breaker
    String tieBreakCase = "{\n"
        + "  \"state\" : {\n"
        + "    \"players\" : [\n" + "      {\n"
        + "        \"color\" : \"red\",\n"
        + "        \"score\" : 10,\n"
        + "        \"places\" : [[2,0],[0,1]]\n" + "      },\n"
        + "      {\n"
        + "        \"color\" : \"white\",\n"
        + "        \"score\" : 0,\n"
        + "        \"places\" : [[1,0],[1,1]]\n" + "      }],\n"
        + "      \"board\" : [[2,3,4],[1,1,1],[5,5,5],[4,4,4],[3,3,3],[2,2,2]]\n" + "  },\n" + "\n"
        + "  \"from\" : [2, 0],\n"
        + "  \"to\" : [4, 0]\n" + "}";

    JsonArray tieArray = XJson.processInput(new Scanner(tieBreakCase));
    this.tieMRQ = tieArray.get(0).getAsJsonObject();
    this.tieGameState = XTree.mrqToGameState(this.tieMRQ);

  }

  @Test public void testMrqToJson() {
    GameState actualState = XTree.mrqToGameState(this.regularMRQ);//How the method actually works

    //Construct an example step by step of how it should work
    GameState expectedState = XState.jsonToGameState(this.regularMRQ.getAsJsonObject("state"));//before move
    assertNotEquals(expectedState, actualState);
    //Then make the move on the expected
    expectedState.movePenguin(new Coord(0, 0), new Coord(0, 4));

    assertFalse(actualState.getTileAt(new Coord(0, 0)).isPresent());//checks the penguin moved
    assertEquals(expectedState, actualState);
  }


  @Test public void testCalculateValidOutcomesFalse() {
    //Case that returns False
    GameTree strategyTree = new HexGameTree(this.falseGameState);
    List<Move> actualOutcome = XTree.calculateValidOutcomeMoves(strategyTree,
        XBoard.jsonToCoord(this.falseMRQ.getAsJsonArray("to")));

    //Construct the expected outcome
    List<Move> expectedOutcomes = new ArrayList<>();

    assertEquals(expectedOutcomes, actualOutcome);
  }

  @Test public void testCalculateValidOutcomesRegular() {
    //Regular case with no tie breaker
    GameTree strategyTree = new HexGameTree(this.regularGameState);
    List<Move> actualOutcome = XTree.calculateValidOutcomeMoves(strategyTree,
        XBoard.jsonToCoord(this.regularMRQ.getAsJsonArray("to")));

    //Construct the expected outcome
    List<Move> expectedOutcomes = new ArrayList<>();
    expectedOutcomes.add(new Move(new Coord(0,1), new Coord(0,2)));

    assertEquals(expectedOutcomes, actualOutcome);
  }

  @Test public void testCalculateValidOutcomesTieBreaker() {
    //Tie breaker case
    GameTree strategyTree = new HexGameTree(this.tieGameState);
    List<Move> actualOutcome = XTree.calculateValidOutcomeMoves(strategyTree,
        XBoard.jsonToCoord(this.tieMRQ.getAsJsonArray("to")));

    Move actualMove = XTree.determineTieBreaker(actualOutcome);

    //Construct the expected outcome
    Move expectedMove = new Move(new Coord(0,1), new Coord(0,3));

    assertEquals(expectedMove, actualMove);
  }


  @Test public void moveToJson() {
    JsonArray move = new JsonArray();
    JsonArray to = new JsonArray();
    JsonArray from = new JsonArray();

    to.add(0);
    to.add(1);
    from.add(3);
    from.add(5);

    move.add(from);
    move.add(to);
    assertEquals(move, XTree.moveToJson(new Move(new Coord(5, 3), new Coord(1, 0))));
  }

  @Test public void testMoveToJson() {
    JsonArray move = new JsonArray();
    JsonArray to = new JsonArray();
    JsonArray from = new JsonArray();

    to.add(1);
    to.add(2);
    from.add(5);
    from.add(5);

    move.add(from);
    move.add(to);
    assertEquals(move, XTree.moveToJson(new Move(new Coord(5, 5), new Coord(2, 1))));
  }
}
