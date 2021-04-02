package mazeview;

import java.util.List;

import mazecontroller.MazeFeatures;

/**
 * Operations encapsulated by the maze view.
 */
public interface MazeView {

  /**
   * Display a picture at a given location in the maze.
   * The following are valid picture keys: player, bats, pit, wumpus, breeze, stench, or a valid
   * tile key.
   * NOTE: Player must be set at a location AFTER all other pictures have been set at that location.
   * @param location the location to display the picture
   * @param picKey the key of the picture to display
   */
  void setPictureAtLocation(int location, String picKey);

  /**
   * Get the tile key associated with a list of directions.
   * @param directions the list of directions
   * @return the tile key
   */
  String getTileKey(List<Integer> directions);

  /**
   * Display the maze view.
   * @param numRows the number of rows in the maze
   * @param numCols the number of columns in the maze
   */
  void displayMaze(int numRows, int numCols);

  /**
   * Display the menu view.
   */
  void displayMenu();

  /**
   * Set a message detailing the results of a move in the view.
   * @param message the message to display
   * @param player the player who moved
   */
  void setMoveMessage(String message, int player);

  /**
   * Set the game-over message in the view.
   * @param winner the winner if there is one, otherwise -1
   */
  void setGameOverMessage(int winner);

  /**
   * Set the number of arrows that a player has left to display in the view.
   * @param arrows the number of arrows that the player has left to display in the view
   * @param player the player
   */
  void setArrowCount(int arrows, int player);

  /**
   * Set the current player.
   * @param player the current player
   */
  void setCurrentPlayer(int player);

  /**
   * Reset the focus of the main frame.
   */
  void resetFocus();

  /**
   * Set the menu features on the view.
   * @param features the features to set on the menu
   */
  void setMenuFeatures(MazeFeatures features);

  /**
   * Set the game features on the view.
   * @param features the features to set on the game
   */
  void setGameFeatures(MazeFeatures features);
}
