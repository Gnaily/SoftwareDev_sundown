package com.fish.common.board;

import com.fish.common.Coord;
import com.fish.common.tile.HexTile;
import com.fish.common.tile.ProtectedTile;
import com.fish.common.tile.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of a GameBoard object in a game of Hey, That's my Fish! (HTMF)
 *
 * DATA DEFINITION:
 * A collection of hexagon-shaped tiles stored in a 2d-array of Tile called tiles.
 * The location of each Tile in the data representation of the board can be located using a Coord.
 * For any given Coord (x, y)
 * the x value represents the column number and
 * the y value represents the row number.
 *
 * <p>
 *  The Coordinate system for a hexGameBoard is designed as follows:
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
 *   This is an example of a board with 6 rows and 2 columns.
 *
 * </p>
 *
 * Note that the data structure for the above board looks as follows:
 * {{0,0  ,  0,1  ,  0,2  ,  0,3  ,  0,4  ,  0,5},
 *  {1,0  ,  1,1  ,  1,2  ,  1,3  ,  1,4  ,  1,5}};
 *
 * A Tile of the board illustrated above can be located using either one of the following methods:
 * tiles[0][1] OR
 * this.getTileAT(new Coord(1,0))
 * But in this implementation the later is used throughout because the method implements checks that
 * enforce the Tile object being retrieved is in fact on this board.
 *
 * This implementation assumes that the game is always initiated as a rectangular board,
 * hence the width and height fields. Technically, if a circular board is desired, this is made
 * possible using the constructor that takes in a list of Coords of where holes should be initiated
 * on the board. Just initiate holes around the pointed edges of the rectangular representation
 * to create a circular playing field.
 *
 * The Random object in the rand field is used to generate constant boards for testing
 * purposes through the use of a random seed.
 *
 * INTERPRETATION:
 * A HexGameBoard represents the collection of hexagon-shaped tiles that a game of HTMF is played on
 * where players can land and move their avatars around on. As the game proceeds, the board
 * changes by tiles becoming holes when players move their avatars from them.
 *
 */
public class HexGameBoard implements GameBoard {

  private Tile[][] tiles;
  private int width;
  private int height;
  private Random rand;

  private static final int MAX_FISH = 5;

  /**
   * Constructor to build a hexGameBoard with randomized fish numbers per tile.
   * @param rows the number of rows of tiles on the board
   * @param cols the number of columns of tiles on the board
   * @param holes a list of the specific location of holes in the initial board
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
   * Constructor to build a hexGameBoard with no holes and the same number of fish
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

    for (int ii = 0; ii < tiles.length; ii++) {
      Tile[] oneCol = tiles[ii];
      for (int jj = 0; jj < oneCol.length; jj++) {
        tiles[ii][jj] = new HexTile(numberOfFish);
      }
    }
  }


  /**
   * Convenience constructor for testing. Includes arg for Rand seed for consistent test outcomes.
   * @param rows the number of rows of tiles on the board
   * @param cols the number of columns of tiles on the board
   * @param holes a list of the specific location of holes in the initial board
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
   * Takes in a 2d Array of int where each int represents the num fish on a Tile, and
   * instantiates a board with those fish values at the corresponding Coord location.
   * Note that the 2d Array needs to be set up such that the x is the column number and
   * y is the row number, similar to the 2dArray example in the HexGameBoard class javadoc above.
   * @param values a 2d Array of integers, where each int represents the number of fish on a tile
   */
  public HexGameBoard(int[][] values) {
    if (values.length == 0 || values[0].length == 0) {
      throw new IllegalArgumentException("You must add at least one tile to the board.");
    }

    this.tiles = new Tile[values.length][values[0].length];
    this.width = values.length;
    this.height = values[0].length;

    for (int ii = 0; ii < this.width; ii++) {
      for (int jj = 0; jj < this.height; jj++) {
        if (values[ii][jj] == 0) {
          tiles[ii][jj] = new HexTile();
        }
        else {
          tiles[ii][jj] = new HexTile(values[ii][jj]);
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
    for (int ii = 0; ii < this.width; ii++) {
      for (int jj = 0; jj < this.height; jj++) {
        if (holes.contains(new Coord(ii, jj))) {
          this.tiles[ii][jj] = new HexTile();
        }
        else {
          this.tiles[ii][jj] = new HexTile(tileFishValues.remove(
              this.rand.nextInt(tileFishValues.size())));
        }
      }
    }
  }

  //Generates a list of random tile fish values to initialize each Tile
  private List<Integer> generateTileValues(int numValsNeeded, int minOneFishTiles) {
    int ones = 0;
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
   * @param origin the coord of origin
   * @param penguinLocs the locations of all penguins on this board
   * @return a list of Coord indicating the possible valid moves
   * @throws IllegalArgumentException if the coord of origin is out of bounds or is a hole
   */
  @Override
  public List<Coord> getTilesReachableFrom(Coord origin, List<Coord> penguinLocs) throws IllegalArgumentException {

    int x = origin.getX();
    int y = origin.getY();

    checkTileInBounds(origin, "Cannot move from a tile that is out of bounds");
    checkTilePresent(origin, "Cannot move from a hole");
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


  //Generates a list of all Coord locations reachable from a starting xx, yy Coord point
  //in a straight line stemming from the point of origin.
  //Holes and penguins are considered blockers that stop tiles from being reachable.
  // param op is an operation that defines how the x value should be changed
  // param yIncrement is the value to increment/decrement y by in the search for valid Tiles
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
   * returns the ProtectedTile object at that coordinate location.
   * The result of this method may be sent to an external player to evaluate the number of points
   * they may receive from passing over the tile. For this reason, the return tile must be
   * protected so that they cannot mutate the tile or turn it into a hole.
   * @param loc the coordinate location of the desired tile on the board
   * @return the ProtectedTile object at that location
   * @throws IllegalArgumentException if the requested tile is out of the bounds of the board
   */
  @Override
  public ProtectedTile getTileAt(Coord loc) throws IllegalArgumentException {
    checkTileInBounds(loc, "The tile you requested is out of bounds");
    return tiles[loc.getX()][loc.getY()];
  }

  /**
   * Given a coordinate location within the dimensions of the game board,
   * turns that Tile into a hole by altering its isPresent boolean to False.
   * Returns the tile as a ProtectedTile so that it is immutable but can be sent to an external
   * player to know the number of fish at that tile.
   * @param loc the coordinate location of the tile to remove on the board
   * @return the Tile object at that location
   * @throws IllegalArgumentException if the requested tile is out of bounds or is already a hole
   * on the board
   */
  @Override
  public ProtectedTile removeTileAt(Coord loc) throws IllegalArgumentException {
    checkTileInBounds(loc, "Cannot remove a tile that is not on the board");
    checkTilePresent(loc, "Cannot remove a tile where a hole is already located");

    Tile TileToRemove = tiles[loc.getX()][loc.getY()];
    TileToRemove.meltTile();
    return TileToRemove;
  }


  /////////////////////////////////Getters and Helpers

  public GameBoard getCopyGameBoard() {
    return new HexGameBoard(this.getBoardDataRepresentation());
  }

  /**
   * Constructs a 2dArray of integers that represent the number of fish on the Tile at location
   * Coord(jj, ii), where ii and jj directly correlate to the indices of a 2dArray[ii][jj]
   * (note the ii's and jj's are inverted between the Coord representation and the 2dArray indices)
   * @return a 2dArray of integers
   */
  public int[][] getBoardDataRepresentation() {
    int[][] boardDataRep = new int[this.getWidth()][this.getHeight()];
    //Gets a 2d array of int representing the number of fish on each tile at Coord(ii, jj)
    for (int ii = 0; ii < this.getWidth(); ii++) {
      for (int jj = 0; jj < this.getHeight(); jj++) {
        if (this.getTileAt(new Coord(ii,jj)).isPresent()) {
          boardDataRep[ii][jj] = this.getTileAt(new Coord(ii,jj)).getNumFish();
        }
        else {
          boardDataRep[ii][jj] = 0;
        }
      }
    }
    return boardDataRep;
  }

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


  @Override
  public boolean equals(Object o) {
    if (o instanceof HexGameBoard) {
      HexGameBoard other = (HexGameBoard) o;
      // BOARD EQUALITY:
      // SAME NUMBER OF TILES, HOLES in the same places, TILES with same num fish
      if (this.width == other.getWidth() && this.height == other.getHeight()) {
        for (int ii = 0; ii < this.width; ii++) {
          for (int jj = 0; jj < this.height; jj++) {
            Coord loc = new Coord(ii, jj);
            ProtectedTile thisTile = this.getTileAt(loc);
            ProtectedTile otherTile = other.getTileAt(loc);
            if (!thisTile.equals(otherTile)) {
              return false;
            }
          }
        }
        return true;
      }
    }
    return false;
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
