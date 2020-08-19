package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OrderedDithering implements Filter {

    private static int log(int x) {
        return (int) (Math.log(x) / Math.log(2));
    }

    private int findCloseColor(int color, int n) {
        if (n == 1) {
            return 0;
        }
        return ((int) Math.round(color * (n - 1) / 255d)) * 255 / (n - 1);
    }

    private int[][] getMatrix(int matrixOrder) {
        int[][] bayer = new int[(int) Math.pow(2, matrixOrder)][(int) Math.pow(2, matrixOrder)];
        for (int m = 0; m <= matrixOrder; ++m) {
            int dim = 1 << m;
            for (int y = 0; y < dim; ++y) {
                for (int x = 0; x < dim; ++x) {
                    int v = 0;
                    int mask = m - 1;
                    int xc = x ^ y;
                    for (int bit = 0; bit < 2 * m; --mask) {
                        v |= ((y >> mask) & 1) << bit++;
                        v |= ((xc >> mask) & 1) << bit++;
                        bayer[y][x] = v;
                    }
                }
            }
        }
        return bayer;
    }

    private boolean checkValues(int red, int blue, int green, int size) {
        return red > 0 && blue > 0 && green > 0 && size > 0;
    }

    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        int redVal = java.lang.Integer.valueOf(args[0][0].toString());
        int greenVal = java.lang.Integer.valueOf(args[0][1].toString());
        int blueVal = java.lang.Integer.valueOf(args[0][2].toString());
        int size = java.lang.Integer.valueOf(args[0][3].toString());
        if (!checkValues(redVal, greenVal, blueVal, size))
            return oldImage;

        int matrixOrder = log(size);
        double div = 1 / Math.pow(size, 2);
        double dr = 255.0 / (redVal - 1);
        double dg = 255.0 / (greenVal - 1);
        double db = 255.0 / (blueVal - 1);
        int[][] bayer = getMatrix(matrixOrder);
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2.dispose();
        for (int y = 0; y < newImage.getHeight(); ++y) {
            for (int x = 0; x < newImage.getWidth(); ++x) {
                Color color = new Color(oldImage.getRGB(x, y));
                int oldRed = color.getRed();
                int oldGreen = color.getGreen();
                int oldBlue = color.getBlue();
                double error = bayer[x % size][y % size] * div - 1 / 2d;
                int newRed = Math.max(0, Math.min(255, (int) Math.round(oldRed + error * dr)));
                int newGreen = Math.max(0, Math.min(255, (int) Math.round(oldGreen + error * dg)));
                int newBlue = Math.max(0, Math.min(255, (int) Math.round(oldBlue + error * db)));
                int closeRed = findCloseColor(newRed, redVal);
                int closeGreen = findCloseColor(newGreen, greenVal);
                int closeBlue = findCloseColor(newBlue, blueVal);
                Color closeColor = new Color(closeRed, closeGreen, closeBlue);
                newImage.setRGB(x, y, closeColor.getRGB());
            }
        }
        return newImage;
    }
}
