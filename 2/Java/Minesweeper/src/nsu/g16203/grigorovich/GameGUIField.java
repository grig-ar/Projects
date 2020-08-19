package nsu.g16203.grigorovich;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GameGUIField extends GameField {
    public GUICell[][] GUIfield;
    public ArrayList<JButton> buttonsList;
    public BufferedImage[] images = new BufferedImage[14];
    public boolean isGame = true;
    public boolean first = true;
    public int clock = 0;

    public GameGUIField(int x, int y, int minesAmount) {
        super(x, y, minesAmount);
        GUIfield = new GUICell[xCoord][yCoord];
        for (int i = 0; i < xCoord; ++i) {
            for (int j = 0; j < yCoord; ++j) {
                GUIfield[i][j] = new GUICell(i, j, field[i][j].getState());
            }
        }
    }

    public GUICell getCell(int x, int y) {
        return GUIfield[x][y];
    }

    public void showAll() {
        for(GUICell[] row : GUIfield){
            for(GUICell cell : row){
                cell.show();
            }
        }
    }

    public boolean isAllOpened() {
        for(GUICell[] row : GUIfield){
            for(GUICell cell : row){
                if (cell.getState() == 0 && cell.isHidden())
                    return false;
            }
        }
        return true;
    }

    public void printField() {
        GUICell temp;
        for (int i = 0; i < xCoord; ++i) {
            for (int j = 0; j < yCoord; ++j) {
                temp = getCell(i,j);
                if (temp.isMarked()) {
                    if (!isGame && temp.getState() == 0)
                        buttonsList.get(i*yCoord + j).setIcon(new ImageIcon(images[12]));
                    else
                        buttonsList.get(i*yCoord + j).setIcon(new ImageIcon(images[10]));
                    continue;
                }

                if (temp.isHidden())
                    buttonsList.get(i*yCoord + j).setIcon(new ImageIcon(images[9]));
                else {
                    switch (temp.getState()) {
                        case 0:
                            if (getMinesEnv(i, j) == 0)
                                buttonsList.get(i*yCoord + j).setIcon(new ImageIcon(images[0]));
                            else
                                buttonsList.get(i*yCoord + j).setIcon(new ImageIcon(images[getMinesEnv(i, j)]));
                            break;
                        case -1:
                            buttonsList.get(i*yCoord + j).setIcon(new ImageIcon(images[11]));
                            break;
                        case -2:
                            buttonsList.get(i*yCoord + j).setIcon(new ImageIcon(images[13]));
                            break;
                            default:
                            break;
                   }
                }
            }
        }
    }
    public void addMine(int x, int y) {
        Random rnd = new Random(System.currentTimeMillis());
        int xTemp = rnd.nextInt(xCoord);
        int yTemp = rnd.nextInt(yCoord);
        while(GUIfield[xTemp][yTemp].getState() != 0 || (xTemp == x && yTemp == y)) {
            xTemp = rnd.nextInt(xCoord);
            yTemp = rnd.nextInt(yCoord);
        }
        GUIfield[xTemp][yTemp].setState(-1);
        for (int k = -1; k < 2; ++k) {
            for (int t = -1; t < 2; ++t) {
                if((k + xTemp >=0) && (k + xTemp < xCoord) && (t + yTemp >= 0) && (t + yTemp < yCoord))
                    minesEnv[k+xTemp][t+yTemp]++;
            }
        }
    }

    public void removeMine(int x, int y) {
        GUIfield[x][y].setState(0);
        for (int k = -1; k < 2; ++k) {
            for (int t = -1; t < 2; ++t) {
                if((k + x >=0) && (k + x < xCoord) && (t + y >= 0) && (t + y < yCoord))
                    minesEnv[k+x][t+y]--;
            }
        }
    }
}
