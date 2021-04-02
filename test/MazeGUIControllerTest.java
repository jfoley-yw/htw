import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import mazecontroller.HTWController;
import mazecontroller.MazeFeatures;
import mazeview.MazeView;

/**
 * A class to test the HTWController class.
 */
public class MazeGUIControllerTest {
  private MazeFeatures controller;
  private StringBuilder viewLog;

  @Before
  public void setUp() {
    this.viewLog = new StringBuilder();
    MazeView view = new MazeViewMock(this.viewLog);
    this.controller = new HTWController(view);
    this.controller.startGame(3, 3, true, 5, 5,
            10, 10, 3, true);
  }

  @Test
  public void testStartGame() {
    this.testStartGameActions(this.viewLog.toString());
    assertTrue(this.viewLog.toString().contains("resetFocus called"));
  }

  @Test
  public void testRestartGame() {
    this.controller.restartGame();
    int logSize = this.viewLog.length();
    this.testStartGameActions(this.viewLog.substring(logSize / 2));
    assertTrue(this.viewLog.substring(logSize / 2).toString().contains("resetFocus called"));
  }

  @Test
  public void testMoveInDirection() {
    this.controller.moveInDirection(0);
    this.testMoveNorthToLocationSix();
    assertTrue(this.viewLog.toString().contains("setCurrentPlayer called with player: 1"));
  }

  @Test
  public void testMoveToLocation() {
    this.controller.moveToLocation(6);
    this.testMoveNorthToLocationSix();
    assertTrue(this.viewLog.toString().contains("setCurrentPlayer called with player: 1"));
  }

  @Test
  public void testShootArrow() {
    this.controller.shootArrow(0, 1);
    String log = this.viewLog.toString();
    assertTrue(log.contains("setMoveMessageCalled with message: has missed the wumpus and" +
            " lost an arrow! and player: 0"));
    assertTrue(log.contains("setCurrentPlayer called with player: 1"));
  }

  @Test
  public void testEndGame() {
    this.controller.endGame();
    assertTrue(this.viewLog.toString().contains("displayMenu called"));
  }

  private void testMoveNorthToLocationSix() {
    String log = this.viewLog.toString();
    assertTrue(log.contains("setPictureAtLocation called with location: 6 and key: 0123"));
    assertTrue(log.contains("setPictureAtLocation called with location: 6 and key: bats"));
    assertTrue(log.contains("setPictureAtLocation called with location: 6 and key: breeze"));
    assertTrue(log.contains("setPictureAtLocation called with location: 6 and key: pit"));
    assertTrue(log.contains("setPictureAtLocation called with location: 6 and key: player0"));
    assertTrue(log.contains("setMoveMessageCalled with message: has fallen into a pit and lost!" +
            " and player: 0"));
  }

  private void testStartGameActions(String log) {
    assertTrue(log.contains("displayMaze called with numRows: 3 and numCols: 3"));
    assertTrue(log.contains("setGameFeatures called"));
    assertTrue(log.contains("setPictureAtLocation called with location: 0 and key: player0"));
    assertTrue(log.contains("setPictureAtLocation called with location: 8 and key: player1"));
    assertTrue(log.contains("setArrowCount called with arrows: 3 and player: 0"));
    assertTrue(log.contains("setMoveMessageCalled with message: will start the game!" +
            " and player: 0"));
    assertTrue(log.contains("setCurrentPlayer called with player: 0"));
  }
}
