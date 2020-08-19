package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.image.BufferedImage;

public class Double implements Filter {
    private static final int SCALE_VALUE = 2;

    @Override
    public BufferedImage applyTo(BufferedImage oldImage, Object[]... args) {
        int centerX = oldImage.getWidth() / 2;
        int centerY = oldImage.getHeight() / 2;
        int cornerX = centerX - oldImage.getWidth() / 4;
        int cornerY = centerY - oldImage.getHeight() / 4;
        BufferedImage newImage = oldImage.getSubimage(cornerX, cornerY, oldImage.getWidth() / SCALE_VALUE, oldImage.getHeight() / SCALE_VALUE);
        int width = newImage.getWidth();
        int height = newImage.getHeight();
        BufferedImage after = new BufferedImage(width * SCALE_VALUE, height * SCALE_VALUE, BufferedImage.TYPE_INT_RGB);
        double px, py;
        double xRatio = 1 / (double) SCALE_VALUE;
        double yRatio = 1 / (double) SCALE_VALUE;
        for (int y = 0; y < after.getHeight(); ++y) {
            for (int x = 0; x < after.getWidth(); ++x) {
                px = Math.floor(x * xRatio);
                py = Math.floor(y * yRatio);
                after.setRGB(x, y, newImage.getRGB((int) px, (int) py));
            }
        }
        return after;
    }
}
