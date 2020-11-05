<div align="center">
    <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/Logo.png" width="300"/>
    <h1>Sundown | Game System</h1>
</div>

**Purpose**  
The Fish Game System is the ideal outlet for hackers to practice their coding skills. The system will provide game servers to which hackers can connect player software and compete in tournaments for cash prizes.

**Use Cases**  
While the primary audience of this system are AI software developers, there are three total use cases for this system:
* AI Players, who connect software for automated play
* House Players, who play the games via an online user interface
* Observers, who watch tournament statistics and ongoing gameplay, intended for both friends to watch on and for company monitoring to ensure everyone is following the rules

**Table of Contents**
1. [Hey That's my Fish!](#htmf)
    1. [Running](#running)
    2. [Navigating Directories](#navDir)
    3. [Maintaining Repository](#maintain)
    4. [Testing](#testing)

## *Hey, That's my Fish!* Implementation <a name="htmf"></a>
The first game we will publish is *Hey, That's my Fish!* (HTMF). The game consists of hexagon tiles with a particular number of fish on them. The game board will initialize with a fixed number of holes and 1-fish tiles in it. The 2-4 players will be assigned a color and 6 - N penguins, where N is the number of players. The players will place penguins in a round robin way, in ascending order of age.  

Once the game begins, players will move one penguin at a time in a straight direction. Each time a penguin is moved, the tile of origin turns into a hole and the player collects all fish on that tile. The collection of fish is the player's score. If a penguin becomes stranded, the player loses that penguin on the board. If a player uses their penguin to strand another player's penguin, the former player earns the fish on the tile the stranded player's penguin departs from. The game ends when all penguins are stranded and no moves can be made.

### i. Running <a name="running"></a>
In the absence of an executable, navigate to the HeyThatsMyFishMain.java file and run the main method from an IDE. There are three example boards available/instantiated in the main method.

    //Example : A board with no holes and random number of fish on each tile
    GameBoard noHolesBoard = new HexGameBoard(6, 2, new ArrayList<Coord>(), 5, 1);

    //Example : A board with holes and random number of fish on each tile
    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1), new Coord(2, 2), new Coord(1, 4));
    GameBoard holesBoard = new HexGameBoard(8, 3, holes, 8, 1);

    //Example : A board with no holes and constant number of fish on each tile
    GameBoard constantFishNumBoard = new HexGameBoard(4, 4, 2);

Pick your favorite GameBoard and use it as an argument to instantiate the GameState that calls draw.Game(). For Example

    //Create the GameState
    GameState state = new HexGameState();
    List<Player> players = initilizePlayers(); //initializePlayers is a method defined below the main method to generate players
    state.initGame(holesBoard, players);

    //Call the View
    GameView gv = new HexBoardView(state);
    gv.drawGame();

Run the main method to view the board in a new window.


### ii. Navigating Directories <a name="navDir"></a>
**Common**  
Foundations for the model have been built in the Other/src/main/java/com/fish/model directory, in compliance with Maven file structure requirements.  
```
Common/Other/src/main/java/com.fish/
    model/
     |  board/
     |   | GameBoard.java
     |   | HexGameBoard.java
     |   | TwoNumberOperation.java
     |  state/
     |   | GameStage.java
     |   | GameState.java
     |   | HexGameState.java
     |   | Player.java
     |   | PlayerColor.java
     |  tile/
     |   | HexTile.java
     |   | Tile.java
     |  Coord.java
```

* `Coord` : A coordinate point with an X value and a Y value. Use `getX()` and `getY()` to retrieve values. *Interpretation:* The precise location of a Tile in the game board data representation where X is the corresponding COLUMN on a visual plane and Y is the corresponding ROW on a visual plane. See image below of the Coord layout in a hexagon board.

* `Tile` : An interface implemented by `HexTile` that has an int of the number of fish on that Tile and a boolean isPresent that represents whether or not there is a hole in that Tile place on a GameBoard.

* `TwoNumberOperation` : An interface for a function object that takes in two integers and performs an operation on them that results in a int. This is used in the getTilesReachableFrom(Coord start) method in the HexGameBoard class to take in the x and y values of the Coord and perform a multitude of operations on them to obtain all of the tiles reachable from that location.

* `GameBoard` : An interface implemented by `HexGameBoard` that represents the collection of tiles a game of HTMF is played on. The `HexGameBoard` contains a 2-d Array of `Tile`, and each individual `Tile` holds informaiton about whether or not it should be present on the visible playing plane. A GameBoard's only functionality is to maintain the layout and visibility of `Tile` objects.

* `GameStage` : One of NOT_STARTED, PLACING_PENGUINS, IN_PLAY, or GAME_OVER. *Interpretation:* An enumeration representing one of four stages a game of HTMF progresses through. Please see instructions below for details of each stage.

* `PlayerColor` : One of BLACK, BROWN, WHITE, or RED. *Interpretation:* An enumeration representing one of the four possible colors of avatars (penguins) in a game of HTMF.

* `Player` : An internal player representation that has a score (int), age (int) and color (PlayerColor). *Interpretation:* An internal participant of a game of HTMF.

* `GameState` : An interface implemented by `HexGameState` that manages the HTMF game logic. A HexGameState has a `GameStage`, a `GameBoard`, a List of Player in the order of player turns, an int of the current player index, and a Map of Coord to PlayerColor of penguin locations, expanded below..

* `Map<Coord, PlayerColor>` : A map with Coord keys and PlayerColor values. *Interpretation:* A collection penguin avatar locations on the board. The coordinate locations are unique in that no two penguins may land on the same tile at once. The PlayerColor is the player's assigned avatar color whose penguin is at the coordinate location.

**Game Tree**  
For representing a full game of HTMF, all files of the Game Tree are gathered in the game/directory.

```
Common/Other/src/main/java/com.fish/
    game/
     |  GameTree.java
     |  HexGameTree
     |  IFunc
     |  Move
     |  MoveState
     |  NodeCounter
```
* `Move` : A complete move from a starting Coord to an ending Coord in a game of HTMF. Note: This does **not** represent a valid move as there are absolutely no restrictions to how the start and end Coord are instantiated. *Interpretation:* A possible action that a player can make during their turn of the game.

* `MoveState` : An abstract pairing of a Move and a GameState, used by a GameTree to represent the history of the tree without storing the Tree itself. *Interpretation*: A sequence of all moves that were made from the starting node of a game as represented in a GameTree. Note that the Move is the Move made FROM the State it is paired with, not TO the state it is paired with. Please see diagram below in testing section on the structure of a GameTree.

* `GameTree` : An interface implemented by `HexGameTree` that represents entire games of HTMf. A HexGameTree has a GameState, and List of MoveState representing the tree history, and a Map of Move to Gamestate representing the possible moves stemming from the current gamestate at the current player's turn.

* `IFunc` is a function interface for applying actions to state in GameTrees. `NodeCounter` is one example of the actions made possible by the IFunc.

**View**  
Foundations for the view can be found in the Other/src/main/java/com/fish/view directory.
```
Common/Other/src/main/java/com.fish/
    view/
     |  GameView.java
     |  HexBoardView.java
```
`GameView` is an interface implemented by `HexBoardView` with one single method: drawGame(), which launches the image of the HTMF gameboard.


**JSON**  
All of the functionality for our test harnesses is in the json/ directory.  
```
Common/Other/src/main/java/com.fish/
    json/
     |  XBoard
     |  XJson
     |  XState
     |  XTree
```
XJson contains two static methods that aid in the processing of JSON input, XBoard processes JSON input that represents game boards, and likewise XState processes a JSON representations of a State, performs one action (moves the first player's first penguin) and either returns false or converts the resulting state back into JSON and returns that.

**Player**

```
Player/Other/src/main/java/com/player
    json/
     |  XStrtegy.java
    player/
     | HousePlayer.java
     | minimax/
        | MoveValue.java
        | MinimaxStrategy.java
        | MinimaxAlgorithm.java
```

The Player repository contains the 3 pieces of functionality:
- `HousePlayer.java`: An implementation of the `PlayerInterface` found in `Common` that can be used
by the referee.
- `minimax/` A sample player strategy.
- `XStrategy.java` A Test harness main method for running the sample strategy.

**Minimax**

The minimax package defines a sample algorithm that a hacker could use when
implementing a decision-making algorithm for placing and moving penguins.

Penguins are placed in the following order:
- Starting in the top left of the board, traverse each row from left to right for
a valid penguin locations
- if no location is found, move on to the next row in the board.
- This placement algorithm makes an assumption that the referee has setup a game
with enough penguin placement locations for the number of players.

The best penguin movement for a given gamestate is determined as follows:
- The algorithm is run for the current players
- Search the GameTree until the current player has taken and/or been skipped N times
- At each intermediate state, chose a move that nodes's player will make
  - if it is the player running the algorithm's turn, chose the move that results
  in the most points for that player after finishing the lookahead on all future states.
  - if it is not the player running the algorithm's turn, chose the move that
  results in the player running the algorithm receiving the fewest points after
  finishing the lookahead.

`MinimaxStrategy.java` is the wrapper class that implements these two static functions.

`MinimaxAlgorithm.java` is an implementation of the `GameTree`'s `IFunc` interface
that can be applied to all nodes in a `GameTree` and return the best move for
the starting player.

`MoveValue.java` is a Java Object that combines a `Move` with an integer, representing
the number of points the starting player will have after that move is made.

**XStrategy**

`XStrategy` is the main method that runs the test harness described in `xstrategy`.
It uses the JSON functionality found in `Common`'s JSON parsing methods to translate
the input to a GameState, uses the `MinimaxStrategy` to determine the best move,
and then uses the JSON parsing methods to turn the action into JSON for printing.

**HousePlayer**

The HousePlayer is a basic implementation of the `PlayerInterface` that uses the
minimax strategy for placing and moving pengiuns. It allows for the desired search
depth to be configured in the constructor.


**Admin**

```
Admin/Other/src/main/java/com/player
    game/
     |  DemoGame.java
    referee/
     | Results.java
     | HexReferee.java
     | Referee.java
```

The `Admin` project currently contains the following functionality:
- `referee/`: The referee implementation and the interface defining the actions a referee can perform
- `DemoGame.java`: A demo main method for running a small game of fish and printing out the results

**Referee**

The Referee is to be used by a tournament manager for running a game of Fish and
receiving the results. Currently, the only statistics reported at the end of the game
are the winners and the cheaters. This could be changed (if desired) by adding more
information (such as the complete game tree) to the `Results` class. Currently,
the interface for the `Referee` only contains a single method - running a game of
fish for a given list of players. This is the place where more options could be
given to the tournament manager. This could include (but is not limited to):
- allowing the manager to pick the board the game is played on
- allowing the manager to increase/decrease the amount of time a player is
allowed to calculate their move
- etc.

**DemoGame**

The DemoGame main method can be run or altered as desired to showcase the functionality
of a referee to investors. Currently, it runs a game for two players - one who looks
ahead a single move according to the minimax Strategy, and one who looks ahead two.
Once the game is run, the method prints out the results to the console. This
demo shows how the tournament manager might use the referee to run a game in the future.


### iii. Maintaining Repository <a name="maintain"></a>
The main functionality of the overall game system lies in four primary interfaces: Tile, GameBoard, GameState, and GameTree. The individual functionality of each can be understood by examining method signatures. Use the following interface summaries to pinpoint functionatliy you wish to examine:

<div align="center">
  <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/InterfaceTile.png"/>
  <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/InterfaceGameBoard.png"/>
  <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/InterfaceGameStage.png" width="400"/>
  <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/InterfaceGameTree.png" width="375"/>
</div>
**Note** There is a typo in the GameState diagram - its title says GameStage but it should be GameState.

Beyond the overall game found in `Common`, there are two other important directories for
the fish project: `Admin` and `Player`

The `Player` project contains sample implementations of the type of code a hacker
may write to participate in a game of Fish remotely. Currently, there exists one
sample algorithm (`MinimaxStrategy.java`) and a sample implementation of the `PlayerInterface`
that utilizes that algorithm. The sample implementation is written as a `HousePlayer`,
to demonstrate how the referee might interact with a player added to the game
by company running the tournament in order to give them a chance to win.

The `Admin` project contains software components concerned with running a Fishy
tournament for a group of internal and external players. It defines a Referee
that can be used by the tournament manager to run a game of Fish for a given set
of players and be given back the results from the game. It also contains an
interface for the Tournament Manager, that once implemented, will be capable of
running an entire Tournament of Fish.


### iv. Unit Testing <a name="testing"></a>
**How to Run Unit Tests**  
From the Fish directory, run `./xtest`. This will run all unit tests and output feedback on the number of tests passed/failed, and where the failed tests are. This will run tests
from Common, Player, and the Admin folders. In order for this to run, ensure that
your computer has a command line version of Maven installed.

For individual test classes, in order to test a random generated board, there exists a convenience constructor in the HexGameBoard class just for testing. It takes in the regular arguments required by a HexGameBoard plus an integer to seed the Random object in HexGameBoard.  

    //Convenience Constructor for Testing:
    public HexGameBoard(int rows, int cols, List<Coord> holes, int minOneFishTiles, int randSeed) {
        this(rows, cols, holes, minOneFishTiles);
        this.rand = new Random(randSeed);
        this.fillBoardWithTiles(holes, minOneFishTiles);
    }

For all of our tests the seed is set to 1. The first three examples below are used in the GameBoard test classes, and referenced above in [Running](#running), looked like the following during our prototype stage. Please click each photo for a closer look. The constructor for the board object used to generate each board is captured in the screenshot above the board.

<div align="center">
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/holesBoard.png" alt="holesBoard" width="420"/>
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/noHolesBoard.png" alt="noHolesBoard" width="355"/>
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/constantBoard.png" alt="constantFishNumBoard" width="540"/>
</div>
<br>

While progressing to the GameState, the visual component was approved upon, and all of our GameState test cases are based off the following three boards, named in our tests as 'twoPlayerGame,' 'fourPlayerGame,' and 'constantFishNumGame':

<div align="center">
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/fourPlayerGame.png" alt="fourPlayerGame" width="420"/>
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/twoPlayerGame.png" alt="twoPlayerGame" width="355"/>
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/constantFishNumGame.png" alt="constantFishNumGame" width="540"/>
</div>
<br>

Finally, presented here is a generalized example of our GameTree structure (this does not correspond to our test examples due to the complexity of the test cases). Emphasized in the diagram is the functionality of traversing the GameTree backwards while a Player is strategizing. In order to avoid storing large tree data, every time a player traverses nodes on the Tree a new one is made while the previous one is not stored, but in the new one is enough information to reconstruct the previous node:

<div align="center">
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/Other/images/GameTreeExample.png" alt="GameTree Example" width="420"/>
</div>


### v. Test Harness

**xstrategy**

From the 6 directory, run `./xstrategy`.
This testing harness expects json input from STD in and will print results to STD out.

The input should be JSON in the following format:
```
{
  [D, State]
}
```

 Were `D` is a number, either 1 or 2, and `State` is a valid GameState as described
 below. This harness will calculate the best move for the current player using the
 minimax strategy searching to a depth of `D`. The output will either be an `Action`
 (the output described for `xtree`), or `false` if there is no valid move for
 the current player.

 The best move is determined as follows:
 - Whenever it is the current player's turn, they will choose the move that nets them
 the most penguins
 - Whenever it is an opponents turn, they will choose the move the nets the player
 running the algorithm the fewest penguins
 - Look ahead `D` moves for each player and determine the best initial move the
 player should take.

An example of running this program with an input file is:
`./xstrategy < /path/to/file.json`.

**xtree**

From the 5 directory run `./xtree`.
This testing harness expects input from STD in and will send its results of STD out.

The input should JSON be in the following format:
```
{
  "state" : State,
  "from" : Position,
  "to" : Position
}
```

Where `State` and `Position` follow the requirements laid out in the descriptions
below. The `from` and `to` fields represent a valid move for the current players
in the state. The output should be a valid move from the next player in the format:
`[Position, Position]`.
If there is no desirable move for the next player, the harness will output
`false` instead.

A desirable move is determine as follows:
- The next player will attempt to move its penguin to one of the hexagons
adjacent to the `from` position passed in
- it will search in the follow order: North, NE, SE, South, SW, NW and chose to
move to the first of the list it can reach
- if multiple penguins can move to the desired location, the player will move the
penguin from the lowest row, and if there is still a tie, the penguin from the lowest
column.

An example of running this test with a valid JSON input file would be:
`./xtree < /path/to/file.json`.

**xstate**  

From the 4 directory run `./xstate`.
This testing harness expects input from STD in and will send output to STD out.

The input should be in the following format:
```
{
  "players" : Player*,
  "board" : Board
}
```
Where Board is a JSON representation of a GameBoard as specified in the `xboard`
section of the testing harness.

`Player*` is defined as:
```
[Player, Player, ..., Player]
```
Which specified the order the players take turn for the current GameState.

Each Player is defined as:
```
{
  "color" : Color,
  "score" : Natural,
  "places" : [Position, Position, ... , Position]
}
```
Where Color will be a String, either `"red"`, `"white"`, `"brown"`, or `"black"`

Score will be their current score in the game
Places will be a JSON array of positions (as defined in the steps for the `xboard`
  harness).

The output will be JSON representing the state after a single move is taken.

The move is defined as follows:
The first player in the player list will attempt to move their 1st penguin 1 space
in each of the following directions in order: North, NorthEast, SouthEast, South, SouthWest, NorthWest.

If a valid move is found, the harness will update the board, player order, player score,
and player penguins and print out the output.

If no valid move is found, the output will simply be `False`.

To run the test harness with an input file, send the file through the command
line. For example, `./xstate < path/to/file.json`.


**xboard**

From the 3 directory run `./xboard`.
This will expect input from STD in. The input is expected to be in the format
```
Board-Posn is
      { "position" : Position,
        "board" : Board}
    Board is a JSON array of JSON arrays where each element is
    either 0 or a number between 1 and 5.
    The size of the board may not exceed a total of 25 tiles.
    INTERPRETATION A 0 denotes a hole in the board configuration. All other
    numbers specify the number of fish displayed on the tile.

    Position is a JSON array that contains two natural numbers:
      [board-row,board-column].
    INTERPRETATION The position uses the computer graphics coordinate system
      meaning the Y axis points downwards. The position refers to a tile with at least one fish on it.

```

To send in a file instead of typing directly into STD in, run `./xboard < /path/to/file.json`

The output will be the number of tiles reachable from the given position on the given board.

**generating a new test harness**
Whenever a new test harness is desired, one can be created through maven. This can
be done in an IDE or through the command line. Generating a jar from an executable
differs depending on the ide, so only the command line method will be described here.

From the desired project folder (`Common/Other`, `Player/Other`, `Admin/Other`), run:

`mvn -Dmy.mainClass={Class with main method} clean compile package`.

When running this command, `{Class with main method}` should be replace with the
desired class with a main method to build a jar for. For example, if you desired to
build XTree, the entire command would be:

`mvn -Dmy.mainClass=com.fish.json.XTree clean compile package` from the Common/Other directory.

This will create a new jarfile in the projects `target` folder that you can then
run from the command line. If you wish to change the name of the jarfile generated,
you can edit the `artifactId` field in the project's pom.xml file.
