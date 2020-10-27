package com.fish.json;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.model.state.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.*;

public class XStateTest {

  JsonObject stateAsJson;
  GameState gs;
  JsonObject smallJsonState;
  GameState smallGameState;

  @Before public void setUp() throws Exception {
    String jsonAsString = "{\n" + "  \"players\" : [\n" + "    {\n" + "      \"color\" : \"red\",\n"
        + "      \"score\" : 10,\n" + "      \"places\" : [[0,0],[1,0]]\n" + "    },\n" + "    {\n"
        + "      \"color\" : \"white\",\n" + "      \"score\" : 0,\n"
        + "      \"places\" : [[0,1],[1,1]]\n" + "    }],\n"
        + "    \"board\" : [[2,3,4],[1,1,1],[5,5,5],[4,4,4],[3,3,3],[2,2,2]]\n" + "}\n";

    JsonArray xJsonArray = XJson.processInput(new Scanner(jsonAsString));
    this.stateAsJson = xJsonArray.get(0).getAsJsonObject();
    this.gs = setupGameState();

    jsonAsString = "{\n" + "  \"players\" : [\n" + "    {\n" + "      \"color\" : \"red\",\n"
        + "      \"score\" : 10,\n" + "      \"places\" : [[0,0],[1,0]]\n" + "    },\n" + "    {\n"
        + "      \"color\" : \"white\",\n" + "      \"score\" : 0,\n"
        + "      \"places\" : [[0,1],[1,1]]\n" + "    }],\n"
        + "    \"board\" : [[2,3,4],[1,1,1],[5]]\n" + "}\n";

    xJsonArray = XJson.processInput(new Scanner(jsonAsString));
    this.smallJsonState = xJsonArray.get(0).getAsJsonObject();
    this.smallGameState = setupSmallGameState();

  }

  private GameState setupGameState() {
    int[][] valuesFromInput = XBoard.getTileValues(stateAsJson, "board");
    GameBoard boardFromInput = new HexGameBoard(valuesFromInput);

    JsonArray playerArray = stateAsJson.getAsJsonArray("players");
    List<InternalPlayer> players = XState.getPlayersList(playerArray);

    return new HexGameState(GameStage.IN_PLAY, boardFromInput, players);
  }

  private GameState setupSmallGameState() {
    int[][] valuesFromInput = XBoard.getTileValues(smallJsonState, "board");
    GameBoard boardFromInput = new HexGameBoard(valuesFromInput);

    JsonArray playerArray = smallJsonState.getAsJsonArray("players");
    List<InternalPlayer> players = XState.getPlayersList(playerArray);

    return new HexGameState(GameStage.IN_PLAY, boardFromInput, players);
  }

  @Test
  public void getPlayerColor() {
    assertEquals(PlayerColor.WHITE, XState.getPlayerColor("white"));
    assertEquals(PlayerColor.RED, XState.getPlayerColor("red"));
    assertEquals(PlayerColor.BLACK, XState.getPlayerColor("black"));
    assertEquals(PlayerColor.BROWN, XState.getPlayerColor("brown"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getPlayerColorBadInput() {
    XState.getPlayerColor("blue");
  }

  @Test public void getPlayersList() {
    JsonArray playerArray = stateAsJson.getAsJsonArray("players");
    List<InternalPlayer> players = XState.getPlayersList(playerArray);
    assertEquals(2, players.size());
    assertEquals(10, players.get(0).getScore());
    assertEquals(PlayerColor.RED, players.get(0).getColor());
  }

  @Test public void placePenguins() {
    JsonArray playerArray = stateAsJson.getAsJsonArray("players");
    Map<Coord, PlayerColor> penguinLocs = XState.placePenguins(playerArray);

    assertNull(penguinLocs.get(new Coord(2, 2)));
    assertEquals(PlayerColor.RED, penguinLocs.get(new Coord(0, 0)));
    assertEquals(PlayerColor.RED, penguinLocs.get(new Coord(0, 1)));
    assertEquals(PlayerColor.WHITE, penguinLocs.get(new Coord(1, 0)));
    assertEquals(PlayerColor.WHITE, penguinLocs.get(new Coord(1, 1)));
  }

  @Test public void getFirstPlayersFirstPenguin() {
    JsonArray playerArray = stateAsJson.getAsJsonArray("players");
    Coord firstP = XState.getFirstPlayersFirstPenguin(playerArray);

    assertEquals(new Coord(0, 0), firstP);
  }

  @Test public void getDirectionalTilesEvenY() {
    List<Coord> moves = XState.getDirectionalTiles(new Coord(4, 4));
    assertEquals(6, moves.size());

    assertEquals(new Coord(4, 2), moves.get(0));
    assertEquals(new Coord(4, 3), moves.get(1));
    assertEquals(new Coord(4, 5), moves.get(2));
    assertEquals(new Coord(4, 6), moves.get(3));
    assertEquals(new Coord(3, 5), moves.get(4));
    assertEquals(new Coord(3, 3), moves.get(5));
  }

  @Test public void getDirectionalTilesOddY() {
    List<Coord> moves = XState.getDirectionalTiles(new Coord(5, 5));
    assertEquals(6, moves.size());

    assertEquals(new Coord(5, 3), moves.get(0));
    assertEquals(new Coord(6, 4), moves.get(1));
    assertEquals(new Coord(6, 6), moves.get(2));
    assertEquals(new Coord(5, 7), moves.get(3));
    assertEquals(new Coord(5, 6), moves.get(4));
    assertEquals(new Coord(5, 4), moves.get(5));
  }

  @Test public void findLegalMove() {
    assertEquals(new Coord(0, 2), XState.findLegalMove(this.gs, new Coord(0, 0)));
    assertEquals(new Coord(2, 0), XState.findLegalMove(this.gs, new Coord(1, 1)));

    gs.movePenguin(new Coord(0, 1), new Coord(0, 2));
    assertNull(XState.findLegalMove(this.gs, new Coord(0, 0)));
  }

  @Test public void adjustStateAfterMove() {
    gs.movePenguin(new Coord(0, 0), new Coord(0, 2));
    XState.adjustStateAfterMove(this.stateAsJson, this.gs,
        new Coord(0, 0), new Coord(0, 2));

    assertEquals(12, this.stateAsJson.getAsJsonArray("players")
      .get(1).getAsJsonObject().getAsJsonPrimitive("score").getAsInt());
    assertEquals(0, this.stateAsJson.getAsJsonArray("board")
        .get(0).getAsJsonArray().get(0).getAsInt());
    JsonArray out = new JsonArray();
    out.add(2);
    out.add(0);
    assertEquals(out, this.stateAsJson.getAsJsonArray("players")
        .get(1).getAsJsonObject().getAsJsonArray("places").get(0).getAsJsonArray());

  }

  @Test public void testAdjustStateAfterMove() {
    gs.movePenguin(new Coord(0, 1), new Coord(0, 2));
    XState.adjustStateAfterMove(this.stateAsJson, this.gs,
        new Coord(0, 1), new Coord(0, 2));

    assertEquals(11, this.stateAsJson.getAsJsonArray("players")
        .get(1).getAsJsonObject().getAsJsonPrimitive("score").getAsInt());
    assertEquals(0, this.stateAsJson.getAsJsonArray("board")
        .get(1).getAsJsonArray().get(0).getAsInt());

    JsonArray out = new JsonArray();
    out.add(2);
    out.add(0);
    assertEquals(out, this.stateAsJson.getAsJsonArray("players")
        .get(1).getAsJsonObject().getAsJsonArray("places").get(0).getAsJsonArray());
  }

  @Test public void updateBoardPosition() {
    this.gs.movePenguin(new Coord(0,0), new Coord(0,2));
    this.gs.movePenguin(new Coord(1, 0), new Coord(1,2));
    XState.updateBoardPosition(this.stateAsJson, this.gs);
    assertEquals(0, this.stateAsJson.getAsJsonArray("board")
        .get(0).getAsJsonArray().get(0).getAsInt());

    XState.updateBoardPosition(this.stateAsJson, this.gs);
    assertEquals(0, this.stateAsJson.getAsJsonArray("board")
        .get(0).getAsJsonArray().get(1).getAsInt());
  }

  @Test public void updatePlayerScore() {
    JsonObject fp = this.stateAsJson.getAsJsonArray("players").get(0).getAsJsonObject();
    gs.movePenguin(new Coord(0, 1), new Coord(0, 2));
    XState.updatePlayerScore(fp, this.gs, new Coord(0, 2));

    assertEquals(11, fp.getAsJsonPrimitive("score").getAsInt());
  }

  @Test public void testUpdatePlayerScore() {
    JsonObject fp = this.stateAsJson.getAsJsonArray("players").get(0).getAsJsonObject();
    gs.movePenguin(new Coord(0, 0), new Coord(0, 2));
    XState.updatePlayerScore(fp, this.gs, new Coord(0, 2));

    assertEquals(12, fp.getAsJsonPrimitive("score").getAsInt());
  }

  @Test public void updatePlayerPenguinPositions()  {
    JsonObject fp = this.stateAsJson.getAsJsonArray("players").get(0).getAsJsonObject();
    XState.updatePlayerPenguinPositions(fp, new Coord(3, 3));

    JsonArray out = new JsonArray();
    out.add(3);
    out.add(3);
    assertEquals(out, fp.getAsJsonArray("places").get(0).getAsJsonArray());

    XState.updatePlayerPenguinPositions(fp, new Coord(1, 2));

    out = new JsonArray();
    out.add(2);
    out.add(1);
    assertEquals(out, fp.getAsJsonArray("places").get(0).getAsJsonArray());

  }

  @Test
  public void testReconstructStateToJson() {
    JsonObject state = XState.reconstructStateToJson(this.gs);

    assertEquals(6, state.getAsJsonArray("board").size());
    assertEquals(2, state.getAsJsonArray("players").size());
    assertEquals(2, state.entrySet().size());
  }

  @Test
  public void testCreatePlayerObject() {
    JsonObject p1 = XState.createPlayerObject(this.gs.getPlayers().get(0), this.gs);

    assertEquals(10, p1.getAsJsonPrimitive("score").getAsInt());
    assertEquals("red", p1.getAsJsonPrimitive("color").getAsString());
    assertEquals(2, p1.getAsJsonArray("places").size());
  }

  @Test
  public void testCreateBoard() {
    JsonArray board = XState.createBoardJson(this.gs);

    assertEquals(6, board.size());
    assertEquals(3, board.get(0).getAsJsonArray().size());

    assertEquals(5, board.get(2).getAsJsonArray().get(2).getAsInt());
    assertEquals(3, board.get(4).getAsJsonArray().get(1).getAsInt());
  }

  @Test
  public void testPadOutBoard() {
    JsonArray board = XState.createBoardJson(this.smallGameState);

    assertEquals(3, board.size());
    assertEquals(3, board.get(2).getAsJsonArray().size());

    assertEquals(0, board.get(2).getAsJsonArray().get(1).getAsInt());

  }

  @Test
  public void testThereAndBackAgain() {
    assertEquals(this.stateAsJson, XState.reconstructStateToJson(this.gs));
  }
}
