package mazeview;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

/**
 * The portion of the view that shows the user relevant messages during the game.
 */
public class MessageView extends JPanel {
  private JLabel message;
  private JLabel arrows;
  private JLabel player;

  /**
   * Create a MessageView object.
   */
  public MessageView() {
    super();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JPanel messagePanel = new JPanel();
    messagePanel.setLayout(new FlowLayout());
    this.message = new JLabel();
    messagePanel.add(this.message);
    this.add(messagePanel);

    JPanel playerPanel = new JPanel();
    playerPanel.setLayout(new FlowLayout());
    this.player = new JLabel();
    playerPanel.add(this.player);
    this.add(playerPanel);

    JPanel arrowsPanel = new JPanel();
    arrowsPanel.setLayout(new FlowLayout());
    this.arrows = new JLabel();
    arrowsPanel.add(this.arrows);
    this.add(arrowsPanel);
  }

  /**
   * Update the arrow message.
   * @param arrows the number of arrows to show
   * @param player the player to show
   */
  public void updateArrowMessage(int arrows, int player) {
    this.arrows.setText("Player " + (player + 1) + " has " + arrows + " arrows remaining!");
    this.arrows.repaint();
  }

  /**
   * Update the move-based message.
   * @param message the message to show
   * @param player the player to show
   */
  public void updateMessage(String message, int player) {
    this.message.setText("Player " + (player + 1) + " " + message);
    this.message.repaint();
  }

  /**
   * Update the player message.
   * @param player the player to show
   */
  public void updatePlayerMessage(int player) {
    this.player.setText("It is Player " + (player + 1) + "'s turn!");
  }

  /**
   * Set the game-over message for the game.
   * @param winner the winner to show, or -1 if there is no winner
   */
  public void setGameOverMessage(int winner) {
    if (winner < 0) {
      this.player.setText("All players have lost the game!");
    } else {
      this.player.setText("Player " + (winner + 1) + " has won the game!");
    }
    this.arrows.setText("");
  }
}
