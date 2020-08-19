package ru.nsu.fit.g16203.grigorovich.model;

import ru.nsu.fit.g16203.grigorovich.utilityFiles.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
    ImagePanel panelB;
    ImagePanel panelC;
    private static final int THICKNESS_BORDER = 1;
    private static final int LENGTH_BORDER = 2;
    private static final int SPACING_BORDER = 3;
    private static final Color COLOR_BORDER = Color.BLACK;

    private static final int HEIGHT_IMAGE = 350;
    private static final int WIDTH_IMAGE = 350;
    private static final int HEIGHT_PANEL = 350 + 2 * THICKNESS_BORDER;
    private static final int WIDTH_PANEL = 350 + 2 * THICKNESS_BORDER;
    private BufferedImage image;
    BufferedImage selectedImage;
    public boolean isSelectEnabled = false;
    private boolean isSelect = false;
    boolean isPanelC = false;
    public boolean filterOn = false;
    private int xCoord;
    private int yCoord;

    private static Pair<Integer, Integer> getScaledImageSizes(BufferedImage image) {
        double sideRatio = image.getHeight() / (double) image.getWidth();
        double widthScaleRatio = image.getWidth() / (double) WIDTH_IMAGE;
        double heightScaleRatio = image.getHeight() / (double) HEIGHT_IMAGE;
        int imageWidth;
        int imageHeight;
        if (sideRatio >= 1) {
            imageHeight = (heightScaleRatio > 1) ? HEIGHT_IMAGE : image.getHeight();
            imageWidth = ((int) Math.round(imageHeight / sideRatio));

        } else {
            imageWidth = (widthScaleRatio > 1) ? WIDTH_IMAGE : image.getWidth();
            imageHeight = ((int) Math.round(imageWidth * sideRatio));
        }
        return new Pair<>(imageWidth, imageHeight);
    }

    ImagePanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(WIDTH_PANEL, HEIGHT_PANEL));
        setBorder((BorderFactory.createDashedBorder(COLOR_BORDER, THICKNESS_BORDER, LENGTH_BORDER, SPACING_BORDER, true)));

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (!isSelectEnabled)
                    return;
                isSelect = true;
                xCoord = e.getX();
                yCoord = e.getY();
                repaint();
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!isSelectEnabled)
                    return;
                xCoord = e.getX();
                yCoord = e.getY();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isSelect = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    BufferedImage loadImage(File file) {
        try {
            image = ImageIO.read(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error");
            return null;
        }
        repaint();
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) this.image.getGraphics();
        g2.drawImage(image, null, null);
        g2.dispose();
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (image == null)
            return;
        if (filterOn || isPanelC) {
            g2.drawImage(image, null, null);
            filterOn = false;
            return;
        }

        Pair sizes = getScaledImageSizes(image);
        int newImageWidth = (Integer) sizes.t;
        int newImageHeight = (Integer) sizes.u;
        double sideRatio = image.getHeight() / (double) image.getWidth();
        double widthScaleRatio = image.getWidth() / (double) WIDTH_IMAGE;
        double heightScaleRatio = image.getHeight() / (double) HEIGHT_IMAGE;
        if (isSelect || isSelectEnabled) {
            BufferedImage selectingImage = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) selectingImage.getGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(image, 0, 0, newImageWidth, newImageHeight, 0, 0, image.getWidth(), image.getHeight(), null);
            graphics.setXORMode(Color.WHITE);
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{5f, 5f}, 0f));
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int scaledRectWidth, scaledRectHeight;
            if (sideRatio < 1) {
                scaledRectWidth = (int) Math.round(WIDTH_IMAGE / widthScaleRatio);
                scaledRectHeight = Math.min(newImageHeight, (int) Math.round(HEIGHT_IMAGE / widthScaleRatio));
            } else {
                scaledRectWidth = Math.min(newImageWidth, (int) Math.round(WIDTH_IMAGE / heightScaleRatio));
                scaledRectHeight = (int) Math.round(HEIGHT_IMAGE / heightScaleRatio);
            }
            int rectX = (xCoord + scaledRectWidth / 2) <= newImageWidth ? xCoord - scaledRectWidth / 2 : newImageWidth - scaledRectWidth;
            int rectY = (yCoord + scaledRectHeight / 2) <= newImageHeight ? yCoord - scaledRectHeight / 2 : newImageHeight - scaledRectHeight;
            if (xCoord - scaledRectWidth / 2 <= 0)
                rectX = 0;
            if (yCoord - scaledRectWidth / 2 <= 0)
                rectY = 0;
            graphics.drawRect(rectX, rectY, scaledRectWidth, scaledRectHeight);
            try {
                if (heightScaleRatio > widthScaleRatio) {
                    int selectedWidth;
                    int selectedHeight;
                    if ((int) Math.round(rectX * heightScaleRatio) + WIDTH_IMAGE > image.getWidth())
                        selectedWidth = image.getWidth() - (int) Math.round(rectX * heightScaleRatio);
                    else
                        selectedWidth = WIDTH_IMAGE;
                    if ((int) Math.round(rectY * heightScaleRatio) + HEIGHT_IMAGE > image.getHeight())
                        selectedHeight = image.getHeight() - (int) Math.round(rectY * heightScaleRatio);
                    else
                        selectedHeight = HEIGHT_IMAGE;
                    selectedImage = image.getSubimage((int) Math.round(rectX * heightScaleRatio), (int) Math.round(rectY * heightScaleRatio), selectedWidth, selectedHeight);
                } else {
                    int selectedWidth;
                    int selectedHeight;
                    if ((int) Math.round(rectX * widthScaleRatio) + WIDTH_IMAGE > image.getWidth())
                        selectedWidth = image.getWidth() - (int) Math.round(rectX * widthScaleRatio);
                    else
                        selectedWidth = WIDTH_IMAGE;
                    if ((int) Math.round(rectY * widthScaleRatio) + HEIGHT_IMAGE > image.getHeight())
                        selectedHeight = image.getHeight() - (int) Math.round(rectY * widthScaleRatio);
                    else
                        selectedHeight = HEIGHT_IMAGE;
                    selectedImage = image.getSubimage((int) Math.round(rectX * widthScaleRatio), (int) Math.round(rectY * widthScaleRatio), selectedWidth, selectedHeight);
                }
            } catch (RasterFormatException ex) {
                ex.printStackTrace();
            }
            if (panelB != null && isSelect) {
                panelB.setImage(selectedImage);
            }
            g2.drawImage(selectingImage, THICKNESS_BORDER, THICKNESS_BORDER, null);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image, THICKNESS_BORDER, THICKNESS_BORDER, newImageWidth + THICKNESS_BORDER, newImageHeight + THICKNESS_BORDER, THICKNESS_BORDER, THICKNESS_BORDER, image.getWidth(), image.getHeight(), null);
        }
    }
}

