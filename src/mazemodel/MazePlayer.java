package mazemodel;

/**
 * An implementation of the Player interface that represents a player in a maze. A player in a maze
 * has a current location in the maze.
 */
class MazePlayer extends ObjectWithLocationImplementation implements Player {
  private int arrows;

  /**
   * Construct a MazePlayer object.
   * @param location the initial location of the player
   */
  public MazePlayer(Location location) {
    super(location);
    this.arrows = 0;
  }

  @Override
  public void addArrows(int arrows) {
    this.validateArrows(arrows, false);
    this.arrows += arrows;
  }

  @Override
  public void removeArrows(int arrows) {
    this.validateArrows(arrows, true);
    this.arrows -= arrows;
  }

  @Override
  public int getArrows() {
    return this.arrows;
  }

  private void validateArrows(int arrows, boolean subtract) {
    if (arrows < 0) {
      throw new IllegalArgumentException("number of arrows cannot be negative");
    }

    if (subtract && arrows > this.arrows) {
      throw new IllegalStateException("you cannot remove more arrows than you have");
    }
  }
}
