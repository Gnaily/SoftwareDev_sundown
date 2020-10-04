package com.fish.model;

import com.fish.player.Player;

public class Penguin {

  private PlayerColor color;
  private Coord location;

  public Penguin(PlayerColor color, Coord loc) {
    this.color = color;
    this.location = loc;
  }

  public PlayerColor getColor() {
    return this.color;
  }

  public Coord getLocation() {
    return this.location;
  }
}
