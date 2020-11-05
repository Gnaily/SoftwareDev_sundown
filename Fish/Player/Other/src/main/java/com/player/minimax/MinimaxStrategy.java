package com.player.minimax;

import com.fish.game.HexGameTree;
import com.fish.game.Move;
import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.PlayerColor;
import com.fish.model.tile.ProtectedTile;
import com.fish.model.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Player Strategy class for placing and moving penguins. This class defines two static methods -
 * on that places penguins and another that calculates the best move for a penguin.
 *
 * Penguin placement is as follows:
 * - Given a GameState, place a penguin in the lowest row possible, and within each row, the lowest
 *  column possible. This method returns a Coord of the recommended penguin location
 *
 * Penguin movement is as follows:
 * - Given a GameState and a number of moves to look ahead, apply a minimax algorithm to the game
 *  tree for maximizing the current player's penguins after N turns (the number passed in). This
 *  version of minimax calculates the point value of the maximizing player's score at each node, then
 *  decides what to do depending on if it is the player's or an opponent's turn.
 *    - if it is the player's turn, select the move that results in the most points for the player
 *    - if it is an opponents turn, select the move that results in the fewest points for the player.
 */
public class MinimaxStrategy {

  /**
   * Basic Strategy for finding the next valid PenguinPlacement.
   *
   * Given a GameState, the next valid penguin placement is as followed:
   *  - the tile in the lowest row with only one 1 fish on it
   *  - if there are multiple tiles in the row that satisfy this condition, the one with the lowest
   *   column number.
   * @param gs the gamestate to find a penguin location for
   * @return The Coord representing where the player should put their penguin
   */
  public static Coord findNextPenguinPlacement(GameState gs) {
    Map<Coord, PlayerColor> penguins = gs.getPenguinLocations();
    for (int ii = 0; ii < gs.getHeight(); ii++) {
      for (int jj = 0; jj < gs.getWidth(); jj++) {
        // check: if there is a penguin at the location
        // if it is a legal place to put a penguin

        Coord loc = new Coord(jj, ii);
        if (penguins.get(loc) != null) {
          continue;
        }
        ProtectedTile tile = gs.getTileAt(loc);
        if (tile.isPresent() && tile.getNumFish() == 1) {
          return loc;
        }
      }
    }

    // return value is needed for compilation, however this line should never be reached
    return null;
  }

  /**
   * Find the best move for the current player after looking ahead N turns according to a basic minimax
   * algorithm. This looks ahead N turns for the current player, which at a max could be N*number of players
   * turns in the game of Fish.
   *
   * For each node, the starting player's score is calculated and the decision is made as follows:
   *  - if it is an opponent's turn, chose the move that results in the starting player having the fewest points
   *   out of all children nodes
   *  - if it is the starting player's turn, chose the move that results in the them having the most
   *   points out of all children nodes
   *
   * Note: in our implementation of GameState, players who do not have any moves are skipped automatically.
   * As a result, the gameState passed in will always have a move available for the current player
   * assuming that it is a valid gamestate.
   *
   * @param gs The Gamestate to evaluate the minimax function on
   * @param nn The number of moves to make on behalf of the starting player
   * @return The best move for the player as determined by the algorithm
   */
  public static Move findCurrentPlayersBestMove(GameState gs, int nn) {
    if (nn <= 1) {
      return MinMaxAlgorithm.findBestMove(gs);
    }
    MinMaxAlgorithm algo = new MinMaxAlgorithm(nn - 1, gs.getCurrentPlayer());
    List<MoveValue> moveValues = HexGameTree.applyToAllReachableStates(
        new HexGameTree(gs),
        algo,
        new ArrayList<>());

    MoveValue bestMove = MinMaxAlgorithm.calculateBestMove(moveValues).get(0);

    return bestMove.getMove();
  }

}
