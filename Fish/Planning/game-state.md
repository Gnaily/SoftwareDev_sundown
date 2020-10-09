# Game State Planning

### Data Representation
&nbsp;&nbsp;Our game state will be managed by a model interface which will contain all of the
necessary data and functionality to play a fair game of Hey, That’s my Fish! To
achieve this, the model will keep track of a List of `Players` participating in the
game. Each player will have a `PlayerColor`, score, and age associated with them.


&nbsp;&nbsp; Next, a `Referee` must facilitate assigning `Players` to colors, and determining the
order of play based on player age. The referee will also be responsible throughout
gameplay for determining the validity of moves and all actions taken by players.
If a player makes a bad move, the referee has the right and responsibility to
terminate their participation in the game.

&nbsp;&nbsp; The model will also keep track of the current stage of the game.
This will be a Java enumeration, one of `NOT_STARTED`, `PLACING_PENGUINS`, `IN_PLAY`,
and `GAME_OVER`.

&nbsp;&nbsp; Once a game is started, the model will instantiate a `GameBoard`.
The functionality of the board is described in the existing `GameBoard`
interface. This includes determining valid moves, manipulating penguin 
locations, and managing tiles.

### External Interface

Our external interface for the Hey, That's my Fish! Game-State will be a
Java Interface with the following functionality:

* Starting the Game
 * Decide what type of `GameBoard` to use
 * Determine how many players will be participating in the game
 * Advance `GameStage` from `NOT_STARTED` to `PLACING_PENGUINS`
* Placing a penguin on the Board
 * Confirm with the referee that it is a valid placement
 * Determine if the game should be advanced from `PLACING_PENGUINS` to `IN_PLAY`
* Moving a penguin
 * Confirm with the referee that it is a valid movement
 * Manipulate the `GameBoard` and player scores to reflect the penguin movement
* Removing a `Player` from the game
 * If the referee decides a player has made an invalid move, the model must be able to
 remove the player and their penguins from the game
* Checking if the game is over
 * After every move of a penguin, the model will need to evaluate if the game is over
* Deciding the winners of the gameplay
* Getting the current board state
 * Return a Read-Only version of the `GameBoard` to allow players/AI to strategize
