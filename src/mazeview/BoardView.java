package mazeview;

import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JButton;

import mazecontroller.MazeFeatures;

/**
 * The portion of the view that represents the maze-board.
 */
public class BoardView extends JPanel {
  private List<JButton> board;
  private List<BufferedImage> boardImages;
  private Map<String, String> pics;
  private Map<String, Integer> offsets;
  private int currentPlayer0Location;
  private int currentPlayer1Location;
  private Set<String> tiles;
  private Set<String> obstacles;

  /**
   * Create a BoardView object.
   * @param rows the number of rows on the board
   * @param cols the number of columns on the board
   * @throws IOException an IOException
   */
  public BoardView(int rows, int cols) throws IOException {
    super();
    this.setLayout(new GridLayout(rows, cols));
    this.initializePicMap();
    this.initializeOffsets();
    this.initializeTiles();
    this.initializeObstacles();
    this.initializeBoard(rows, cols);
    this.currentPlayer0Location = -1;
    this.currentPlayer1Location = -1;
  }

  /**
   * Set a tile at a location on the board.
   * @param location the location to set the tile
   * @param tile the tile to set
   * @throws IOException an IOException
   */
  public void setTileAtLocation(int location, String tile) throws IOException {
    this.updateImage(location, ImageIO.read(
            this.getClass().getResourceAsStream(this.pics.get(tile))
    ), true);
  }

  /**
   * Set a player at a location on the board.
   * @param location the location to set the player
   * @param playerKey the key of the player
   * @throws IOException an IOException
   */
  public void setPlayerLocation(int location, String playerKey) throws IOException {
    int currentPlayerLocation = (playerKey.equals("player0") ? this.currentPlayer0Location :
            this.currentPlayer1Location);

    if (playerKey.equals("player0")) {
      this.currentPlayer0Location = location;
    } else {
      this.currentPlayer1Location = location;
    }

    if (currentPlayerLocation >= 0) {
      this.updateImage(currentPlayerLocation,
              this.boardImages.get(currentPlayerLocation), false);
    }

    this.updateImage(location, this.getOverlayedImage(location, playerKey), false);
  }

  /**
   * Set an obstacle at a location on the board.
   * @param location the location to set the obstacle
   * @param obstacle the obstacle to set
   * @throws IOException an IOException
   */
  public void setObstacleAtLocation(int location, String obstacle) throws IOException {
    this.updateImage(location, this.getOverlayedImage(location, obstacle), true);
  }

  /**
   * Get valid tile keys.
   * @return a set of valid tile keys
   */
  public Set<String> getTileKeys() {
    return this.tiles;
  }

  /**
   * Get valid obstacles.
   * @return a set of valid obstacles
   */
  public Set<String> getObstacles() {
    return this.obstacles;
  }

  /**
   * Set the board features.
   * @param features the features to set
   */
  public void setBoardFeatures(MazeFeatures features) {
    this.setBoardClickListeners(features);
  }

  private void setBoardClickListeners(MazeFeatures features) {
    for (int i = 0; i < this.board.size(); i++) {
      int finalI = i;
      this.board.get(i).addActionListener(l -> features.moveToLocation(finalI));
    }
  }

  private void initializeTiles() {
    this.tiles = new HashSet<>();
    this.tiles.add("0");
    this.tiles.add("01");
    this.tiles.add("02");
    this.tiles.add("03");
    this.tiles.add("012");
    this.tiles.add("013");
    this.tiles.add("023");
    this.tiles.add("0123");
    this.tiles.add("1");
    this.tiles.add("12");
    this.tiles.add("13");
    this.tiles.add("123");
    this.tiles.add("2");
    this.tiles.add("23");
    this.tiles.add("3");
  }

  private void initializeObstacles() {
    this.obstacles = new HashSet<>();
    this.obstacles.add("bats");
    this.obstacles.add("pit");
    this.obstacles.add("wumpus");
    this.obstacles.add("breeze");
    this.obstacles.add("stench");
  }

  private void updateImage(int location, BufferedImage newImage, boolean save) throws IOException {
    JButton b = this.board.get(location);
    BackgroundPanel bp = (BackgroundPanel) b.getComponent(0);
    if (save) {
      this.boardImages.set(location, newImage);
    }
    if (location == this.currentPlayer0Location) {
      newImage = this.getOverlayedImage(location, "player0");
    }
    if (location == this.currentPlayer1Location) {
      newImage = this.getOverlayedImage(location, "player1");
    }
    bp.setImage(newImage);
    b.repaint();
  }

  private BufferedImage getOverlayedImage(int location, String imageKey) throws IOException {
    return this.overlay(this.boardImages.get(location), this.pics.get(imageKey),
            this.offsets.get(imageKey));
  }

  private void initializeBoard(int rows, int cols) throws IOException {
    this.board = new ArrayList<>();
    this.boardImages = new ArrayList<>();
    for (int i = 0; i < rows * cols; i++) {
      BufferedImage blackPic = ImageIO.read(
              this.getClass().getResourceAsStream(this.pics.get("black"))
      );
      this.boardImages.add(blackPic);
      BackgroundPanel black = new BackgroundPanel(blackPic);
      JButton location = new JButton();
      location.add(black);
      this.board.add(location);
      this.add(location);
    }
  }

  private void initializePicMap() {
    this.pics = new HashMap<>();
    this.pics.put("bats", "/images/bats.png");
    this.pics.put("black", "/images/black.png");
    this.pics.put("breeze", "/images/breeze.png");
    this.pics.put("2", "/images/E.png");
    this.pics.put("23", "/images/EW.png");
    this.pics.put("0", "/images/N.png");
    this.pics.put("02", "/images/NE.png");
    this.pics.put("023", "/images/NEW.png");
    this.pics.put("01", "/images/NS.png");
    this.pics.put("012", "/images/NSE.png");
    this.pics.put("0123", "/images/NSEW.png");
    this.pics.put("013", "/images/NSW.png");
    this.pics.put("03", "/images/NW.png");
    this.pics.put("pit", "/images/pit.png");
    this.pics.put("player0", "/images/player.png");
    this.pics.put("1", "/images/S.png");
    this.pics.put("12", "/images/SE.png");
    this.pics.put("123", "/images/SEW.png");
    this.pics.put("stench", "/images/stench.png");
    this.pics.put("13", "/images/SW.png");
    this.pics.put("3", "/images/W.png");
    this.pics.put("wumpus", "/images/wumpus.png");
    this.pics.put("player1", "/images/thief.png");
  }

  private void initializeOffsets() {
    this.offsets = new HashMap<>();
    this.offsets.put("player0", 0);
    this.offsets.put("player1", 0);
    this.offsets.put("bats", 5);
    this.offsets.put("breeze", 10);
    this.offsets.put("pit", 15);
    this.offsets.put("stench", 20);
    this.offsets.put("wumpus", 25);
  }

  private BufferedImage overlay(BufferedImage starting, String fpath, int offset)
          throws java.io.IOException {
    BufferedImage overlay = ImageIO.read(
            this.getClass().getResourceAsStream(fpath)
    );
    int w = Math.max(starting.getWidth(), overlay.getWidth());
    int h = Math.max(starting.getHeight(), overlay.getHeight());
    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combined.getGraphics();
    g.drawImage(starting, 0, 0, null);
    g.drawImage(overlay, offset, offset, null);
    return combined;
  }
}
