package mazemodel;

/**
 * This class represents a perfect maze. In a perfect maze, there exists only one path between
 * any two locations in the maze.
 */
public class PerfectMaze extends MazeImpl {

  /**
   * Construct a PerfectMaze object.
   * @param numRows the number of rows in the maze
   * @param numCols the number of columns in the maze
   * @param wrapping a boolean representing whether or not the maze is wrapping
   * @param seed a seed to set so that the same "random" maze is generated each time, pass in -1
   *             if setting a seed is not desired
   */
  public PerfectMaze(int numRows, int numCols, boolean wrapping, int seed,
                     int pitPercent, int batPercent) {
    super(numRows, numCols, wrapping, seed, pitPercent, batPercent);
  }
}
