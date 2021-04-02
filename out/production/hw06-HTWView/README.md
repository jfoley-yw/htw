# HW 6 - HTWView

## Instructions

1. To play the game with the GUI, run the program like so:
```
java -jar <path/to/hw06-HTWView.jar> --gui
```

2. To play the game in the console mode, run the program like so:
```
java -jar <path/to/hw06-HTWView.jar> --text <numRows> <numCols> <numWalls> <seed> <percentPits> <percentBats> <playerArrows> <twoPlayers> <wrapping>
```
where `numRows`, `numCols`, `numWalls`, `seed`, `percentPits`, `percentBats`, and `playerArrows` are numbers, and `twoPlayers` and `wrapping` are either `true`
or `false`.

Example:

```
java -jar hw06-HTWView.jar --text 5 5 10 10 20 20 5 true false
```

When running the game with the GUI, passing in any other command-line arguments besides for `--gui` will result in the program exiting immediately.

When running the game in console mode, passing in any set of arguments that differ from the set/order of arguments above will result in the program exiting
immediately.

If the game is run with no command-line arguments then the program will exit immediately.

## List of features

1. The program can either be run in GUI mode or in console mode.

2. In either case, the user can specify:

- The number of rows in the maze.
- The number of columns in the maze.
- The number of walls in the maze.
- A random seed.
- The percentage of caves with pits.
- The percentage of caves with bats.
- The number of arrows that each player starts out with.
- Whether the game is single-player or two-player.
- Whether the maze is wrapping or non-wrapping.

3. In GUI mode, the user can move the player by either clicking a location in the maze or pressing an arrow key.

4. In GUI mode, the user can shoot an arrow by entering the direction of the shot and the distance of the shot in the controls panel below the maze.

5. In GUI mode, the user can restart the game with the same random seed by clicking the "Restart" button.

6. In GUI mode, the user can end the game prematurely by clicking the "End Game" button.

7. In GUI mode, if the entire maze does not fit onto the screen, then the user can scroll the screen to see the entire maze.

8. In GUI mode, locations only become visible in the maze once at least one player has visited that location.

9. In two-player mode, the game ends when one of the players kills the wumpus, or when both players lose the game. A player loses the game if the player is eaten
by the wumpus, falls into a pit, or runs out of arrows.

## Changes from HW5

1. The maze model and console controller from HW5 were both adjusted to support playing a game with two players. This required slight modifications to most of
the methods in the model as well as some slight changes to the logic within the console controller.

2. The `avoidedBats()` method in the maze model was replaced with a new method called `playerOnBat()`. This is so that the GUI controller can get the information
necessary from the model to update the relevant tiles in the view with bat images. Before, `avoidedBats()` would return `false` if the player was picked up and
carried away by bats, but still wound up in a location with bats. Therefore `avoidedBats()` was an unreliable method to determine if a player was standing on a
bat.

3. The name of the `movePlayer()` method in the maze model was changed to `movePlayerInDirection()`. This was so that an additional method in the maze model could
be added, namely `movePlayerToLocation()`. `movePlayerInDirection()` is called in the GUI implementation when a user presses an arrow key, and
`movePlayerToLocation()` is called in the GUI implementation when a user clicks on a location in the maze. In the console implementation, only
`movePlayerInDirection()` is needed.

4. An additional method, `getHallwaysTraveled()`, was also added to the maze model. This is so that the GUI controller can get the hallway locations traveled
through in the most recent move and tell the view to draw these hallway tiles in the maze interface.

## Notes

1. I used a custom Java Swing component that I found online in order to support dynamically resizing images when the window is resized. The class is called
`BackgroundPanel` and the code was found here: https://tips4java.wordpress.com/2008/10/12/background-panel/. I make sure to note this in a comment in
`BackgroundPanel.java`. I only use this class in one place in my code (in `BoardView.java`).

2. When running the GUI implementation, the window will initialize to be very small and the user will have to resize it to see the full screen. This is a small
bug in the implementation.

3. In two-player mode, if both players are on the same tile, then one player will completely overlap the other player (so it looks like only player is on the
tile). This is another small bug in the implementation.

4. If a player is picked up and carried away by bats, then the only location that will become visible is the final location that the player ends up in at the end
of the turn.

5. In GUI single-player mode, the player will be initialized at location (0, 0). In GUI two-player mode, Player 1 will initialize at location (0, 0) and
Player 2 will initialize at location (numRows - 1, numCols - 1). If either of these locations are hallways, then the corresponding player will instead
be initialized at the nearest cave (using Euclidean distance).

6. In the `HTW_game.png` image uploaded in the `/res` directory, you can see that the fairy player is seemingly in a random part of the maze surrounded by black
tiles. This is because in a prior turn the fairy player was transported by bats. You can also see a breeze on the tile above the fairy (signaling that a pit
is nearby). I specify this only because the breeze image can be difficult to see on a tile.
