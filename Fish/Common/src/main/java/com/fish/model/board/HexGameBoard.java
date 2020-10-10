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

  public static final int MAX_FISH = 5;

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


  /////////////////////////////////Move Handling

  /**
   * Given a coordinate of origin, returns a list of all possible coordinates a player can
   * make a valid move to from the origin.
   * @param start the coord of origin
   * @return a list of Coord indicating the possible valid moves
   * @throws IllegalArgumentException if the coord of origin is out of bounds or is a hole
   */
  @Override
  public List<Coord> getTilesReachableFrom(Coord start) throws IllegalArgumentException {

    int x = start.getX();
    int y = start.getY();

    checkTileInBounds(start, "Cannot move from a tile that is out of bounds");
    checkTilePresent(start, "Cannot move from a tile that does not exist");
    List<Coord> moves = new ArrayList<>();

    moves.addAll(this.getTilesAboveBelow(x, y, -2));
    moves.addAll(this.getTilesAboveBelow(x, y, 2));

    moves.addAll(this.getTilesLeftDiagonal(x, y, 1));
    moves.addAll(this.getTilesLeftDiagonal(x, y, -1));

    moves.addAll(this.getTilesRightDiagonal(x, y, 1));
    moves.addAll(this.getTilesRightDiagonal(x, y, -1));

    return moves;
  }

  //Get valid moves directly above and below tile of origin
  //yIncrement == -2 -> up
  //yIncrement == 2 -> down
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

  //Get valid moves up-left diagonal OR down-left diagonal of tile of origin, depending on:
  //yIncrement == -1 -> up left
  //yIncrement == 1 -> down left
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

  //Get valid moves up-right diagonal OR down-right diagonal of tile of origin, depending on:
  //yIncrement == -1 -> up right
  //yIncrement == 1 -> down right
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

  /**
   * Given a coordinate location within the dimensions of the game board,
   * returns the Tile object at that coordinate location.
   * @param loc the coordinate location of the desired tile on the board
   * @return the Tile object at that location
   * @throws IllegalArgumentException if the requested tile is out of the bounds of the board
   */
  @Override
  public Tile getTileAt(Coord loc) throws IllegalArgumentException {
    checkTileInBounds(loc, "Cannot retrieve a tile not on the board");
    //We do not check for TilePresent because we want this to return null if the tile
    //has been removed to differentiate from an arg that is out of bounds vs a tile that's
    //been removed
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
    checkTileInBounds(loc, "Cannot remove a tile not on the board");
    checkTilePresent(loc, "Cannot remove a tile that has already been removed");
    int xx = loc.getX();
    int yy = loc.getY();

    Tile returnTile = getTileAt(loc);
    tiles[xx][yy] = null;
    return returnTile;
  }


  /////////////////////////////////Penguin Handling

  /**
   * Returns a HashMap of penguin locations, formatted such that the Coord is the unique
   * identifier of the location (since only one penguin can be on a tile at a time) and
   * the PlayerColor is the value, to identify which player's penguin is on that location.
   * @return a HashMap of Coord to PlayerColor values
   */
  @Override
  public HashMap<Coord, PlayerColor> getPenguinLocations(){
    return new HashMap<>(this.penguinLocs);
  }

  /**
   * Adds a penguin to the board at the given Coord location. Stores the PlayerColor of the
   * particular player whose penguin is being placed. This method it only to be used at game
   * start-up as players may not place a penguin once gameplay has begun.
   * @param loc the coordinate location to place a penguin
   * @param playerColor the color associated with the player placing the penguin
   * @throws IllegalArgumentException if given Coord is out of bounds or there is a hole there
   */
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

  /**
   * Removes the element of the penguinLocs HashMap with the given unique Coord.
   * @param loc the coordinate location to remove the penguin
   * @return the playerColor that was at that location
   * @throws IllegalArgumentException if there is no penguin present in the given location
   */
  @Override
  public PlayerColor removePenguin(Coord loc) throws IllegalArgumentException {
    //--> Needs a check in place to make sure the user can not remove another player's penguin
    if (this.penguinLocs.get(loc) == null) {
      throw new IllegalArgumentException("There is no Penguin here to move");
    }
    return this.penguinLocs.remove(loc);
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
    int xx = loc.getX();
    int yy = loc.getY();
    if (tiles[xx][yy] == null) {
      throw new IllegalArgumentException(specificMsg);
    }
  }

}
