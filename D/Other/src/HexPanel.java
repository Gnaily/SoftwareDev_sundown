import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HexPanel extends JPanel {

  private int size;

  public HexPanel(int size) {
    this.setBackground(Color.WHITE);
    this.size = size;
    this.setPreferredSize(new Dimension(3 * size, 2 * size));
  }


  /**
   * Renders the image of the hexagon using lines. Sets the color of the hexagon to red.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.RED);
    g2d.drawLine(0, size, size, 2 * size);
    // minus 1 added to make the red line appear on the botttom
    g2d.drawLine(size, 2 * size - 1, 2 * size, 2 * size - 1);
    g2d.drawLine(2 * size, 2 * size, 3 * size, size);
    g2d.drawLine(3 * size, size, 2 * size, 0);
    g2d.drawLine(2 * size, 0, size, 0);
    g2d.drawLine(size, 0, 0, size);
  }
}
