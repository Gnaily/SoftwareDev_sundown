# Manager Protocol Design
### Repo: Sundown

## Purpose:
The purpose of the tournament manager is to run a complete tournament of Hey,
That's my Fish! A complete tournament will require multiple rounds of Fish,
and with each round some amount of players will be eliminated. The Tournament Manager
component will allow players to sign up for a tournament that has not started yet,
generate referees to run games of Fish, distribute players to referees, and
compiles results from games for the observers.

## Use Case:
- Players
  - Players will not directly interact with the tournament manager, however
  their information will be passed from the sign-up service to the manager.
  - Once the manager has player information and is ready to start a tournament,
  it will notify players their game is starting and send them to their
  respective referees.
- Observers
  - Observers will be able to oversee how a tournament is going, the players eliminated
  each round, and overall statistics.
  - In this case, observers could be the system administrators running the tournament
  or outside observers following the intense action. The level of control / how much
  information they are able to view about the tournament will depend on their
  clearance level.

## Tournament Structure
A tournament will consist of an unknown amount of rounds. At the beginning of the tournament,
each Player will be sent to a game of Fish to participate in. The manager will attempt
to put all players into a game with 3 others (for a total of 4), however if the players
do not divide up evenly the manager will create games of 2 or 3.

Once a game is completed, the winners will be separated from the losers. Each game of Fish
can have multiple winners. Once all games in a round are completed, all of the winners
will advance to the next round. This process will continue until there is only a single
winner left.

**Note:** Since a game can have multiple winners, there could be multiple final rounds
with only 2 players. If this is the case, the tournament manager should continue to
run rounds until one of the players is the definitive, fish-eating champion.

## Data Description
Fields:
- List<Player> participants: This list will the ongoing participants in a tournament. Once
the tournament has started, these participants will be split up and sent to a referee.
- List<Player> roundWinners: This is a list of all of the winners for the current round
of Fish. This list will start empty. Once a game is completed, its winners will be added
to this list. Once all games for a round are completed, this list will be moved over
to the participants list and cleared again for the next round.
- TournamentPhase tournamentPhase: An enum representing where in the tournament process
the manager currently is. This will be one of `SIGN_UPS`, `ONGOING`, `COMPLETE`
- Map<Integer, List<Player>> eliminations: The Key of this map will represent a round number,
while the List it maps to will be all of the players who were eliminated in that round.

`Player`, as used in this data description, should contain (at a minimum) the following information:
- how to contact the player and receive their proposed game actions
- their age (for ordering turns)
- The games of Fish this player participated in during this tournament


## Protocol
The tournament manager will start in the `SIGN_UPS` phase. Signups for an ongoing
tournament should be able to be limited in multiple ways:
- Time-based
- Limited number of players
- A combination of both

Once the desired signup period has ended, the tournament manager will proceed to the
`ONGOING` phase.
- This phase will consist of multiple rounds of fish as outlined above under Tournament Structure
- Throughout this phase, the tournament manager should be able to report results of
ongoing and completed games to observers
- The tournament manager should automate the process of transitioning from one round
of the tournament to the next

Once there is a single player remaining who has not been eliminated, the tournament
phase will advance to `COMPELTE`. The tournament manager should continue to be able
to report results of individual games/rounds to observers, as well as report the
overall winner of the tournament so that they can collect their prize.
