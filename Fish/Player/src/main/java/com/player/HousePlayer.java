package com.player;

import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.PlayerColor;

import java.util.List;

public class HousePlayer implements PlayerInterface {
  @Override
  public Coord getPenguinPlacement() {
    return null;
  }

  @Override
  public Move getPengiunMovement() {
    return null;
  }

  @Override
  public void receivePlayerRemoved(PlayerColor color) {

  }

  @Override
  public void receivePenguinPlacement(Coord loc, PlayerColor color) {

  }

  @Override
  public void receivePenguinMovement(Move move, PlayerColor color) {

  }

  @Override
  public void receiveInitialGameState(GameState gs) {

  }

  @Override
  public void receiveGameOver(List<PlayerColor> winners) {

  }
}
