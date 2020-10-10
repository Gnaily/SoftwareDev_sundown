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

    //Draw just the hex tiles
    for (int ii = 0; ii < board.getWidth(); ii++) {
      for (int jj = 0; jj < board.getHeight(); jj++) {
        Tile tile = board.getTileAt(new Coord(ii, jj));
        if (tile != null) {
          this.drawHexagon(tile, this.calculateTopLeftXValue(ii, jj), jj * PIXEL_STEP, g2d);
        }
      }
    }

    //Draw the penguins where necessary
    HashMap<Coord, PlayerColor> penguinLocs = board.getPenguinLocations();
    for (Coord c : penguinLocs.keySet()) {
      this.drawPenguin(penguinLocs.get(c), calculateTopLeftXValue(c.getX(), c.getY()), c.getY() * PIXEL_STEP, g2d);
    }
  }

  /**
   * Draw the tile with the top-left coordinate located at (x, y)
   *
   * @param tile tile to draw
   * @param xx Top Left x pixel of where to draw the hexagon
   * @param yy Top left y pixel of where to draw the hexagon
   */
  private void drawHexagon(Tile tile, int xx, int yy, Graphics2D g2d) {

    // light blue, icy color
    g2d.setColor(new Color(158, 195, 255));

    int[] xValues = {xx, xx + PIXEL_STEP, xx + 2 * PIXEL_STEP, xx + PIXEL_STEP, xx, xx - PIXEL_STEP};
    int[] yValues = {yy, yy, yy + PIXEL_STEP, yy + 2 * PIXEL_STEP, yy + 2 * PIXEL_STEP, yy + PIXEL_STEP};

    g2d.fillPolygon(xValues, yValues, 6);
    g2d.setColor(Color.WHITE);
    g2d.drawPolygon(xValues, yValues, 6);

    // drawing all fish on tile
    for (int ii = 1; ii <= tile.getNumFish(); ii++) {
      g2d.setColor(Color.PINK);
      g2d.fillOval(xx, yy + PIXEL_STEP / 3 * ii, PIXEL_STEP, PIXEL_STEP / 4);
      int[] xs = {xx, xx, xx + PIXEL_STEP / 4};
      int[] ys = {yy + PIXEL_STEP / 3 * ii,
              yy + PIXEL_STEP / 3 * ii + PIXEL_STEP / 4, yy + PIXEL_STEP / 3 * ii + PIXEL_STEP  / 8};

      g2d.fillPolygon(xs, ys, 3);

      g2d.setColor(Color.BLACK);
      g2d.fillOval(xx + PIXEL_STEP * 3 / 4, yy + PIXEL_STEP / 3 * ii + PIXEL_STEP / 16, PIXEL_STEP / 16, PIXEL_STEP / 16);
    }
  }

  /**
   * Draw a penguin of given color on tile (xx, yy)
   *
   * @param pc Color to draw the penguin
   * @param xx x-value of the location to draw the penguin
   * @param yy y-value of the location to draw the penguin
   */
  private void drawPenguin(PlayerColor pc, int xx, int yy, Graphics2D g2d) {
    // penguin head
    switch(pc) {
      case BLACK:
        g2d.setColor(Color.BLACK);
        break;
      case WHITE:
        g2d.setColor(Color.WHITE);
        break;
      case RED:
        g2d.setColor(Color.RED);
        break;
      case BROWN:
        g2d.setColor(new Color(102, 51, 0));
        break;
    }
    g2d.fillOval(xx, yy + PIXEL_STEP/2, PIXEL_STEP, PIXEL_STEP);

    // penguin eyes
    g2d.setColor(Color.WHITE);
    g2d.fillOval(xx + PIXEL_STEP / 4, yy + 3 * PIXEL_STEP / 4, PIXEL_STEP / 4, PIXEL_STEP / 4);
    g2d.fillOval(xx + PIXEL_STEP / 2, yy + 3 * PIXEL_STEP / 4, PIXEL_STEP / 4, PIXEL_STEP / 4);
    g2d.setColor(Color.BLACK);
    g2d.drawOval(xx + PIXEL_STEP / 4, yy + 3 * PIXEL_STEP / 4, PIXEL_STEP / 4, PIXEL_STEP / 4);
    g2d.drawOval(xx + PIXEL_STEP / 2, yy + 3 * PIXEL_STEP / 4, PIXEL_STEP / 4, PIXEL_STEP / 4);
    g2d.fillOval(xx + PIXEL_STEP * 5 / 16, yy + PIXEL_STEP * 13 / 16, PIXEL_STEP / 8, PIXEL_STEP / 8);
    g2d.fillOval(xx + PIXEL_STEP * 9 / 16, yy + PIXEL_STEP * 13 / 16, PIXEL_STEP / 8, PIXEL_STEP / 8);

    // penguin beak
    g2d.setColor(Color.ORANGE);
    int[] xs = {xx + PIXEL_STEP / 4, xx + PIXEL_STEP * 3 / 4, xx + PIXEL_STEP / 2};
    int[] ys = {yy + PIXEL_STEP * 9 / 8, yy + PIXEL_STEP * 9 / 8, yy + PIXEL_STEP * 11 / 8};
    g2d.fillPolygon(xs, ys, 3);
  }



  //Calculates window size based off the PIXEL_STEP and number of hexagons
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
