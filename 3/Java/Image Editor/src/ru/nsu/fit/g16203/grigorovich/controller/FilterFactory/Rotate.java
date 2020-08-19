package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Rotate implements Filter {

    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        int angle = java.lang.Integer.valueOf(args[0][0].toString());
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        int centerX = oldImage.getWidth() / 2;
        int centerY = oldImage.getHeight() / 2;
        g2.dispose();
        for (int y = 0; y < newImage.getHeight(); ++y) {
            for (int x = 0; x < newImage.getWidth(); ++x) {
                int rotatedX = centerX + (int) Math.round((x - centerX) * cos + (y - centerY) * sin);
                int rotatedY = centerY + (int) Math.round((x - centerX) * -sin + (y - centerY) * cos);
                if (rotatedX >= newImage.getWidth() || rotatedX < 0) {
                    newImage.setRGB(x, y, Color.WHITE.getRGB());
                    continue;
                }
                if (rotatedY >= newImage.getHeight() || rotatedY < 0) {
                    newImage.setRGB(x, y, Color.WHITE.getRGB());
                    continue;
                }
                Color color = new Color(oldImage.getRGB(rotatedX, rotatedY));
                newImage.setRGB(x, y, color.getRGB());
            }
        }
        return newImage;
    }
}
