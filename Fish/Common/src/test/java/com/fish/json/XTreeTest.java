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

  GameState gs1;
  JsonObject mrq1;

  @Before
  public void setUp() {
    String jsonString = "{\n" + "  \"state\" : {\n" + "    \"players\" : [\n" + "      {\n"
        + "        \"color\" : \"red\",\n" + "        \"score\" : 10,\n"
        + "        \"places\" : [[0,0],[0,1]]\n" + "      },\n" + "      {\n"
        + "        \"color\" : \"white\",\n" + "        \"score\" : 0,\n"
        + "        \"places\" : [[1,0],[1,1]]\n" + "      }],\n"
        + "      \"board\" : [[2,3,4],[1,1,1],[5,5,5],[4,4,4],[3,3,3],[2,2,2]]\n" + "  },\n" + "\n"
        + "  \"from\" : [0, 0],\n" + "  \"to\" : [2, 0]\n" + "}";

    JsonArray xJsonArray = XJson.processInput(new Scanner(jsonString));
    this.mrq1 = xJsonArray.get(0).getAsJsonObject();
    this.gs1 = XTree.createStateFromJsonWithMove(this.mrq1);


  }

  @Test public void createStateFromJsonWithMove() {
    GameState gs = XState.jsonToGameState(this.mrq1.getAsJsonObject("state"));
    GameState afterMove = XTree.createStateFromJsonWithMove(this.mrq1);

    assertEquals(2, gs.getTileAt(new Coord(0, 0)).getNumFish());
    assertFalse(afterMove.getTileAt(new Coord(0, 0)).isPresent());
    assertTrue(gs.getTileAt(new Coord(0, 0)).isPresent());
    assertNotEquals(gs, afterMove);
    gs.movePenguin(new Coord(0, 0), new Coord(0, 2));
    assertEquals(gs, afterMove);
  }

  @Test public void jsonArrayToCoord() {
    JsonArray zeroTwo = new JsonArray();
    zeroTwo.add(2);
    zeroTwo.add(0);
    assertEquals(new Coord(0, 2), XTree.jsonArrayToCoord(zeroTwo));

    JsonArray threeOne = new JsonArray();
    threeOne.add(1);
    threeOne.add(3);
    assertEquals(new Coord(3, 1), XTree.jsonArrayToCoord(threeOne));

  }

  @Test public void findDestinationNotNull() {
    List<Coord> dests = new ArrayList<>();
    Set<Move> moves = new HashSet<>();

    dests.add(new Coord(0, 0));
    dests.add(new Coord(0, 1));
    dests.add(new Coord(10, 22));

    moves.add(new Move(new Coord(0, 0), new Coord(1, 0)));
    moves.add(new Move(new Coord(0, 0), new Coord(1, 1)));
    moves.add(new Move(new Coord(0, 1), new Coord(10, 22)));

    assertEquals(new Coord(10, 22), XTree.findDestination(dests, moves));
  }

  @Test public void findDestinationNotNullFirstInList() {
    List<Coord> dests = new ArrayList<>();
    Set<Move> moves = new HashSet<>();

    dests.add(new Coord(0, 0));
    dests.add(new Coord(0, 1));
    dests.add(new Coord(10, 22));

    moves.add(new Move(new Coord(0, 0), new Coord(0, 0)));
    moves.add(new Move(new Coord(0, 0), new Coord(1, 1)));
    moves.add(new Move(new Coord(0, 1), new Coord(10, 22)));

    assertEquals(new Coord(0, 0), XTree.findDestination(dests, moves));
  }

  @Test public void findDestinationMtMoves() {
    List<Coord> dests = new ArrayList<>();
    Set<Move> moves = new HashSet<>();

    dests.add(new Coord(0, 0));
    dests.add(new Coord(0, 1));
    dests.add(new Coord(10, 22));

    assertNull(XTree.findDestination(dests, moves));
  }

  @Test public void findDestinationMtDestinations() {
    List<Coord> dests = new ArrayList<>();
    Set<Move> moves = new HashSet<>();


    moves.add(new Move(new Coord(0, 0), new Coord(0, 0)));
    moves.add(new Move(new Coord(0, 0), new Coord(1, 1)));
    moves.add(new Move(new Coord(0, 1), new Coord(10, 22)));

    assertNull(XTree.findDestination(dests, moves));
  }

  @Test public void findDestinationNoMatches() {
    List<Coord> dests = new ArrayList<>();
    Set<Move> moves = new HashSet<>();

    dests.add(new Coord(0, 0));
    dests.add(new Coord(0, 1));
    dests.add(new Coord(10, 22));

    moves.add(new Move(new Coord(0, 0), new Coord(0, 7)));
    moves.add(new Move(new Coord(0, 0), new Coord(1, 1)));
    moves.add(new Move(new Coord(0, 1), new Coord(10, 23)));

    assertNull(XTree.findDestination(dests, moves));
  }


  @Test
  public void getMoveToDestination() {
    GameTree tree = new HexGameTree(this.gs1);
    assertEquals(new Move(new Coord(0, 1), new Coord(0, 3)),
        XTree.getMoveToDestination(this.gs1, tree, new Coord(0, 3)));
  }

  @Test
  public void getMoveToDestinationNull() {
    GameTree tree = new HexGameTree(this.gs1);
    assertNull(XTree.getMoveToDestination(this.gs1, tree, new Coord(0, 2)));
  }

  @Test public void findEarliestPenguinMove() {
    List<Move> moves = Arrays.asList(new Move(new Coord(0, 5), new Coord(0, 5)),
        new Move(new Coord(1, 3), new Coord(0, 4)),
        new Move(new Coord(0, 1), new Coord(0, 3)),
        new Move(new Coord(0, 1), new Coord(1, 3)),
        new Move(new Coord(2, 2), new Coord(2, 3)),
        new Move(new Coord(5, 1), new Coord(5, 3)));

    assertEquals(new Move(new Coord(0, 1), new Coord(0, 3)),
        XTree.findEarliestPenguinMove(moves, this.gs1));
  }

  @Test
  public void findEarliestPenguinMoveNull() {
    List<Move> moves = Arrays.asList(new Move(new Coord(0, 5), new Coord(0, 5)),
        new Move(new Coord(1, 3), new Coord(0, 4)),
        new Move(new Coord(0, 4), new Coord(0, 3)),
        new Move(new Coord(0, 4), new Coord(1, 3)),
        new Move(new Coord(2, 2), new Coord(2, 3)),
        new Move(new Coord(5, 1), new Coord(5, 3)));

    assertNull(XTree.findEarliestPenguinMove(moves, this.gs1));
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
