package nsu.fit.g16203.grigorovich.model;

import nsu.fit.g16203.grigorovich.utilityFiles.Pair;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PlotRectangle {
    private Pair<ArrayList<Point2D.Double>, double[]> data;

    PlotRectangle(Pair<ArrayList<Point2D.Double>, double[]> data) {
        this.data = data;
    }

    public Pair<ArrayList<Point2D.Double>, double[]> getData() {
        return data;
    }

    public boolean pointInside(double x, double y) {
        return (data.t.get(0).getX() <= x && data.t.get(3).getX() >= x &&
                data.t.get(0).getY() <= y && data.t.get(3).getY() >= y);
    }

    public ArrayList<Point2D.Double> findRoots(double z) {
        ArrayList<Point2D.Double> roots = new ArrayList<>();

        //top
        if (!((z < data.u[0] && z < data.u[1]) || (z > data.u[0] && z > data.u[1]))) {
            if (data.u[1] - data.u[0] > 0)
                roots.add(new Point2D.Double(data.t.get(0).getX() + (data.t.get(1).getX() - data.t.get(0).getX()) * (z - data.u[0]) / (data.u[1] - data.u[0]), data.t.get(0).getY()));
            else
                roots.add(new Point2D.Double(data.t.get(1).getX() - (data.t.get(1).getX() - data.t.get(0).getX()) * (z - data.u[1]) / (data.u[0] - data.u[1]), data.t.get(0).getY()));
        }

        //right
        if (!((z < data.u[1] && z < data.u[3]) || (z > data.u[1] && z > data.u[3]))) {
            if (data.u[3] - data.u[1] > 0)
                roots.add(new Point2D.Double(data.t.get(1).getX(), data.t.get(1).getY() + (data.t.get(3).getY() - data.t.get(1).getY()) * (z - data.u[1]) / (data.u[3] - data.u[1])));
            else
                roots.add(new Point2D.Double(data.t.get(1).getX(), data.t.get(3).getY() - (data.t.get(3).getY() - data.t.get(1).getY()) * (z - data.u[3]) / (data.u[1] - data.u[3])));
        }

        //bottom
        if (!((z < data.u[2] && z < data.u[3]) || (z > data.u[2] && z > data.u[3]))) {
            if (data.u[3] - data.u[2] > 0)
                roots.add(new Point2D.Double(data.t.get(2).getX() + (data.t.get(3).getX() - data.t.get(2).getX()) * (z - data.u[2]) / (data.u[3] - data.u[2]), data.t.get(2).getY()));
            else
                roots.add(new Point2D.Double(data.t.get(3).getX() - (data.t.get(3).getX() - data.t.get(2).getX()) * (z - data.u[3]) / (data.u[2] - data.u[3]), data.t.get(2).getY()));
        }

        //left
        if (!((z < data.u[0] && z < data.u[2]) || (z > data.u[0] && z > data.u[2]))) {
            if (data.u[2] - data.u[0] > 0)
                roots.add(new Point2D.Double(data.t.get(0).getX(), data.t.get(0).getY() + (data.t.get(2).getY() - data.t.get(0).getY()) * (z - data.u[0]) / (data.u[2] - data.u[0])));
            else
                roots.add(new Point2D.Double(data.t.get(0).getX(), data.t.get(2).getY() - (data.t.get(2).getY() - data.t.get(0).getY()) * (z - data.u[2]) / (data.u[0] - data.u[2])));
        }

        return roots;
    }

    public double getValue(double x, double y) {
        return bLerp(data.u[0], data.u[1], data.u[2], data.u[3],
                (x - data.t.get(0).getX()) / (data.t.get(3).getX() - data.t.get(0).getX()),
                (y - data.t.get(0).getY()) / (data.t.get(3).getY() - data.t.get(0).getY()));
    }

    private double lerp(double s, double e, double t) {
        return s + (e - s) * t;
    }

    private double bLerp(double c00, double c10, double c01, double c11, double tx, double ty) {
        return lerp(lerp(c00, c10, tx), lerp(c01, c11, tx), ty);

    }

    private void splitToTriangles() {
        double centerX = (data.t.get(0).getX() + data.t.get(1).getX()) / 2d;
        double centerY = (data.t.get(0).getY() + data.t.get(2).getY()) / 2d;
        double centerZ = (data.u[0] + data.u[1] + data.u[2] + data.u[3]) / 4d;
        Point2D.Double center = new Point2D.Double(centerX, centerY);
        ArrayList<Point2D.Double> tr0 = new ArrayList<>(3);
        tr0.add(data.t.get(0));
        tr0.add(data.t.get(1));
        tr0.add(center);
        PlotTriangle triangle = new PlotTriangle(new Pair<>(tr0, new double[]{data.u[0], data.u[1], centerZ}));

        ArrayList<Point2D.Double> tr1 = new ArrayList<>(3);
        tr1.add(data.t.get(1));
        tr1.add(data.t.get(3));
        tr1.add(center);
        PlotTriangle triangle1 = new PlotTriangle(new Pair<>(tr1, new double[]{data.u[1], data.u[3], centerZ}));
        ArrayList<Point2D.Double> tr2 = new ArrayList<>(3);
        tr2.add(data.t.get(3));
        tr2.add(data.t.get(2));
        tr2.add(center);
        PlotTriangle triangle2 = new PlotTriangle(new Pair<>(tr2, new double[]{data.u[3], data.u[2], centerZ}));
        ArrayList<Point2D.Double> tr3 = new ArrayList<>(3);
        tr3.add(data.t.get(2));
        tr3.add(data.t.get(0));
        tr3.add(center);
        PlotTriangle triangle3 = new PlotTriangle(new Pair<>(tr3, new double[]{data.u[2], data.u[0], centerZ}));
    }

    public ArrayList<Pair<Point2D.Double, Point2D.Double>> groupRoots(ArrayList<Point2D.Double> roots, double z) {
        ArrayList<Pair<Point2D.Double, Point2D.Double>> grouped = new ArrayList<>(2);
        //double centerX = (data.t.get(0).getX() + data.t.get(1).getX()) / 2d;
        //double centerY = (data.t.get(0).getY() + data.t.get(2).getY()) / 2d;
        double centerZ = (data.u[0] + data.u[1] + data.u[2] + data.u[3]) / 4d;

        if (!((z < data.u[0] && z < centerZ) || (z > data.u[0] && z > centerZ))) {
            grouped.add(new Pair<>(roots.get(0), roots.get(3)));
            grouped.add(new Pair<>(roots.get(1), roots.get(2)));
        } else {
            grouped.add(new Pair<>(roots.get(0), roots.get(1)));
            grouped.add(new Pair<>(roots.get(2), roots.get(3)));
        }

        return grouped;
    }

    @Override
    public String toString() {
        return String.valueOf(data.t.get(0).getX()) + ',' + String.valueOf(data.t.get(0).getY()) + '\t' +
                String.valueOf(data.t.get(1).getX()) + ',' + String.valueOf(data.t.get(1).getY()) + '\n' +
                String.valueOf(data.t.get(2).getX()) + ',' + String.valueOf(data.t.get(2).getY()) + '\t' +
                String.valueOf(data.t.get(3).getX()) + ',' + String.valueOf(data.t.get(3).getY());

    }

}
