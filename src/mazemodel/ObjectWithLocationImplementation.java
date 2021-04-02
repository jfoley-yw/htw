package mazemodel;

/**
 * An abstract implementation of an object with a location in a maze.
 */
abstract class ObjectWithLocationImplementation implements ObjectWithLocation {
  private Location location;

  protected ObjectWithLocationImplementation(Location location) {
    if (location == null) {
      throw new IllegalArgumentException("location cannot be null");
    }
    this.location = location;
  }

  @Override
  public Location getLocation() {
    return this.location;
  }

  @Override
  public void setLocation(Location location) {
    this.location = location;
  }
}
