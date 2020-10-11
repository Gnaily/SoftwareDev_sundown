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
  


  @Override
  public Tile getTileAt(Coord loc) {
    return null;
  }

  @Override
  public PlayerColor getCurrentPlayer() {
    return null;
  }

  @Override
  public void placePenguin(Coord loc, PlayerColor playerColor) {

  }

  @Override
  public HashMap<Coord, PlayerColor> getPenguinLocations() {
    return null;
  }

  @Override
  public void movePenguin(Coord from, Coord to) {

  }

  @Override
  public int getPlayerScore(PlayerColor playerColor) {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }
}
