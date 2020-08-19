package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FindEdgesSobel implements Filter {

    private boolean checkValues(int level) {
        return level > 0;

    }

    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        int level = java.lang.Integer.valueOf(args[0][0].toString());
        if (!checkValues(level))
            return oldImage;
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2.dispose();
        Filter desaturate = new Desaturate();
        oldImage = desaturate.applyTo(oldImage);
        for (int y = 0; y < newImage.getHeight(); ++y) {
            for (int x = 0; x < newImage.getWidth(); ++x) {
                int a = 0, b = 0, c = 0, d = 0, f = 0, g = 0, h = 0, i = 0;
                Color color;
                if (y > 1) {
                    color = new Color(oldImage.getRGB(x, y - 1));
                    b = color.getRed();
                    if (x > 1) {
                        color = new Color(oldImage.getRGB(x - 1, y - 1));
                        a = color.getRed();
                        color = new Color(oldImage.getRGB(x - 1, y));
                        d = color.getRed();
                    }
                    if (x < (oldImage.getWidth() - 1)) {
                        color = new Color(oldImage.getRGB(x + 1, y - 1));
                        c = color.getRed();
                        color = new Color(oldImage.getRGB(x + 1, y));
                        f = color.getRed();
                    }
                }
                if (y < (oldImage.getHeight() - 1)) {
                    color = new Color(oldImage.getRGB(x, y + 1));
                    h = color.getRed();
                    if (x > 1) {
                        color = new Color(oldImage.getRGB(x - 1, y + 1));
                        g = color.getRed();
                    }
                    if (x < (oldImage.getWidth() - 1)) {
                        color = new Color(oldImage.getRGB(x + 1, y + 1));
                        i = color.getRed();
                    }
                }
                int Gx = (c + 2 * f + i) - (a + 2 * d + g);
                int Gy = (g + 2 * h + i) - (a + 2 * b + c);
                double G = Math.sqrt(Math.pow(Gx, 2) + Math.pow(Gy, 2));
                if (G > level)
                    newImage.setRGB(x, y, Color.WHITE.getRGB());
                else
                    newImage.setRGB(x, y, Color.BLACK.getRGB());
            }
        }

        return newImage;
    }
}
