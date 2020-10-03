package com.fish.player;

public interface Player {

  // need a definition for a coordinate/penguin
  ArrayList<Coord> getPenguinLocations();

  String getColor();

  int getScore();
}
