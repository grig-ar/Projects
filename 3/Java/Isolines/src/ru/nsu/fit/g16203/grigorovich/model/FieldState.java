package nsu.fit.g16203.grigorovich.model;

import java.awt.*;

public class FieldState {
    private int xGrid;
    private int yGrid;
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    public FieldState(int xGrid, int yGrid, double xMin, double xMax, double yMin, double yMax) {
        this.xGrid = xGrid;
        this.yGrid = yGrid;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public int getXGrid() {
        return xGrid;
    }

    public int getYGrid() {
        return yGrid;
    }

    public double getXMin() {
        return xMin;
    }

    public double getXMax() {
        return xMax;
    }

    public double getYMin() {
        return yMin;
    }

    public double getYMax() {
        return yMax;
    }
}
