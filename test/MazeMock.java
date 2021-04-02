import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mazemodel.Maze;

/**
 * A class that mocks the Maze interface. It can be used to test a maze console controller.
 */
class MazeMock implements Maze {
  private StringBuilder log;
  private Map<Integer, Map<String, Integer>> options;
  private List<Integer> locations;
  private List<Integer> arrows;
  private int offset;


  /**
   * Create a MazeMock object.
   * @param log the log to use
   * @param offset the offset used to calculating location
   */
  public MazeMock(StringBuilder log, int offset) {
    this.log = log;
    this.options = new HashMap<>();
    this.locations = new ArrayList<>();
    this.arrows = new ArrayList<>();
    this.offset = offset;
  }

  /**
   * Set the mock options for a player.
   * @param player the player
   * @param options the mock options
   */
  public void setOptionsForPlayer(int player, Map<String, Integer> options) {
    this.options.put(player, options);
  }

  @Override
  public void addPlayer(int row, int col, int arrows) {
    log.append("addPlayer called with row: " + row + ", col: " + col + ", arrows: " + arrows +
            "\n");
    this.locations.add((row * this.offset) + col);
    this.arrows.add(1);
  }

  @Override
  public int getPlayerLocation(int player) {
    log.append("getPlayerLocation called for player " + player + "\n");
    return this.locations.get(player);
  }

  @Override
  public List<Integer> getValidPlayerMoves(int player) {
    log.append("getValidPlayerMoves called for player " + player + "\n");
    List<Integer> result = new ArrayList<>();
    result.add(0);
    result.add(1);
    result.add(2);
    return result;
  }

  @Override
  public List<List<Integer>> getHallwaysTraveled() {
    return new ArrayList<>();
  }

  @Override
  public void movePlayerInDirection(int direction, int player) {
    if (!this.getValidPlayerMoves(player).contains(direction)) {
      throw new IllegalArgumentException("this is not a valid direction");
    }
    log.append("movePlayerInDirection called with direction: " + direction + " for player: " +
            player + "\n");
    if (player == 0) {
      this.locations.set(player, this.locations.get(player) + 1);
    } else if (player == 1) {
      this.locations.set(player, this.locations.get(player) - 1);
    }
  }

  @Override
  public void movePlayerToLocation(int location, int player) {
    log.append("movePlayerToLocation called with location: " + location + " for player: " +
            player + "\n");
    this.locations.set(player, location);
  }

  @Override
  public boolean playerEaten(int player) {
    log.append("playerEaten called for player " + player + "\n");
    Map<String, Integer> options = this.options.get(player);
    return (options.containsKey("playerEaten") &&
            options.get("playerEaten") == this.locations.get(player));
  }

  @Override
  public boolean playerFallen(int player) {
    log.append("playerFallen called for player " + player + "\n");
    Map<String, Integer> options = this.options.get(player);
    return (options.containsKey("playerFallen") &&
            options.get("playerFallen") == this.locations.get(player));
  }

  @Override
  public boolean gameLost(int player) {
    log.append("gameLost called for player " + player + "\n");
    return (this.playerEaten(player) || this.playerFallen(player) ||
            this.getPlayerArrows(player) == 0);
  }

  @Override
  public boolean wumpusKilled(int player) {
    log.append("wumpusKilled called for player " + player + "\n");
    Map<String, Integer> options = this.options.get(player);
    return (options.containsKey("wumpusKilled") &&
            options.get("wumpusKilled") == this.arrows.get(player));
  }

  @Override
  public boolean smellWumpus(int player) {
    log.append("smellWumpus called for player " + player + "\n");
    Map<String, Integer> options = this.options.get(player);
    return (options.containsKey("smellWumpus") &&
            options.get("smellWumpus") == this.locations.get(player));
  }

  @Override
  public boolean feelDraft(int player) {
    log.append("feelDraft called for player " + player + "\n");
    Map<String, Integer> options = this.options.get(player);
    return (options.containsKey("feelDraft") &&
            options.get("feelDraft") == this.locations.get(player));
  }

  @Override
  public boolean movedByBats(int player) {
    log.append("movedByBats called for player " + player + "\n");
    Map<String, Integer> options = this.options.get(player);
    return (options.containsKey("movedByBats") &&
            options.get("movedByBats") == this.locations.get(player));
  }

  @Override
  public boolean playerOnBat(int player) {
    log.append("playerOnBat called for player " + player + "\n");
    Map<String, Integer> options = this.options.get(player);
    return (options.containsKey("playerOnBat") &&
            options.get("playerOnBat") == this.locations.get(player));
  }

  @Override
  public int getPlayerArrows(int player) {
    log.append("getPlayerArrows called for player " + player + "\n");
    return this.arrows.get(player);
  }

  @Override
  public void shootArrow(int direction, int caves, int player) {
    if (caves < 0) {
      throw new IllegalArgumentException("number of caves cannot be negative");
    }
    if (!this.getValidPlayerMoves(player).contains(direction)) {
      throw new IllegalArgumentException("this is not a valid direction");
    }
    log.append("shoot arrow called with direction: " + direction + ", caves: " + caves +
            " for player: " + player + "\n");
    this.arrows.set(player, this.arrows.get(player) - 1);
  }
}
