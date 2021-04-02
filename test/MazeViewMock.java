import java.util.Collections;
import java.util.List;

import mazecontroller.MazeFeatures;
import mazeview.MazeView;

/**
 * A class that mocks the MazeView interface. It can be used to test a maze view controller.
 */
class MazeViewMock implements MazeView {
  private StringBuilder log;

  /**
   * Create a MazeViewMock object.
   * @param log the log to use
   */
  public MazeViewMock(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void setPictureAtLocation(int location, String picKey) {
    this.log.append("setPictureAtLocation called with location: " + location + " and key: " +
            picKey + "\n");
  }

  @Override
  public String getTileKey(List<Integer> directions) {
    Collections.sort(directions);
    StringBuilder key = new StringBuilder();
    for (int direction : directions) {
      key.append(direction);
    }
    return key.toString();
  }

  @Override
  public void displayMaze(int numRows, int numCols) {
    this.log.append("displayMaze called with numRows: " + numRows + " and numCols: " + numCols +
            "\n");
  }

  @Override
  public void displayMenu() {
    this.log.append("displayMenu called\n");
  }

  @Override
  public void setMoveMessage(String message, int player) {
    this.log.append("setMoveMessageCalled with message: " + message + " and player: " + player
            + "\n");
  }

  @Override
  public void setGameOverMessage(int winner) {
    this.log.append("setGameOverMessage called with winner: " + winner + "\n");
  }

  @Override
  public void setArrowCount(int arrows, int player) {
    this.log.append("setArrowCount called with arrows: " + arrows + " and player: " + player +
            "\n");
  }

  @Override
  public void setCurrentPlayer(int player) {
    this.log.append("setCurrentPlayer called with player: " + player + "\n");
  }

  @Override
  public void resetFocus() {
    this.log.append("resetFocus called\n");
  }

  @Override
  public void setMenuFeatures(MazeFeatures features) {
    this.log.append("setMenuFeatures called\n");
  }

  @Override
  public void setGameFeatures(MazeFeatures features) {
    this.log.append("setGameFeatures called\n");
  }
}
