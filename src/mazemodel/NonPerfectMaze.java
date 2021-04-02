package mazemodel;

/**
 * This class represents a non-perfect maze. A non-perfect maze may have more than one path between
 * any two locations in the maze.
 */
public class NonPerfectMaze extends MazeImpl {

  /**
   * Construct a NonPerfectMaze object.
   * @param numRows the number of rows in the maze
   * @param numCols the number of columns in the maze
   * @param wrapping a boolean representing whether or not the maze is wrapping
   * @param seed a seed to set so that the same "random" maze is generated each time, pass in -1
   *             if setting a seed is not desired
   * @param numWallsRemaining the number of walls that should remain erected in the maze
   */
  public NonPerfectMaze(int numRows, int numCols, boolean wrapping, int seed, int numWallsRemaining,
                        int pitPercent, int batPercent) {
    super(numRows, numCols, wrapping, seed, numWallsRemaining, pitPercent, batPercent);
  }
}
