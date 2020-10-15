<div align="center">
    <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Other/Logo.png" width="300"/>
    <h1>Sundown | Dot Game System</h1>
</div>

**Purpose**  
The Dot Game System is the ideal outlet for hackers to practice their coding skills. The system will provide game servers to which hackers can connect player software and compete in tournaments for cash prizes. 

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

Pick your favorite GameBoard and use it as an argument to instantiate the HexBoardView that calls draw.Game(). For Example

    //Calling the View on one example
    GameView gv = new HexBoardView(noHolesBoard);
    gv.drawGame();

Run the main method to view the board in a new window.

### ii. Navigating Directories <a name="navDir"></a>
**Model**  
Foundations for the model have been laid in the Other/src/main/java/com/fish/model directory, in compliance with Maven file structure requirements.  

```
Other/src/main/java/com.fish/
|--/model
  |--/board
      |--GameBoard.java
      |--HexGameBoard.java
  |--/tile
      |--HexTile.java
      |--Tile.java
  |--Coord.java
  |--PlayerColor.java
```

`Coord` is a class that represents a coordinate point on a cartesian plane and `PlayerColor` is an enumeration that represents one of the four colors a player's penguins can be. These two classes fascilitate the transfer of information between components.

`Tile` is an interface implemented by `HexTile` that has one single functionality: to return the number of fish on that tile.

`GameBoard` is an interface implemented by `HexGameBoard` that manages the state of the board throughout gameplay. The `HexGameBoard` contains a 2-d Array of `Tile` in which null elements represent a hole. It also contains a HashMap of Coordinate keys and `PenguinColor` values that is updated with every move to reflect the location of penguins on the board.

**View**  
Foundations for the view can be found in the Other/src/main/java/com/fish/view directory.

```
Other/src/main/java/com.fish/
|--/view
  |--GameView.java
  |--HexBoardView.java
  |--HexTileView.java
```

`GameView` is an interface implemented by `HexBoardView` with one single method: drawGame(), which launches the image of the HTMF gameboard. 

`HexTileView` is the class that handles the rendering of a single hexagon tile and all that can be drawn over it, including how to position and scale the fish and penguins and which color penguin to render. 

### iii. Maintaining Repository <a name="maintain"></a>
The following UML diagrams support the maintaining of this repository. Pictured first is the model logic components. Second, visual components. Coord and PlayerColor are not pointing to any other class because they are additional tools that are used to facilitate the transfer of information between all other classes. 

<div align="center">
  <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Other/ModelUML.png"
       width="450"/>
    
    
  <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Other/ViewUML.png"
       width="350"/>
</div>
 

### iv. Testing <a name="testing"></a>
**How to Run Unit Tests**  
From the Common directory, run `./xtest` This will run all unit tests through Maven and output feedback on the number of tests passed/failed, and where the failed tests are.  

In order to test a random generated board, there exists a convenience constructor in the HexGameBoard class just for testing. It takes in the regular arguments required by a HexGameBoard plus an integer to seed the Random object in HexGameBoard.  

    //Convenience Constructor for Testing:
    public HexGameBoard(int rows, int cols, List<Coord> holes, int minOneFishTiles, int randSeed) {
        this(rows, cols, holes, minOneFishTiles);
        this.rand = new Random(randSeed);
        this.fillBoardWithTiles(holes, minOneFishTiles);
    }
    
For all of our tests the seed is set to 1, and our three examples used to test the board, referenced above in [Running](#running) look like the following. Please click each photo for a closer look. To examine the exact constructor used to generate it captured at the top of the screenshot.  

<div align="center">
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Other/holesBoard.png" alt="holesBoard" width="420"/>
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Other/noHolesBoard.png" alt="noHolesBoard" width="355"/>
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Other/constantBoard.png" alt="constantFishNumBoard" width="540"/>
</div>

  
    
**How to Run Test Harness**  
*(To be filled in with the further development of the system.)*
