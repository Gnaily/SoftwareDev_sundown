package com.fish;

import com.fish.model.Coord;
import com.fish.model.PlayerColor;
import com.fish.model.board.GameBoard;
import com.fish.model.board.HexGameBoard;
import com.fish.view.GameView;
import com.fish.view.HexBoardView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main method that launches a game of Hey, that's my Fish!
 */
public class HeyThatsMyFishMain {

  public static void main(String[] args) {

    //Example : A board with no holes and random number of fish on each tile
    GameBoard noHolesBoard = new HexGameBoard(1, 2,
        new ArrayList<Coord>(), 0, 1);

    //Example : A board with holes and random number of fish on each tile
    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
        new Coord(2, 2), new Coord(1, 4));
    GameBoard holesBoard = new HexGameBoard(8, 3, holes,
        8, 1);
    holesBoard.placePenguin(new Coord(1, 2), PlayerColor.BLACK);
    holesBoard.placePenguin(new Coord(0, 1), PlayerColor.BROWN);
    holesBoard.placePenguin(new Coord(0, 2), PlayerColor.WHITE);
    holesBoard.placePenguin(new Coord(0, 3), PlayerColor.RED);

    //Example : A board with no holes and constant number of fish on each tile
    GameBoard constantFishNumBoard = new HexGameBoard(4, 4, 2);

    //Calling the View on one example
    GameView gv = new HexBoardView(holesBoard);
    gv.drawGame();

  }

}
