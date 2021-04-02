package mazemodel;

import java.util.List;

/**
 * An interface that represents all operations on a "maze". A maze holds locations that are
 * connected to other locations, and provides operations that allow the player to move throughout
 * these locations.
 */
public interface Maze {

  /**
   * Add a player to the maze. A player must be added before the maze can be solved.
   * @param row the starting row of the player
   * @param col the starting column of the player
   * @param arrows the number of arrows that the player has
   */
  void addPlayer(int row, int col, int arrows);

  /**
   * Get the current location of a player.
   * @param player the player
   * @return the current location of the player
   */
  int getPlayerLocation(int player);

  /**
   * Returns a list representing valid directions that a player can move in.
   * @param player the player
   * @return a list of valid directions that the player can move in (directions are represented as
   *         integers)
   */
  List<Integer> getValidPlayerMoves(int player);

  /**
   * Move a player by one unit in the specified direction. Valid direction values are:
   * 0 - represents North.
   * 1 - represents South.
   * 2 - represents East.
   * 3 - represents West.
   * @param direction the direction to move the player in (represented as an integer)
   * @param player the player
   */
  void movePlayerInDirection(int direction, int player);

  /**
   * Move a player by one unit to the specified location.
   * @param location the location to move the player to
   * @param player the player
   */
  void movePlayerToLocation(int location, int player);

  /**
   * Get the hallways that a player traveled through most recently, along with the directions
   * corresponding to each hallway.
   * @return a 2-D list where each entry is of the form [location, direction1, direction2]
   */
  List<List<Integer>> getHallwaysTraveled();

  /**
   * Return whether or not a player has been eaten.
   * @param player the player
   * @return true if the player has been eaten, false otherwise
   */
  boolean playerEaten(int player);

  /**
   * Return whether or not the player has fallen into a pit.
   * @param player the player
   * @return true if the player has fallen into a pit, false otherwise
   */
  boolean playerFallen(int player);

  /**
   * Return whether or not the game has been lost for a player.
   * @param player the player
   * @return true if the game has been lost, false otherwise
   */
  boolean gameLost(int player);

  /**
   * Return whether or not the wumpus has been killed by a player.
   * @param player the player
   * @return true if the wumpus has been killed, false otherwise
   */
  boolean wumpusKilled(int player);

  /**
   * Return whether or not there is a wumpus in a location adjacent to a player's location.
   * @param player the player
   * @return true if there is a wumpus in a location adjacent to the player's location, false
   *         otherwise
   */
  boolean smellWumpus(int player);

  /**
   * Return whether or not there is a pit in a location adjacent to a player's location.
   * @param player the player
   * @return true if there is a pit in a location adjacent to the player's location, false otherwise
   */
  boolean feelDraft(int player);

  /**
   * Return whether or not a player was moved by bats on the previous turn.
   * @param player the player
   * @return true if the player was moved by bats on the previous turn, false otherwise
   */
  boolean movedByBats(int player);

  /**
   * Return whether or not a player is in the same location as a bat.
   * @param player the player
   * @return true if the player is in the same location as a bat, false otherwise
   */
  boolean playerOnBat(int player);

  /**
   * Get the number of arrows that a player currently has.
   * @param player the player
   * @return the number of arrows that the player currently has
   */
  int getPlayerArrows(int player);

  /**
   * Make a player shoot an arrow through a specified number of caves in a certain direction.
   * @param direction the direction to shoot the arrow in
   * @param caves the number of caves the arrow should travel through
   * @param player the player
   */
  void shootArrow(int direction, int caves, int player);
}
