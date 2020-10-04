package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.Penguin;
import com.fish.model.PlayerColor;
import com.fish.model.tile.Tile;
import com.fish.player.Player;
import java.util.Random

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

  /**
   * FIll board...
   * @param maxFish
   * @param holes
   * @param minOneFishTiles
   */
  private void fillBoard(int maxFish, ArrayList<Coord> holes, int minOneFishTiles) {
    Random rand = new Random(System.currentTimeMillis());

    //Generate an Array of Integers containing the range of fish nums on tiles needed for
    //a game
    ArrayList<Integer> nums = new ArrayList<Integer>();
    for (int ii = 0; ii < board.length*board[0].length - holes.size(); ii++) {
      if (ii < minOneFishTiles) {
        nums.add(1);
      }
      else {
        nums.add(rand.nextInt(maxFish) + 1);
      }
    }

    //fill in the board taking one number at a time from the nums array
    for (int iRow = 0; iRow < board.length; iRow++) {
      Tile[] row = board[iRow];
      for (int iCol = 0; iCol < row.length; iCol++) {
        if (holes.contains(new Coord(iRow, iCol))) {
          board[iRow][iCol] = null
        }
        else {
          board[iRow][iCol] = new BasicFishTile(nums.remove(rand.nextInt(nums.size())));
        }
      }
    }
  }

  /**
   *
   */
  private ArrayList<Coord> getValidMovesFromTile(Coord tileLoc) {
    ArrayList<Coord> moves = new ArrayList<>();

    int x = tileLoc.getX();
    int y = tileLoc.getY();




  }

  /**
   * Get Valid Moves
   * @param player
   * @return
   */
  private ArrayList<Coord> getValidMoves(PlayerColor player) {
    ArrayList<Coord> moves = new ArrayList<>();
    // figure it out

    


    return moves;
  }

  public Tile[][] getBoard{
    return this.board;
  }


}
