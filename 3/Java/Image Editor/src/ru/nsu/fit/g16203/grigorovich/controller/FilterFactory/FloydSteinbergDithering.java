package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.abs;

public class FloydSteinbergDithering implements Filter {

    private int findCloseColor(int color, int n) {
        if (n == 1) {
            return 0;
        }
        return ((int) Math.round(color * (n - 1) / 255d)) * 255 / (n - 1);
    }

    private boolean checkValues(int red, int blue, int green) {
        return red > 0 && blue > 0 && green > 0;
    }

    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        int redVal = java.lang.Integer.valueOf(args[0][0].toString());
        int greenVal = java.lang.Integer.valueOf(args[0][1].toString());
        int blueVal = java.lang.Integer.valueOf(args[0][2].toString());
        if (!checkValues(redVal, greenVal, blueVal))
            return oldImage;
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage oldImageCopy = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2 = (Graphics2D) oldImageCopy.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2.dispose();
        for (int y = 0; y < newImage.getHeight(); ++y) {
            for (int x = 0; x < newImage.getWidth(); ++x) {
                Color color = new Color(oldImageCopy.getRGB(x, y));
                int oldRed = color.getRed();
                int oldGreen = color.getGreen();
                int oldBlue = color.getBlue();
                int closeRed = findCloseColor(oldRed, redVal);
                int closeGreen = findCloseColor(oldGreen, greenVal);
                int closeBlue = findCloseColor(oldBlue, blueVal);
                int errorRed = oldRed - closeRed;
                int errorGreen = oldGreen - closeGreen;
                int errorBlue = oldBlue - closeBlue;
                Color closeColor = new Color(closeRed, closeGreen, closeBlue);
                newImage.setRGB(x, y, closeColor.getRGB());

                if (x < (newImage.getWidth() - 1)) {
                    color = new Color(oldImageCopy.getRGB(x + 1, y));
                    int alignedRed = color.getRed();
                    int newRed = Math.max(0, Math.min(255, (int) (alignedRed + 7 / 16d * errorRed)));
                    int alignedGreen = color.getGreen();
                    int newGreen = Math.max(0, Math.min(255, (int) (alignedGreen + 7 / 16d * errorGreen)));
                    int alignedBlue = color.getBlue();
                    int newBlue = Math.max(0, Math.min(255, (int) (alignedBlue + 7 / 16d * errorBlue)));
                    Color updatedOldColor = new Color(newRed, newGreen, newBlue);
                    oldImageCopy.setRGB(x + 1, y, updatedOldColor.getRGB());
                }

                if (x > 0 && y < (newImage.getHeight() - 1)) {
                    color = new Color(oldImageCopy.getRGB(x - 1, y + 1));
                    int alignedRed = color.getRed();
                    int newRed = Math.max(0, Math.min(255, (int) (alignedRed + 3 / 16d * errorRed)));
                    int alignedGreen = color.getGreen();
                    int newGreen = Math.max(0, Math.min(255, (int) (alignedGreen + 3 / 16d * errorGreen)));
                    int alignedBlue = color.getBlue();
                    int newBlue = Math.max(0, Math.min(255, (int) (alignedBlue + 3 / 16d * errorBlue)));
                    Color updatedOldColor = new Color(newRed, newGreen, newBlue);
                    oldImageCopy.setRGB(x - 1, y + 1, updatedOldColor.getRGB());
                }

                if (y < (newImage.getHeight() - 1)) {
                    color = new Color(oldImageCopy.getRGB(x, y + 1));
                    int alignedRed = color.getRed();
                    int newRed = Math.max(0, Math.min(255, (int) (alignedRed + 5 / 16d * errorRed)));
                    int alignedGreen = color.getGreen();
                    int newGreen = Math.max(0, Math.min(255, (int) (alignedGreen + 5 / 16d * errorGreen)));
                    int alignedBlue = color.getBlue();
                    int newBlue = Math.max(0, Math.min(255, (int) (alignedBlue + 5 / 16d * errorBlue)));
                    Color updatedOldColor = new Color(newRed, newGreen, newBlue);
                    oldImageCopy.setRGB(x, y + 1, updatedOldColor.getRGB());
                }

                if (x < (newImage.getWidth() - 1) && y < (newImage.getHeight() - 1)) {
                    color = new Color(oldImageCopy.getRGB(x + 1, y + 1));
                    int alignedRed = color.getRed();
                    int newRed = Math.max(0, Math.min(255, (int) (alignedRed + 1 / 16d * errorRed)));
                    int alignedGreen = color.getGreen();
                    int newGreen = Math.max(0, Math.min(255, (int) (alignedGreen + 1 / 16d * errorGreen)));
                    int alignedBlue = color.getBlue();
                    int newBlue = Math.max(0, Math.min(255, (int) (alignedBlue + 1 / 16d * errorBlue)));
                    Color updatedOldColor = new Color(newRed, newGreen, newBlue);
                    oldImageCopy.setRGB(x + 1, y + 1, updatedOldColor.getRGB());
                }
            }
        }
        return newImage;
    }
}
