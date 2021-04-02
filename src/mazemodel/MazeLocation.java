package mazemodel;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the Location interface that represents a location in a maze. A MazeLocation
 * object needs an "offset" value in order to calculate its location value based on its row and
 * column. MazeLocation objects can have a door to the North, a door to the South, a door to the
 * East, and a door to the West.
 */
class MazeLocation implements Location {
  private final int row;
  private final int column;
  private final int offset;
  private Map<Integer, Door> doors;
  private boolean wumpus;
  private boolean bat;
  private boolean pit;

  /**
   * Construct a MazeLocation object.
   * @param row the row of the location
   * @param column the column of the location
   * @param offset the offset needed to calculate the object's location value
   */
  public MazeLocation(int row, int column, int offset) {
    super();
    if (row < 0) {
      throw new IllegalArgumentException("row cannot be negative");
    }
    if (column < 0) {
      throw new IllegalArgumentException("column cannot be negative");
    }
    if (offset < 0) {
      throw new IllegalArgumentException("offset cannot be negative");
    }
    this.row = row;
    this.column = column;
    this.offset = offset;
    this.doors = this.initializeDoors();
    this.wumpus = false;
    this.bat = false;
    this.pit = false;
  }

  @Override
  public int getLocation() {
    return (this.row * this.offset) + this.column;
  }

  @Override
  public boolean isHallway() {
    return this.getAdjacentLocations().size() == 2;
  }

  @Override
  public void setDoor(int direction, Door door) {
    this.validateDirection(direction);
    this.doors.put(direction, door);
  }

  @Override
  public Map<Integer, Location> getAdjacentLocations() {
    Map<Integer, Location> result = new HashMap<>();

    for (Map.Entry<Integer, Door> entry : this.doors.entrySet()) {
      if (entry.getValue() != null && entry.getValue().isOpen()) {
        result.put(entry.getKey(), entry.getValue().getOtherSide(this));
      }
    }

    return result;
  }

  @Override
  public boolean hasWumpus() {
    return this.wumpus;
  }

  @Override
  public boolean hasBat() {
    return this.bat;
  }

  @Override
  public boolean hasPit() {
    return this.pit;
  }

  @Override
  public void setWumpus() {
    this.wumpus = true;
  }

  @Override
  public void setBat() {
    this.bat = true;
  }

  @Override
  public void setPit() {
    this.pit = true;
  }

  private void validateDirection(int direction) {
    int[] validDirections = {0, 1, 2, 3};
    boolean valid = false;

    for (int dir : validDirections) {
      if (direction == dir) {
        valid = true;
      }
    }

    if (!valid) {
      throw new IllegalArgumentException("this is not a valid direction");
    }
  }

  private Map<Integer, Door> initializeDoors() {
    Map<Integer, Door> result = new HashMap<>();
    result.put(0, null);
    result.put(1, null);
    result.put(2, null);
    result.put(3, null);
    return result;
  }
}
