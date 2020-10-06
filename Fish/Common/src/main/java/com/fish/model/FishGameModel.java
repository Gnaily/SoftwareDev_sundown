package com.fish.model;

import com.fish.model.tile.Tile;
import com.fish.player.Player;

import java.util.List;

public interface FishGameModel {

  Tile getTileAt(Coord location);

  List<Player> getPlayers();

  int getBoardWidth();

  int getBoardHeight();
}
