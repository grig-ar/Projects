package ru.nsu.fit.g16203.grigorovich.utilityFiles;

import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.border.AbstractBorder;


public class CustomBorder extends AbstractBorder {
    private Color borderColor;
    private int gap;
    private int length;
    private int spacing;

    public CustomBorder(Color color, int gap, int length, int spacing) {
        borderColor = color;
        this.gap = gap;
        this.length = length;
        this.spacing = spacing;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        super.paintBorder(c, g, x, y, width, height);
        Graphics2D g2d;
        if (g instanceof Graphics2D) {
            g2d = (Graphics2D) g;
            g2d.setColor(borderColor);
            int posX = x + spacing;
            int posY = y;
            while (posX < width) {
                g2d.draw(new Line2D.Double((double) posX, (double) height - 1, (double) (posX + length), (double) height - 1));
                posX += length + spacing;
            }
            while (posY < height) {
                g2d.draw(new Line2D.Double((double) x + 1, (double) posY, (double) x + 1, (double) (posY + length)));
                posY += length + spacing;
            }
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return (getBorderInsets(c, new Insets(gap, gap, gap, gap)));
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = gap;
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}
