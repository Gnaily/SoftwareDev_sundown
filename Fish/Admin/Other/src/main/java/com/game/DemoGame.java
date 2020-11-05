package com.game;

import com.fish.externalplayer.PlayerInterface;
import com.player.HousePlayer;
import com.referee.HexReferee;
import com.referee.Referee;
import com.referee.Results;
import java.util.ArrayList;
import java.util.List;

/**
 * This main method can be used to run a demo game of Fish - ie how the manager might run a
 * game and then get the results.
 *
 * Instead of doing anything useful with the results (ie like determining the next game or recording
 * player statistics), this prints out the winners and the cheaters to STDOUT.
 */
public class DemoGame {

  public static void main(String[] args) {

    Referee ref = new HexReferee();

    List<PlayerInterface> extPlayers = new ArrayList<>();
    extPlayers.add(new HousePlayer(2, "brock"));
    extPlayers.add(new HousePlayer(1, "alanna"));

    Results results = ref.runGame(extPlayers);

    System.out.println("winners");
    for (PlayerInterface pi : results.getWinners()) {
      System.out.println(pi.toString());
    }

    System.out.println("cheaters");
    for (PlayerInterface pi : results.getCheaters()) {
      System.out.println(pi.toString());
    }

  }
}
