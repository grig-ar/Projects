package nsu.g16203.grigorovich;

import java.util.Random;

public class GameField {
    public Cell[][] field;
    public int[][] minesEnv;
//    int[][] minesPos;
    public int xCoord;
    public int yCoord;
    public int mines;

    public GameField(int x, int y, int minesAmount) {
        xCoord = x;
        yCoord = y;
        mines = minesAmount;
        boolean isMine = false;
        Random rnd = new Random(System.currentTimeMillis());
        field = new Cell[xCoord][yCoord];
        minesEnv = new int[xCoord][yCoord];
        //minesPos = new int[2][minesAmount];

        for (int i = 0; i < xCoord; ++i)
            for (int j = 0; j < yCoord; ++j)
                field[i][j] = new Cell(i ,j, 0);

        for (int i = 0; i < minesAmount; ++i) {
            int xTemp = rnd.nextInt(xCoord);
            int yTemp = rnd.nextInt(yCoord);
            while(field[xTemp][yTemp].getState() != 0) {
                xTemp = rnd.nextInt(xCoord);
                yTemp = rnd.nextInt(yCoord);
            }
            field[xTemp][yTemp].setState(-1);
            //minesPos[0][i] = xTemp;
            //minesPos[1][i] = yTemp;
        }
        for (int i = 0; i < xCoord; ++i) {
            for (int j = 0; j < yCoord; ++j) {
                if (field[i][j].getState() == -1) {
                    for (int k = -1; k < 2; ++k) {
                        for (int t = -1; t < 2; ++t) {
                            if((k + i >=0) && (k + i < xCoord) && (t + j >= 0) && (t + j < yCoord))
                                minesEnv[k+i][t+j]++;
                        }
                    }
                }
            }
        }
    }
    public void printField() {
        Cell temp;
        System.out.print(yCoord > 10 ? "   " : "  ");
        for (int i = 0; i < yCoord; ++i) {
            System.out.print(i);
            System.out.print(yCoord > 10 ? i < 10 ? "  " : " " : " ");
        }
        System.out.print("\n");
        for (int i = 0; i < xCoord; ++i) {
            System.out.print(i);
            System.out.print(xCoord > 10 ? i < 10 ? "  " : " " : " ");
            for (int j = 0; j < yCoord; ++j) {
                temp = getCell(i,j);
                if (temp.isMarked()) {
                    System.out.print("P");
                    System.out.print((xCoord < 10 && yCoord < 10) ? " " : "  ");
                    continue;
                }
                if (temp.isHidden())
                    System.out.print(".");
                else {
                    switch (temp.getState()) {
                        case 0:
                            if (getMinesEnv(i, j) == 0)
                                System.out.print("o");
                            else
                                System.out.print(getMinesEnv(i, j));
                            break;
                        case -1:
                            System.out.print("Q");
                            break;
                        case -2:
                            System.out.print("*");
                            break;
                        default:
                            break;
                    }
                }
                System.out.print((xCoord < 10 && yCoord < 10) ? " " : "  ");
            }
            System.out.print("\n");
        }
    }

    public int receiveCommand(String cmd, int x, int y) {
        int result = this.field[x][y].receiveCommand(cmd);
        switch (result) {
            case -2:
                showAll();
                return -1;
            case 0:
                if (getMinesEnv(x, y) == 0) {
                    for (int k = -1; k < 2; ++k) {
                        for (int t = -1; t < 2; ++t) {
                            if ((k + x >= 0) && (k + x < xCoord) && (t + y >= 0) && (t + y < yCoord))
                                receiveCommand(cmd, k + x, t + y);
                        }
                    }
                }
            case -1: default:
                return 0;
        }
    }

    public void show(int x, int y){
        field[x][y].show();
    }

    public void showAll() {
        for(Cell[] row : field){
            for(Cell cell : row){
                cell.show();
            }
        }
    }

    public boolean isAllOpened() {
        for(Cell[] row : field){
            for(Cell cell : row){
                if (cell.getState() == 0 && cell.isHidden())
                    return false;
            }
        }
        return true;
    }

    public Cell getCell(int x, int y) {
        return field[x][y];
    }

    public void addMine(int x, int y) {
        Random rnd = new Random(System.currentTimeMillis());
        int xTemp = rnd.nextInt(xCoord);
        int yTemp = rnd.nextInt(yCoord);
        while(field[xTemp][yTemp].getState() != 0 || (xTemp == x && yTemp == y)) {
            xTemp = rnd.nextInt(xCoord);
            yTemp = rnd.nextInt(yCoord);
        }
        field[xTemp][yTemp].setState(-1);
        for (int k = -1; k < 2; ++k) {
            for (int t = -1; t < 2; ++t) {
                if((k + xTemp >=0) && (k + xTemp < xCoord) && (t + yTemp >= 0) && (t + yTemp < yCoord))
                    minesEnv[k+xTemp][t+yTemp]++;
            }
        }
    }

    public void removeMine(int x, int y) {
        field[x][y].setState(0);
        for (int k = -1; k < 2; ++k) {
            for (int t = -1; t < 2; ++t) {
                if((k + x >=0) && (k + x < xCoord) && (t + y >= 0) && (t + y < yCoord))
                    minesEnv[k+x][t+y]--;
            }
        }
    }

    public int getMinesEnv(int x, int y) {
        return minesEnv[x][y];
    }
}
