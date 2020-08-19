package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sharpen implements Filter {
    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2.dispose();
        for (int y = 0; y < newImage.getHeight(); ++y) {
            for (int x = 0; x < newImage.getWidth(); ++x) {
                int sumRed = 0;
                int sumBlue = 0;
                int sumGreen = 0;
                Color color;

                for (int i = -1; i < 2; ++i) {
                    for (int j = -1; j < 2; ++j) {
                        if (i == -1 && j == -1 || i == -1 && j == 1 || i == 1 && j == -1 || i == 1 && j == 1 || i == 0 && j == 0)
                            continue;
                        if ((x + i) > 1 && (x + i) < (newImage.getWidth() - 1) && (y + j) > 0 && (y + j) < (newImage.getHeight() - 1)) {
                            color = new Color(oldImage.getRGB(x + i, y + j));
                            sumRed += color.getRed();
                            sumGreen += color.getGreen();
                            sumBlue += color.getBlue();
                        }
                    }
                }
                Color currentColor = new Color(oldImage.getRGB(x, y));
                int currentRed = 5 * currentColor.getRed();
                int currentGreen = 5 * currentColor.getGreen();
                int currentBlue = 5 * currentColor.getBlue();
                color = new Color(Math.max(0, Math.min(255, currentRed - sumRed)),
                        Math.max(0, Math.min(255, currentGreen - sumGreen)),
                        Math.max(0, Math.min(255, currentBlue - sumBlue)));
                newImage.setRGB(x, y, color.getRGB());
            }
        }
        return newImage;
    }
}
