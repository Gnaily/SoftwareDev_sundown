package com.fish.view;

import com.fish.model.Coord;
import com.fish.model.PlayerColor;
import com.fish.model.tile.Tile;


import java.awt.*;


public class HexagonTileView {

  private Graphics2D g2d;
  private int size;


  public HexagonTileView(Graphics2D g2d, int size) {
    this.g2d = g2d;
    this.size = size;
  }


  /**
   * Renders the image of the hexagon using lines. Sets the color of the hexagon to red.
   */
  public void drawHexagon(Tile tile, Coord topLeft) {
    int xx = topLeft.getX();
    int yy = topLeft.getY();

    g2d.setColor(Color.BLUE);

    int[] xValues = {xx, xx + size, xx + 2 * size, xx + size, xx, xx - size};
    int[] yValues = {yy, yy, yy + size, yy + 2 * size, yy + 2 * size, yy + size};

    g2d.fillPolygon(xValues, yValues, 6);

    g2d.setColor(Color.WHITE);
    g2d.drawPolygon(xValues, yValues, 6);

    // drawing fish
    g2d.setColor(Color.PINK);
    for (int ii = 1; ii <= tile.getNumFish(); ii++) {
      g2d.fillOval(xx, yy + size / 3 * ii, size, size / 4);
    }
  }

  /**
   * Renders a penguin
   */
  public void drawPenguin(Coord c, PlayerColor pc) {
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

    g2d.fillOval(c.getX(), c.getY() + size/2, size, size);
  }

}
