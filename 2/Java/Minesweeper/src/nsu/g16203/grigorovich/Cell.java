package nsu.g16203.grigorovich;

public class Cell {
    public int state; // -1 : mine; -2 : exploded; 0 - default;
    public boolean isHidden;
    public boolean isMarked;

    public Cell(int x, int y, int state) {
        this.isHidden = true;
        this.isMarked = false;
        this.state = state;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void show() {
        this.isHidden = false;
    }

    public int receiveCommand(String cmd) {
        cmd = cmd.toLowerCase();
        if (isHidden) {
            if (cmd.equals("open") && !this.isMarked) {
                if (this.state == -1) {
                    this.state = -2;
                    return -2;
                }
                if (this.state == 0) {
                    this.isHidden = false;
                    return 0;
                }
            }
            if (cmd.equals("mark"))
                this.isMarked = !this.isMarked;
        }
        return -1;
    }


}
