package mazeview;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mazecontroller.MazeFeatures;

/**
 * The view that represents the game portion of HTW.
 */
public class GameView extends JPanel {
  private BoardView board;
  private ControlsView controls;
  private MessageView message;

  /**
   * Create a GameView object.
   * @param rows the number of rows in the maze
   * @param cols the number of columns in the maze
   * @throws IOException an IOException
   */
  public GameView(int rows, int cols) throws IOException {
    super();
    this.setLayout(new BorderLayout());
    this.board = new BoardView(rows, cols);
    JScrollPane boardScroll = new JScrollPane(this.board);
    this.add(boardScroll, BorderLayout.CENTER);
    this.controls = new ControlsView();
    this.add(this.controls, BorderLayout.SOUTH);
    this.message = new MessageView();
    this.add(this.message, BorderLayout.NORTH);
  }

  /**
   * Set a picture at a location in the maze.
   * @param location the location of the picture to set
   * @param picKey the key of the picture to set
   * @throws IOException an IOException
   */
  public void setPictureAtLocation(int location, String picKey) throws IOException {
    if (picKey.equals("player0") || picKey.equals("player1")) {
      this.board.setPlayerLocation(location, picKey);
    } else if (this.board.getTileKeys().contains(picKey)) {
      this.board.setTileAtLocation(location, picKey);
    } else if (this.board.getObstacles().contains(picKey)) {
      this.board.setObstacleAtLocation(location, picKey);
    }
  }

  /**
   * Set the game features.
   * @param features the features to set
   */
  public void setGameFeatures(MazeFeatures features) {
    this.board.setBoardFeatures(features);
    this.controls.setControlsFeatures(features);
  }

  /**
   * Set the arrow count for a player in the view.
   * @param arrows the number of arrows for the player
   * @param player the player
   */
  public void setArrowCount(int arrows, int player) {
    this.message.updateArrowMessage(arrows, player);
  }

  /**
   * Set the message for the most recent move in the view.
   * @param message the message to set
   * @param player the player who made a move
   */
  public void setMoveMessage(String message, int player) {
    this.message.updateMessage(message, player);
  }

  /**
   * Set the game over message in the view.
   * @param winner the winner of the game, or -1 if there is no winner
   */
  public void setGameOverMessage(int winner) {
    this.message.setGameOverMessage(winner);
  }

  /**
   * Set the current player in the view.
   * @param player the current player
   */
  public void setCurrentPlayer(int player) {
    this.message.updatePlayerMessage(player);
  }
}
