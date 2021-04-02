package mazeview;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import mazecontroller.MazeFeatures;

/**
 * The main view class for the HTW GUI game.
 */
public class HTWView extends JFrame implements MazeView {
  private GameView game;
  private MenuView menu;
  private KeyListener keyListener;

  /**
   * Construct a HTWView object.
   */
  public HTWView() {
    super();
    this.setTitle("Hunt the Wumpus");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    this.keyListener = null;

    this.pack();
    this.setVisible(true);
  }

  @Override
  public void displayMaze(int rows, int cols) {
    try {
      this.getContentPane().removeAll();
      this.game = new GameView(rows, cols);
      this.add(this.game, BorderLayout.CENTER);
      this.validate();
    } catch (IOException e) {
      return;
    }
  }

  @Override
  public void displayMenu() {
    this.getContentPane().removeAll();
    this.menu = new MenuView();
    this.add(this.menu, BorderLayout.CENTER);
    this.validate();
  }

  @Override
  public void setPictureAtLocation(int location, String picKey) {
    try {
      this.game.setPictureAtLocation(location, picKey);
    } catch (IOException e) {
      return;
    }
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
  public void setMenuFeatures(MazeFeatures features) {
    this.menu.setMenuFeatures(features);
  }

  @Override
  public void setGameFeatures(MazeFeatures features) {
    this.game.setGameFeatures(features);
    this.setKeyboardListeners(features);
  }

  @Override
  public void setMoveMessage(String message, int player) {
    this.game.setMoveMessage(message, player);
  }

  @Override
  public void setGameOverMessage(int winner) {
    this.game.setGameOverMessage(winner);
  }

  @Override
  public void setArrowCount(int arrows, int player) {
    this.game.setArrowCount(arrows, player);
  }

  @Override
  public void setCurrentPlayer(int player) {
    this.game.setCurrentPlayer(player);
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  private void setKeyboardListeners(MazeFeatures features) {
    if (this.keyListener != null) {
      this.removeKeyListener(this.keyListener);
    }
    this.keyListener = new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // noop
      }

      @Override
      public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch ( keyCode ) {
          case KeyEvent.VK_UP:
            features.moveInDirection(0);
            break;
          case KeyEvent.VK_DOWN:
            features.moveInDirection(1);
            break;
          case KeyEvent.VK_LEFT:
            features.moveInDirection(3);
            break;
          case KeyEvent.VK_RIGHT :
            features.moveInDirection(2);
            break;
          default:
            return;
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // noop
      }
    };
    this.addKeyListener(this.keyListener);
  }
}
