package mazemodel;

/**
 * An implementation of the Door interface that represents a door in a maze. A door in a maze
 * contains two locations and can be opened.
 */
class MazeDoor implements Door {
  private final Location locationOne;
  private final Location locationTwo;
  private boolean open = false;

  /**
   * Construct a MazeDoor object.
   * @param locationOne the location on the first side of the door
   * @param locationTwo the location on the second side of the door
   */
  public MazeDoor(Location locationOne, Location locationTwo) {
    if (locationOne == null || locationTwo == null) {
      throw new IllegalArgumentException("locations cannot be null");
    }
    this.locationOne = locationOne;
    this.locationTwo = locationTwo;
  }

  @Override
  public void openDoor() {
    this.open = true;
  }

  @Override
  public boolean isOpen() {
    return this.open;
  }

  @Override
  public Location getLocationOne() {
    return this.locationOne;
  }

  @Override
  public Location getLocationTwo() {
    return this.locationTwo;
  }

  @Override
  public Location getOtherSide(Location location) {
    if (this.locationOne.equals(location)) {
      return this.locationTwo;
    } else if (this.locationTwo.equals(location)) {
      return this.locationOne;
    } else {
      throw new IllegalArgumentException("the door does not contain this location");
    }
  }
}
