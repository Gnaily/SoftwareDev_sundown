# Game Data Representation Planning

### Purpose
&nbsp;&nbsp;The data representation for a game of Hey, That's My Fish will allow
users to plan their strategy as well as allow the referee to check the validity
of moves. If a player attempts to make an invalid, the referee will easily be able
to detect this by checking if the resultant `GameState` is in the data
representation or not.

### Data Representation
&nbsp;&nbsp;Our representation for a game of Fish will be a tree with a single
root node. This will be the state immediately after players have finished placing
penguins, before any moves have been made. From this state, there will be
some number of out edges (each representing a legal move from this state) that
leads to a subsequent `GameState`. If a node does not have any outgoing edges,
that means there are no legal moves from that position and the game is over.

<div align="center">
    <img src="https://github.ccs.neu.edu/CS4500-F20/sundown/blob/master/Fish/Common/images/gameData.png" width="300"/>
    <p>Visual representation of `GameTree`</p>
</div>

The new data types we are defining will have the following characteristics:
* A `Node` contains:
  * This node's `GameState`
  * A List of moves that were made to reach this `GameState`
  * The parent Node
  * A list of all potential moves from this `GameState`
    * For each potential move, it will also have the resulting node
* A `GameTree` is:
  * The collection of all nodes
  * Has a single root node, representing the game's initial state


### External Interface

Our external interface for the Hey, That's my Fish! `GameTree` will be a
Java Interface with the following functionality:

* Create a `GameTree` given a starting `GameState`
  * For the starting player's turn, get all tiles reachable from all of their penguins
  * Create a new node (with the resultant gamestate) for each of those moves
  * Recur on each new node for the subsequent player's turns
  * Repeat this recursion until path's end in leaf nodes (no legal moves remaining)
* Check if a given `GameState` is reachable from the current node
  * If yes, return a list of potential moves that can be played to reach the
  desired State
* Ability to traverse the tree in both directions
  * Simulate one move at a time and be able to observe the resulting `GameState`
  * Be able to backtrack the simulation to try other potential moves
