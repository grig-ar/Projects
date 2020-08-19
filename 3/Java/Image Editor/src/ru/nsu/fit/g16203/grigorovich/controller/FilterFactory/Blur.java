package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class Blur implements Filter {
    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2.dispose();
        for (int y = 0; y < newImage.getHeight(); ++y) {
            for (int x = 0; x < newImage.getWidth(); ++x) {
                ArrayList<Integer> neighborsRed = new ArrayList<>(25);
                ArrayList<Integer> neighborsGreen = new ArrayList<>(25);
                ArrayList<Integer> neighborsBlue = new ArrayList<>(25);
                Color color;
                for (int i = -2; i < 3; ++i) {
                    for (int j = -2; j < 3; ++j) {
                        if ((x + i) > 1 && (x + i) < (newImage.getWidth() - 1) && (y + j) > 0 && (y + j) < (newImage.getHeight() - 1)) {
                            color = new Color(oldImage.getRGB(x + i, y + j));
                            neighborsRed.add(color.getRed());
                            neighborsGreen.add(color.getGreen());
                            neighborsBlue.add(color.getBlue());
                        }
                    }
                }
                Collections.sort(neighborsRed);
                Collections.sort(neighborsGreen);
                Collections.sort(neighborsBlue);
                color = new Color(neighborsRed.get(neighborsRed.size() / 2 - 1),
                        neighborsGreen.get(neighborsGreen.size() / 2 - 1),
                        neighborsBlue.get(neighborsBlue.size() / 2 - 1));
                newImage.setRGB(x, y, color.getRGB());
            }
        }
        return newImage;
    }
}
