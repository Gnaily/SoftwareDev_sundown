package com.fish.model.state;

// TODO: fill out comments

/**
 *
 */
public class Player {

  private int age;
  private PlayerColor color;
  private int score;

  public Player(int age) {
    this.age = age;
    this.score = 0;
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
}
