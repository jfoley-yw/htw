package mazecontroller;

import java.util.ArrayList;
import java.util.List;

import mazemodel.Maze;
import mazemodel.NonPerfectMaze;
import mazeview.MazeView;

/**
 * A controller for a HTW GUI game.
 */
public class HTWController implements MazeFeatures {
  private Maze model;
  private MazeView view;
  private int rows;
  private int cols;
  private boolean wrapping;
  private int seed;
  private int walls;
  private int pits;
  private int bats;
  private int arrows;
  private int currentPlayer;
  private boolean twoPlayers;

  /**
   * Create a HTWController object.
   * @param view the view that the controller controls
   */
  public HTWController(MazeView view) {
    this.view = view;
    this.view.displayMenu();
    this.view.setMenuFeatures(this);
  }

  @Override
  public void startGame(int numRows, int numCols, boolean wrapping, int seed, int numWalls,
                 int percentPits, int percentBats, int numArrows, boolean twoPlayers) {
    this.rows = numRows;
    this.cols = numCols;
    this.wrapping = wrapping;
    this.seed = seed;
    this.walls = numWalls;
    this.pits = percentPits;
    this.bats = percentBats;
    this.arrows = numArrows;
    this.twoPlayers = twoPlayers;

    this.model = new NonPerfectMaze(numRows, numCols, wrapping, seed, numWalls, percentPits,
            percentBats);
    this.view.displayMaze(numRows, numCols);
    this.view.setGameFeatures(this);

    this.currentPlayer = 0;
    this.model.addPlayer(0, 0, numArrows);
    this.updateViewAfterMove();

    if (this.twoPlayers) {
      this.currentPlayer = 1;
      this.model.addPlayer(numRows - 1, numCols - 1, numArrows);
      this.updateViewAfterMove();
    }

    this.handleViewAfterPlayerPlacement();
    this.view.resetFocus();
  }

  @Override
  public void restartGame() {
    this.startGame(this.rows, this.cols, this.wrapping, this.seed, this.walls, this.pits,
            this.bats, this.arrows, this.twoPlayers);
  }

  @Override
  public void moveInDirection(int direction) {
    if (this.gameOver()) {
      return;
    }
    try {
      this.model.movePlayerInDirection(direction, this.currentPlayer);
      this.showHallways();
      this.updateViewAfterMove();
      if (this.twoPlayers) {
        this.switchPlayers();
      }
      this.conditionallySetGameOverMessage();
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Override
  public void moveToLocation(int location) {
    if (this.gameOver()) {
      return;
    }
    try {
      this.model.movePlayerToLocation(location, this.currentPlayer);
      this.showHallways();
      this.updateViewAfterMove();
      if (this.twoPlayers) {
        this.switchPlayers();
      }
      this.conditionallySetGameOverMessage();
      this.view.resetFocus();
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Override
  public void shootArrow(int direction, int caves) {
    if (this.gameOver()) {
      return;
    }
    try {
      this.model.shootArrow(direction, caves, this.currentPlayer);
      this.view.setMoveMessage(this.getShootArrowMessage(), this.currentPlayer);
      if (this.twoPlayers) {
        this.switchPlayers();
      } else {
        this.view.setArrowCount(this.model.getPlayerArrows(0), 0);
      }
      this.conditionallySetGameOverMessage();
      this.view.resetFocus();
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Override
  public void endGame() {
    this.view.displayMenu();
    this.view.setMenuFeatures(this);
  }

  private void updateViewAfterMove() {
    int location = this.model.getPlayerLocation(this.currentPlayer);
    this.view.setPictureAtLocation(location,
            this.view.getTileKey(this.model.getValidPlayerMoves(this.currentPlayer)));
    this.setConditions(location);
    this.view.setPictureAtLocation(location, "player" + this.currentPlayer);
    this.view.setMoveMessage(this.getMovePlayerMessage(), this.currentPlayer);
  }

  private void setConditions(int location) {
    if (this.model.playerOnBat(this.currentPlayer)) {
      this.view.setPictureAtLocation(location, "bats");
    }
    if (this.model.feelDraft(this.currentPlayer)) {
      this.view.setPictureAtLocation(location, "breeze");
    }
    if (this.model.smellWumpus(this.currentPlayer)) {
      this.view.setPictureAtLocation(location, "stench");
    }
    if (this.model.playerEaten(this.currentPlayer)) {
      this.view.setPictureAtLocation(location, "wumpus");
    }
    if (this.model.playerFallen(this.currentPlayer)) {
      this.view.setPictureAtLocation(location, "pit");
    }
  }

  private String getMovePlayerMessage() {
    if (this.model.playerEaten(this.currentPlayer)) {
      return "has been eaten by the wumpus and lost!";
    } else if (this.model.playerFallen(this.currentPlayer)) {
      return "has fallen into a pit and lost!";
    } else if (this.model.movedByBats(this.currentPlayer)) {
      return "has been moved by bats!";
    } else if (this.model.playerOnBat(this.currentPlayer)) {
      return "has successfully avoided bats!";
    } else {
      return "has moved successfully!";
    }
  }

  private String getShootArrowMessage() {
    if (this.model.wumpusKilled(this.currentPlayer)) {
      return "has killed the wumpus!";
    } else if (this.model.gameLost(this.currentPlayer)) {
      return "has ran out of arrows and lost!";
    } else {
      return "has missed the wumpus and lost an arrow!";
    }
  }

  private void showHallways() {
    if (!this.model.movedByBats(this.currentPlayer)) {
      List<List<Integer>> hallways = this.model.getHallwaysTraveled();
      for (List<Integer> hallway : hallways) {
        List<Integer> directions = new ArrayList<>();
        directions.add(hallway.get(1));
        directions.add(hallway.get(2));
        this.view.setPictureAtLocation(hallway.get(0), this.view.getTileKey(directions));
      }
    }
  }

  private boolean gameOver() {
    if (this.model.wumpusKilled(0) ||
            (this.twoPlayers && this.model.wumpusKilled(1))) {
      return true;
    }
    return this.model.gameLost(0) && (!this.twoPlayers || this.model.gameLost(1));
  }

  private void switchPlayers() {
    this.currentPlayer = Math.abs(this.currentPlayer - 1);
    if (this.model.gameLost(this.currentPlayer)) {
      this.currentPlayer = Math.abs(this.currentPlayer - 1);
    }
    this.view.setCurrentPlayer(this.currentPlayer);
    this.view.setArrowCount(this.model.getPlayerArrows(this.currentPlayer), this.currentPlayer);
  }

  private void conditionallySetGameOverMessage() {
    if (!this.gameOver()) {
      return;
    }
    int winner = -1;
    if (this.model.wumpusKilled(0)) {
      winner = 0;
    } else if (this.twoPlayers && this.model.wumpusKilled(1)) {
      winner = 1;
    }
    this.view.setGameOverMessage(winner);
  }

  private void handleViewAfterPlayerPlacement() {
    this.conditionallySetGameOverMessage();
    if (this.gameOver()) {
      return;
    }
    if (this.twoPlayers && this.model.gameLost(1)) {
      this.currentPlayer = 1;
      this.view.setMoveMessage(this.getMovePlayerMessage(), 1);
      this.currentPlayer = 0;
      this.view.setArrowCount(this.model.getPlayerArrows(this.currentPlayer), 0);
      this.view.setCurrentPlayer(0);
    } else if (this.twoPlayers && this.model.gameLost(0)) {
      this.currentPlayer = 0;
      this.view.setMoveMessage(this.getMovePlayerMessage(), 0);
      this.currentPlayer = 1;
      this.view.setArrowCount(this.model.getPlayerArrows(this.currentPlayer), 1);
      this.view.setCurrentPlayer(1);
    } else {
      this.currentPlayer = 0;
      this.view.setArrowCount(this.model.getPlayerArrows(this.currentPlayer), 0);
      this.view.setMoveMessage("will start the game!", 0);
      this.view.setCurrentPlayer(0);
    }
  }
}
