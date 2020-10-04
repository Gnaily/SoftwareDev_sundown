package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.Penguin;
import com.fish.model.PlayerColor;
import com.fish.model.tile.Tile;
import com.fish.player.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FishGameBoard {

  // what does a board need
  // dimension (with tiles)
  //  - tiles contain their own information of fish
  //  - hole vs tile
  //     - representation of hole

  // board should have max number of fish on a tile;

  private Tile[][] board;
  private ArrayList<Penguin> penguins;

  // ListOfPlayer -> each Player has List of Penguins
  // ListOfPenguin -> location and player color

  // not sure about this/how to best do it
  private static int DEFAULT_MAX_FISH = 6;


  public FishGameBoard(int rows, int columns, int maxFish, ArrayList<Coord> holes, int minOneFishTiles) {

    if (rows < 1 || columns < 1 || rows * columns - holes.size() < minOneFishTiles) {
      // make the message better
      throw new IllegalArgumentException("Invalid usage of Board Constructor");
    }
    this.board = new Tile[rows][columns];

    // fill in the board with tiles at all locations not in holes
    this.fillBoard(maxFish, holes, minOneFishTiles);
  }

  // fill in later
  private void fillBoard(int maxFish, ArrayList<Coord> holes, int minOneFishTiles) {


  }

  private ArrayList<Coord> getValidMoves(PlayerColor player) {
    ArrayList<Coord> moves = new ArrayList<>();
    // figure it out
    


    return moves;
  }


}
