package com.fish.model.state;


/**
 * Class to represent the internal player during a game of Fish. The Player of a game of HTMF
 *  contains information about their score, age, and color. Before a game of HTMF begins, the
 *  referee will assign each of the Player's a color and send them to the GameState.
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
   * @param color (PlayerColor) Color to set this player to
   */
  public void setPlayerColor(PlayerColor color) {
    this.color = color;
  }

  /**
   * Get the age of this player
   *
   * @return (int) the player's age
   */
  public int getAge() {
    return this.age;
  }

  /**
   * Get this player's score during a game of HTMF
   *
   * @return (int) this player's score
   */
  public int getScore() {
    return this.score;
  }

  /**
   * Add points to this player's score during a game of HTMF
   *
   * @param points (int) the points to add
   */
  public void addToScore(int points) {
    this.score += points;
  }

  /**
   * Get this player's assigned color
   *
   * @return (PlayerColor) the player's color
   */
  public PlayerColor getColor() {
    return this.color;
  }

  /**
   * Returns a copy of this Player
   * @return the Player
   */
  public Player getCopyPlayer() {
    Player p = new Player(this.age);
    p.addToScore(this.score);
    p.setPlayerColor(this.color);
    return p;
  }
}
