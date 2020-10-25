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

  public Player(int age, PlayerColor pc) {
    this.age = age;
    this.color = pc;
    this.score = 0;
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
   * Get this player's assigned color
   *
   * @return (PlayerColor) the player's color
   */
  public PlayerColor getColor() {
    return this.color;
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
   * Returns a copy of this Player
   * @return the Player
   */
  public Player getCopyPlayer() {
    Player p = new Player(this.age, this.color);
    p.addToScore(this.score);
    return p;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Player) {
      Player other = (Player) o;

      return this.score == other.getScore() && this.age == other.getAge() && this.color == other.getColor();


    }

    return false;
  }
}
