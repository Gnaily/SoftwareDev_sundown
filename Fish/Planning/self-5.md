## Self-Evaluation Form for Milestone 5

Under each of the following elements below, indicate below where your
TAs can find:

- the data definition, including interpretation, of penguin placements for setups
Coord object definition in Coord class:
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/1caf81e7efb61a362c7fbb511f7fc7adac578d22/Fish/Common/src/main/java/com/fish/model/Coord.java#L3-L12

Coord layout definition in the GameBoard class:
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/1caf81e7efb61a362c7fbb511f7fc7adac578d22/Fish/Common/src/main/java/com/fish/model/board/HexGameBoard.java#L22-L48

- the data definition, including interpretation, of penguin movements for turns
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/1caf81e7efb61a362c7fbb511f7fc7adac578d22/Fish/Common/src/main/java/com/fish/game/Move.java#L7-L16

- the unit tests for the penguin placement strategy
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/1caf81e7efb61a362c7fbb511f7fc7adac578d22/Fish/Player/src/test/java/com/player/minimax/MinimaxStrategyTest.java#L52-L73

- the unit tests for the penguin movement strategy;
  given that the exploration depth is a parameter `N`, there should be at least two unit tests for different depths
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/1caf81e7efb61a362c7fbb511f7fc7adac578d22/Fish/Player/src/test/java/com/player/minimax/MinimaxStrategyTest.java#L76-L111

- any game-tree functionality you had to add to create the `xtest` test harness:
  - where the functionality is defined in `game-tree.PP`
  The functionality added checks that the given move is valid:
  https://github.ccs.neu.edu/CS4500-F20/sundown/blob/1caf81e7efb61a362c7fbb511f7fc7adac578d22/Fish/Common/src/main/java/com/fish/game/HexGameTree.java#L143-L146

  - where the functionality is used in `xtree`
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/1caf81e7efb61a362c7fbb511f7fc7adac578d22/Fish/Common/src/main/java/com/fish/json/XTree.java#L108

  - you may wish to submit a `git-diff` for `game-tree` and any auxiliary modules

**Please use GitHub perma-links to the range of lines in specific
file or a collection of files for each of the above bullet points.**

  WARNING: all perma-links must point to your commit "1caf81e7efb61a362c7fbb511f7fc7adac578d22".
  Any bad links will result in a zero score for this self-evaluation.
  Here is an example link:
    <https://github.ccs.neu.edu/CS4500-F20/sundown/tree/1caf81e7efb61a362c7fbb511f7fc7adac578d22/Fish>
