package com.fish.model.state;


import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: fill out comments / add as necessary

/**
 * Add Later
 */
public class HexGameState implements GameState {

  private GameStage gameStage;
  private GameBoard gameBoard;
  private List<Player> players; //only purpose is to increment score
  // int index = 0, 1, 2, 3
  private int currentPlayerIndex;
  private Map<Coord, PlayerColor> penguinLocs;


  /**
   * Initiates a game of HTMF that has not been started. Once all players are ready, the referee
   *  will start the game.
   */
  public HexGameState() {
    this.gameStage = GameStage.NOT_STARTED;
  }

  public void startGame(GameBoard board, List<Player> players) {
    this.gameStage = GameStage.PLACING_PENGUINS;
    this.penguinLocs = new HashMap<>();
    this.gameBoard = board;
    this.players = players;
    this.currentPlayerIndex = 0;
  }

  @Override
  public Tile getTileAt(Coord loc) {
    return this.gameBoard.getTileAt(loc);
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
    if (this.penguinLocs.get(from) == null) {
      throw new IllegalArgumentException("There is no Penguin here to move");
    }
    Player player = findPlayer(this.penguinLocs.get(from));

    if (this.gameBoard.getTileAt(from) == null || this.gameBoard.getTileAt(to) == null) {
      throw new IllegalArgumentException("Both tiles must not be empty!");
    }
    if (this.penguinLocs.get(to) != null) {
      throw new IllegalArgumentException("There is already a penguin here!");
    }

    PlayerColor color = this.penguinLocs.remove(from);
    this.penguinLocs.put(to, color);

    Tile tile = this.gameBoard.removeTileAt(from);
    player.addToScore(tile.getNumFish());
  }


  /////////////////////////////////Player Handling

  @Override
  public PlayerColor getCurrentPlayer() {
    return this.players.get(this.currentPlayerIndex).getColor();
  }

  @Override
  public void advanceToNextPlayer() {
    this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
  }


  @Override
  public int getPlayerScore(PlayerColor playerColor) {
    Player player = this.findPlayer(playerColor);

    return player.getScore();
  }


  @Override
  public void removePlayer(PlayerColor color) {
    Player toRemove = this.findPlayer(color);

    for (Coord cc : this.penguinLocs.keySet()) {
      if (this.penguinLocs.get(cc) == color) {
        this.penguinLocs.remove(cc);
      }
    }

    this.players.remove(toRemove);
    this.currentPlayerIndex %= this.players.size();
  }

  //Returns the player indicated by the given PlayerColor
  private Player findPlayer(PlayerColor color) {
    for (Player pp : this.players) {
      if (pp.getColor() == color) {
        return pp;
      }
    }

    throw new IllegalArgumentException("Player not found");
  }


  ///////////////////////State Handling
  @Override
  public GameStage getGameStage() {
    return this.gameStage;
  }

  @Override
  public boolean isGameOver() {
    List<Coord> pengs = new ArrayList<>(this.penguinLocs.keySet());
    for (Coord c : this.penguinLocs.keySet()) {
      if (this.gameBoard.getTilesReachableFrom(c, pengs).size() > 0) {
        return false;
      }
    }

    this.gameStage = GameStage.GAMEOVER;
    return true;
  }

  ///////////////////////Helpers
  @Override
  public int getWidth() {
    return this.gameBoard.getWidth();
  }

  @Override
  public int getHeight() {
    return this.gameBoard.getHeight();
  }
}
