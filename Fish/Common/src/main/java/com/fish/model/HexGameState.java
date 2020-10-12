package com.fish.model;


import com.fish.model.board.GameBoard;
import com.fish.model.tile.Tile;
import java.util.HashMap;
import java.util.List;

/**
 * Add Later
 */
public class HexGameState implements GameState {
  GameStage gameStage;
  GameBoard gameBoard;
  List<Player> players;
  private HashMap<Coord, PlayerColor> penguinLocs;
  

  // TODO: Constructors
  // TODO: Figure out how to store current/next player

  @Override
  public Tile getTileAt(Coord loc) {
    return this.gameBoard.getTileAt(loc);
  }

  @Override
  public PlayerColor getCurrentPlayer() {
    return null;
  }

  /////////////////////////////////Penguin Handling

  @Override
  public HashMap<Coord, PlayerColor> getPenguinLocations(){
    return new HashMap<>(this.penguinLocs);
  }


  @Override
  public void placePenguin(Coord loc, PlayerColor playerColor) throws IllegalArgumentException {
    if (this.gameBoard.getTileAt(loc) == null) {
      throw new IllegalArgumentException("There is no tile here!");
    }
    if (this.penguinLocs.containsKey(loc)) {
      throw new IllegalArgumentException("There is already a penguin on this tile");
    }
    this.penguinLocs.put(loc, playerColor);
  }


  @Override
  public void movePenguin(Coord from, Coord to) throws IllegalArgumentException {
    //--> Needs a check in place to make sure the user can not remove another player's penguin
    if (this.penguinLocs.get(from) == null) {
      throw new IllegalArgumentException("There is no Penguin here to move");
    }
    if (this.gameBoard.getTileAt(from) == null || this.gameBoard.getTileAt(to) == null) {
      throw new IllegalArgumentException("Both tiles must not be empty!");
    }
    if (this.penguinLocs.get(to) != null) {
      throw new IllegalArgumentException("There is already a penguin here!");
    }

    PlayerColor color = this.penguinLocs.remove(from);
    this.penguinLocs.put(to, color);
  }

  // TODO: player scores
  @Override
  public int getPlayerScore(PlayerColor playerColor) {
    return 0;
  }

  // TODO: game over logic
  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public int getWidth() {
    return this.gameBoard.getWidth();
  }

  @Override
  public int getHeight() {
    return this.gameBoard.getHeight();
  }
}
