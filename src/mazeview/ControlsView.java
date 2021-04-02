package mazeview;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import mazecontroller.MazeFeatures;

/**
 * The portion of the view that represents controls during the game.
 */
public class ControlsView extends JPanel {
  private JTextField shootDirection;
  private JTextField shootDistance;
  private JButton shoot;
  private JButton restart;
  private JButton end;
  private Map<String, Integer> directions;

  /**
   * Create a new ControlsView object.
   */
  public ControlsView() {
    super();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JPanel shootInstructions = new JPanel();
    shootInstructions.setLayout(new FlowLayout());
    shootInstructions.add(new JLabel("Valid directions to shoot are N, S, E, and W. Caves" +
            " must be a non-negative integer."));
    this.add(shootInstructions);

    JPanel shootPanel = new JPanel();
    shootPanel.setLayout(new FlowLayout());

    JPanel shootDirectionPanel = new JPanel();
    shootDirectionPanel.setLayout(new FlowLayout());
    shootDirectionPanel.add(new JLabel("Direction:"));
    this.shootDirection = new JTextField(5);
    shootDirectionPanel.add(this.shootDirection);
    shootPanel.add(shootDirectionPanel);

    JPanel shootDistancePanel = new JPanel();
    shootDistancePanel.setLayout(new FlowLayout());
    shootDistancePanel.add(new JLabel("Caves:"));
    this.shootDistance = new JTextField(5);
    shootDistancePanel.add(this.shootDistance);
    shootPanel.add(shootDistancePanel);

    this.shoot = new JButton("Shoot!");
    shootPanel.add(this.shoot);

    this.add(shootPanel);

    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new FlowLayout());

    this.restart = new JButton("Restart Game");
    optionsPanel.add(this.restart);

    this.end = new JButton("End Game");
    optionsPanel.add(this.end);

    this.add(optionsPanel);

    this.initializeDirections();
  }

  /**
   * Set the controls features.
   * @param features the features to set
   */
  public void setControlsFeatures(MazeFeatures features) {
    this.setShootListener(features);
    this.setRestartListener(features);
    this.setEndListener(features);
  }

  private void setShootListener(MazeFeatures features) {
    this.shoot.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          features.shootArrow(
                  ControlsView.this.directions.get(ControlsView.this.shootDirection.getText()),
                  Integer.valueOf(ControlsView.this.shootDistance.getText())
          );
        } catch (Exception error) {
          return;
        }
      }
    });
  }

  private void setRestartListener(MazeFeatures features) {
    this.restart.addActionListener(l -> features.restartGame());
  }

  private void setEndListener(MazeFeatures features) {
    this.end.addActionListener(l -> features.endGame());
  }

  private void initializeDirections() {
    this.directions = new HashMap<>();
    this.directions.put("N", 0);
    this.directions.put("S", 1);
    this.directions.put("E", 2);
    this.directions.put("W", 3);
  }
}
