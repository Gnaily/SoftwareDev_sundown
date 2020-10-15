package com.fish;

import com.google.gson.JsonObject;

import com.fish.json.XBoard;
import com.fish.json.XJson;
import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.HexGameState;
import com.fish.model.state.Player;
import com.fish.model.state.PlayerColor;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.view.GameView;
import com.fish.view.HexBoardView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Main method that launches a game of Hey, that's my Fish!
 */
public class HTMFMain {

  public static void main(String[] args) {

    //Example : A board with no holes and random number of fish on each tile
    GameBoard noHolesBoard = new HexGameBoard(6, 2,
        new ArrayList<Coord>(), 5, 1);

    //Example : A board with holes and random number of fish on each tile
    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
        new Coord(2, 2), new Coord(1, 4));
    GameBoard holesBoard = new HexGameBoard(8, 3, holes,
        8, 1);

    //Example : A board with no holes and constant number of fish on each tile
    GameBoard constantFishNumBoard = new HexGameBoard(4, 4, 2);


    //Create the GameState
    GameState state = new HexGameState();
    List<Player> players = initilizePlayers();
    state.initGame(holesBoard, players);

    state.placePenguin(new Coord(1, 2), PlayerColor.BLACK);
    state.placePenguin(new Coord(0, 1), PlayerColor.BROWN);
    state.placePenguin(new Coord(0, 2), PlayerColor.WHITE);
    state.placePenguin(new Coord(0, 3), PlayerColor.RED);

    //Calling the View with the state
    GameView gv = new HexBoardView(state);
    gv.drawGame();

  }

  private static List<Player> initilizePlayers() {
    List<Player> players = Arrays.asList(new Player(10), new Player(12), new Player(44), new Player(55));
    players.get(0).setColorColor(PlayerColor.BROWN);
    players.get(1).setColorColor(PlayerColor.BLACK);
    players.get(2).setColorColor(PlayerColor.WHITE);
    players.get(3).setColorColor(PlayerColor.RED);
    return players;
  }

}
