package mazecontroller;

/**
 * The features offered by the maze game.
 */
public interface MazeFeatures {

  /**
   * Start the game.
   * @param numRows the number of rows in the maze
   * @param numCols the number of columns in the maze
   * @param wrapping true if the maze is wrapping, false otherwise
   * @param seed the random seed to set for the maze
   * @param numWalls the number of remaining walls in the maze
   * @param percentPits the percentage of caves that have pits
   * @param percentBats the percentage of caves that have bats
   * @param numArrows the number of arrows for the player
   * @param twoPlayers true if there are two players in the game, false otherwise
   */
  void startGame(int numRows, int numCols, boolean wrapping, int seed, int numWalls,
                 int percentPits, int percentBats, int numArrows, boolean twoPlayers);

  /**
   * Restart the current game.
   */
  void restartGame();

  /**
   * Move the player in the specified direction.
   * @param direction the direction to move the player in
   */
  void moveInDirection(int direction);

  /**
   * Move the player to the specified location.
   * @param location the location to move the player to
   */
  void moveToLocation(int location);

  /**
   * Shoot an arrow in the specified direction through the specified number of caves.
   * @param direction the direction to shoot the arrow in
   * @param caves the number of caves to shoot the arrow through
   */
  void shootArrow(int direction, int caves);

  /**
   * End the game.
   */
  void endGame();
}
