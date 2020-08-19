package ru.nsu.fit.g16203.grigorovich.model;

import ru.nsu.fit.g16203.grigorovich.utilityFiles.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainPanel extends JPanel {
    public boolean isSelect;
    public ImagePanel panelA;
    private BufferedImage lastSelected;
    public ImagePanel panelB;
    public ImagePanel panelC;
    private PlotPanel absorptionPanel;
    private PlotPanel emissionPanel;

    public MainPanel() {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(1100, 700));
        init();
    }

    public void init() {
        panelA = new ImagePanel();
        panelB = new ImagePanel();
        panelA.panelB = panelB;
        panelC = new ImagePanel();
        panelC.isPanelC = true;
        JPanel graphicsPanel = new JPanel();
        graphicsPanel.setLayout(new GridLayout(1, 2, 10, 0));
        absorptionPanel = new PlotPanel();
        emissionPanel = new PlotPanel();
        add(panelA);
        add(panelB);
        add(panelC);
        graphicsPanel.add(absorptionPanel);
        graphicsPanel.add(emissionPanel);
        add(graphicsPanel);
        absorptionPanel.setVisible(false);
        emissionPanel.setVisible(false);
    }

    public void loadImageA(File file) {
        BufferedImage scaledImage = panelA.loadImage(file);
    }

    public void copyImageToC() {
        panelC.setImage(panelB.getImage());
    }

    public void copyImageToB() {
        panelB.panelC = panelC;
        panelC.panelB = panelB;
        panelB.setImage(panelC.getImage());
    }

    public void translateImage() {
        if (isSelect) {
            if (panelA.selectedImage != null && lastSelected != panelA.selectedImage) {
                lastSelected = panelA.selectedImage;
                panelB.setImage(panelA.selectedImage);
            }
        }
        else
            panelA.repaint();
    }

    public void setOriginalImage(File file) {
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error");
            return;
        }
        double sideRatio = originalImage.getHeight() / (double) originalImage.getWidth();
    }

    public void showConfig(List<Pair<Integer, Double>> absorptionPoints, List<Pair<Integer, int[]>> emissionPoints) {
        absorptionPanel.absorptionPlot(absorptionPoints);
        emissionPanel.emissionPlot(emissionPoints);
        absorptionPanel.setVisible(true);
        emissionPanel.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
