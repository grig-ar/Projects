package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Watercolor implements Filter {
    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
        g2.drawImage(oldImage, null, null);
        g2.dispose();
        Blur blur = new Blur();
        newImage = blur.applyTo(newImage);
        Sharpen sharpen = new Sharpen();
        newImage = sharpen.applyTo(newImage);
        return newImage;
    }
}
