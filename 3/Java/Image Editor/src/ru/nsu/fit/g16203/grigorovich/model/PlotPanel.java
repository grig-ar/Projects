package ru.nsu.fit.g16203.grigorovich.model;

import ru.nsu.fit.g16203.grigorovich.utilityFiles.CustomBorder;
import ru.nsu.fit.g16203.grigorovich.utilityFiles.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlotPanel extends JPanel {
    private static final int WIDTH_PANEL = 500;
    private static final int HEIGHT_PANEL = 175;
    private static final int HEIGHT_PLOT = HEIGHT_PANEL - 1;
    private static final int SHIFT_PLOT = 2;
    private static final int LENGTH_BORDER = 2;
    private static final int GAP_BORDER = 15;
    private static final int SPACING_BORDER = 3;
    private static final double NORMALIZATION_COEFFICIENT = 261d;
    private static final Color COLOR_BORDER = Color.BLACK;
    private boolean isAbsorption = false;
    private boolean isEmission = false;

    private List<Pair<Integer, Double>> absorptionPoints = new ArrayList<>();
    private List<Pair<Integer, int[]>> emissionPoints = new ArrayList<>();

    PlotPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(WIDTH_PANEL, HEIGHT_PANEL));
        CustomBorder customBorder = new CustomBorder(COLOR_BORDER, GAP_BORDER, LENGTH_BORDER, SPACING_BORDER);
        setBorder(customBorder);
    }

    void absorptionPlot(List<Pair<Integer, Double>> absorptionPoints) {
        this.absorptionPoints = absorptionPoints;
        isAbsorption = true;
        repaint();

    }

    void emissionPlot(List<Pair<Integer, int[]>> emissionPoints) {
        this.emissionPoints = emissionPoints;
        isEmission = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (isAbsorption) {
            for (int i = 0; i < absorptionPoints.size() - 1; ++i) {
                Pair<Integer, Double> coords1 = absorptionPoints.get(i);
                Pair<Integer, Double> coords2 = absorptionPoints.get(i + 1);
                int x1 = (int) Math.round(coords1.t / 100d * WIDTH_PANEL);
                int y1 = (int) Math.round((1 - coords1.u) * (HEIGHT_PANEL - 1));
                int x2 = (int) Math.round(coords2.t / 100d * WIDTH_PANEL);
                int y2 = (int) Math.round((1 - coords2.u) * (HEIGHT_PANEL - 1));
                g2.drawLine(x1, y1, x2, y2);
            }
            return;
        }
        if (isEmission) {
            for (int i = 0; i < emissionPoints.size() - 1; ++i) {
                Pair<Integer, int[]> coords1 = emissionPoints.get(i);
                Pair<Integer, int[]> coords2 = emissionPoints.get(i + 1);
                int x1 = (int) Math.round(coords1.t / 100d * WIDTH_PANEL);
                int x2 = (int) Math.round(coords2.t / 100d * WIDTH_PANEL);
                int y1Red = (int) Math.round((1 - coords1.u[0] / NORMALIZATION_COEFFICIENT) * HEIGHT_PLOT);
                int y2Red = (int) Math.round((1 - coords2.u[0] / NORMALIZATION_COEFFICIENT) * HEIGHT_PLOT);
                int y1Green = (int) Math.round((1 - coords1.u[1] / NORMALIZATION_COEFFICIENT) * HEIGHT_PLOT) - SHIFT_PLOT;
                int y2Green = (int) Math.round((1 - coords2.u[1] / NORMALIZATION_COEFFICIENT) * HEIGHT_PLOT) - SHIFT_PLOT;
                int y1Blue = (int) Math.round((1 - coords1.u[2] / NORMALIZATION_COEFFICIENT) * HEIGHT_PLOT) - 2 * SHIFT_PLOT;
                int y2Blue = (int) Math.round((1 - coords2.u[2] / NORMALIZATION_COEFFICIENT) * HEIGHT_PLOT) - 2 * SHIFT_PLOT;
                g2.setColor(Color.RED);
                g2.drawLine(x1, y1Red, x2, y2Red);
                g2.setColor(Color.GREEN);
                g2.drawLine(x1, y1Green, x2, y2Green);
                g2.setColor(Color.BLUE);
                g2.drawLine(x1, y1Blue, x2, y2Blue);

            }
        }
    }
}
