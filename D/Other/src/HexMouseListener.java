import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 *
 */
class HexMouseListener implements MouseListener {
  private int size;
  private int inset;

  public HexMouseListener(int size, int inset) {
    this.size = size;
    this.inset = inset; //inset subtracts border from coordinates
  }

  /**
   * Handles exiting the program once the mouse has been clicked inside the hexagon
   * @param mouseEvent the coordinates of the mouse click
   */
  @Override public void mouseClicked(MouseEvent mouseEvent) {
    int xx = mouseEvent.getX();
    int yy = mouseEvent.getY() - inset;

    if (xx + yy < size || yy - xx > size || xx - yy > size * 2 || xx + yy > size * 4) {
      // skip
    }
    else {
      System.exit(0);
    }
  }

  @Override public void mousePressed(MouseEvent mouseEvent) {
    //nothing
  }

  @Override public void mouseReleased(MouseEvent mouseEvent) {
    //nothing
  }

  @Override public void mouseEntered(MouseEvent mouseEvent) {
    //nothing
  }

  @Override public void mouseExited(MouseEvent mouseEvent) {
    //nothing
  }
}
