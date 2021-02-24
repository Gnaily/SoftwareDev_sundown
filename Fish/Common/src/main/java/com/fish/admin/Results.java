package com.fish.admin;

import com.fish.player.PlayerInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * Summarizes game results of a game of HTMF.
 * Only functionality is to retrieve outcome.
 */
public class Results {
  List<PlayerInterface> winners;
  List<PlayerInterface> cheaters;

  public Results(List<PlayerInterface> winners, List<PlayerInterface> cheaters) {
    this.winners = winners;
    this.cheaters = cheaters;
  }

  public List<PlayerInterface> getWinners() {
    return new ArrayList<>(this.winners);
  }

  public List<PlayerInterface> getCheaters() {
    return new ArrayList<>(this.cheaters);
  }

}
