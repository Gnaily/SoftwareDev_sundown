package com.fish.model;

import java.util.ArrayList;
import java.util.List;

// TODO: fill out comments

/**
 *
 */
public class Player {

  private int age;
  private PlayerColor color;
  private int score;
  //private List<Coord> penguinLocs;

  public Player(int age) {
    this.age = age;
    this.score = 0;
    //this.penguinLocs = new ArrayList<>();
  }


  /**
   *
   * @param color
   */
  public void setColorColor(PlayerColor color) {
    this.color = color;
  }

  /**
   *
   * @return
   */
  public int getAge() {
    return this.age;
  }

  /**
   *
   * @return
   */
  public int getScore() {
    return this.score;
  }

  /**
   *
   * @param points
   */
  public void addToScore(int points) {
    this.score += points;
  }

  /**
   *
   * @return
   */
  public PlayerColor getColor() {
    return this.color;
  }


//  /**
//   *
//   * @param loc
//   */
//  public void removePenguin(Coord loc) {
//    this.penguinLocs.remove(loc);
//  }
//
//
//  /**
//   *
//   * @param loc
//   */
//  public void addPenguin(Coord loc) {
//    this.penguinLocs.add(loc);
//  }
//
//  public List<Coord> getPenguinLocations() {
//    return new ArrayList<>(this.penguinLocs);
//  }

}
