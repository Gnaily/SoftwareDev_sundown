## Self-Evaluation Form for Milestone 6

Indicate below where your TAs can find the following elements in your strategy and/or player-interface modules:

The implementation of the "steady state" phase of a board game
typically calls for several different pieces: playing a *complete
game*, the *start up* phase, playing one *round* of the game, playing a *turn*,
each with different demands. The design recipe from the prerequisite courses call
for at least three pieces of functionality implemented as separate
functions or methods:

- the functionality for "place all penguins"
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/main/java/com/referee/HexReferee.java#L168-L216

- a unit test for the "place all penguins" functionality
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/test/java/com/referee/HexRefereeTest.java#L133-L168

- the "loop till final game state"  function
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/main/java/com/referee/HexReferee.java#L218-L257

- this function must initialize the game tree for the players that survived the start-up phase
Line of tree initialization:
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/main/java/com/referee/HexReferee.java#L230

- a unit test for the "loop till final game state"  function
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/test/java/com/referee/HexRefereeTest.java#L196-L236


- the "one-round loop" function
Note: We do not run rounds, we run a while loop until the game is over:
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/main/java/com/referee/HexReferee.java#L232-L233

- a unit test for the "one-round loop" function
No tests for this because this is part of the while loop until the game is over

- the "one-turn" per player function
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/main/java/com/referee/HexReferee.java#L254-L257

- a unit test for the "one-turn per player" function with a well-behaved player
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/test/java/com/referee/HexRefereeTest.java#L243-L258

- a unit test for the "one-turn" function with a cheating player
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/test/java/com/referee/HexRefereeTest.java#L238-L241

- a unit test for the "one-turn" function with an failing player
Failing due to timeout:
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/test/java/com/referee/TimeoutTest.java#L15-L25
Note that the test is commented out since it takes a full minute to run.

- for documenting which abnormal conditions the referee addresses
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/main/java/com/referee/HexReferee.java#L35-L41

- the place where the referee re-initializes the game tree when a player is kicked out for cheating and/or failing
We unfortunately overlooked this but this is where it will go when we refactor:
https://github.ccs.neu.edu/CS4500-F20/sundown/blob/4f771c87692d572860740b2ab5250f839fce9a4b/Fish/Admin/Other/src/main/java/com/referee/HexReferee.java#L243-L245


**Please use GitHub perma-links to the range of lines in specific
file or a collection of files for each of the above bullet points.**

  WARNING: all perma-links must point to your commit "4f771c87692d572860740b2ab5250f839fce9a4b".
  Any bad links will be penalized.
  Here is an example link:
    <https://github.ccs.neu.edu/CS4500-F20/sundown/tree/4f771c87692d572860740b2ab5250f839fce9a4b/Fish>
