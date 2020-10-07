package com.fish.view;

import com.fish.model.Coord;
import com.fish.model.Penguin;
import com.fish.model.PlayerColor;
import com.fish.model.board.GameBoard;
import com.fish.model.board.GeneralGameBoard;
import com.fish.model.tile.Tile;

import java.awt.*;
import java.util.ArrayList;

import java.util.HashMap;
import javax.swing.*;

public class HexagonFishGameView extends JPanel {

  private GameBoard board;

  // the length of one side of a hexagon
  // In the future, this should scale with number of tiles
  final static int PIXEL_STEP = 50;

  public HexagonFishGameView(GameBoard board) {
    this.board = board;

    // adjust for entire board
    this.setPreferredSize(this.calculateWindowSize(this.board.getWidth(), this.board.getHeight()));
  }

  Dimension calculateWindowSize(int boardWidth, int boardHeight) {
    return new Dimension(PIXEL_STEP * (4 * boardWidth + 1 - 2 * (boardHeight % 2)),
            PIXEL_STEP * (1 + boardHeight));
  }

  /**
   * Renders the image of the hexagon using lines. Sets the color of the hexagon to red.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    HexagonTileView htv = new HexagonTileView(g2d, PIXEL_STEP);

    for (int ii = 0; ii < board.getWidth(); ii++) {
      for (int jj = 0; jj < board.getHeight(); jj++) {
        Tile tile = board.getTileAt(ii, jj);
        if (tile != null) {
          htv.drawHexagon(board.getTileAt(ii, jj),
                  this.calculateTopLeftCorner(ii, jj));
        }
      }
    }

    HashMap<Coord, PlayerColor> penguinLocs = board.getPenguinLocations();

    for (Coord c : penguinLocs.keySet()) {
      htv.drawPenguin(c, penguinLocs.get(c));
    }
  }

  Coord calculateTopLeftCorner(int xx, int yy) {
    return new Coord(PIXEL_STEP * (4 * xx + 1 + 2 * (yy % 2)),
            yy * PIXEL_STEP);
  }


  /**
   * Main method used for Rendering boards of different sizes
   *
   * @param args Command line arguments, not used
   */
  public static void main(String[] args) {

    JFrame f = new JFrame("Hexagon");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    GameBoard board = new GeneralGameBoard(1, 3,
            //Arrays.asList(new Coord(0, 1), new Coord(0, 2), new Coord(3, 3)),
            new ArrayList<>(),
            1, false);

    JPanel panel = new HexagonFishGameView(board);
    f.add(panel);

    f.pack();
    f.setVisible(true);
  }
}

/**
 * ii * 4 * SIZE + SIZE + 2 * SIZE * (jj % 2)
 *
 * SIZE * (4 * ii + 1 + 2 * (jj % 2))
 */
