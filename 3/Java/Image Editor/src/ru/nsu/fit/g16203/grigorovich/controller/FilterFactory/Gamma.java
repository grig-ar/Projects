package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Gamma implements Filter {
    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        double gammaCoef = java.lang.Double.valueOf(args[0][0].toString());
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2.dispose();
        for (int y = 0; y < newImage.getHeight(); ++y) {
            for (int x = 0; x < newImage.getWidth(); ++x) {
                Color oldColor = new Color(newImage.getRGB(x, y));
                Color newColor = new Color(Math.max(0, Math.min(255, (int) Math.round(255 * Math.pow(oldColor.getRed() / 255d, gammaCoef)))),
                        Math.max(0, Math.min(255, (int) Math.round(255 * Math.pow(oldColor.getGreen() / 255d, gammaCoef)))),
                        Math.max(0, Math.min(255, (int) Math.round(255 * Math.pow(oldColor.getBlue() / 255d, gammaCoef)))));
                newImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return newImage;
    }
}
