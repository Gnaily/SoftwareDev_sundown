# Sundown

<h3>Dot Game System</h3>
<b>Purpose</b>
<p><i>(To be filled in with the further development of the system.)</i></p>
<b>Use Cases</b>
<p><i>(To be filled in with the further development of the system.)</i></p>

<h3><i>Hey, That's my Fish!</i> Implementation</h3>
<b>How to Run</b>
<p>For now, in the absence of an executable, navigate to the HeyThatsMyFishMain.java file and run the main method from an IDE. There are three example boards available/instantiated in the main method: One with no holes and a random number of fish on tiles, one with holes and a random number of fish on tiles, and lastly a board with no holes and a fixed number of fish on the tiles. They are named noHolesBoard, holesBoard, and constantFishNumBoard, respectively. Pick your favorite board and use it as an argument to instantiate the HexBoardView that calls draw.Game(). Run the main method to view the view.</p>

# Navigating Directories 
  <b>Model</b>
    <p>Foundations for the model have been laid in the Common/src/main/java/com/fish/model directory, in compliance with Maven file structure requirements.</p>

```
Common/src/main/java/com.fish/
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

`GameBoard` is an interface implemented by `HexGameBoard` that manages the state of the board throughout gameplay. The `HexGameBoard` contains a double nested List of `Tile` in which null elements represent a hole. It also contains a HashMap of Coordinate keys and `PenguinColor` values that is updated with every move to reflect the location of penguins on the board.

<b>View</b>
  <p>Foundations for the view can be found in the Common/src/main/java/com/fish/view directory</p>

```
Common/src/main/java/com.fish/
|--/view
  |--GameView.java
  |--HexBoardView.java
  |--HexTileView.java
```

`GameView` is an interface implemented by `HexBoardView` with one single method: drawGame(), which launches the image of the HTMF gameboard. 

`HexTileView` is the class that handles the rendering of a single hexagon tile and all that can be drawn over it, including how to position and scale the fish and penguins and which color penguin to render. 

# Testing
<b>How to Run Unit Tests</b>
From the Common directory, run `./xtest` This will run all unit tests through Maven and output feedback on the number of tests passed/failed, and where the failed tests are. 

In order to test a random generated board, there exists a convenience constructor in the HexGameBoard class just for testing. It takes in the regular arguments required by a HexGameBoard plus a integer to seed the Random object in the fields of HexGameBoard.

    public HexGameBoard(int rows, int cols, List<Coord> holes, int minOneFishTiles,
      int randSeed) {
    this(rows, cols, holes, minOneFishTiles);
    this.rand = new Random(randSeed);
    this.fillBoardWithTiles(holes, minOneFishTiles);
    }
    
For all of our tests the seed is set to 1, and our three examples used to test the board look like the following. Please click each photo for a closer look, and to examine the exact constructor used to generate it captured at the top of the screenshot.

<div class="imageContainer">
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/holesBoard.png" alt="holesBoard" width="420"/>
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/noHolesBoard.png" alt="noHolesBoard" width="410"/></div>
<img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/constantBoard.png" alt="constantFishNumBoard" width="540"/>


<b>How to Run Test Harness</b>
<p><i>(To be filled in with the further development of the system.)</i></p>
