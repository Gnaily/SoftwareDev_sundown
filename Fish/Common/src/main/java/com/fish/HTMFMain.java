//package com.fish;
//
//import com.fish.model.Coord;
//import com.fish.model.state.GameState;
//import com.fish.model.state.HexGameState;
//import com.fish.model.state.HexPlayer;
//import com.fish.model.state.PlayerColor;
//import com.fish.model.board.HexGameBoard;
//import com.fish.view.GameView;
//import com.fish.view.HexBoardView;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Main method that launches a game of Hey, that's my Fish!
// */
//public class HTMFMain {
//
//  public static void main(String[] args) {
//
//    GameState twoPlayerGame = new HexGameState();
//    GameState fourPlayerGame = new HexGameState();
//    GameState constantFishNumGame = new HexGameState();
//
//    //Set up each specific state BEFORE launching the game:
//
//    // Two Players:
//    List<HexPlayer> twoPlayers = new ArrayList<>(Arrays.asList(
//        new HexPlayer(5, PlayerColor.WHITE), new HexPlayer(50, PlayerColor.RED)));
//
//    //Board: 6 rows x 2 columns; no holes
//    twoPlayerGame.initGame(new HexGameBoard(6, 2, new ArrayList<>(),
//        5, 1), twoPlayers);
//
//    //Place Penguins
//    twoPlayerGame.placePenguin(new Coord(1, 2), PlayerColor.WHITE);
//    twoPlayerGame.placePenguin(new Coord(0, 1), PlayerColor.RED);
//
//
//    ////////////////////////////////////
//    // Four Players:
//    List<HexPlayer> fourPlayers = new ArrayList<>(Arrays.asList(
//        new HexPlayer(10, PlayerColor.BROWN), new HexPlayer(12, PlayerColor.BLACK),
//        new HexPlayer(44, PlayerColor.WHITE), new HexPlayer(55, PlayerColor.RED)));
//
//    //Board: 8 rows x 3 columns; holes from holes list
//    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
//        new Coord(2, 2), new Coord(1, 4));
//    fourPlayerGame.initGame(new HexGameBoard(8, 3, holes, 8, 1),
//        fourPlayers);
//
//    //Place Penguins
//    fourPlayerGame.placePenguin(new Coord(0, 1), PlayerColor.BROWN);
//    fourPlayerGame.placePenguin(new Coord(0, 2), PlayerColor.BLACK);
//    fourPlayerGame.placePenguin(new Coord(0, 3), PlayerColor.WHITE);
//    fourPlayerGame.placePenguin(new Coord(1, 2), PlayerColor.RED);
//
//
//    ////////////////////////////////////
//    //Use Two Players, but initialize the board with a constant number of fish for controlled testing
//    List<HexPlayer> twoConstPlayers = new ArrayList<>(Arrays.asList(
//        new HexPlayer(5, PlayerColor.BROWN), new HexPlayer(50, PlayerColor.BLACK)));
//
//    //Board: 4 rows x 4 columns; no holes and 2 fish on each tile
//    constantFishNumGame.initGame(new HexGameBoard(4, 4, 2), twoConstPlayers);
//
//    //Place Penguins
//    constantFishNumGame.placePenguin(new Coord(0, 0), PlayerColor.BROWN);
//    constantFishNumGame.placePenguin(new Coord(1, 0), PlayerColor.BLACK);
//
//
//    ////////////////////////////////////LAUNCH:
//    //Calling the View with the state you wish to launch
//    GameView gv = new HexBoardView(twoPlayerGame);
//    gv.drawGame();
//  }
//}
