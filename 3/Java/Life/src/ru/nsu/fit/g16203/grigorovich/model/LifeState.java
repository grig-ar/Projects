package ru.nsu.fit.g16203.grigorovich.model;

public class LifeState {
    public LifeState(int cols, int rows, int stroke, int size, double liveBeginValue, double liveEndValue, double birthBeginValue, double birthEndValue, double firstImpactValue, double secondImpactValue, boolean isXor, int timer) {
        this.cols = cols;
        this.rows = rows;
        this.stroke = stroke;
        this.size = size;
        this.liveBeginValue = liveBeginValue;
        this.liveEndValue = liveEndValue;
        this.birthBeginValue = birthBeginValue;
        this.birthEndValue = birthEndValue;
        this.firstImpactValue = firstImpactValue;
        this.secondImpactValue = secondImpactValue;
        this.isXor = isXor;
        this.timer = timer;
    }

    public int cols;
    public int rows;
    public int stroke;
    public int size;
    public double liveBeginValue;
    public double liveEndValue;
    public double birthBeginValue;
    public double birthEndValue;
    public double firstImpactValue;
    public double secondImpactValue;
    public boolean isXor;
    public int timer;
}
