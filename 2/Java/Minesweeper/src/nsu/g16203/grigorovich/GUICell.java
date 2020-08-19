package nsu.g16203.grigorovich;

import java.awt.event.MouseEvent;

public class GUICell extends Cell {

    public GUICell(int x, int y, int state) {
        super(x, y, state);
    }

    public int receiveCommand(MouseEvent e) {
        if (isHidden) {
            if ((e.getButton() == MouseEvent.BUTTON1 || e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) && !this.isMarked) {
                if (this.state == -1) {
                    this.state = -2;
                    return -2;
                }
                if (this.state == 0) {
                    this.isHidden = false;
                    return 0;
                }
            }
            if (e.getButton() == MouseEvent.BUTTON3 && this.isMarked) {
                this.isMarked = !this.isMarked;
                return -1;
            }
            if (e.getButton() == MouseEvent.BUTTON3 && !this.isMarked) {
                this.isMarked = !this.isMarked;
                return 1;
            }
        }
        return -3;
    }
}
