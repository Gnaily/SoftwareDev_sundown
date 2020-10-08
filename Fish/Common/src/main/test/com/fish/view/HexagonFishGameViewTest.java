package com.fish.view;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;
import com.fish.model.board.GeneralGameBoard;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.Assert.*;

public class HexagonFishGameViewTest {

  private GameBoard board;
  private HexagonFishGameView hfgv;

  @Before
  public void setUp() throws Exception {
    this.board = new GeneralGameBoard(4, 5,
            Arrays.asList(new Coord(0, 1), new Coord(0, 2), new Coord(3, 3)),
            4, false);
    this.hfgv = new HexagonFishGameView(this.board);
  }

  @Test
  public void testCalculateTopLeftCorner() {
    int step = HexagonFishGameView.PIXEL_STEP;
    assertEquals(new Coord(step, 0), this.hfgv.calculateTopLeftCorner(0, 0));
    assertEquals(new Coord(step * 15, step * 3), this.hfgv.calculateTopLeftCorner(3, 3));
    assertEquals(new Coord(step * 37, step * 12), this.hfgv.calculateTopLeftCorner(9, 12));
  }

  @Test
  public void testGetWindowDimension() {
    int step = HexagonFishGameView.PIXEL_STEP;
    assertEquals(new Dimension(step * 3, step * 2), this.hfgv.calculateWindowSize(1, 1));
  }
}