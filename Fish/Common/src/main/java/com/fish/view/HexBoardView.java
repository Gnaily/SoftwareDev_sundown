package com.fish.view;

import com.fish.model.Coord;
import com.fish.model.state.GameState;
import com.fish.model.state.PlayerColor;
import com.fish.model.tile.Tile;

import java.awt.*;

import java.util.HashMap;
import javax.swing.*;

/**
 * Implementation of the drawing the GUI a game of Hey, that's my fish! (HTMF).
 * This implementation uses hexagon-shaped images to represent a Tile in the HTMF game.
 * Each hexagon image may over-lay an image of a pink oval, which is a fish,
 * and a colored circle with a penguin face, which represents a player's penguin of the associated
 * player color.
 * The dimensions of the board are determined by...
 * (FILL IN LATER)
 */
public class HexBoardView extends JPanel implements GameView {

  private GameState state;
  //Pixel Step is the length of the side of a single hex.
  //In the future this will be calculated by the dimensions of the state and what can fit
  //in a single window.
  final static int PIXEL_STEP = 50;

  /**
   * Constructor for rendering the image of the hexagon board.
   * @param state a GameView object that contains all of the logistical data for placing images in
   * the correct location in the view
   */
  public HexBoardView(GameState state) {
    this.state = state;
    this.setPreferredSize(this.calculateWindowSize());
  }

  /**
   * Draws each game piece needed in a game of HTMF and brings together the layout of the game.
   */
  @Override
  public void drawGame() {
    JFrame f = new JFrame("Hey, That's my Fish!");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(this);
    f.pack();
    f.setVisible(true);
  }

  //Draw the collection of hex tiles, skipping over null values
  //Then draw the peguins at the appropriate locations
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    for (int ii = 0; ii < state.getWidth(); ii++) {
      for (int jj = 0; jj < state.getHeight(); jj++) {
        Tile tile = state.getTileAt(new Coord(ii, jj));
        if (tile != null) {
          this.drawHexagon(tile, this.calculateTopLeftXValue(new Coord(ii, jj)), jj * PIXEL_STEP, g2d);
        }
      }
    }

    HashMap<Coord, PlayerColor> penguinLocs = state.getPenguinLocations();
    for (Coord c : penguinLocs.keySet()) {
      this.drawPenguin(penguinLocs.get(c), calculateTopLeftXValue(c),
          c.getY() * PIXEL_STEP, g2d);
    }
  }

  /**
   * Render the image of a single hexagon-shaped tile with the top-left coordinate located at (x, y)
   * Then, render the image of pink fish on that hexagon tile
   *
   * @param tile tile to draw
   * @param xx Top Left x pixel of where to draw the hexagon
   * @param yy Top left y pixel of where to draw the hexagon
   */
  private void drawHexagon(Tile tile, int xx, int yy, Graphics2D g2d) {

    // light blue, icy color
    g2d.setColor(new Color(158, 195, 255));

    // the (x, y) coordinates on the panel for each of the points in the hexagon
    // starting with the top left corner of the hexagon and moving in a clockwise direction to
    // each vertex.
    int[] xValues = {xx, xx + PIXEL_STEP, xx + 2 * PIXEL_STEP, xx + PIXEL_STEP, xx, xx - PIXEL_STEP};
    int[] yValues = {yy, yy, yy + PIXEL_STEP, yy + 2 * PIXEL_STEP, yy + 2 * PIXEL_STEP, yy + PIXEL_STEP};

    g2d.fillPolygon(xValues, yValues, 6);
    g2d.setColor(Color.WHITE);
    g2d.drawPolygon(xValues, yValues, 6);

    // drawing all fish on tile
    for (int ii = 1; ii <= tile.getNumFish(); ii++) {
      g2d.setColor(Color.PINK);
      // main body of the fish
      g2d.fillOval(xx, yy + PIXEL_STEP / 3 * ii, PIXEL_STEP, PIXEL_STEP / 4);

      // the (x, y) coordinates of the triangle for the fish's tail
      int[] xs = {xx, xx, xx + PIXEL_STEP / 4};
      int[] ys = {yy + PIXEL_STEP / 3 * ii,
              yy + PIXEL_STEP / 3 * ii + PIXEL_STEP / 4, yy + PIXEL_STEP / 3 * ii + PIXEL_STEP  / 8};

      g2d.fillPolygon(xs, ys, 3);

      // The fish eyeball dot
      g2d.setColor(Color.BLACK);
      g2d.fillOval(xx + PIXEL_STEP * 3 / 4, yy + PIXEL_STEP / 3 * ii + PIXEL_STEP / 16,
          PIXEL_STEP / 16, PIXEL_STEP / 16);
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

    // penguin pupils
    g2d.fillOval(xx + PIXEL_STEP * 5 / 16, yy + PIXEL_STEP * 13 / 16, PIXEL_STEP / 8, PIXEL_STEP / 8);
    g2d.fillOval(xx + PIXEL_STEP * 9 / 16, yy + PIXEL_STEP * 13 / 16, PIXEL_STEP / 8, PIXEL_STEP / 8);

    // penguin beak
    g2d.setColor(Color.ORANGE);
    // (x, y) coordinates for the triangular beak
    int[] xs = {xx + PIXEL_STEP / 4, xx + PIXEL_STEP * 3 / 4, xx + PIXEL_STEP / 2};
    int[] ys = {yy + PIXEL_STEP * 9 / 8, yy + PIXEL_STEP * 9 / 8, yy + PIXEL_STEP * 11 / 8};
    g2d.fillPolygon(xs, ys, 3);
  }

  //Calculates window size based off the PIXEL_STEP and number of hexagons
  Dimension calculateWindowSize() {
    int w = this.state.getWidth();
    int h = this.state.getHeight();

    /*
     The width of the window is determined as follows:
      - 4 times the width of the board (number of columns) + 1

      Each hexagon has a width of 3 units. The first column consists of 5 total units of width.
      Each additional column adds 4 more units of width to the total.

      | -- 5 ---| -- 4 -- |
        ,--.     >--.
       /    \___/    \___
       \0,0 /   \1,0 /   \
        >--< 0,1 >--< 1,1|
            \___/    \___/


     */


    return new Dimension(PIXEL_STEP * ((4 * w) + 1),
        PIXEL_STEP * (1 + h));
  }

  //Calculates the proper top left corner pixel coordinate given
  //the center x and y values of an image
  int calculateTopLeftXValue(Coord hexLocation) {

    /*
      |1| - 4 - | -2-|
        ,--.     >--.
       /    \___/    \___
       \0,0 /   \1,0 /   \
        >--< 0,1 >--< 1,1|
            \___/    \___/

        The top left x value of a hexagon's location moves by 4 every increase in x.
        For hexagons on odd y values (for example, (1,1), the x location is increased by 2 from the
        previous drawing point.
        All of these values are offset by an initial value of 1
     */
    return PIXEL_STEP * (4 * hexLocation.getX() + 1 + 2 * (hexLocation.getY() % 2));
  }

}
