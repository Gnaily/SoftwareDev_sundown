<div align="center">
    <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/images/Logo.png" width="300"/>
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
**Model**  
Foundations for the model have been built in the Other/src/main/java/com/fish/model directory, in compliance with Maven file structure requirements.  

```
Common/src/main/java/com.fish/
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

`GameBoard` is an interface implemented by `HexGameBoard` that represents the collection of tiles a game of HTMF is played on. The `HexGameBoard` contains a 2-d Array of `Tile`, and each individual `Tile` holds informaiton about whether or not it should be present on the visible playing plane. A GameBoard's only functionality is to maintain the layout and visibility of `Tile` objects.

`GameState` is an interface implemented by `HexGameState` that manages the HTMF gaeme logic. A HexGameState has a `GameStage`, which is an enumeration that represents the different stages of a game: NOT_STARTED, PLACING_PENGUINS, IN_PLAY, and GAMEOVER. A `Player` represents an internal player and holds information about the player's age, score, and `PlayerColor`, which is an enumeration representing one of the four colors a player's avatars can be assigned.

`Tile` is an interface implemented by `HexTile` and has two purposes: to hold and return the number of fish on a tile, and to hold information about whether or not the tile has 'melted' eg disappears after a penguin departs from it.

`Coord` is a class that represents a coordinate point on a cartesian plane. Its xx and yy values correspond to the ii and jj indices of the 2-d Array of Tiles in the GameBoard object. This class fascilitates the transfer of information between components.

**View**  
Foundations for the view can be found in the Other/src/main/java/com/fish/view directory.

```
Common/src/main/java/com.fish/
    view/
     |  GameView.java
     |  HexBoardView.java
```

`GameView` is an interface implemented by `HexBoardView` with one single method: drawGame(), which launches the image of the HTMF gameboard.

### iii. Maintaining Repository <a name="maintain"></a>
The following UML diagrams support the maintaining of this repository. Pictured first is the model logic components. Second, visual components. Coord and PlayerColor are not pointing to any other class because they are additional tools that are used to facilitate the transfer of information between all other classes. 
*Needs Updating*
<div align="center">
  <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/images/ModelUML.png"
       width="450"/>


  <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/images/ViewUML.png"
       width="350"/>
</div>


### iv. Unit Testing <a name="testing"></a>
**How to Run Unit Tests**  
From the Fish directory, run `./xtest`. This will run all unit tests and output feedback on the number of tests passed/failed, and where the failed tests are.  

For individual test classes, in order to test a random generated board, there exists a convenience constructor in the HexGameBoard class just for testing. It takes in the regular arguments required by a HexGameBoard plus an integer to seed the Random object in HexGameBoard.  

    //Convenience Constructor for Testing:
    public HexGameBoard(int rows, int cols, List<Coord> holes, int minOneFishTiles, int randSeed) {
        this(rows, cols, holes, minOneFishTiles);
        this.rand = new Random(randSeed);
        this.fillBoardWithTiles(holes, minOneFishTiles);
    }

For all of our tests the seed is set to 1. Our three examples used throughout the test classes, and referenced above in [Running](#running), looked like the following during our prototype stage (we have since improved the visual component but the placement of holes and number of fish are the same). Please click each photo for a closer look. The constructor for the board object used to generate each board is captured in the screenshot above the board.

<div align="center">
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/images/holesBoard.png" alt="holesBoard" width="420"/>
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/images/noHolesBoard.png" alt="noHolesBoard" width="355"/>
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/images/constantBoard.png" alt="constantFishNumBoard" width="540"/>
</div>

### v. Test Harness

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
