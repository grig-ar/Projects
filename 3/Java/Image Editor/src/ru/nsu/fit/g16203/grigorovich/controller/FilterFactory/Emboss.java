package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Emboss implements Filter {
    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2.dispose();
        Filter desaturate = new Desaturate();
        desaturate.applyTo(oldImage);
        for (int y = 0; y < newImage.getHeight(); ++y) {
            for (int x = 0; x < newImage.getWidth(); ++x) {
                Color color;
                int up = 0, down = 0, right = 0, left = 0;

                if (y > 1) {
                    color = new Color(oldImage.getRGB(x, y - 1));
                    up = color.getRed();
                }
                if (y < (newImage.getHeight() - 1)) {
                    color = new Color(oldImage.getRGB(x, y + 1));
                    down = color.getRed();
                }
                if (x < (newImage.getWidth() - 1)) {
                    color = new Color(oldImage.getRGB(x + 1, y));
                    right = color.getRed();
                }
                if (x > 1) {
                    color = new Color(oldImage.getRGB(x - 1, y));
                    left = color.getRed();
                }

                int currentRed = Math.max(0, Math.min(255, up - down + right - left + 128));
                int currentGreen = Math.max(0, Math.min(255, up - down + right - left + 128));
                int currentBlue = Math.max(0, Math.min(255, up - down + right - left + 128));
                Color currentColor = new Color(currentRed, currentGreen, currentBlue);
                newImage.setRGB(x, y, currentColor.getRGB());
            }
        }
        return newImage;
    }
}
