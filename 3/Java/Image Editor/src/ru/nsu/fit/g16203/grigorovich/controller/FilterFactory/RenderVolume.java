package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import javafx.geometry.Point3D;
import ru.nsu.fit.g16203.grigorovich.utilityFiles.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;


public class RenderVolume implements Filter {
    private static final int GRID_SPACING = 100;
    BufferedImage newImage;
    private double[] absorptionValues;
    private double[][] colorValues;
    private double dx, dy, dz, df;
    private boolean isAbsorption, isEmission;

    private static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }

    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        int layersX = Integer.valueOf(args[0][0].toString());
        int layersY = Integer.valueOf(args[0][1].toString());
        int layersZ = Integer.valueOf(args[0][2].toString());
        dx = 1 / (double) layersX;
        dy = 1 / (double) layersY;
        dz = 1 / (double) layersZ;
        isAbsorption = Boolean.valueOf(args[0][3].toString());
        isEmission = Boolean.valueOf(args[0][4].toString());
        List<?> temp = convertObjectToList(args[0][5]);
        List<Pair<Integer, Double>> absorptionPoints = (List<Pair<Integer, Double>>) temp;
        temp = convertObjectToList(args[0][6]);
        List<Pair<Integer, int[]>> emissionPoints = (List<Pair<Integer, int[]>>) temp;
        temp = convertObjectToList(args[0][7]);
        List<Pair<double[], Double>> chargePoints = (List<Pair<double[], Double>>) temp;
        newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2.dispose();
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (int i = 0; i < layersX; ++i) {
            for (int j = 0; j < layersY; ++j) {
                for (int k = 0; k < layersZ; ++k) {
                    double currentVal = 0.0;
                    for (Pair<double[], Double> charge : chargePoints) {
                        Point3D chargePoint = new Point3D(charge.t[0], charge.t[1], charge.t[2]);
                        double chargeValue = charge.u;
                        Point3D current = new Point3D(dx * (i + 0.5), dy * (j + 0.5), dz * (k + 0.5));
                        double dist = current.distance(chargePoint);
                        if (dist < 0.1)
                            dist = 0.1;
                        currentVal += chargeValue / dist;
                    }
                    if (min > currentVal)
                        min = currentVal;
                    if (max < currentVal)
                        max = currentVal;
                }
            }
        }

        df = (max - min) / (double) GRID_SPACING;

        colorValues = new double[3][];
        absorptionValues = new double[GRID_SPACING];
        double[] redEmissionValues = new double[GRID_SPACING];
        double[] greenEmissionValues = new double[GRID_SPACING];
        double[] blueEmissionValues = new double[GRID_SPACING];
        colorValues[0] = redEmissionValues;
        colorValues[1] = greenEmissionValues;
        colorValues[2] = blueEmissionValues;
        for (int i = 0; i < absorptionPoints.size() - 1; ++i) {
            Pair<Integer, Double> point1 = absorptionPoints.get(i);
            Pair<Integer, Double> point2 = absorptionPoints.get(i + 1);
            int diffX = point2.t - point1.t;
            double diffY = point2.u - point1.u;
            if (diffY == 0) {
                for (int j = point1.t; j < point2.t; ++j)
                    absorptionValues[j] = point2.u;
                continue;
            }
            if (diffX == 0) {
                absorptionValues[point2.t] = point2.u;
                continue;
            }
            for (int j = point1.t; j < point2.t; ++j)
                absorptionValues[j] = point1.u + (diffY) / (diffX) * (j - point1.t);
        }

        for (int i = 0; i < emissionPoints.size() - 1; ++i) {
            Pair<Integer, int[]> point1 = emissionPoints.get(i);
            Pair<Integer, int[]> point2 = emissionPoints.get(i + 1);
            int diffX = point2.t - point1.t;
            for (int c = 0; c < 3; ++c) {
                int diffY = point2.u[c] - point1.u[c];
                if (diffY == 0) {
                    for (int j = point1.t; j < point2.t; ++j)
                        colorValues[c][j] = point2.u[c];
                    continue;
                }
                if (diffX == 0) {
                    colorValues[c][point2.t] = point2.u[c];
                    continue;
                }
                for (int j = point1.t; j < point2.t; ++j)
                    colorValues[c][j] = point1.u[c] + (diffY) / (double) (diffX) * (j - point1.t);
            }
        }

        double di = layersX / (double) oldImage.getWidth();
        double dj = layersY / (double) oldImage.getHeight();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Callable<Object>> todo = new ArrayList<>(oldImage.getWidth() * oldImage.getHeight());
        for (int j = 0; j < oldImage.getHeight(); ++j) {
            for (int i = 0; i < oldImage.getWidth(); ++i) {
                Color color = new Color(oldImage.getRGB(i, j));
                int finalI = i;
                int finalJ = j;
                double finalMin1 = min;
                Future<Void> future = service.submit(() -> {
                    calcVolume(color, di, dj, finalI, finalJ, layersZ, finalMin1, chargePoints);
                    return null;
                });
            }
        }
        service.shutdown();
        try {
            service.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return newImage;
    }

    private void calcVolume(Color color, double di, double dj, int i, int j, int z, double min, List<Pair<double[], Double>> chargePoints) {
        double red = color.getRed();
        double green = color.getGreen();
        double blue = color.getBlue();

        double x = dx * ((int) Math.round(di * i));
        double y = dy * ((int) Math.round(dj * j));
        for (int k = 0; k < z; ++k) {
            double currentVal = 0d;
            for (Pair<double[], Double> charge : chargePoints) {
                Point3D chargePoint = new Point3D(charge.t[0], charge.t[1], charge.t[2]);
                double chargeValue = charge.u;
                Point3D current = new Point3D(x, y, dz * (k + 0.5));
                double dist = current.distance(chargePoint);
                if (dist < 0.1)
                    dist = 0.1;
                currentVal += chargeValue / dist;
            }
            int index = (int) Math.round((currentVal - min) / df);
            double absorptionValue = absorptionValues[Math.max(0, index - 1)];
            int redColorValue = (int) colorValues[0][Math.max(0, index - 1)];
            int greenColorValue = (int) colorValues[1][Math.max(0, index - 1)];
            int blueColorValue = (int) colorValues[2][Math.max(0, index - 1)];
            if (isAbsorption) {
                red *= Math.exp(-absorptionValue * dz);
                green *= Math.exp(-absorptionValue * dz);
                blue *= Math.exp(-absorptionValue * dz);
            }
            if (isEmission) {
                red += redColorValue * dz;
                green += greenColorValue * dz;
                blue += blueColorValue * dz;
            }
        }
        newImage.setRGB(i, j,
                new Color(Math.max(0, Math.min(255, (int) Math.round(red))),
                        Math.max(0, Math.min(255, (int) Math.round(green))),
                        Math.max(0, Math.min(255, (int) Math.round(blue)))).getRGB());
    }
}
