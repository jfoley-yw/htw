package mazemodel;

/**
 * An interface that represents an object with a location in a maze.
 */
interface ObjectWithLocation {

  /**
   * Get the current location of the object.
   * @return a Location object representing the object's current location
   */
  Location getLocation();

  /**
   * Set the object's location.
   * @param location the location of the object
   */
  void setLocation(Location location);
}
