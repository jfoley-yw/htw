package mazemodel;

import java.util.Map;

/**
 * An interface that represents all relevant operations on a "location". A location has a row and a
 * column and doors that provide access to other locations.
 */
interface Location {

  /**
   * Get the numeric location of the Location object. This is a function of the Location object's
   * row and column and some offset.
   * @return the numeric location of the Location object
   */
  int getLocation();

  /**
   * Return whether or not the location is a hallway.
   * @return true if the location is a hallway, false otherwise
   */
  boolean isHallway();

  /**
   * Set a door in a specific direction for the location.
   * @param direction the direction to set the door in
   * @param door the door to set for the location
   */
  void setDoor(int direction, Door door);

  /**
   * Set a wumpus at this location.
   */
  void setWumpus();

  /**
   * Set a pit at this location.
   */
  void setPit();

  /**
   * Set a bat at this location.
   */
  void setBat();

  /**
   * Return whether or not this location has a wumpus.
   * @return true if this location has a wumpus, false otherwise
   */
  boolean hasWumpus();

  /**
   * Return whether or not this location has a pit.
   * @return true if this location has a pit, false otherwise
   */
  boolean hasPit();

  /**
   * Return whether or not this location has a bat.
   * @return true if this location has a bat, false otherwise
   */
  boolean hasBat();

  /**
   * Get the locations adjacent to the given location.
   * @return the locations adjacent to the given location
   */
  Map<Integer, Location> getAdjacentLocations();
}
