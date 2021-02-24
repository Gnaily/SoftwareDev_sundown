import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.Line2D;

public class Hexagon extends JFrame {

  /**
   * Opens JFrame with Hexagon
   * @param args should contain size of hexagon
   */
  public static void main(String[] args)
  {
    int size = processCliArg(args);

    JFrame f = new JFrame("Hexagon");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new HexPanel(size);
    f.add(panel);
    panel.addMouseListener(new HexMouseListener(size, f.getInsets().top));

    f.pack();
    f.setVisible(true);
  }

  /**
   * Processes the command line arguments
   * @param args should contain the size of the hexagon
   * @return the int representing the size of the hexagon
   */
  private static int processCliArg(String[] args) {
    if (args.length != 1) {
      printUsage();
    }
    //size must be initialized for compiler; over-written on line 31 or program exits
    int size = 0;
    try {
      size = Integer.parseInt(args[0]);
      if (size <= 0) {
        throw new IllegalArgumentException("Size cannot be negative!");
      }
    }
    catch (Exception e) {
      printUsage();
    }
    return size;
  }

  /**
   * Helps handle exceptions thrown when command line arguments are invalid
   */
  private static void printUsage() {
    System.out.println("usage: ./xgui positive-integer");
    System.exit(1);
  }



}
