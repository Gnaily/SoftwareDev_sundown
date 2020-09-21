import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.Line2D;

public class Hexagon extends JFrame {

  public static void main(String[] args)
  {
    if (args.length != 1) {
      printUsage();
      return;
    }
    int size;
    try {
      size = Integer.parseInt(args[0]);
      if (size <= 0) {
        throw new IllegalArgumentException("Size cannot be negative!");
      }
    }
    catch (Exception e) {
      printUsage();
      return;
    }
    JFrame f = new JFrame("Hexagon");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new HexPanel(size);
    f.add(panel);
    panel.addMouseListener(new HexMouseListener(size, f.getInsets().top));

    f.pack();
    f.setVisible(true);
  }

  private static void printUsage() {
    System.out.println("usage: ./xgui positive-integer");
  }



}
