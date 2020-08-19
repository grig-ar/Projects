package nsu.fit.g16203.grigorovich.model;

import nsu.fit.g16203.grigorovich.utilityFiles.CIELab;
import nsu.fit.g16203.grigorovich.utilityFiles.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlotPanel extends JPanel {
    private static final double PRECISION = 1000d;
    private static final int WIDTH_DEFAULT = 600;
    private static final int HEIGHT_DEFAULT = 600;
    private static final int WIDTH_LEGEND = WIDTH_DEFAULT;
    private static final int HEIGHT_IMAGE_LEGEND = 100;
    private static final int HEIGHT_LEGEND = 130;
    private static final int INTERPOLATION_TYPE_WITHOUT_INTERPOLATION = -1;
    private static final int INTERPOLATION_TYPE_RGB = 1;
    private static final int INTERPOLATION_TYPE_LAB = 2;
    private static final Color[] PALETTE_DEFAULT = {
            new Color(76, 0, 153), Color.BLUE,
            new Color(0, 90, 255), new Color(51, 153, 255),
            new Color(51, 255, 255), new Color(153, 255, 204),
            new Color(51, 255, 51), new Color(153, 255, 51),
            Color.ORANGE, Color.YELLOW
    };
    private int xGrid, yGrid;
    private double xMin, xMax, yMin, yMax, zMin, zMax, dx, dy, dz;
    private int levelAmount;
    private Color isolineColor;
    private Color[] palette;
    private double[][] gridValues;
    private ArrayList<PlotRectangle> grid;
    ArrayList<Double> legendBorder = null;
    private BufferedImage image;
    private CIELab cieLab = new CIELab();
    public int interpolationType = INTERPOLATION_TYPE_WITHOUT_INTERPOLATION;
    private boolean isLegend;
    private boolean isGrid = false;
    private boolean isIsolines = false;
    private PlotPanel legend = null;
    private JLabel status = null;

    public PlotPanel(int xGrid, int yGrid, double xMin, double xMax, double yMin,
                     double yMax, int levelAmount, Color[] palette, Color isolineColor, boolean isLegend) {

        this.isLegend = isLegend;
        if (this.isLegend) {
            image = new BufferedImage(WIDTH_LEGEND, HEIGHT_IMAGE_LEGEND, BufferedImage.TYPE_INT_ARGB);
            setPreferredSize(new Dimension(WIDTH_LEGEND, HEIGHT_LEGEND));
        } else {
            image = new BufferedImage(WIDTH_DEFAULT + 1, HEIGHT_DEFAULT + 1, BufferedImage.TYPE_INT_ARGB);
            setPreferredSize(new Dimension(WIDTH_DEFAULT + 1, HEIGHT_DEFAULT + 1));
        }
        this.xGrid = xGrid;
        this.yGrid = yGrid;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.palette = (palette == null) ? PALETTE_DEFAULT : palette;
        this.isolineColor = isolineColor;
        this.levelAmount = levelAmount;
        calcParameters();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        legendBorder = new ArrayList<>(levelAmount);
        grid = new ArrayList<>(xGrid * yGrid);
        calcGridValues();
        if (!isLegend) {
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    super.mouseMoved(e);
                    Point current = e.getPoint();
                    double xCoord = current.getX();
                    double yCoord = current.getY();
                    double x = (xMin + xCoord * dx);
                    double y = (yMin + yCoord * dy);
                    double z = Math.round(getFunctionValue(x, y) * PRECISION) / PRECISION;
//                    double xTest = -3.6;
//                    double yTest = -6.0;
//                    for (PlotRectangle rect : grid) {
//                        if (rect.pointInside(xTest, yTest))
//                            System.out.println(rect.toString());
//                    }
                    //double x = Math.round((xMin + xCoord * dx) * PRECISION) / PRECISION;
                    //double y = Math.round((yMin + yCoord * dy) * PRECISION) / PRECISION;
                    //double z = Math.round(getFunctionValue(x, y) * PRECISION) / PRECISION;
                    double lerpZ = -1336;
                    for (PlotRectangle rect : grid) {
                        if (rect.pointInside(x, y))
                            lerpZ = rect.getValue(x, y);
                    }
                    lerpZ = Math.round(lerpZ * PRECISION) / PRECISION;
                    if (lerpZ < -10) {
                        System.out.println("kek");
                    }
                    String text = "X = " + String.valueOf(Math.round(x * PRECISION) / PRECISION) + " Y = " + String.valueOf(Math.round(y * PRECISION) / PRECISION)
                            + " Z = " + String.valueOf(z) + " lerpZ = " + String.valueOf(lerpZ);
                    status.setText(text);
                }
            });
        }
    }

    private void calcParameters() {
        if (isLegend) {
            dx = new BigDecimal((Math.abs(xMin) + Math.abs(xMax)) / (double) WIDTH_DEFAULT).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            dy = new BigDecimal((Math.abs(yMin) + Math.abs(yMax)) / (double) HEIGHT_IMAGE_LEGEND).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            dx = new BigDecimal((Math.abs(xMin) + Math.abs(xMax)) / (double) WIDTH_DEFAULT).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            dy = new BigDecimal((Math.abs(yMin) + Math.abs(yMax)) / (double) HEIGHT_DEFAULT).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        calcMaxAndMin();
        dz = new BigDecimal((Math.abs(zMin) + Math.abs(zMax)) / (double) levelAmount).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

//    public void loadPlot() {
//        double xCoord, yCoord, zCoord;
//        for (int y = 0; y < image.getHeight(); ++y) {
//            yCoord = new BigDecimal(yMin + y * dy).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//            for (int x = 0; x < image.getWidth(); ++x) {
//                xCoord = new BigDecimal(xMin + x * dx).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                zCoord = new BigDecimal(getFunctionValue(xCoord, yCoord)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//                Color currentColor = null;
//                for (int c = 0; c < levelAmount; ++c) {
//                    if (zCoord <= new BigDecimal((zMin + (c + 1) * dz)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) {
//                        currentColor = palette[c];
//                        break;
//                    }
//                }
//                assert currentColor != null;
//                image.setRGB(x, y, currentColor.getRGB());
//            }
//        }
//        repaint();
//    }

    public void setColorParameters(Color[] newPalette, Color isolineColor, int xGrid, int yGrid) {
        this.palette = newPalette;
        this.levelAmount = newPalette.length;
        this.isolineColor = isolineColor;
        this.xGrid = xGrid;
        this.yGrid = yGrid;
        interpolationType = INTERPOLATION_TYPE_WITHOUT_INTERPOLATION;

        calcParameters();
        if (!isLegend) {
            legendBorder = new ArrayList<>(levelAmount);
            calcGridValues();
        }
        paintPlot();
        //loadPlot();
    }

    public void setFieldParameters(int xGrid, int yGrid, double xMin, double xMax, double yMin, double yMax) {
        this.xGrid = xGrid;
        this.yGrid = yGrid;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        calcParameters();
        calcGridValues();
        paintPlot();
    }

    public void paintPlot() {
        if (!isLegend)
            legendBorder.clear();
        long start = System.nanoTime();
        double xCoord, yCoord, zCoord;
        Map<Color, Double> centers = new HashMap<>();
        Map<Color, Double> bounds = new HashMap<>();
        for (int i = 0; i < levelAmount; ++i) {
            centers.put(palette[i], (zMin + dz * (i + 0.5)));
            if (!isLegend)
                legendBorder.add((zMin + dz * i));
        }

        for (int y = 0; y < image.getHeight(); ++y) {
            yCoord = yMin + y * dy;
            for (int x = 0; x < image.getWidth(); ++x) {
                xCoord = xMin + x * dx;
                if (isLegend)
                    zCoord = getLegendValue(xCoord);
                else {
                    zCoord = getFunctionValue(xCoord, yCoord);
                    //zCoord = getLegendValue(xCoord);
                }
                Color prevColor = null, currentColor = null, nextColor = null;
                for (int c = 0; c < levelAmount; ++c) {
                    double current = Math.round((zMin + (c + 1) * dz) * 100) / 100d;
                    if (zCoord <= current) {
                        //if (zCoord <= (zMin + (c + 1) * dz)) {
                        currentColor = palette[c];
                        //legendBorder.add(zCoord);
                        if (zCoord < centers.get(currentColor)) {
                            prevColor = palette[(c == 0) ? 0 : c - 1];
                            nextColor = palette[c];
                        } else {
                            prevColor = palette[c];
                            nextColor = palette[(c == levelAmount - 1) ? levelAmount - 1 : c + 1];
                        }
                        break;
                    }
                }

                if (interpolationType == INTERPOLATION_TYPE_WITHOUT_INTERPOLATION) {
                    assert currentColor != null;
                    try {
                        image.setRGB(x, y, currentColor.getRGB());
                    } catch (Exception ex) {
                        System.out.println("keke");
                    }
                    continue;
                }

                if (prevColor == nextColor)
                    currentColor = prevColor;
                else {
                    double firstPoint = centers.get(prevColor);
                    double secondPoint = centers.get(nextColor);
                    assert prevColor != null;
                    assert nextColor != null;
                    if (interpolationType == INTERPOLATION_TYPE_LAB) {
                        float[] floatFirst = {prevColor.getRed() / 255f, prevColor.getGreen() / 255f, prevColor.getBlue() / 255f};
                        float[] floatSecond = {nextColor.getRed() / 255f, nextColor.getGreen() / 255f, nextColor.getBlue() / 255f};
                        float[] firstLAB = cieLab.fromRGB(floatFirst);
                        float[] secondLAB = cieLab.fromRGB(floatSecond);
                        float[] current = lerpLAB(firstLAB, secondLAB, (float) ((zCoord - firstPoint) / (secondPoint - firstPoint)));
                        float[] currentRGB = cieLab.toRGB(current);
                        currentColor = new Color(currentRGB[0], currentRGB[1], currentRGB[2]);
                    }
                    if (interpolationType == INTERPOLATION_TYPE_RGB) {
                        currentColor = lerpRGB(prevColor, nextColor, (float) ((zCoord - firstPoint) / (secondPoint - firstPoint)));
                    }
                }
                assert currentColor != null;
                image.setRGB(x, y, currentColor.getRGB());
            }
        }
        long end = System.nanoTime();

        System.out.println(end - start);
        setLegendValues();
        repaint();
    }

    private void calcMaxAndMin() {
        double xCoord, yCoord;
        ArrayList<Double> values = new ArrayList<>();
        for (int y = 0; y < image.getHeight(); ++y) {
            yCoord = yMin + y * dy;
            for (int x = 0; x < image.getWidth(); ++x) {
                xCoord = xMin + x * dx;
                if (isLegend)
                    values.add(getLegendValue(xCoord));
                else
                    values.add(getFunctionValue(xCoord, yCoord));
            }
        }
        zMax = new BigDecimal(Collections.max(values)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        zMin = new BigDecimal(Collections.min(values)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static Color lerpRGB(Color a, Color b, float t) {
        return new Color(Math.round(a.getRed() + (b.getRed() - a.getRed()) * t),
                Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * t),
                Math.round(a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t));
    }

    private static float[] lerpLAB(float[] a, float[] b, float t) {
        return new float[]{a[0] + (b[0] - a[0]) * t, a[1] + (b[1] - a[1]) * t, a[2] + (b[2] - a[2]) * t};
    }

    public void connectStatusLabel(JLabel status) {
        this.status = status;
    }

    public void connectLegend(PlotPanel legend) {
        this.legend = legend;
    }

    private void setLegendValues() {
        if (!isLegend)
            legend.legendBorder = legendBorder;
    }

    private void calcGridValues() {
        grid.clear();
        gridValues = new double[(xGrid + 1)][(yGrid + 1)];
        int xGridStep = Math.round(WIDTH_DEFAULT / (float) xGrid);
        int yGridStep = Math.round(HEIGHT_DEFAULT / (float) yGrid);
        double funcXGridStep, funcYGridSTep;
        funcXGridStep = dx * xGridStep;
        funcYGridSTep = dy * yGridStep;
        double yVal, xVal;
        Point2D.Double ul, ur, bl, br;
        double z0, z1, z2, z3;
        ArrayList<Point2D.Double> points;
        for (int y = 0; y < yGrid; ++y) {
            yVal = yMin + y * funcYGridSTep;
            for (int x = 0; x < xGrid; ++x) {
                points = new ArrayList<>(4);
                xVal = xMin + x * funcXGridStep;
                ul = new Point2D.Double(Math.round(xVal * PRECISION) / PRECISION, Math.round(yVal * PRECISION) / PRECISION);
                ur = new Point2D.Double(Math.round((xVal + funcXGridStep) * PRECISION) / PRECISION, Math.round(yVal * PRECISION) / PRECISION);
                bl = new Point2D.Double(Math.round(xVal * PRECISION) / PRECISION, Math.round((yVal + funcYGridSTep) * PRECISION) / PRECISION);
                br = new Point2D.Double(Math.round((xVal + funcXGridStep) * PRECISION) / PRECISION, Math.round((yVal + funcYGridSTep) * PRECISION) / PRECISION);
                points.add(ul);
                points.add(ur);
                points.add(bl);
                points.add(br);
                z0 = getFunctionValue(ul.getX(), ul.getY());
                z1 = getFunctionValue(ur.getX(), ur.getY());
                z2 = getFunctionValue(bl.getX(), bl.getY());
                z3 = getFunctionValue(br.getX(), br.getY());
                grid.add(new PlotRectangle(new Pair<>(points, new double[]{z0, z1, z2, z3})));
            }
        }
//        for (int y = 0; y < yGrid + 1; ++y) {
//            yVal = yMin + y * funcYGridSTep;
//            for (int x = 0; x < xGrid + 1; ++x) {
//                xVal = xMin + x * funcXGridStep;
//                gridValues[x][y] = getFunctionValue(xVal, yVal);
//            }
//        }
    }


    private double getFunctionValue(double x, double y) {
        //sin(cos(x))*sin(cos(y))
        return Math.sin(Math.cos(x)) * Math.sin(Math.cos(y));

        //cos(sin(sin(x)))*sin(sin(cos(y)))
        //return Math.cos(Math.sin(Math.sin(x))) * Math.sin(Math.sin(Math.cos(y)));

        //sin(sqrt(x^2+y^2))
        //return Math.sin(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));

        //return x;

        //return y;
    }

//    private double[] getVertex(double x, double y) {
//
//    }
//
//    private double getLerpFunctionValue(double x, double y) {
//        Bilinear
//    }

    private double getLegendValue(double x) {
        return x;
    }

    public void switchGridMode() {
        if (!isLegend) {
            isGrid = !isGrid;
            repaint();
        }
    }

    public void switchIsolinesMode() {
        if (!isLegend) {
            isIsolines = !isIsolines;
            repaint();
        }
    }

//    private void setLegendValues() {
//        if (isLegend) {
//            legendBorder.clear();
//            for (int i = 0; i < levelAmount; ++i) {
//                double current = Math.round((zMin + (i + 1) * dz) * 100) / 100d;
//                legendBorder.add(current);
//            }
//        }
//    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Stroke defaultStroke = g2.getStroke();
        if (!isGrid)
            g2.drawImage(image, null, null);
        if (isGrid && !isLegend) {
            g2.drawImage(image, null, null);
            g2.setXORMode(Color.WHITE);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{5f, 5f}, 0f));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int xGridStep = Math.round(WIDTH_DEFAULT / (float) xGrid);
            int yGridStep = Math.round(HEIGHT_DEFAULT / (float) yGrid);
//            g2.drawLine(xGridStep, 0, xGridStep, image.getHeight());
//
//            //g2.dispose();
            for (int i = 1; i < xGrid; ++i) {
                g2.drawLine(xGridStep * i, 0, xGridStep * i, image.getHeight());
            }
            for (int j = 1; j < yGrid; ++j) {
                g2.drawLine(0, yGridStep * j, image.getWidth(), yGridStep * j);
            }
            //g2.drawImage(image, null, null);
            g2.setPaintMode();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        if (isIsolines && !isLegend) {
            g2.setColor(isolineColor);
            g2.setStroke(defaultStroke);
//            if (grid.size() > 60) {
//                ArrayList<Point2D.Double> rootsTest = grid.get(132).findRoots(0.6);
//                //g2.drawOval((int) Math.round((rootsTest.get(0).getX() - xMin) / dx), (int) Math.round((rootsTest.get(0).getY() - yMin) / dy), 4, 4);
//                //g2.drawOval((int) Math.round((rootsTest.get(1).getX() - xMin) / dx), (int) Math.round((rootsTest.get(1).getY() - yMin) / dy), 4, 4);
//                g2.drawLine((int) Math.round((rootsTest.get(0).getX() - xMin) / dx), (int) Math.round((rootsTest.get(0).getY() - yMin) / dy),
//                        (int) Math.round((rootsTest.get(1).getX() - xMin) / dx), (int) Math.round((rootsTest.get(1).getY() - yMin) / dy));
//            }
            for (int i = 0; i < legendBorder.size(); ++i) {
                for (PlotRectangle rect : grid) {
                    ArrayList<Point2D.Double> roots = rect.findRoots(legendBorder.get(i));

                    if (roots.size() == 2) {
                        //g2.drawOval((int) Math.round((roots.get(0).getX() - xMin) / dx), (int) Math.round((roots.get(0).getY() - yMin) / dy), 4, 4);
                        //g2.drawOval((int) Math.round((roots.get(1).getX() - xMin) / dx), (int) Math.round((roots.get(1).getY() - yMin) / dy), 4, 4);
                        g2.drawLine((int) Math.round((roots.get(0).getX() - xMin) / dx), (int) Math.round((roots.get(0).getY() - yMin) / dy),
                                (int) Math.round((roots.get(1).getX() - xMin) / dx), (int) Math.round((roots.get(1).getY() - yMin) / dy));
                    }

                    if (roots.size() == 4) {
                        ArrayList<Pair<Point2D.Double, Point2D.Double>> grouped = rect.groupRoots(roots, legendBorder.get(i));
                        g2.drawLine((int) Math.round((grouped.get(0).t.getX() - xMin) / dx), (int) Math.round((grouped.get(0).t.getY() - yMin) / dy),
                                (int) Math.round((grouped.get(0).u.getX() - xMin) / dx), (int) Math.round((grouped.get(0).u.getY() - yMin) / dy));
                        g2.drawLine((int) Math.round((grouped.get(1).t.getX() - xMin) / dx), (int) Math.round((grouped.get(1).t.getY() - yMin) / dy),
                                (int) Math.round((grouped.get(1).u.getX() - xMin) / dx), (int) Math.round((grouped.get(1).u.getY() - yMin) / dy));
                    }

                }
            }
        }

        if (isLegend) {
            double val;
            Font font = new Font("Calibri", Font.BOLD, 12);
            FontMetrics metrics = g.getFontMetrics(font);
            g2.setColor(Color.BLACK);
            g2.setFont(font);
            for (int i = 1; i < levelAmount; ++i) {
                val = Math.round(legendBorder.get(i) * 10) / 10d;
                //val = new BigDecimal(legendBorder.get(i)).setScale(1, BigDecimal.ROUND_HALF_UP);
                String text = String.valueOf(val);
                g2.drawString(text, WIDTH_LEGEND / levelAmount * (i) - (metrics.stringWidth(text)) / 2, HEIGHT_IMAGE_LEGEND + 20);
            }
        }
    }
}
