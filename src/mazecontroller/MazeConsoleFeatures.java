package mazecontroller;

import java.io.IOException;

import mazemodel.Maze;

/**
 * The features offered by the maze console game.
 */
public interface MazeConsoleFeatures {

  /**
   * Start the game.
   * @param maze the maze to use in the game
   * @param rows the number of rows in the maze
   * @param cols the number of columns in the maze
   * @param playerArrows the number of arrows each player has
   * @param twoPlayers true if the game has two players, false otherwise
   * @throws IOException an IOException
   */
  void start(Maze maze, int rows, int cols, int playerArrows, boolean twoPlayers)
          throws IOException;
}
