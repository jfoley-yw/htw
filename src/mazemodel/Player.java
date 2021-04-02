package mazemodel;

/**
 * An interface that represents operations on a "player". A player has a current location.
 */
interface Player extends ObjectWithLocation {

  /**
   * Add arrows for the current player.
   * @param arrows the number of arrows to add
   */
  void addArrows(int arrows);

  /**
   * Remove arrows from the current player.
   * @param arrows the number of arrows to remove
   */
  void removeArrows(int arrows);

  /**
   * Get the number of arrows that the current player has.
   * @return the number of arrows that the current player has
   */
  int getArrows();
}
