package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.tile.BasicFishTile;
import com.fish.model.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishGameBoard implements FishBoard {

  private Tile[][] tiles;
  private int width;
  private int height;

  public static final int MAX_FISH = 5;

  /**
   * Constructor for creating a game with some holes and a minimum number of one-fish tiles
   *
   * @param rows number of rows this board should have
   * @param cols number of columns this board should have
   * @param holes locations of the holes this board contains
   * @param minOneFishTiles minimum number of one-fish tiles to have in this board
   * @param random whether the tiles should be assigned randomly or in order
   */
  public FishGameBoard(int rows, int cols, List<Coord> holes, int minOneFishTiles, boolean random) {
    if (rows < 1 || cols < 1) {
      throw new IllegalArgumentException("There must be at least 1 row and column");
    }
    if (rows * cols - holes.size() < minOneFishTiles) {
      throw new IllegalArgumentException("There are not enough spaces for the minimum number of "
          + "one fish tiles");
    }

    this.tiles = new Tile[rows][cols];
    this.width = rows;
    this.height = cols;

    this.fillBoardWithTiles(holes, minOneFishTiles, random);

  }

  private void fillBoardWithTiles(List<Coord> holes, int minOneFishTiles, boolean random) {
    Random rand = new Random(System.currentTimeMillis());

    List<Integer> nums = generateTilesValues(this.width * this.height - holes.size(), random, minOneFishTiles);
    //fill in the board taking one number at a time from the nums array
    for (int iRow = 0; iRow < tiles.length; iRow++) {
      Tile[] row = tiles[iRow];
      for (int iCol = 0; iCol < row.length; iCol++) {
        if (holes.contains(new Coord(iRow, iCol))) {
          tiles[iRow][iCol] = null;
        }
        else {
          if (random) {
            tiles[iRow][iCol] = new BasicFishTile(nums.remove(rand.nextInt(nums.size())));
          }
          else {
            tiles[iRow][iCol] = new BasicFishTile(nums.remove(0));
          }
        }
      }
    }
  }

  /**
   * Generates an array of numbers to populate fish values with
   *
   * @param number number of values to generate
   * @param random Whether or not to randomly assign values
   * @param oneFish The minimum number of 1 values to return
   * @return List of specified length with specified number of minimum one-fish tiles
   */
  private List<Integer> generateTilesValues(int number, boolean random, int oneFish) {
    Random rand = new Random(System.currentTimeMillis());

    List<Integer> nums = new ArrayList<Integer>();
    for (int ii = 0; ii < number; ii++) {
      if (ii < oneFish) {
        nums.add(1);
      }
      else {
        if (random) {
          nums.add(rand.nextInt(MAX_FISH) + 1);
        }
        else {
          nums.add(ii % MAX_FISH);
        }
      }
    }
    return nums;
  }

  // 2 - same number of fish on every tile



  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public Tile getTileAt(int xx, int yy) {
    if (xx < 0 || xx >= this.width || yy < 0 || yy >= this.height) {
      return null;
    }
    return tiles[xx][yy];
  }

  @Override
  public Tile removeTileAt(int xx, int yy) throws IllegalArgumentException {
    if (xx < 0 || xx >= this.width || yy < 0 || yy >= this.height) {
      throw new IllegalArgumentException("Cannot remove a tile not within the array");
    }
    Tile tile = tiles[xx][yy];

    if (tile != null) {
      return tile;
    }

    throw new IllegalArgumentException("The tile here is already gone!");
  }

  //TODO: implement
  @Override
  public List<Coord> getTilesReachableFrom(Coord start) throws IllegalArgumentException {

    List<Coord> moves = new ArrayList<>();

    // there are six direction you can move: up, down, upleft, upright, downleft, downright

    // this should get split off into new functions or something probably
    int yStart = start.getY();
    int xStart = start.getX();
    // up:
    for (int ii = yStart - 2; ii >= 0; ii -= 2) {
      if (tiles[xStart][ii] != null) {
        moves.add(new Coord(xStart, ii));
      } else {
        break;
      }
    }

    //down:
    for (int ii = yStart + 2; ii < this.width; ii += 2) {
      if (tiles[xStart][ii] != null) {
        moves.add(new Coord(xStart, ii));
      } else {
        break;
      }
    }

      //upleft
      int jj = xStart;
      for (int ii = yStart - 1; ii >= 0; --ii) {
        if (ii % 2 == 1) {
          --jj;
        }
        if (tiles[jj][ii] != null) {
          moves.add(new Coord(jj, ii));
        } else {
          break;
        }
      }

    //upright
    jj = xStart;
    for (int ii = yStart - 1; ii >= 0; --ii) {
      if (ii % 2 == 0) {
        ++jj;
      }
      if (tiles[jj][ii] != null) {
        moves.add(new Coord(jj, ii));
      } else {
        break;
      }
    }

    //downleft
    jj = xStart;
    for (int ii = yStart + 1; ii >= 0; ++ii) {
      if (ii % 2 == 1) {
        --jj;
      }
      if (tiles[jj][ii] != null) {
        moves.add(new Coord(jj, ii));
      } else {
        break;
      }
    }

    //downright
    jj = xStart;
    for (int ii = yStart + 1; ii >= 0; ++ii) {
      if (ii % 2 == 0) {
        ++jj;
      }
      if (tiles[jj][ii] != null) {
        moves.add(new Coord(jj, ii));
      } else {
        break;
      }
    }


    return moves;
  }
}
