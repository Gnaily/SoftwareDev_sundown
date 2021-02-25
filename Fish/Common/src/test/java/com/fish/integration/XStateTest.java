package com.fish.integration;

import com.fish.common.Coord;
import com.fish.common.state.GameStage;
import com.fish.common.state.GameState;
import com.fish.common.state.InternalPlayer;
import com.fish.common.state.PlayerColor;
import com.fish.common.state.ProtectedPlayer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.*;

public class XStateTest {

  private JsonObject southMoveJson;

  private JsonObject falseMoveJson;
  private GameState falseMoveGameState;

  private JsonObject northWestMoveJson;
  private GameState northWestMoveGameState;

  @Before public void setUp() throws Exception {

    //Example of making a south move
    String southMoveString = "{\n"
        + "  \"players\" : [\n"
        + "    {\n"
        + "      \"color\" : \"red\",\n"
        + "      \"score\" : 10,\n"
        + "      \"places\" : [[0,0],[1,0]]\n" + "    },\n"
        + "    {\n"
        + "      \"color\" : \"white\",\n"
        + "      \"score\" : 0,\n"
        + "      \"places\" : [[0,1],[1,1]]\n"
        + "    }],\n"
        + "    \"board\" : [[2,3,4],[1,1,1],[5,5,5],[4,4,4],[3,3,3],[2,2,2]]\n" + "}\n";
    JsonArray southMoveArray = XJson.processInput(new Scanner(southMoveString));
    this.southMoveJson = southMoveArray.get(0).getAsJsonObject();
    //Hold off on making the regularGameState to be able to test this

    //Example of a false outcome
    String falseString = "{\n"
        + "  \"players\" : [\n"
        + "    {\n"
        + "      \"color\" : \"red\",\n"
        + "      \"score\" : 10,\n"
        + "      \"places\" : [[0,0],[1,0]]\n" + "    },\n"
        + "    {\n"
        + "      \"color\" : \"white\",\n"
        + "      \"score\" : 0,\n"
        + "      \"places\" : [[0,1],[1,1]]\n"
        + "    }],\n"
        + "    \"board\" : [[2,3,4],[1,1,1]]\n" + "}\n";
    JsonArray falseMoveArray = XJson.processInput(new Scanner(falseString));
    this.falseMoveJson = falseMoveArray.get(0).getAsJsonObject();
    this.falseMoveGameState = XState.jsonToGameState(this.falseMoveJson);

    //Example of pushing the algorithm to the last possible move, northwest
    String nwString = "{\n"
        + "  \"players\" : [\n"
        + "    {\n"
        + "      \"color\" : \"red\",\n"
        + "      \"score\" : 10,\n"
        + "      \"places\" : [[3,1],[4,1]]\n" + "    },\n"
        + "    {\n"
        + "      \"color\" : \"white\",\n"
        + "      \"score\" : 0,\n"
        + "      \"places\" : [[0,1],[1,1]]\n"
        + "    }],\n"
        + "    \"board\" : [[2,3,4],[1,1,1],[5,5,0],[4,4,4],[3,3,0],[2,0,2]]\n" + "}\n";
    JsonArray nwArray = XJson.processInput(new Scanner(nwString));
    this.northWestMoveJson = nwArray.get(0).getAsJsonObject();
    this.northWestMoveGameState = XState.jsonToGameState(this.northWestMoveJson);
  }

  @Test
  public void testJsonToGameState(){
    GameState gs = XState.jsonToGameState(this.southMoveJson);

    //Test that the characteristics of the board are accurately set up
    assertEquals(GameStage.IN_PLAY, gs.getGameStage());
    assertEquals(3, gs.getWidth());
    assertEquals(6, gs.getHeight());
    assertEquals(2, gs.getTileAt(new Coord(0,0)).getNumFish());
    assertEquals(1, gs.getTileAt(new Coord(0,1)).getNumFish());
    assertEquals(PlayerColor.RED, gs.getCurrentPlayer());

    Map<PlayerColor, Integer> scoreBoard = new HashMap<>();
    scoreBoard.put(PlayerColor.RED, 10);
    scoreBoard.put(PlayerColor.WHITE, 0);
    assertEquals(scoreBoard, gs.getScoreBoard());

  }

  @Test public void testJsonToPlayer() {
    //Generate players from the Json
    List<InternalPlayer> players = XState.jsonToPlayer(this.southMoveJson);

    //Check that the characteristics of the players are right
    assertEquals(2, players.size());
    assertEquals(10, players.get(0).getScore());
    assertEquals(PlayerColor.RED, players.get(0).getColor());

    //Check the penguins of players is right
    List<Coord> redsPenguins = new ArrayList<>();
    redsPenguins.add(new Coord(0,0));
    redsPenguins.add(new Coord(0,1));

    assertEquals(redsPenguins, players.get(0).getPenguinLocs());
  }


  @Test public void testGetDirectionalTilesEvenY() {
    List<Coord> moves = XState.getDirectionalTiles(new Coord(4, 4));
    assertEquals(6, moves.size());

    assertEquals(new Coord(4, 2), moves.get(0));
    assertEquals(new Coord(4, 3), moves.get(1));
    assertEquals(new Coord(4, 5), moves.get(2));
    assertEquals(new Coord(4, 6), moves.get(3));
    assertEquals(new Coord(3, 5), moves.get(4));
    assertEquals(new Coord(3, 3), moves.get(5));
  }

  @Test public void testGetDirectionalTilesOddY() {
    List<Coord> moves = XState.getDirectionalTiles(new Coord(5, 5));
    assertEquals(6, moves.size());

    assertEquals(new Coord(5, 3), moves.get(0));
    assertEquals(new Coord(6, 4), moves.get(1));
    assertEquals(new Coord(6, 6), moves.get(2));
    assertEquals(new Coord(5, 7), moves.get(3));
    assertEquals(new Coord(5, 6), moves.get(4));
    assertEquals(new Coord(5, 4), moves.get(5));
  }

  @Test public void testAttemptDirectionalAlgo() {
    GameState southGS = XState.jsonToGameState(this.southMoveJson);

    //This method either outputs a new, altered GameState, or the original regularGameState if no move has been made
    GameState southOutput = XState.attemptDirectionalAlgoOnFirstPlayerFirstPenguin(southGS);
    GameState falseOutput = XState.attemptDirectionalAlgoOnFirstPlayerFirstPenguin(this.falseMoveGameState);
    GameState nwOutput = XState.attemptDirectionalAlgoOnFirstPlayerFirstPenguin(this.northWestMoveGameState);

    //Test the equality of the input vs output:
    assertNotEquals(southOutput, southGS);//Move has been made, so output no longer equals input GS
    assertEquals(falseOutput, this.falseMoveGameState);//move has not been made, so the gamestate is the same
    assertNotEquals(nwOutput, this.northWestMoveGameState);//move has been made, so output no longer equals input GS
  }

  @Test public void getFirstPlayersFirstPenguin() {
    GameState southGS = XState.jsonToGameState(this.southMoveJson);
    Coord fpfpSouth = XState.getFirstPlayersFirstPenguin(southGS);

    Coord fpfpFalse = XState.getFirstPlayersFirstPenguin(this.falseMoveGameState);
    Coord fpfpNW = XState.getFirstPlayersFirstPenguin(this.northWestMoveGameState);

    assertEquals(new Coord(0, 0), fpfpSouth);
    assertEquals(new Coord(0, 0), fpfpFalse);
    assertEquals(new Coord(1, 3), fpfpNW); //Note the inverse row,col - the input is (3,1)
  }

  @Test
  public void testReconstructOnePlayerToJson() {
    //Example of a player that doesnt end up moving:
    ProtectedPlayer aPlayer = this.falseMoveGameState.getPlayers().get(0);
    JsonObject actualJsonOutput = XState.reconstructPlayerToJson(aPlayer);
    JsonArray playersArray = this.falseMoveJson.getAsJsonArray("players");
    JsonObject expectedJsonOutput = playersArray.get(0).getAsJsonObject();

    assertEquals(expectedJsonOutput, actualJsonOutput);

    //Example of a reconstruction of a player that has moved
    GameState nwOutput = XState.attemptDirectionalAlgoOnFirstPlayerFirstPenguin(this.northWestMoveGameState);
    //notice the player list has rotated, so now the red penguin is at index 1
    actualJsonOutput = XState.reconstructPlayerToJson(nwOutput.getPlayers().get(1));

    assertEquals(14, actualJsonOutput.getAsJsonPrimitive("score").getAsInt());
    assertEquals("red", actualJsonOutput.getAsJsonPrimitive("color").getAsString());
    assertEquals(2, actualJsonOutput.getAsJsonArray("places").size());
  }

  @Test
  public void testReconstructStateToJson() {
    GameState southGS = XState.jsonToGameState(this.southMoveJson);
    GameState southOutput = XState.attemptDirectionalAlgoOnFirstPlayerFirstPenguin(southGS); //apply the move to the output
    JsonObject outputStateAsJson = XState.reconstructStateToJson(southOutput);//reconstruct as json

    assertEquals(2, outputStateAsJson.entrySet().size());
    assertEquals(6, outputStateAsJson.getAsJsonArray("board").size());
    assertEquals(2, outputStateAsJson.getAsJsonArray("players").size());

    JsonArray boardArray = outputStateAsJson.getAsJsonArray("board");
    assertEquals(0,
        boardArray.get(0).getAsJsonArray().get(0).getAsJsonPrimitive().getAsInt());//check the hole was made from the move
    assertEquals(5,
        boardArray.get(2).getAsJsonArray().get(0).getAsJsonPrimitive().getAsInt());//check the destination Tile is not a hole

    JsonArray playersArray = outputStateAsJson.getAsJsonArray("players");
    JsonObject whitePlayer = playersArray.get(0).getAsJsonObject();
    assertEquals("white", whitePlayer.getAsJsonPrimitive("color").getAsString());//check that the players rotated
    JsonObject redPlayer = playersArray.get(1).getAsJsonObject();
    assertEquals("red", redPlayer.getAsJsonPrimitive("color").getAsString());//check that the players rotated

    assertEquals(12, redPlayer.getAsJsonPrimitive("score").getAsInt());//check the score was updated

    JsonArray listOfPosn = redPlayer.getAsJsonArray("places");
    JsonArray onePosn = listOfPosn.get(0).getAsJsonArray();
    int rowNum = onePosn.get(0).getAsJsonPrimitive().getAsInt();
    int colNum = onePosn.get(1).getAsJsonPrimitive().getAsInt();
    assertEquals(2, rowNum);//check places was updated
    assertEquals(0, colNum);//check places was updated
  }


  @Test
  public void testThereAndBackAgain() {
    GameState southGS = XState.jsonToGameState(this.southMoveJson);
    assertEquals(this.southMoveJson, XState.reconstructStateToJson(southGS));
  }

  @Test
  public void getAsPlayerColor() {
    assertEquals(PlayerColor.WHITE, XState.getAsPlayerColor("white"));
    assertEquals(PlayerColor.RED, XState.getAsPlayerColor("red"));
    assertEquals(PlayerColor.BLACK, XState.getAsPlayerColor("black"));
    assertEquals(PlayerColor.BROWN, XState.getAsPlayerColor("brown"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getPlayerColorBadInput() {
    XState.getAsPlayerColor("blue");
  }


  ///tests for old method of changing gamestate to json

//  @Test public void adjustStateAfterMove() {
//    gs.movePenguin(new Coord(0, 0), new Coord(0, 2));
//    XState.adjustStateAfterMove(this.southMoveJson, this.gs,
//        new Coord(0, 0), new Coord(0, 2));
//
//    assertEquals(12, this.southMoveJson.getAsJsonArray("players")
//      .get(1).getAsJsonObject().getAsJsonPrimitive("score").getAsInt());
//    assertEquals(0, this.southMoveJson.getAsJsonArray("board")
//        .get(0).getAsJsonArray().get(0).getAsInt());
//    JsonArray out = new JsonArray();
//    out.add(2);
//    out.add(0);
//    assertEquals(out, this.southMoveJson.getAsJsonArray("players")
//        .get(1).getAsJsonObject().getAsJsonArray("places").get(0).getAsJsonArray());
//
//  }
//
//  @Test public void testAdjustStateAfterMove() {
//    gs.movePenguin(new Coord(0, 1), new Coord(0, 2));
//    XState.adjustStateAfterMove(this.southMoveJson, this.gs,
//        new Coord(0, 1), new Coord(0, 2));
//
//    assertEquals(11, this.southMoveJson.getAsJsonArray("players")
//        .get(1).getAsJsonObject().getAsJsonPrimitive("score").getAsInt());
//    assertEquals(0, this.southMoveJson.getAsJsonArray("board")
//        .get(1).getAsJsonArray().get(0).getAsInt());
//
//    JsonArray out = new JsonArray();
//    out.add(2);
//    out.add(0);
//    assertEquals(out, this.southMoveJson.getAsJsonArray("players")
//        .get(1).getAsJsonObject().getAsJsonArray("places").get(0).getAsJsonArray());
//  }
//
//  @Test public void updateBoardPosition() {
//    this.gs.movePenguin(new Coord(0,0), new Coord(0,2));
//    this.gs.movePenguin(new Coord(1, 0), new Coord(1,2));
//    XState.updateBoardPosition(this.southMoveJson, this.gs);
//    assertEquals(0, this.southMoveJson.getAsJsonArray("board")
//        .get(0).getAsJsonArray().get(0).getAsInt());
//
//    XState.updateBoardPosition(this.southMoveJson, this.gs);
//    assertEquals(0, this.southMoveJson.getAsJsonArray("board")
//        .get(0).getAsJsonArray().get(1).getAsInt());
//  }
//
//  @Test public void updatePlayerScore() {
//    JsonObject fp = this.southMoveJson.getAsJsonArray("players").get(0).getAsJsonObject();
//    gs.movePenguin(new Coord(0, 1), new Coord(0, 2));
//    XState.updatePlayerScore(fp, this.gs, new Coord(0, 2));
//
//    assertEquals(11, fp.getAsJsonPrimitive("score").getAsInt());
//  }
//
//  @Test public void testUpdatePlayerScore() {
//    JsonObject fp = this.southMoveJson.getAsJsonArray("players").get(0).getAsJsonObject();
//    gs.movePenguin(new Coord(0, 0), new Coord(0, 2));
//    XState.updatePlayerScore(fp, this.gs, new Coord(0, 2));
//
//    assertEquals(12, fp.getAsJsonPrimitive("score").getAsInt());
//  }
//
//  @Test public void updatePlayerPenguinPositions()  {
//    JsonObject fp = this.southMoveJson.getAsJsonArray("players").get(0).getAsJsonObject();
//    XState.updatePlayerPenguinPositions(fp, new Coord(3, 3));
//
//    JsonArray out = new JsonArray();
//    out.add(3);
//    out.add(3);
//    assertEquals(out, fp.getAsJsonArray("places").get(0).getAsJsonArray());
//
//    XState.updatePlayerPenguinPositions(fp, new Coord(1, 2));
//
//    out = new JsonArray();
//    out.add(2);
//    out.add(1);
//    assertEquals(out, fp.getAsJsonArray("places").get(0).getAsJsonArray());
//
//  }
}
