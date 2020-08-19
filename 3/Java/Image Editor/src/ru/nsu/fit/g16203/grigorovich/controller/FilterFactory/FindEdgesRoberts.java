package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FindEdgesRoberts implements Filter {

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
        desaturate.applyTo(oldImage);
        for (int y = 0; y < newImage.getHeight(); ++y) {
            for (int x = 0; x < newImage.getWidth(); ++x) {
                int Gx1, Gx2 = 0, Gy1 = 0, Gy2 = 0;

                Color color = new Color(oldImage.getRGB(x, y));
                Gx1 = color.getRed();
                if (x < (oldImage.getWidth() - 1) && y < (oldImage.getHeight() - 1)) {
                    color = new Color(oldImage.getRGB(x + 1, y + 1));
                    Gx2 = -color.getRed();
                }

                if (x < (oldImage.getWidth() - 1)) {
                    color = new Color(oldImage.getRGB(x + 1, y));
                    Gy1 = color.getRed();
                }
                if (y < (oldImage.getHeight() - 1)) {
                    color = new Color(oldImage.getRGB(x, y + 1));
                    Gy2 = -color.getRed();
                }

                double G = Math.sqrt(Math.pow(Gx1 + Gx2, 2) + Math.pow(Gy1 + Gy2, 2));
                if (G > level)
                    newImage.setRGB(x, y, Color.WHITE.getRGB());
                else
                    newImage.setRGB(x, y, Color.BLACK.getRGB());
            }
        }

        return newImage;
    }
}
