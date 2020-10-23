# PlayerActions API Protocol | Repo: Sundown

## Purpose
The PlayerAction API enables an AI player to join and play out a game of Hey, That's my Fish! (HTMF). This protocol outlines the flow of communication required between an AI player component and the game's referee to carry out a full, valid game of HTMF. The player interface is the gateway to the overall Fish Game System that provides game servers to which hackers can connect AI player software and compete in tournaments for prizes.

## Use Cases
This API is intended for:

* Hackers : Use this protocol to write robust algorithms that
  - compete in games of HTMF on your behalf
  - request information about the ongoing game the AI is competing in
  - process that information to make the best moves on your behalf
  - make actionable requests to the referee at the appropriate time
  - win cash money via tournaments
* Referees : An internal referee will expect precise information in the defined format sent to the server via this interface to advance through games.


## Data Descriptions
A hacker must be familiar with the following data structures to produce a robust AI player:

* `GameStage` : One of NOT_STARTED, PLACING_PENGUINS, IN_PLAY, or GAME_OVER. *Interpretation:* An enumeration representing one of four stages a game of HTMF progresses through. Please see instructions below for details of each stage.

* `Coord` : A coordinate point with an X value and a Y value. Use `getX()` and `getY()` to retrieve values. *Interpretation:* The precise location of a Tile in the game board data representation.

* `int[][]` : A 2-d Array of integers. *Interpretation:* The collection of tiles in a game of HTMF arranged into a 2-d Array of int wherein each integer represents the number of fish on the tile at that precise location on the game board.

* `Map<Coord, PlayerColor>` : A map with Coord keys and PlayerColor values. *Interpretation:* A collection penguin avatar locations on the board. The coordinate locations are unique in that no two penguins may land on the same tile at once. The PlayerColor is the player's assigned avatar color whose penguin is at the coordinate location.

* `PlayerColor` : One of BLACK, BROWN, WHITE, or RED. *Interpretation:* An enumeration representing one of the four possible colors of avatars (penguins) in a game of HTMF.

* `Player` : A player object has a score (int), age (int) and color (PlayerColor). *Interpretation:* An internal participant of a game of HTMF.


## Protocol Instructions
Strict adherence to the protocol is required to successfully participate in tournaments. Breaking the protocol will result in termination from the tournament.

At any time, the AI player may call the following getters to retrieve information regarding the ongoing game:
* getCurrentGameStage() : GameStage
* getCurrentBoard() : int[][]
* getPenguinLocations() : Map<Coord, PlayerColor>
* getCurrentPlayer() : PlayerColor
* getPlayerList() : List[Player]

Invoke the following getters on a Player from the List of Players to retrieve more information:
* getAge() : int
* getColor() : PlayerColor
* getScore() : int

Each game is split into four stages, listed below. Use `getCurrentGameStage()` at any point to retrieve the current GameStage. Within each of the game stages, you may:

* **GameStage.NOT_STARTED** :
  - NOT_STARTED is the 'waiting room' stage wherein the referee is gathering players and establishing connection with them.
  - In this initial stage of a game, an AI player must call `waitForGameStart()` to wait for the referee to return a message indicating the game and all of its players are ready.
  - The message returned to all players will include their assigned `PlayerColor` and a `List of Players` in the order of player turns stored in a JsonArray.


* **GameStage.PLACING_PENGUINS** :
  - PLACING_PENGUINS is the official start of game play when players must place penguin avatars on the board in their initial locations in a round robin way.
  - The AI player must use the `getCurrentPlayer()` method to determine if it is their turn to place a penguin.
    - If it is **not** their turn, they can use `waitForPenguinPlacement()` to receive a message regarding the most recent penguin placement. They can then determine if their turn is coming up to place a penguin, otherwise repeat the previous operation.
    - If it **is** their turn, they will send a *request* to place a penguin using the `placePenguin()` method.
  - If a message is sent back from the referee saying the placement failed, the player may be kicked from the game.
  - The referee will determine how many penguins each player gets. Continue to use the getters to understand which stage the game is in and which player's turn it is.


* **GameStage.IN_PLAY** :
  - This is the stage actual game playing proceeds. The movePenguin method may now be called and the referee will advance turns.
  - This stage will follow the same cycle as the previous stage...
  - The AI player will use the `getCurrentPlayer()` method to determine if it is their turn to move a penguin.
    - If it is **not** their turn, use `waitForPenguinMovement()` to receive the most recent penguin movement.
    - If it **is** their turn, they will send a request indicating their desired move using `movePenguin()`.
  - The referee will determine when the game is over. Continue to use `getCurrentPlayer()` to know if you should move a penguin and `getCurrentGameStage()` to know when the game is over.
  - If the player’s turn is skipped, then all of its penguins are stranded and they cannot make any more moves for the remainder of the current game, but they may wait around for the game outcome.


* **GameStage.GAME_OVER** :
  - The game is over one one of two conditions are met:
    - All penguins are stranded / No penguins on the board have valid moves
    - One one player remains as a result of all others being terminated
  - Once the game advances into GAME_OVER, Use the method `waitForGameResults()` to wait for the referee to indicate game winners and prize distribution.


Note that if you call a method/action outside of its appropriate stage, you will be eliminated from the game.
