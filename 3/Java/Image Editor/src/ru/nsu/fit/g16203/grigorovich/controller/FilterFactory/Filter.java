package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.awt.image.BufferedImage;

public interface Filter {
    BufferedImage applyTo(BufferedImage oldImage, Object[]... args);
}
