package com.fish.player;
import com.fish.common.Coord;
import com.fish.common.game.Move;
import com.fish.common.state.GameStage;
import com.fish.common.state.GameState;
import com.fish.common.state.PlayerColor;
import java.util.List;

/**
 * Implementation of the PLayer interface that defines what actions a player should be able to make.
 * In this case, this is a local player who uses the minimax strategy to determine their moves.
 *
 * Depth to traverse the Minimax strategy can be set in the constructor.
 */
public class HousePlayer implements PlayerInterface {

  private int depth;
  private GameState gs;
  private String name;

  public HousePlayer(int depth, String name) {
    this.depth = depth;
    this.name = name;
  }


  @Override
  public Coord getPenguinPlacement() {
    if (this.gs != null) {
      return MinimaxStrategy.findNextPenguinPlacement(this.gs);
    }

    throw new IllegalArgumentException("State must be set before placing penguins");
  }

  @Override
  public Move getPengiunMovement() {
    if (this.gs != null) {
      if (this.gs.getGameStage() != GameStage.IN_PLAY) {
        this.gs.startPlay();
      }
      return MinimaxStrategy.findCurrentPlayersBestMove(this.gs, this.depth);
    }

    throw new IllegalArgumentException("State must be set before moving penguins");
  }

  @Override
  public void receivePlayerRemoved(PlayerColor color) {
    this.gs.removeCurrentPlayer();
  }

  @Override
  public void receivePenguinPlacement(Coord loc, PlayerColor color) {
    this.gs.placePenguin(loc, color);
  }

  @Override
  public void receivePenguinMovement(Move move, PlayerColor color) {
    if (this.gs.getGameStage() != GameStage.IN_PLAY) {
      this.gs.startPlay();
    }
    this.gs.movePenguin(move.getOrigin(), move.getDestination());
  }

  /**
   * Recieve the initial gamestate for this game of Fish. The implementation of this class assumes
   * that this is a mutable version of the gamestate that they are able to manipulate for testing
   * and planning purposes.
   *
   * @param gs the starting gamestate (with no pengiuns) passed to the player by the referee.
   */
  @Override
  public void receiveInitialGameState(GameState gs) {
    this.gs = gs;
  }

  @Override
  public void receiveGameOver(List<PlayerColor> winners) {
    // this method does nothing for now - this player does not care or have anything to do once they
    // have received the winners.
  }

  // Used for outputting the results of a test game of fish.
  @Override
  public String toString() {
    return this.name + " : " + this.depth;
  }
}
