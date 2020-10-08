package com.fish.view;

import com.fish.model.Coord;
import com.fish.model.board.GameBoard;

import com.fish.model.board.HexGameBoard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;


public class HexBoardViewTest {

  private GameBoard board;
  private GameView hbv;

  @Before
  public void setUp() throws Exception {
    this.board = new HexGameBoard(4, 5,
        Arrays.asList(new Coord(0, 1), new Coord(0, 2), new Coord(3, 3)),
        4, 1);

    this.hbv = new HexBoardView(this.board);
  }

//  @Test
//  public void testCalculateTopLeftCorner() {
//    //We know that PIXEL_STEP is set to 50 for now
//    int step = 50;
//    Assert.assertEquals(new Coord(step, 0), this.hbv.calculateTopLeftCorner(0, 0));
//    Assert.assertEquals(new Coord(step * 15, step * 3), this.hbv.calculateTopLeftCorner(3, 3));
//    Assert.assertEquals(new Coord(step * 37, step * 12), this.hbv.calculateTopLeftCorner(9, 12));
//  }
//
//  @Test
//  public void testGetWindowDimension() {
//    //We know that PIXEL_STEP is set to 50 for now
//    int step = 50;
//    Assert.assertEquals(new Dimension(step * 3, step * 2), this.hbv.calculateWindowSize());
//  }
}