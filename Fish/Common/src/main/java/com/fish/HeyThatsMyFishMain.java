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
    //Examples to Draw
    GameBoard noHolesBoard = new HexGameBoard(6, 2,
        new ArrayList<Coord>(), 5, 1);

    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
        new Coord(2, 2), new Coord(1, 4));
    GameBoard holesBoard = new HexGameBoard(8, 3, holes,
        8, 1);

    GameBoard constantFishNumBoard = new HexGameBoard(4, 4, 2);

    //Place a penguin to see the penguin visual implemented
    noHolesBoard.placePenguin(new Coord(0,0), PlayerColor.BROWN);


    //Calling the View on one example
    GameView gv = new HexBoardView(noHolesBoard);
    gv.drawGame();

  }

}
