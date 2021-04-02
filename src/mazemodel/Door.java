package mazemodel;

/**
 * An interface that represents operations on a "door". A door connects two locations and can be
 * opened.
 */
interface Door {

  /**
   * Open the door.
   */
  void openDoor();

  /**
   * Return whether or not the door is open.
   * @return true if the door is open, false otherwise
   */
  boolean isOpen();

  /**
   * Return the location on the first side of the door.
   * @return the location on the first side of the door
   */
  Location getLocationOne();

  /**
   * Return the location on the second side of the door.
   * @return the location on the second side of the door
   */
  Location getLocationTwo();

  /**
   * Get the location on the side of the door opposite the specified location.
   * @param location the specified location
   * @return the location on the side of the door opposite the specified location
   */
  Location getOtherSide(Location location);
}
