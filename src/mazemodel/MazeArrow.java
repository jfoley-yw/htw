package mazemodel;

/**
 * A class that represents an arrows in a maze.
 */
public class MazeArrow extends ObjectWithLocationImplementation implements Arrow {

  /**
   * Create a MazeArrow object.
   * @param location the location of the maze arrow
   */
  public MazeArrow(Location location) {
    super(location);
  }
}
