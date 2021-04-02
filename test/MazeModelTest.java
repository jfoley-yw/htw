import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;

import mazemodel.Maze;
import mazemodel.NonPerfectMaze;

/**
 * A class to test the model of the Hunt the Wumpus MVC game.
 */
public class MazeModelTest {
  private Maze maze;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Creates a wrapping, non-perfect maze that has the following open doors.
   * (4, 7).
   * (3, 6).
   * (6, 7).
   * (0, 1).
   * (1, 2).
   * (2, 5).
   * (4, 5).
   * (7, 8).
   * (0, 2).
   * (0, 3).
   * (5, 8).
   * (2, 8).
   * (6, 8).
   * The following location has a wumpus.
   * 2.
   * The following locations have pits.
   * 5, 7.
   * The following location has a bat.
   * 6.
   * The following locations are hallways.
   * 1, 3, 4.
   */
  @Before
  public void setUp() throws Exception {
    this.maze = new NonPerfectMaze(3, 3, true, 10,
            5, 30, 30);
  }

  @Test
  public void testGetValidPlayerMoves() {
    this.maze.addPlayer(0, 0, 3);

    int[] expectedValidPlayerMoves = {1, 2, 3};

    List<Integer> validPlayerMovesList = this.maze.getValidPlayerMoves(0);
    int[] validPlayerMovesArray = new int[validPlayerMovesList.size()];
    for (int i = 0; i < validPlayerMovesList.size(); i++) {
      validPlayerMovesArray[i] = validPlayerMovesList.get(i);
    }

    assertArrayEquals(expectedValidPlayerMoves, validPlayerMovesArray);
  }

  @Test
  public void testGetHallwaysTraveled() {
    this.maze.addPlayer(0, 0, 3);
    this.maze.movePlayerToLocation(6, 0);

    List<List<Integer>> hallways = this.maze.getHallwaysTraveled();
    assertEquals(1, hallways.size());
    List<Integer> hallway = hallways.get(0);
    assertEquals(3, hallway.size());
    assertEquals(3, (int)hallway.get(0));
    assertEquals(0, (int)hallway.get(1));
    assertEquals(1, (int)hallway.get(2));
  }

  @Test
  public void testGetHallwaysTraveledWithNoPlayers() {
    List<List<Integer>> hallways = this.maze.getHallwaysTraveled();
    assertEquals(0, hallways.size());
  }

  @Test
  public void testNavigationThroughMaze() {
    this.maze.addPlayer(0, 0, 3);

    assertTrue(this.maze.smellWumpus(0));
    assertFalse(this.maze.feelDraft(0));
    assertEquals(0, this.maze.getPlayerLocation(0));

    this.maze.movePlayerInDirection(1, 0);

    assertFalse(this.maze.playerOnBat(0));
    assertTrue(this.maze.movedByBats(0));
    assertEquals(0, this.maze.getPlayerLocation(0));

    this.maze.movePlayerToLocation(6, 0);

    assertTrue(this.maze.playerOnBat(0));
    assertFalse(this.maze.movedByBats(0));
    assertTrue(this.maze.feelDraft(0));
    assertFalse(this.maze.smellWumpus(0));
    assertEquals(6, this.maze.getPlayerLocation(0));

    this.maze.movePlayerInDirection(3, 0);

    assertFalse(this.maze.playerOnBat(0));
    assertFalse(this.maze.movedByBats(0));
    assertTrue(this.maze.feelDraft(0));
    assertTrue(this.maze.smellWumpus(0));
    assertEquals(8, this.maze.getPlayerLocation(0));

    this.maze.shootArrow(0, 3, 0);
    assertFalse(this.maze.wumpusKilled(0));

    this.maze.shootArrow(1, 1, 0);
    assertTrue(this.maze.wumpusKilled(0));

    this.maze.movePlayerInDirection(0, 0);
    assertTrue(this.maze.gameLost(0));
    assertTrue(this.maze.playerFallen(0));

    this.maze.movePlayerInDirection(0, 0);
    assertTrue(this.maze.playerEaten(0));

    assertEquals(1, this.maze.getPlayerArrows(0));

    // add a second player
    this.maze.addPlayer(2, 2, 5);

    assertEquals(8, this.maze.getPlayerLocation(1));
    assertEquals(5, this.maze.getPlayerArrows(1));
    assertFalse(this.maze.wumpusKilled(1));
    assertFalse(this.maze.playerFallen(1));
    assertFalse(this.maze.movedByBats(1));
    assertFalse(this.maze.playerOnBat(1));
    assertTrue(this.maze.smellWumpus(1));
    assertTrue(this.maze.feelDraft(1));

    this.maze.shootArrow(1, 1, 1);

    assertTrue(this.maze.wumpusKilled(1));

    this.maze.movePlayerInDirection(1, 1);

    assertTrue(this.maze.gameLost(1));
    assertTrue(this.maze.playerEaten(1));
  }

  @Test
  public void testCreateMazeWithNegativeNumRows() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("number of rows must be positive");

    Maze badMaze = new NonPerfectMaze(-1, 2, true, -1,
            5, 5, 5);
  }

  @Test
  public void testCreateMazeWithZeroColumns() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("number of columns must be positive");

    Maze badMaze = new NonPerfectMaze(1, 0, true, -1,
            5, 5, 5);
  }

  @Test
  public void testGetValidMovesForNonExistentPlayer() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("this player does not exist");

    this.maze.getValidPlayerMoves(0);
  }

  @Test
  public void testMoveNonExistentPlayer() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("this player does not exist");

    this.maze.movePlayerInDirection(0, 0);
  }

  @Test
  public void testSmellWumpusWithNonExistentPlayer() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("this player does not exist");

    this.maze.smellWumpus(0);
  }

  @Test
  public void testFeelDraftWithNonExistentPlayer() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("this player does not exist");

    this.maze.feelDraft(0);
  }

  @Test
  public void testPlayerFallenWithNonExistentPlayer() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("this player does not exist");

    this.maze.playerFallen(0);
  }

  @Test
  public void testPlayerEatenWithNonExistentPlayer() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("this player does not exist");

    this.maze.playerEaten(0);
  }

  @Test
  public void testGameLostWithNonExistentPlayer() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("this player does not exist");

    this.maze.gameLost(0);
  }

  @Test
  public void testShootArrowWithNonExistentPlayer() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("this player does not exist");

    this.maze.shootArrow(1, 1, 0);
  }

  @Test
  public void testMakeInvalidMove() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("this is not a valid direction");

    this.maze.addPlayer(0, 0, 3);
    this.maze.movePlayerInDirection(0, 0);
  }

  @Test
  public void testMoveToInvalidLocation() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("the player cannot move to this location");

    this.maze.addPlayer(0, 0, 3);
    this.maze.movePlayerToLocation(8, 0);
  }

  @Test
  public void testShootArrowInInvalidDirection() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("this is not a valid direction");

    this.maze.addPlayer(0, 0, 3);
    this.maze.shootArrow(0, 1, 0);
  }

  @Test
  public void testShootArrowThroughNegativeNumberOfCaves() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("number of caves cannot be negative");

    this.maze.addPlayer(0, 0, 3);
    this.maze.shootArrow(1, -1, 0);
  }

  @Test
  public void testShootArrowWhenNoArrowsExist() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("you cannot remove more arrows than you have");

    this.maze.addPlayer(0, 0, 0);
    this.maze.shootArrow(1, 1, 0);
  }

  @Test
  public void testInitializePlayerWithNegativeArrows() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("number of arrows cannot be negative");

    this.maze.addPlayer(0, 0, -1);
  }

  @Test
  public void testBadNumRemainingWalls() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("numRemainingWalls is not valid");

    Maze badMaze = new NonPerfectMaze(2, 2, true, -1,
            10, 5, 5);
  }
}
