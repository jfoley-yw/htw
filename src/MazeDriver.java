import java.io.IOException;
import java.io.InputStreamReader;

import mazecontroller.HTWConsoleController;
import mazecontroller.HTWController;
import mazecontroller.MazeConsoleFeatures;
import mazecontroller.MazeFeatures;
import mazemodel.Maze;
import mazemodel.NonPerfectMaze;
import mazeview.HTWView;
import mazeview.MazeView;

/**
 * A driver for the HTW GUI and console games.
 */
public class MazeDriver {

  /**
   * Conditionally play either the HTW GUI game or the HTW console game.
   * @param args system arguments
   * @throws IOException an IOException
   */
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      System.out.println("No program inputs provided");
      return;
    } else if (args[0].equals("--gui")) {
      if (args.length > 1) {
        System.out.println("Invalid program inputs");
        return;
      }
      MazeView view = new HTWView();
      MazeFeatures controller = new HTWController(view);
    } else if (args[0].equals("--text")) {
      if (args.length != 10) {
        System.out.println("Invalid program inputs");
        return;
      }
      Maze model = null;
      int rows;
      int cols;
      int arrows;
      boolean twoPlayers;
      try {
        rows = Integer.valueOf(args[1]);
        cols = Integer.valueOf(args[2]);
        int walls = Integer.valueOf(args[3]);
        int seed = Integer.valueOf(args[4]);
        int pits = Integer.valueOf(args[5]);
        int bats = Integer.valueOf(args[6]);
        arrows = Integer.valueOf(args[7]);
        twoPlayers = Boolean.valueOf(args[8]);
        boolean wrapping = Boolean.valueOf(args[9]);
        model = new NonPerfectMaze(rows, cols, wrapping, seed, walls, pits, bats);
      } catch (Exception e) {
        System.out.println("Invalid program inputs");
        return;
      }
      MazeConsoleFeatures controller = new HTWConsoleController(new InputStreamReader(System.in),
              System.out);
      controller.start(model, rows, cols, arrows, twoPlayers);
    } else {
      System.out.println("Invalid program inputs");
    }
  }
}
