package com.fish.view;

import com.fish.model.Coord;
import com.fish.model.PlayerColor;
import com.fish.model.board.GameBoard;
import com.fish.model.tile.Tile;

import java.awt.*;

import java.util.HashMap;
import javax.swing.*;

/**
 * Implementation of the drawing of a board of a game of Hey, That's my Fish!
 */
public class HexBoardView extends JPanel implements GameView {

  private GameBoard board;
  //Pixel Step is the length of the side of a single hex.
  //In the future this will be calculated by the dimensions of the board and what can fit
  //in a single window.
  final static int PIXEL_STEP = 50;

  public HexBoardView(GameBoard board) {
    this.board = board;
    this.setPreferredSize(this.calculateWindowSize());
  }


  @Override
  public void drawGame() {
    JFrame f = new JFrame("Hey, That's my Fish!");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(this);
    f.pack();
    f.setVisible(true);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    HexTileView htv = new HexTileView(g2d, PIXEL_STEP);

    //Draw just the hex tiles
    for (int ii = 0; ii < board.getWidth(); ii++) {
      for (int jj = 0; jj < board.getHeight(); jj++) {
        Tile tile = board.getTileAt(new Coord(ii, jj));
        if (tile != null) {
          htv.drawHexagon(tile, this.calculateTopLeftXValue(ii, jj), jj * PIXEL_STEP);
        }
      }
    }

    //Draw the penguins where necessary
    HashMap<Coord, PlayerColor> penguinLocs = board.getPenguinLocations();
    for (Coord c : penguinLocs.keySet()) {
      htv.drawPenguin(penguinLocs.get(c), calculateTopLeftXValue(c.getX(), c.getY()), c.getY() * PIXEL_STEP);
    }
  }



  //Calculates window size based off the size and number of hexagons
  Dimension calculateWindowSize() {
    int w = this.board.getWidth();
    int h = this.board.getHeight();

    return new Dimension(PIXEL_STEP * ((4 * w) + 1 - (2 * (h % 2))),
        PIXEL_STEP * (1 + h));
  }

  //Calculates the proper top left corner pixel coordinate given
  //the center x and y values of an image
  int calculateTopLeftXValue(int xx, int yy) {
    return PIXEL_STEP * (4 * xx + 1 + 2 * (yy % 2));
  }

}
