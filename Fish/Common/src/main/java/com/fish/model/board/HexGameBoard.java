package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.PlayerColor;
import com.fish.model.tile.HexTile;
import com.fish.model.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Implementation of a GameBoard for a general game of Hey, That's my Fish!
 * Contains data representations for the tiles on the board, the locations of penguins/players,
 * and dimensions of the board.
 */
public class HexGameBoard implements GameBoard {

  private Tile[][] tiles;
  private HashMap<Coord, PlayerColor> penguinLocs;
  private int width;
  private int height;
  private Random rand;

  private static final int MAX_FISH = 5;

  /**
   * Constructor to build a general game board.
   * (The Random arg allows you to generate the same board multiple times)
   * @param rows the number of rows of tiles on the board
   * @param cols the number of columns of tiles on the board
   * @param holes the specific number of holes to start the game with
   * @param minOneFishTiles the minimum number of 1-fish tiles to start the game with
   */
  public HexGameBoard(int rows, int cols, List<Coord> holes, int minOneFishTiles) {
    if (rows < 1 || cols < 1) {
      throw new IllegalArgumentException("There must be at least one row and one column");
    }
    if (rows * cols - holes.size() < minOneFishTiles) {
      throw new IllegalArgumentException("There are not enough spaces for the minimum number of "
          + "one fish tiles");
    }
    this.tiles = new Tile[cols][rows];
    this.penguinLocs = new HashMap<>();
    this.width = cols;
    this.height = rows;
    this.rand = new Random(System.currentTimeMillis());

    this.fillBoardWithTiles(holes, minOneFishTiles);
  }

  /**
   * Constructor to build a general game board with no holes and the same number of fish
   * on every tile.
   * @param rows the number of rows of tiles on the board
   * @param cols the number of columns of tiles on the board
   * @param numberOfFish the number of fish to put on every tile
   */
  public HexGameBoard(int rows, int cols, int numberOfFish) {
    if (rows < 1 || cols < 1) {
      throw new IllegalArgumentException("There must be at least 1 row and column");
    }

    this.tiles = new Tile[cols][rows];
    this.penguinLocs = new HashMap<>();
    this.width = cols;
    this.height = rows;

    for (int iRow = 0; iRow < tiles.length; iRow++) {
      Tile[] row = tiles[iRow];
      for (int iCol = 0; iCol < row.length; iCol++) {
        tiles[iRow][iCol] = new HexTile(numberOfFish);
      }
    }
  }

  /**
   * Convenience constructor for testing. Includes arg for Rand seed for consistent testing outcomes.
   */
  public HexGameBoard(int rows, int cols, List<Coord> holes, int minOneFishTiles,
      int randSeed) {
    this(rows, cols, holes, minOneFishTiles);
    this.rand = new Random(randSeed);
    this.fillBoardWithTiles(holes, minOneFishTiles);
  }


  //Fills the board with randomized tiles
  private void fillBoardWithTiles(List<Coord> holes, int minOneFishTiles) {
    List<Integer> tileFishValues = generateTileValues(this.width * this.height - holes.size(),
        minOneFishTiles);
    //fill in the board taking one number at a time from the tileFishValues array
    for (int iRow = 0; iRow < this.tiles.length; iRow++) {
      for (int iCol = 0; iCol < this.tiles[iRow].length; iCol++) {
        if (holes.contains(new Coord(iRow, iCol))) {
          this.tiles[iRow][iCol] = null;
        }
        else {
          this.tiles[iRow][iCol] = new HexTile(tileFishValues.remove(
              this.rand.nextInt(tileFishValues.size())));
        }
      }
    }
  }

  //Generates random tile fish values
  private List<Integer> generateTileValues(int numValsNeeded, int minOneFishTiles) {
    List<Integer> fishValues = new ArrayList<Integer>();
    for (int ii = 0; ii < numValsNeeded; ii++) {
      if (ii < minOneFishTiles) {
        fishValues.add(1);
      }
      else {
        fishValues.add(this.rand.nextInt(MAX_FISH) + 1);
      }
    }
    return fishValues;
  }


  /////////////////////////////////
  @Override
  public List<Coord> getTilesReachableFrom(Coord start) throws IllegalArgumentException {

    int x = start.getX();
    int y = start.getY();

    checkTileInBounds(start, "Cannot move from a tile that does not exist");
    List<Coord> moves = new ArrayList<>();

    //Moving straight down

    moves.addAll(this.getTilesAboveBelow(x, y, -2));
    moves.addAll(this.getTilesAboveBelow(x, y, 2));

    moves.addAll(this.getTilesLeftDiagonal(x, y, 1));
    moves.addAll(this.getTilesLeftDiagonal(x, y, -1));

    moves.addAll(this.getTilesRightDiagonal(x, y, 1));
    moves.addAll(this.getTilesRightDiagonal(x, y, -1));

    return moves;
  }

  private List<Coord> getTilesAboveBelow(int xx, int yy, int yIncrement) {

    List<Coord> moves = new ArrayList<>();
    for (yy = yy + yIncrement; yy >= 0 && yy < this.height; yy += yIncrement) {
      if (this.tiles[xx][yy] != null) {
        moves.add(new Coord(xx, yy));
      }
      else {
        break;
      }
    }

    return moves;
  }

  private List<Coord> getTilesLeftDiagonal(int xx, int yy, int yIncrement) {

    List<Coord> moves = new ArrayList<>();

    for (yy = yy + yIncrement; yy >= 0 && yy < this.height; yy += yIncrement) {
      xx -= yy % 2;
      if (xx >= 0 && xx < this.width && this.tiles[xx][yy] != null
              && this.penguinLocs.get(new Coord(xx, yy)) == null) {
        moves.add(new Coord(xx, yy));
      }
      else {
        break;
      }
    }

    return moves;
  }

  private List<Coord> getTilesRightDiagonal(int xx, int yy, int yIncrement) {

    List<Coord> moves = new ArrayList<>();

    for (yy = yy + yIncrement; yy >= 0 && yy < this.height; yy += yIncrement) {
      xx += (yy + 1) % 2;
      if (xx >= 0 && xx < this.width && this.tiles[xx][yy] != null
              && this.penguinLocs.get(new com.fish.model.Coord(xx, yy)) == null) {
        moves.add(new com.fish.model.Coord(xx, yy));
      }
      else {
        break;
      }
    }

    return moves;
  }




  /////////////////////////////////Tile Handling
  @Override
  public Tile getTileAt(Coord loc) {
    checkTileInBounds(loc, "Cannot retrieve a tile not on the board");
    //We do not check for TilePresent because we want this to return null if the tile
    //has been removed to differentiate from an arg that is out of bounds vs a tile that's
    //been removed
    return tiles[loc.getX()][loc.getY()];
  }

  @Override
  public Tile removeTileAt(Coord loc) {
    checkTileInBounds(loc, "Cannot remove a tile not on the board");
    checkTilePresent(loc, "Cannot remove a tile that has already been removed");
    //Will need to store num fish info for score keeping later
    int xx = loc.getX();
    int yy = loc.getY();

    Tile returnTile = getTileAt(loc);
    tiles[xx][yy] = null;
    return returnTile;
  }


  /////////////////////////////////Penguin Handling
  @Override
  public void placePenguin(Coord loc, PlayerColor playerColor) throws IllegalArgumentException {
    checkTileInBounds(loc, "Cannot place a penguin off the board");
    checkTilePresent(loc, "Cannot place a penguin on a tile that " +
        "does not exist");
    if (this.penguinLocs.containsKey(loc)) {
      throw new IllegalArgumentException("There is already a penguin on this tile");
    }
    this.penguinLocs.put(loc, playerColor);
  }

  @Override
  public PlayerColor removePenguin(Coord loc) throws IllegalArgumentException {
    //--> Needs a check in place to make sure the user can not remove another player's penguin
    if (this.penguinLocs.get(loc) == null) {
      throw new IllegalArgumentException("There is no Penguin here to move");
    }
    return this.penguinLocs.remove(loc);
  }

  @Override
  public HashMap<Coord, PlayerColor> getPenguinLocations(){
    return new HashMap<>(this.penguinLocs);
  }

  /////////////////////////////////Getters and Helpers
  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  private void checkTileInBounds(Coord loc, String specificMsg) throws IllegalArgumentException {
    int xx = loc.getX();
    int yy = loc.getY();
    if (xx < 0 || xx >= this.width || yy < 0 || yy >= this.height) {
      throw new IllegalArgumentException(specificMsg);
    }
  }

  private void checkTilePresent(Coord loc, String specificMsg) throws IllegalArgumentException {
    int xx = loc.getX();
    int yy = loc.getY();
    if (tiles[xx][yy] == null) {
      throw new IllegalArgumentException(specificMsg);
    }
  }

}
