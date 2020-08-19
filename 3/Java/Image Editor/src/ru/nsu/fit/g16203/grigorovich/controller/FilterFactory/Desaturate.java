package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Desaturate implements Filter {
    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2.dispose();
        for (int y = 0; y < newImage.getHeight(); ++y) {
            for (int x = 0; x < newImage.getWidth(); ++x) {
                Color oldColor = new Color(newImage.getRGB(x, y));
                int gray = (int) (oldColor.getRed() * 0.299 + oldColor.getBlue() * 0.114 + oldColor.getGreen() * 0.587);
                if (gray > 255)
                    gray = 255;
                Color newColor = new Color(gray, gray, gray);
                newImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return newImage;
    }
}
