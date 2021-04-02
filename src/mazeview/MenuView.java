package mazeview;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

import mazecontroller.MazeFeatures;

/**
 * The view that represents the menu portion of HTW.
 */
public class MenuView extends JPanel {
  private JTextField rows;
  private JTextField cols;
  private JTextField walls;
  private JTextField seed;
  private JTextField pits;
  private JTextField bats;
  private JTextField arrows;
  private JCheckBox twoPlayers;
  private JCheckBox wrapping;
  private JButton start;

  /**
   * Create a MenuView object.
   */
  public MenuView() {
    super();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JPanel label = new JPanel();
    label.setLayout(new FlowLayout());
    label.add(new JLabel("HTW Menu:"));
    this.add(label);

    this.rows = new JTextField(15);
    this.cols = new JTextField(15);
    this.walls = new JTextField(15);
    this.seed = new JTextField(15);
    this.pits = new JTextField(15);
    this.bats = new JTextField(15);
    this.arrows = new JTextField(15);

    this.rows.setText("10");
    this.cols.setText("10");
    this.walls.setText("50");
    this.seed.setText("35");
    this.pits.setText("20");
    this.bats.setText("20");
    this.arrows.setText("5");

    this.createInputField(this.rows, "Number of rows:");
    this.createInputField(this.cols, "Number of columns:");
    this.createInputField(this.walls, "Number of walls:");
    this.createInputField(this.seed, "Random seed:");
    this.createInputField(this.pits, "Percentage of caves with pits:");
    this.createInputField(this.bats,"Percentage of caves with bats:");
    this.createInputField(this.arrows, "Starting number of arrows:");

    JPanel twoPlayersPanel = new JPanel();
    twoPlayersPanel.setLayout(new FlowLayout());
    twoPlayersPanel.add(new JLabel("Two players:"));
    this.twoPlayers = new JCheckBox();
    twoPlayersPanel.add(this.twoPlayers);
    this.add(twoPlayersPanel);

    JPanel wrappingPanel = new JPanel();
    wrappingPanel.setLayout(new FlowLayout());
    wrappingPanel.add(new JLabel("Wrapping:"));
    this.wrapping = new JCheckBox();
    wrappingPanel.add(this.wrapping);
    this.add(wrappingPanel);

    JPanel startPanel = new JPanel();
    startPanel.setLayout(new FlowLayout());
    this.start = new JButton("Start Game");
    startPanel.add(this.start);
    this.add(startPanel);
  }

  /**
   * Set the menu features.
   * @param features the features to set
   */
  public void setMenuFeatures(MazeFeatures features) {
    this.setStartListener(features);
  }

  private void setStartListener(MazeFeatures features) {
    this.start.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          features.startGame(
                  Integer.valueOf(MenuView.this.rows.getText()),
                  Integer.valueOf(MenuView.this.cols.getText()),
                  MenuView.this.wrapping.isSelected(),
                  Integer.valueOf(MenuView.this.seed.getText()),
                  Integer.valueOf(MenuView.this.walls.getText()),
                  Integer.valueOf(MenuView.this.pits.getText()),
                  Integer.valueOf(MenuView.this.bats.getText()),
                  Integer.valueOf(MenuView.this.arrows.getText()),
                  MenuView.this.twoPlayers.isSelected()
          );
        } catch (Exception error) {
          return;
        }
      }
    });
  }

  private void createInputField(JTextField text, String label) {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    panel.add(new JLabel(label));
    panel.add(text);
    this.add(panel);
  }
}
