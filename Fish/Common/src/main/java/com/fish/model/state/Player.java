package com.fish.model.state;


/**
 * Class to represent the internal player during a game of Fish
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
   * Sets the player's color for a game of HTMF. This method will be used by the referee to assign
   * colors to players
   *
   * @param color Color to set this player to
   */
  public void setColorColor(PlayerColor color) {
    this.color = color;
  }

  /**
   * Get the age of this player
   *
   * @return the player's age
   */
  public int getAge() {
    return this.age;
  }

  /**
   * Get this player's score during a game of HTMF
   *
   * @return this player's score
   */
  public int getScore() {
    return this.score;
  }

  /**
   * Add points to this player's score during a game of HTMF
   *
   * @param points the points to add
   */
  public void addToScore(int points) {
    this.score += points;
  }

  /**
   * Get this player's assigned color
   *
   * @return the player's color
   */
  public PlayerColor getColor() {
    return this.color;
  }
}
