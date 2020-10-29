# Referee Design and API
### Repo: Sundown

## Purpose

The referee manages a game of Hey, That's My Fish for a given set of Players.
The referee is in charge of running a complete game of Fish. To begin, this component
will set up a board that is suitable for the number of Players in the game. During
the course of play, the referee will query each player for their move. Once it
receives the player's choice, it will determine the validity of that action
and either make the move on behalf of the player or decide to remove the player
from the game. The referee is also responsible for updating all players and
observers of a game of Fish with what moves have been made. Once the game is
over, the referee will report the results to the tournament manager.

## Use Case

* <b>Tournament Manager:</b> The tournament manager will create the referee and
hand it the player's it would like the referee to run a game of Fish for. From there,
the manager will allow the referee to run the game and wait to receive the results.
* <b>Game Observer:</b> If any outside observers would like to follow the game of
Fish a referee is running, they will notify the referee that they would like to
observe the game. After that, the referee will send the observer all actions taken
by the Players of the game so that they can follow the nail-biting action.


## Data Description
### Fields
* Removed and cheating players
  * List of Players who did not adhere to the Fish code of conduct
* How to contact each Players
  * For each player, the referee needs to be able to send and recieve messages
* How to contact spectators of the game
* The current GameState
  * This will be constructed by the Referee
  * Initially distributed to all players so that they have an understanding of the board
  and can strategize
  * Referee will alter the state on behalf of each of the Players

### Constructor
* Take in the external representation of each Players
  * This includes age and contact method for each player
* Transform external players into internal Players
  * Assign colors
  * Determine initial order based on age
* Calculate the number of penguins each player should place
* Construct a Board and State that can be used for this game of Fish.


## API
* requestPlacePenguin()
  * Query the GameState for the current Player
  * Send a request to the current Player for their next pengiun placement
  * Alter GameState and/or kick the player depending on the legality of the action
* requestMovePenguin()
  * Query the GameState for the current Player
  * Send a request to the current Player for their next pengiun movement
  * Alter GameState and/or kick the player depending on the legality of the action
* reportStateChanged()
  * Broadcast to all players and spectators the new State
  * In the message, include what happened to cause the new State
    * Penguin placement
    * Penguin movement
    * Player kicked
* reportResults()
  * Once the game is over, report the final results to the tournament manager,
  players, and observers
  * This includes individual scores as well as who was kicked from the game
