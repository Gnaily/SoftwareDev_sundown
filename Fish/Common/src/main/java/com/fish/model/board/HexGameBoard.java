package com.fish.model.board;

import com.fish.model.Coord;
import com.fish.model.tile.HexTile;
import com.fish.model.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation for a GameBoard object in a game of Hey, That's my Fish! (HTMF)
 *
 * The GameBoard object is a collection of Tile objects that represent the tiles used to play out
 * a game of HTMF. This implementation handles hexagon-shaped tiles, the layout of which is described
 * below. The location of each Tile in the data representation of the board can be found using a
 * Coord object, which has one x and one y value that directly refer to the indices of
 * the 2-d Array where the Tiles are stored. For example, the Tile at the top-left of the board
 * illustrated below can be located using either one of the following methods:
 * tiles[0][0] OR
 * this.getTileAT(new Coord(0,0))
 * But in this implementation the later is used throughout because the method implements checks that
 * enforce the Tile object being retrieved is in fact on this board.
 *
 * <p>
 *  The Coordinate system for a hexagonal game is used as follows:
 *
 *    ,--.     >--.
 *   /    \___/    \___
 *   \0,0 /   \1,0 /   \
 *    >--< 0,1 >--< 1,1|
 *   /    \___/    \___/
 *   \0,2 /   \1,2 /   \
 *    >--< 0,3 >--< 1,3|
 *   /    \___/    \___/
 *   \0,4 /   \1,4 /   \
 *    `--< 0,5 >--< 1,5|
 *        \___/    \___/
 *
 *   This is an example of a board with 6 rows and 2 columns. For any given coordinate (x, y) the
 *   x value represents the column number and the y value represents the row number.
 * </p>
 *
 * A Tile that isPresent represents a Tile still visible on the game board, whereas a Tile that
 * returns false for isPresent represents a hole in the visible/playable board.
 */
public class HexGameBoard implements GameBoard {

  private Tile[][] tiles;
  private int width;
  private int height;
  private Random rand;

  private static final int MAX_FISH = 5;

  /**
   * Constructor to build a general hex game board with randomized fish numbers per tile.
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

    if (numberOfFish > MAX_FISH) {
      throw new IllegalArgumentException("The number of fish per tile is too many to fit on one tile");
    }

    this.tiles = new Tile[cols][rows];
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
   * Convenience constructor for testing. Includes arg for Rand seed for consistent test outcomes.
   * @param rows the number of rows of tiles on the board
   * @param cols the number of columns of tiles on the board
   * @param holes the specific number of holes to start the game with
   * @param minOneFishTiles the minimum number of 1-fish tiles to start the game with
   * @param randSeed random seed to produce the same outcome multiple times
   */
  public HexGameBoard(int rows, int cols, List<Coord> holes, int minOneFishTiles,
      int randSeed) {
    this(rows, cols, holes, minOneFishTiles);
    this.rand = new Random(randSeed);
    this.fillBoardWithTiles(holes, minOneFishTiles);
  }


  /**
   * Convenience constructor for the test harness.
   * @param values
   */
  public HexGameBoard(int[][] values) {
    if (values.length == 0 || values[0].length == 0) {
      throw new IllegalArgumentException("Tiles must be at least 1 by 1");
    }

    this.tiles = new Tile[values.length][values[0].length];
    this.height = values[0].length;
    this.width = values.length;

    for (int iRow = 0; iRow < this.width; iRow++) {

      for (int iCol = 0; iCol < this.height; iCol++) {
        if (values[iRow][iCol] == 0) {
          tiles[iRow][iCol] = new HexTile();
        }
        else {
          tiles[iRow][iCol] = new HexTile(values[iRow][iCol]);
        }
      }
    }

  }


  /////////////////////////////////Initialize board

  //Fills the board with randomized tiles
  private void fillBoardWithTiles(List<Coord> holes, int minOneFishTiles) {
    List<Integer> tileFishValues = generateTileValues(
        this.width * this.height - holes.size(), minOneFishTiles);

    //fill in the board taking one number at a time from the tileFishValues array
    for (int iRow = 0; iRow < this.tiles.length; iRow++) {
      for (int iCol = 0; iCol < this.tiles[iRow].length; iCol++) {
        if (holes.contains(new Coord(iRow, iCol))) {
          this.tiles[iRow][iCol] = new HexTile();
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
    int ones = 0;

    // generate a list of random tile values
    List<Integer> fishValues = new ArrayList<>();
    for (int ii = 0; ii < numValsNeeded; ii++) {
      int nextVal = this.rand.nextInt(MAX_FISH) + 1;
      if (nextVal == 1) {
        ones++;
      }
      fishValues.add(nextVal);
    }

    // enforce there is the minimum number of one-fish tiles requested
    int ii = 0;
    while (ones < minOneFishTiles && ii < numValsNeeded) {
      if (fishValues.get(ii) != 1) {
        fishValues.set(ii, 1);
        ones++;
      }

      ii++;
    }
    return fishValues;
  }


  /////////////////////////////////Move Handling

  /**
   * Given a coordinate of origin, returns a list of all possible coordinates a player can
   * make a valid move to from the origin.
   * A valid move is defined by landing on any Coord location in a straight line from the
   * Coord of origin in either the up, down, up-left, up-right, down-left, or down-right directions
   * on the visible board.
   * The coordinates of each direction are calculated by observing the change in x and y values
   * as the coordinates move away from the Coord of origin.
   * 'Reachable' also means that there is no hole or penguin in the way to get there in a straight line.
   * @param start the coord of origin
   * @param penguinLocs the locations of all penguins on this board
   * @return a list of Coord indicating the possible valid moves
   * @throws IllegalArgumentException if the coord of origin is out of bounds or is a hole
   */
  @Override
  public List<Coord> getTilesReachableFrom(Coord start, List<Coord> penguinLocs) throws IllegalArgumentException {

    int x = start.getX();
    int y = start.getY();

    checkTileInBounds(start, "Cannot move from a tile that is out of bounds");
    checkTilePresent(start, "Cannot move from a hole");
    List<Coord> moves = new ArrayList<>();

    // These TwoNumberOperations define different rules for incrementing x values while
    // finding reachable tiles (see hexagon coordinate diagram at the top of the file for how
    // tile coordinates are determined).

    // Does not perform an operation and returns the first value, which is the xx value.
    // This is used for when moving directly above and below the starting tile, because in this case
    // the xx value does not change.
    TwoNumberOperation returnX = (int aa, int bb) -> aa;

    // Use to traverse Up-Left OR Down-Left
    // Decreases the first value by 1 whenever the second value is odd.
    TwoNumberOperation decrementXonYOdd = (int aa, int bb) -> aa - bb % 2;

    // Use to traverse Up-Right OR Down-Right
    // Increases the first value by 1 whenever the second value is even.
    TwoNumberOperation incrementXonYEven = (int aa, int bb) -> aa + (bb + 1) % 2;

    // directly up and down
    moves.addAll(this.getTilesStraightLine(x, y, returnX, -2, penguinLocs));
    moves.addAll(this.getTilesStraightLine(x, y, returnX, 2, penguinLocs));

    // right side diagonals
    moves.addAll(this.getTilesStraightLine(x, y, incrementXonYEven, -1, penguinLocs));
    moves.addAll(this.getTilesStraightLine(x, y, incrementXonYEven, 1, penguinLocs));

    // left side diagonals
    moves.addAll(this.getTilesStraightLine(x, y, decrementXonYOdd, -1, penguinLocs));
    moves.addAll(this.getTilesStraightLine(x, y, decrementXonYOdd, 1, penguinLocs));

    return moves;
  }

  /**
   * Get tiles reachable from a starting (x, y) location
   *
   * <p>
   *   Holes (isPresent == false for a tile location) and Penguins are considered blockers that stop
   *   tiles from being reachable.
   * </p>
   *
   * @param xx starting x location
   * @param yy starting y location
   * @param op Operation that defines how the x value should be changed
   * @param yIncrement value to increment/decrement y by for every new tile to search
   * @return A list of all tiles reachable from the given location following the increment rules
   */
  private List<Coord> getTilesStraightLine(int xx, int yy,
      TwoNumberOperation op, int yIncrement, List<Coord> penguinLocs) {

    List<Coord> moves = new ArrayList<>();

    for (yy = yy + yIncrement; yy >= 0 && yy < this.height; yy += yIncrement) {
      xx = op.performOperation(xx, yy);
      if (xx >= 0 && xx < this.width
          && this.tiles[xx][yy].isPresent()
          && !penguinLocs.contains(new Coord(xx, yy))) {
        moves.add(new Coord(xx, yy));
      }
      else {
        break;
      }
    }
    return moves;
  }


  /////////////////////////////////Tile Handling

  /**
   * Given a coordinate location within the dimensions of the game board,
   * returns the Tile object at that coordinate location.
   * The Tile object that is returned may be a hole, in which case if you call
   * isPresent on that Tile it returns false.
   * @param loc the coordinate location of the desired tile on the board
   * @return the Tile object at that location
   * @throws IllegalArgumentException if the requested tile is out of the bounds of the board
   */
  @Override
  public Tile getTileAt(Coord loc) throws IllegalArgumentException {
    checkTileInBounds(loc, "Cannot retrieve a tile not on the board");
    return tiles[loc.getX()][loc.getY()];
  }

  /**
   * Given a coordinate location within the dimensions of the game board,
   * removes the tile from that coordinate location on the board, replacing it with null,
   * and returns the Tile.
   * @param loc the coordinate location of the tile to remove on the board
   * @return the Tile object at that location
   * @throws IllegalArgumentException if the requested tile is out of bounds or is already a hole
   * on the board, represented by a null value
   */
  @Override
  public Tile removeTileAt(Coord loc) throws IllegalArgumentException {
    checkTileInBounds(loc, "Cannot remove a tile that is not on the board");
    checkTilePresent(loc, "Cannot remove a tile where a hole is already located");

    Tile returnTile = getTileAt(loc);
    returnTile.meltTile();
    return returnTile;
  }


  /////////////////////////////////Getters and Helpers

  /**
   * Returns the width of the game board, defined by the number of columns on the visual board
   * @return an int with the width
   */
  @Override
  public int getWidth() {
    return this.width;
  }

  /**
   * Returns the height of the game board, defined by the number of rows on the visual board
   * @return an int with the height
   */
  @Override
  public int getHeight() {
    return this.height;
  }

  //Purpose: To reduce the amount of times we need to write out checks that a Coord is within
  //the dimensions of the board (which is in almost every method)
  private void checkTileInBounds(Coord loc, String specificMsg) throws IllegalArgumentException {
    int xx = loc.getX();
    int yy = loc.getY();
    if (xx < 0 || xx >= this.width || yy < 0 || yy >= this.height) {
      throw new IllegalArgumentException(specificMsg);
    }
  }

  //Purpose: To reduce the amount of times we need to write out checks that a tile at the given
  //Coord is present, and not a hole.
  private void checkTilePresent(Coord loc, String specificMsg) throws IllegalArgumentException {
    if (!this.getTileAt(loc).isPresent()) {
      throw new IllegalArgumentException(specificMsg);
    }
  }
}
