package com.player;

import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.PlayerColor;

import java.util.List;

public interface PlayerInterface {

  // REQUESTS //

  Coord getPenguinPlacement();

  Move getPengiunMovement();


  // MESSAGES //

  void receivePlayerRemoved(PlayerColor color);

  void receivePenguinPlacement(Coord loc, PlayerColor color);

  void receivePenguinMovement(Move move, PlayerColor color);

  void receiveInitialGameState(GameState gs);

  void receiveGameOver(List<PlayerColor> winners);

}
