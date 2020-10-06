package com.fish.player;

import com.fish.model.Coord;
import com.fish.model.PlayerColor;

import java.util.List;

public interface Player {

  List<Coord> getPenguinLocations();

  PlayerColor getColor();

  int getScore();

  void movePenguin(Coord start, Coord finish);
}
