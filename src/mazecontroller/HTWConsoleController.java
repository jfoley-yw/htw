package mazecontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import mazemodel.Maze;

/**
 * An implementation of MazeController based on the game Hunt the Wumpus.
 */
public class HTWConsoleController implements MazeConsoleFeatures {
  private Readable in;
  private Appendable out;

  public HTWConsoleController(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public void start(Maze maze, int rows, int cols, int playerArrows, boolean twoPlayers)
          throws IOException {
    this.validateMazeExists(maze);
    Scanner scanner = new Scanner(this.in);

    maze.addPlayer(0, 0, playerArrows);
    this.initialBatsMessage(maze, 0, twoPlayers);
    int currentPlayer = 0;

    if (twoPlayers) {
      maze.addPlayer(rows - 1, cols - 1, playerArrows);
      this.initialBatsMessage(maze, 1, true);
    }

    while (!this.gameOver(maze, twoPlayers)) {
      if (twoPlayers) {
        this.out.append("It is Player " + (currentPlayer + 1) + "'s turn\n");
      }
      if (maze.smellWumpus(currentPlayer)) {
        this.out.append("You can smell a wumpus nearby...\n");
      }
      if (maze.feelDraft(currentPlayer)) {
        this.out.append("You can feel a draft nearby...\n");
      }

      this.out.append("You are at location " + maze.getPlayerLocation(currentPlayer) + "\n");
      this.out.append("You have " + maze.getPlayerArrows(currentPlayer) + " arrows left\n");
      this.out.append(this.getDirectionMessage(maze, currentPlayer));
      this.out.append("\n");

      this.out.append("Shoot or Move (S-M)? Or q to quit: ");

      boolean move;
      switch (scanner.next()) {
        case "S":
          move = false;
          break;
        case "M":
          move = true;
          break;
        case "q":
          this.out.append("Goodbye!");
          return;
        default:
          this.out.append("Not a valid choice!\n");
          this.out.append("\n");
          continue;
      }

      this.out.append("Which direction? ");

      int direction;
      switch (scanner.next()) {
        case "N":
          direction = 0;
          break;
        case "S":
          direction = 1;
          break;
        case "E":
          direction = 2;
          break;
        case "W":
          direction = 3;
          break;
        default:
          this.out.append("Not a valid choice!\n");
          this.out.append("\n");
          continue;
      }

      try {
        if (!move) {
          this.out.append("How many caves do you want to shoot through? ");
          int caves = scanner.nextInt();
          maze.shootArrow(direction, caves, currentPlayer);
        } else {
          maze.movePlayerInDirection(direction, currentPlayer);
        }
      } catch (IllegalArgumentException e) {
        this.out.append(e.getMessage());
        this.out.append("\n\n");
        continue;
      } catch (InputMismatchException e) {
        this.out.append("Input must be a number!\n\n");
        continue;
      }

      if (move && maze.movedByBats(currentPlayer)) {
        this.out.append("You have been picked up and carried away by bats...\n");
      } else if (move && maze.playerOnBat(currentPlayer)) {
        this.out.append("You have avoided swooping bats!\n");
      } else if (!move && !maze.wumpusKilled(currentPlayer)) {
        this.out.append("You missed the wumpus and lost an arrow!\n");
      }

      this.conditionalLoseMessage(maze, currentPlayer, twoPlayers);

      if (twoPlayers) {
        currentPlayer = this.conditionalSwitchPlayer(maze, currentPlayer);
      }

      this.out.append("\n");
    }

    this.finalMessage(maze, twoPlayers);
  }

  private void finalMessage(Maze maze, boolean twoPlayers) throws IOException {
    if (!twoPlayers && maze.wumpusKilled(0)) {
      this.out.append("You have killed the wumpus and won the game!");
    } else if (twoPlayers && maze.wumpusKilled(0)) {
      this.out.append("Player 1 has killed the wumpus and won the game!");
    } else if (twoPlayers && maze.wumpusKilled(1)) {
      this.out.append("Player 2 has killed the wumpus and won the game!");
    } else if (twoPlayers) {
      this.out.append("All players have lost the game!");
    }
  }

  private void conditionalLoseMessage(Maze maze, int player, boolean twoPlayers)
          throws IOException {
    if (!maze.gameLost(player)) {
      return;
    }
    String playerString = this.getPlayerString(player, twoPlayers);
    if (maze.playerEaten(player)) {
      this.out.append(playerString + " been eaten by the wumpus and lost!\n");
    } else if (maze.playerFallen(player)) {
      this.out.append(playerString + " fallen into a pit and lost!\n");
    } else {
      this.out.append(playerString + " run out of arrows and lost!\n");
    }
  }

  private String getPlayerString(int player, boolean twoPlayers) {
    String playerString = "You have";
    if (twoPlayers) {
      playerString = "Player " + (player + 1) + " has";
    }
    return playerString;
  }

  private int conditionalSwitchPlayer(Maze maze, int player) {
    int next = Math.abs(player - 1);
    if (maze.gameLost(next)) {
      return player;
    }
    return next;
  }

  private String getDirectionMessage(Maze maze, int player) {
    Map<Integer, String> directionMap = new HashMap<>();
    directionMap.put(0, "N");
    directionMap.put(1, "S");
    directionMap.put(2, "E");
    directionMap.put(3, "W");

    List<Integer> validMoves = maze.getValidPlayerMoves(player);
    String result = "Tunnels lead to the";

    for (int i = 0; i < validMoves.size() - 1; i++) {
      result += (" " + directionMap.get(validMoves.get(i)) + ",");
    }

    result += (" " + directionMap.get(validMoves.get(validMoves.size() - 1)) + "\n");
    return result;
  }

  private void initialBatsMessage(Maze maze, int player, boolean twoPlayers) throws IOException {
    String playerString = this.getPlayerString(player, twoPlayers);
    if (maze.movedByBats(player)) {
      this.out.append(playerString +
              " been picked up and carried away by bats...\n");
    } else if (maze.playerOnBat(player)) {
      this.out.append(playerString + " avoided swooping bats!\n");
    }
  }

  private boolean gameOver(Maze maze, boolean twoPlayers) {
    if (maze.wumpusKilled(0) ||
            (twoPlayers && maze.wumpusKilled(1))) {
      return true;
    }
    return maze.gameLost(0) && (!twoPlayers || maze.gameLost(1));
  }

  private void validateMazeExists(Maze maze) {
    if (maze == null) {
      throw new IllegalArgumentException("maze cannot be null");
    }
  }
}
