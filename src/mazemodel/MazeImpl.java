package mazemodel;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * An abstract class that implements the Maze interface. This class abstracts common logic between
 * perfect and non-perfect mazes.
 */
abstract class MazeImpl implements Maze {
  private List<List<Location>> maze = new ArrayList<List<Location>>();
  private List<Door> doors = new ArrayList<Door>();
  private boolean wrapping;
  private int numRemainingWalls;
  private int pitPercent;
  private int batPercent;
  private List<Player> players;
  private Random random;
  private List<Boolean> wumpusKilledStatus;
  private List<Boolean> movedByBatsStatus;
  private List<List<Integer>> hallwaysTraveled;

  protected MazeImpl(int numRows, int numCols, boolean wrapping, int seed, int numRemainingWalls,
                     int pitPercent, int batPercent) {
    this.commonSetUp(numRows, numCols, wrapping, seed, true, numRemainingWalls,
            pitPercent, batPercent);
  }

  protected MazeImpl(int numRows, int numCols, boolean wrapping, int seed,
                     int pitPercent, int batPercent) {
    this.commonSetUp(numRows, numCols, wrapping, seed, false, -1,
            pitPercent, batPercent);
  }

  @Override
  public void addPlayer(int row, int col, int arrows) {
    this.validateRow(row);
    this.validateColumn(col);

    // if a player is initialized in a hallway, then set the player's initial location to the
    // closest cave
    if (this.maze.get(row).get(col).isHallway()) {
      List<Integer> startLocation = this.getClosestCaveToLocation(row, col);
      row = startLocation.get(0);
      col = startLocation.get(1);
    }

    Player player = new MazePlayer(this.maze.get(row).get(col));
    player.addArrows(arrows);
    this.players.add(player);
    this.wumpusKilledStatus.add(false);
    this.movedByBatsStatus.add(false);
    this.conditionallyMovePlayerWithBat(player);
  }

  @Override
  public int getPlayerLocation(int player) {
    this.validatePlayerExists(player);
    return this.players.get(player).getLocation().getLocation();
  }

  @Override
  public List<Integer> getValidPlayerMoves(int player) {
    this.validatePlayerExists(player);

    List<Integer> result = new ArrayList<Integer>();
    Location playerLocation = this.players.get(player).getLocation();

    for (Map.Entry<Integer, Location> entry : playerLocation.getAdjacentLocations().entrySet()) {
      result.add(entry.getKey());
    }

    return result;
  }

  @Override
  public void movePlayerInDirection(int direction, int player) {
    this.validatePlayerExists(player);
    this.validateDirectionToMove(direction, player);
    this.hallwaysTraveled = new ArrayList<>();
    this.moveObject(this.players.get(player), direction, 1, this.hallwaysTraveled);
    this.conditionallyMovePlayerWithBat(this.players.get(player));
  }

  @Override
  public void movePlayerToLocation(int location, int player) {
    this.validatePlayerExists(player);
    int direction = this.getDirectionOfLocation(location, player);
    if (direction < 0) {
      throw new IllegalArgumentException("the player cannot move to this location");
    }
    this.movePlayerInDirection(direction, player);
  }

  @Override
  public List<List<Integer>> getHallwaysTraveled() {
    return this.hallwaysTraveled;
  }

  @Override
  public void shootArrow(int direction, int caves, int player) {
    this.validatePlayerExists(player);
    this.validateDirectionToMove(direction, player);
    this.validateCaves(caves);
    this.players.get(player).removeArrows(1);
    Arrow arrow = new MazeArrow(this.players.get(player).getLocation());
    boolean completed = this.moveObject(arrow, direction, caves, new ArrayList<>());
    if (completed && arrow.getLocation().hasWumpus()) {
      this.wumpusKilledStatus.set(player, true);
    }
  }

  @Override
  public boolean wumpusKilled(int player) {
    this.validatePlayerExists(player);
    return this.wumpusKilledStatus.get(player);
  }

  @Override
  public boolean playerEaten(int player) {
    this.validatePlayerExists(player);
    return this.players.get(player).getLocation().hasWumpus();
  }

  @Override
  public boolean playerFallen(int player) {
    this.validatePlayerExists(player);
    return this.players.get(player).getLocation().hasPit();
  }

  @Override
  public boolean gameLost(int player) {
    this.validatePlayerExists(player);
    return (this.playerEaten(player) || this.playerFallen(player) ||
            this.getPlayerArrows(player) == 0);
  }

  @Override
  public int getPlayerArrows(int player) {
    this.validatePlayerExists(player);
    return this.players.get(player).getArrows();
  }

  @Override
  public boolean smellWumpus(int player) {
    this.validatePlayerExists(player);

    for (Location adj : this.getAdjacentLocations(player)) {
      if (adj.hasWumpus()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean feelDraft(int player) {
    this.validatePlayerExists(player);

    for (Location adj : this.getAdjacentLocations(player)) {
      if (adj.hasPit()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean movedByBats(int player) {
    this.validatePlayerExists(player);
    return this.movedByBatsStatus.get(player);
  }

  @Override
  public boolean playerOnBat(int player) {
    this.validatePlayerExists(player);
    return this.players.get(player).getLocation().hasBat();
  }

  private void conditionallyMovePlayerWithBat(Player player) {
    int playerIndex = this.players.indexOf(player);
    this.movedByBatsStatus.set(playerIndex, false);

    if (!player.getLocation().hasBat()) {
      return;
    }

    while (player.getLocation().hasBat() && !player.getLocation().hasWumpus()
           && !player.getLocation().hasPit()) {
      int move = this.getRandomNumber(100);
      if (move < 50) {
        this.movedByBatsStatus.set(playerIndex, true);
        List<List<Integer>> caves = this.getNonHallwayLocations();
        List<Integer> rowCol = caves.get(this.getRandomNumber(caves.size()));
        player.setLocation(this.maze.get(rowCol.get(0)).get(rowCol.get(1)));
      } else {
        break;
      }
    }
  }

  private boolean moveObject(ObjectWithLocation obj, int direction, int caves,
                             List<List<Integer>> hallways) {
    while (caves > 0) {
      Location next = null;

      if (obj.getLocation().getAdjacentLocations().containsKey(direction)) {
        next = obj.getLocation().getAdjacentLocations().get(direction);
      } else {

        if (obj.getLocation().isHallway()) {
          int oppositeDirection = this.getOppositeDirection(direction);
          Map<Integer, Location> adj = obj.getLocation().getAdjacentLocations();
          for (Map.Entry<Integer, Location> entry : adj.entrySet()) {
            if (entry.getKey() != oppositeDirection) {
              next = entry.getValue();
              direction = entry.getKey();
            }
          }

        } else {
          return false;
        }

      }

      if (!next.isHallway()) {
        caves -= 1;
      } else {
        List<Integer> hallwayEntry = new ArrayList<>();
        hallwayEntry.add(next.getLocation());
        for (Map.Entry<Integer, Location> entry : next.getAdjacentLocations().entrySet()) {
          hallwayEntry.add(entry.getKey());
        }
        hallways.add(hallwayEntry);
      }
      obj.setLocation(next);
    }
    return true;
  }

  private int getOppositeDirection(int direction) {
    if (direction == 0) {
      return 1;
    }
    if (direction == 1) {
      return 0;
    }
    if (direction == 2) {
      return 3;
    }
    if (direction == 3) {
      return 2;
    }
    return -1;
  }

  private void commonSetUp(int numRows, int numCols, boolean wrapping, int seed,
                           boolean shouldHaveNRWValue, int numRemainingWalls,
                           int pitPercent, int batPercent) {
    if (numRows <= 0) {
      throw new IllegalArgumentException("number of rows must be positive");
    }
    if (numCols <= 0) {
      throw new IllegalArgumentException("number of columns must be positive");
    }

    this.validatePercentage(pitPercent, "percentage of pits is not valid");
    this.validatePercentage(batPercent, "percentage of bats is not valid");

    this.pitPercent = pitPercent;
    this.batPercent = batPercent;
    this.wrapping = wrapping;
    this.players = new ArrayList<>();
    this.wumpusKilledStatus = new ArrayList<>();
    this.movedByBatsStatus = new ArrayList<>();
    this.hallwaysTraveled = new ArrayList<>();

    if (seed >= 0) {
      this.random = new Random(seed);
    } else {
      this.random = new Random();
    }

    this.constructMaze(numRows, numCols);

    this.addDoors(this.doors);

    if (!shouldHaveNRWValue) {
      numRemainingWalls = this.doors.size() - (numRows * numCols) + 1;
    }
    this.validateNumRemainingWalls(numRemainingWalls, numRows, numCols);
    this.numRemainingWalls = numRemainingWalls;

    this.buildMazePaths();

    this.setWumpus();
    this.setPits();
    this.setBats();
  }

  private void setWumpus() {
    List<List<Integer>> caves = this.getNonHallwayLocations();
    List<Integer> rowCol = caves.get(this.getRandomNumber(caves.size()));
    this.maze.get(rowCol.get(0)).get(rowCol.get(1)).setWumpus();
  }

  private void setPits() {
    List<List<Integer>> caves = this.getNonHallwayLocations();
    for (List<Integer> rowCol : caves) {
      int pit = this.getRandomNumber(100);
      if (pit < this.pitPercent) {
        this.maze.get(rowCol.get(0)).get(rowCol.get(1)).setPit();
      }
    }
  }

  private void setBats() {
    List<List<Integer>> caves = this.getNonHallwayLocations();
    for (List<Integer> rowCol : caves) {
      int bat = this.getRandomNumber(100);
      if (bat < this.batPercent) {
        this.maze.get(rowCol.get(0)).get(rowCol.get(1)).setBat();
      }
    }
  }

  private List<List<Integer>> getNonHallwayLocations() {
    List<List<Integer>> result = new ArrayList<List<Integer>>();

    for (int i = 0; i < this.maze.size(); i++) {
      for (int j = 0; j < this.maze.get(i).size(); j++) {
        if (!this.maze.get(i).get(j).isHallway()) {
          List<Integer> rowCol = new ArrayList<Integer>();
          rowCol.add(i);
          rowCol.add(j);
          result.add(rowCol);
        }
      }
    }

    return result;
  }

  private void constructMaze(int numRows, int numCols) {
    for (int row = 0; row < numRows; row++) {
      this.maze.add(new ArrayList<Location>());
      for (int col = 0; col < numCols; col++) {
        Location location = new MazeLocation(row, col, numCols);
        this.maze.get(row).add(location);
      }
    }
  }

  private void buildMazePaths() {
    List<Door> saved = new ArrayList<Door>();
    List<Integer> connectedComponents = this.createConnectedComponents();

    int numDoors = this.doors.size();
    while (numDoors > 0) {
      Door door = this.doors.remove(this.getRandomNumber(numDoors));
      numDoors -= 1;
      Location location1 = door.getLocationOne();
      Location location2 = door.getLocationTwo();
      int component1 = this.getComponentForLocation(connectedComponents, location1.getLocation());
      int component2 = this.getComponentForLocation(connectedComponents, location2.getLocation());
      if (component1 > component2) {
        this.setComponentForLocation(connectedComponents, component2, component1);
        door.openDoor();
      } else if (component2 > component1) {
        this.setComponentForLocation(connectedComponents, component1, component2);
        door.openDoor();
      } else {
        saved.add(door);
      }
    }

    int remainingWallsToRemove = saved.size() - this.numRemainingWalls;
    for (int i = 0; i < remainingWallsToRemove; i++) {
      saved.get(i).openDoor();
    }
  }

  private int getRandomNumber(int upperBound) {
    return this.random.nextInt(upperBound);
  }

  private int getComponentForLocation(List<Integer> connectedComponents, int location) {
    while (location != connectedComponents.get(location)) {
      location = connectedComponents.get(location);
    }
    return location;
  }

  private void setComponentForLocation(List<Integer> connectedComponents, int location,
                                       int newComponent) {
    connectedComponents.set(location, newComponent);
  }

  private List<Integer> createConnectedComponents() {
    List<Integer> connectedComponents = new ArrayList<Integer>();
    Integer numRows = this.maze.size();
    Integer numCols = this.maze.get(0).size();

    for (int i = 0; i < (numRows * numCols); i++) {
      connectedComponents.add(i);
    }

    return connectedComponents;
  }

  private void addDoors(List<Door> doors) {
    this.addEastWestDoors(doors);
    this.addNorthSouthDoors(doors);
    if (this.wrapping) {
      this.addWrappingDoors(doors);
    }
  }

  private void addEastWestDoors(List<Door> doors) {
    for (int row = 0; row < this.maze.size(); row++) {
      List<Location> currRow = this.maze.get(row);
      for (int col = 0; col < currRow.size() - 1; col++) {
        Location westLocation = currRow.get(col);
        Location eastLocation =  currRow.get(col + 1);
        Door door = new MazeDoor(westLocation, eastLocation);
        westLocation.setDoor(2, door);
        eastLocation.setDoor(3, door);
        doors.add(door);
      }
    }
  }

  private void addNorthSouthDoors(List<Door> doors) {
    for (int col = 0; col < this.maze.get(0).size(); col++) {
      for (int row = 0; row < this.maze.size() - 1; row++) {
        Location northLocation = this.maze.get(row).get(col);
        Location southLocation = this.maze.get(row + 1).get(col);
        Door door = new MazeDoor(northLocation, southLocation);
        northLocation.setDoor(1, door);
        southLocation.setDoor(0, door);
        doors.add(door);
      }
    }
  }

  private void addWrappingDoors(List<Door> doors) {
    for (int row = 0; row < this.maze.size(); row++) {
      List<Location> currRow = this.maze.get(row);
      Location westMostLocation = currRow.get(0);
      Location eastMostLocation = currRow.get(currRow.size() - 1);
      Door door = new MazeDoor(westMostLocation, eastMostLocation);
      westMostLocation.setDoor(3, door);
      eastMostLocation.setDoor(2, door);
      doors.add(door);
    }

    for (int col = 0; col < this.maze.get(0).size(); col++) {
      Location northMostLocation = this.maze.get(0).get(col);
      Location southMostLocation = this.maze.get(this.maze.size() - 1).get(col);
      Door door = new MazeDoor(northMostLocation, southMostLocation);
      northMostLocation.setDoor(0, door);
      southMostLocation.setDoor(1, door);
      doors.add(door);
    }
  }

  private List<Location> getAdjacentLocations(int player) {
    List<Location> result = new ArrayList<>();
    for (int dir : this.getValidPlayerMoves(player)) {
      Arrow sensor = new MazeArrow(this.players.get(player).getLocation());
      this.moveObject(sensor, dir, 1, new ArrayList<>());
      result.add(sensor.getLocation());
    }
    return result;
  }

  private List<Integer> getClosestCaveToLocation(int row, int col) {
    double closestDistance = Double.POSITIVE_INFINITY;
    List<Integer> closest = new ArrayList<>();
    for (List<Integer> rowCol : this.getNonHallwayLocations()) {
      int currRow = rowCol.get(0);
      int currCol = rowCol.get(1);
      double dist = Math.sqrt((currRow - row) * (currRow - row) + (currCol - col) *
              (currCol - col));
      if (dist < closestDistance) {
        closestDistance = dist;
        closest = rowCol;
      }
    }
    return closest;
  }

  private int getDirectionOfLocation(int location, int player) {
    for (int dir : this.getValidPlayerMoves(player)) {
      Arrow sensor = new MazeArrow(this.players.get(player).getLocation());
      this.moveObject(sensor, dir, 1, new ArrayList<>());
      if (sensor.getLocation().getLocation() == location) {
        return dir;
      }
    }
    return -1;
  }

  private void validatePlayerExists(int player) {
    if (player >= this.players.size()) {
      throw new IllegalStateException("this player does not exist");
    }
  }

  private void validateRow(int row) {
    if (row < 0 || row >= this.maze.size()) {
      throw new IllegalArgumentException("not a valid row");
    }
  }

  private void validateColumn(int col) {
    if (col < 0 || col >= this.maze.get(0).size()) {
      throw new IllegalArgumentException("not a valid column");
    }
  }

  private void validateNumRemainingWalls(int numRemainingWalls, int numRows, int numCols) {
    if (numRemainingWalls < 0 || numRemainingWalls >
            (this.doors.size() - (numRows * numCols) + 1)) {
      throw new IllegalArgumentException("numRemainingWalls is not valid");
    }
  }

  private void validateDirectionToMove(int direction, int player) {
    if (!this.getValidPlayerMoves(player).contains(direction)) {
      throw new IllegalArgumentException("this is not a valid direction");
    }
  }

  private void validatePercentage(int percent, String error) {
    if (percent < 0 || percent > 100) {
      throw new IllegalArgumentException(error);
    }
  }

  private void validateCaves(int caves) {
    if (caves < 0) {
      throw new IllegalArgumentException("number of caves cannot be negative");
    }
  }
}
