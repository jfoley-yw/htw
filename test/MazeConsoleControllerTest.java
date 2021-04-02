import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import mazecontroller.HTWConsoleController;
import mazecontroller.MazeConsoleFeatures;

/**
 * A class to test the HTWConsoleController class.
 */
public class MazeConsoleControllerTest {
  private StringBuffer outSinglePlayer;
  private StringBuffer outTwoPlayer;
  private StringBuffer outError;

  @Before
  public void setUp() throws IOException {
    // set up two-player game

    Map<String, Integer> options0 = new HashMap<>();
    options0.put("playerOnBat", 0);
    options0.put("smellWumpus", 1);
    options0.put("playerFallen", 2);

    Map<String, Integer> options1 = new HashMap<>();
    options1.put("movedByBats", 8);
    options1.put("feelDraft", 7);
    options1.put("wumpusKilled", 0);

    this.outTwoPlayer = new StringBuffer();
    Reader in = new StringReader("M E M N M S M N M S S E 1");
    StringBuilder log = new StringBuilder();

    MazeMock mock = new MazeMock(log, 3);
    mock.setOptionsForPlayer(0, options0);
    mock.setOptionsForPlayer(1, options1);

    MazeConsoleFeatures controller = new HTWConsoleController(in, this.outTwoPlayer);
    controller.start(mock, 3, 3, 3, true);

    // set up single-player game

    options0 = new HashMap<>();
    options0.put("playerEaten", 1);

    this.outSinglePlayer = new StringBuffer();
    in = new StringReader("M N");
    log = new StringBuilder();

    mock = new MazeMock(log, 3);
    mock.setOptionsForPlayer(0, options0);

    controller = new HTWConsoleController(in, this.outSinglePlayer);
    controller.start(mock, 3, 3, 3, false);

    // set up error game

    options0 = new HashMap<>();

    this.outError = new StringBuffer();
    in = new StringReader("M B S E -1 S E B S W 1 S E 1");
    log = new StringBuilder();

    mock = new MazeMock(log, 3);
    mock.setOptionsForPlayer(0, options0);

    controller = new HTWConsoleController(in, this.outError);
    controller.start(mock, 3, 3, 1, false);
  }

  @Test
  public void testPlayerOneOnBat() {
    assertTrue(this.outTwoPlayer.toString().contains("Player 1 has avoided swooping bats!"));
  }

  @Test
  public void testPlayerTwoMovedByBats() {
    assertTrue(this.outTwoPlayer.toString().contains("Player 2 has been picked up and carried" +
            " away by bats..."));
  }

  @Test
  public void testPlayerOneSmellsWumpus() {
    assertTrue(this.outTwoPlayer.toString().contains("It is Player 1's turn\n" +
            "You can smell a wumpus nearby..."));
  }

  @Test
  public void testPlayerTwoFeelsDraft() {
    assertTrue(this.outTwoPlayer.toString().contains("It is Player 2's turn\n" +
            "You can feel a draft nearby..."));
  }

  @Test
  public void testPlayerOneFallsIntoPit() {
    assertTrue(this.outTwoPlayer.toString().contains("Player 1 has fallen into a pit and lost!"));
  }

  @Test
  public void testPlayerTwoKillsWumpus() {
    assertTrue(this.outTwoPlayer.toString().contains("Player 2 has killed the wumpus and won the" +
            " game!"));
  }

  @Test
  public void testSinglePlayerEatenByWumpus() {
    assertTrue(this.outSinglePlayer.toString().contains("You have been eaten by the wumpus and" +
            " lost!"));
  }

  @Test
  public void testPlayerRunsOutOfArrows() {
    assertTrue(this.outError.toString().contains("You have run out of arrows and lost!"));
  }

  @Test
  public void testInvalidDirectionChoice() {
    assertTrue(this.outError.toString().contains("Not a valid choice!"));
  }

  @Test
  public void testNegativeNumberOfCaves() {
    assertTrue(this.outError.toString().contains("number of caves cannot be negative"));
  }

  @Test
  public void testNonNumberCaveInput() {
    assertTrue(this.outError.toString().contains("Input must be a number!"));
  }

  @Test
  public void testUnavailableDirectionChoice() {
    assertTrue(this.outError.toString().contains("this is not a valid direction"));
  }
}
