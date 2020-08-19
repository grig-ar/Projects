package ru.nsu.fit.g16203.grigorovich.model;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JPanel;

import static java.lang.Math.abs;

public class MainPanel extends JPanel {
    private static final double LIVE_BEGIN = 2.0;
    private static final double LIVE_END = 3.3;
    private static final double BIRTH_BEGIN = 2.3;
    private static final double BIRTH_END = 2.9;
    private static final double FST_IMPACT = 1.0;
    private static final double SND_IMPACT = 0.3;
    private static final int IMPACT_SIZE_MINIMUM = 15;
    private final Color COLOR_NEUTRAL_CELL = new Color(231, 196, 169);
    public final Color COLOR_ALIVE_CELL = new Color(153, 255, 153);
    private BufferedImage image = null;
    private Graphics g = null;
    private Point mousePt;
    public boolean impactOn = false;
    public boolean xorOn = false;
    public int size;
    public int rows;
    public int cols;
    public int stroke;
    public double liveBeginValue;
    public double liveEndValue;
    public double birthBeginValue;
    public double birthEndValue;
    public double firstImpactValue;
    public double secondImpactValue;
    public Hex[][] field;
    private Hex[][] newField;
    private int imgWidth;
    private int imgHeight;
    public boolean isRunning;
    private int fieldWidth;
    private int fieldHeight;

    public class Hex {
        int size;
        public Color color;
        HexPoint center;
        double impact = 0.0;

        int[][][] firstNeighborDirections = {
                {{1, 0}, {0, -1}, {-1, -1},
                        {-1, 0}, {-1, 1}, {0, 1}},

                {{1, 0}, {1, -1}, {0, -1},
                        {-1, 0}, {0, 1}, {1, 1}},
        };

        int[][][] secondNeighborDirections = {
                {{0, -2}, {1, -1}, {1, 1},
                        {0, 2}, {-2, 1}, {-2, -1}},

                {{0, -2}, {2, -1}, {2, 1},
                        {0, 2}, {-1, 1}, {-1, -1}},
        };

        public int row;
        public int col;

        Hex(int size, int col, int row, HexPoint center, Color color) {
            this.size = size;
            this.row = row;
            this.col = col;
            this.color = color;
            this.center = center;
        }

        public void setAlive(boolean newFd) {
            if (color == COLOR_ALIVE_CELL)
                return;

            addImpact(newFd);
            spanFill((int) Math.round(center.getX()), (int) Math.round(center.getY()), COLOR_ALIVE_CELL.getRGB(), color.getRGB());
            repaint();

            color = COLOR_ALIVE_CELL;
        }

        public void setNeutral(boolean newFd) {
            if (color == COLOR_NEUTRAL_CELL)
                return;

            subtractImpact(newFd);
            spanFill((int) Math.round(center.getX()), (int) Math.round(center.getY()), COLOR_NEUTRAL_CELL.getRGB(), color.getRGB());
            repaint();

            color = COLOR_NEUTRAL_CELL;
        }

        ArrayList<Hex> getFirstNeighbors(boolean newFd) {
            ArrayList<Hex> neighbors = new ArrayList<>(6);
            int parity = row & 1;
            for (int direction = 0; direction < 6; ++direction) {
                int[] dir = firstNeighborDirections[parity][direction];
                if (col + dir[0] < 0 || row + dir[1] < 0 || col + dir[0] >= cols || row + dir[1] >= rows ||
                        ((col + dir[0] == (cols - 1) || cols == 1) && ((row + dir[1]) & 1) == 1))
                    continue;
                if (newFd)
                    neighbors.add(newField[col + dir[0]][row + dir[1]]);
                else
                    neighbors.add(field[col + dir[0]][row + dir[1]]);
            }
            return neighbors;
        }

        ArrayList<Hex> getSecondNeighbors(boolean newFd) {
            ArrayList<Hex> neighbors = new ArrayList<>(6);
            int parity = row & 1;
            for (int direction = 0; direction < 6; ++direction) {
                int[] secondDir = secondNeighborDirections[parity][direction];
                if (col + secondDir[0] < 0 || row + secondDir[1] < 0 || col + secondDir[0] >= cols || row + secondDir[1] >= rows ||
                        ((col + secondDir[0] == (cols - 1) || cols == 1) && ((row + secondDir[1]) & 1) == 1))
                    continue;
                if (newFd)
                    neighbors.add(newField[col + secondDir[0]][row + secondDir[1]]);
                else
                    neighbors.add(field[col + secondDir[0]][row + secondDir[1]]);
            }
            return neighbors;
        }

        void addImpact(boolean newFd) {
            ArrayList<Hex> firstNeighbors = getFirstNeighbors(newFd);
            ArrayList<Hex> secondNeighbors = getSecondNeighbors(newFd);
            for (Hex hex : firstNeighbors)
                hex.impact = (double) Math.round((hex.impact + FST_IMPACT) * 100d) / 100d;

            for (Hex hex : secondNeighbors)
                hex.impact = (double) Math.round((hex.impact + SND_IMPACT) * 100d) / 100d;
        }

        void subtractImpact(boolean newFd) {
            ArrayList<Hex> firstNeighbors = getFirstNeighbors(newFd);
            ArrayList<Hex> secondNeighbors = getSecondNeighbors(newFd);
            for (Hex hex : firstNeighbors)
                hex.impact = (double) Math.round((hex.impact - FST_IMPACT) * 100d) / 100d;

            for (Hex hex : secondNeighbors)
                hex.impact = (double) Math.round((hex.impact - SND_IMPACT) * 100d) / 100d;
        }

        Color step() {
            Color state = color;
            if (state == COLOR_NEUTRAL_CELL) {
                if (impact >= BIRTH_BEGIN && impact <= BIRTH_END)
                    state = COLOR_ALIVE_CELL;
            } else {
                if (impact < LIVE_BEGIN || impact > LIVE_END)
                    state = COLOR_NEUTRAL_CELL;
            }
            return state;
        }

    }

    private Hex lastHex;

    public MainPanel(int cols, int rows, int stroke, int size, double liveBeginValue, double liveEndValue,
                     double birthBeginValue, double birthEndValue, double firstImpactValue, double secondImpactValue, MainPanel oldPanel) {
        this.cols = cols;
        this.rows = rows;
        this.size = size;
        this.stroke = stroke;
        this.liveBeginValue = liveBeginValue;
        this.liveEndValue = liveEndValue;
        this.birthBeginValue = birthBeginValue;
        this.birthEndValue = birthEndValue;
        this.firstImpactValue = firstImpactValue;
        this.secondImpactValue = secondImpactValue;
        setPreferredSize(new Dimension((int) Math.round(Math.sqrt(3) * size) * cols + 10, 2 * size * rows * 3 / 4 + 30));
        fieldWidth = (int) Math.round(Math.sqrt(3) * size) * cols + 10;
        fieldHeight = 2 * size * rows * 3 / 4 + 30;
        setBackground(Color.WHITE);
        field = new Hex[cols][rows];
        double width = Math.sqrt(3.0) * size;
        int height = 2 * size;
        paintField();
        for (int i = 0; i < this.cols; ++i) {
            for (int j = 0; j < this.rows; ++j) {
                if ((i == (this.cols - 1) || this.cols == 1) && (j & 1) == 1)
                    continue;
                HexPoint center = new HexPoint(size * 0.9 + width * (i % (cols - (j & 1))) + (width / 2) * (j & 1), size + ((height * 3.0 / 4.0) * j));
                field[i][j] = new Hex(size, i, j, center, COLOR_NEUTRAL_CELL);
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isRunning)
                    return;
                int coordX = e.getX();
                int coordY = e.getY();
                if (isNotInBounds(coordX, coordY, image))
                    return;

                int oldValue = image.getRGB(coordX, coordY);
                if (oldValue == Color.WHITE.getRGB() || oldValue == Color.BLACK.getRGB())
                    return;
                Hex testHex = coordsToHexCell(coordX, coordY);
                if (xorOn)
                    if (testHex.color == COLOR_NEUTRAL_CELL)
                        testHex.setAlive(false);
                    else
                        testHex.setNeutral(false);
                else
                    testHex.setAlive(false);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (isRunning)
                    return;
                mousePt = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (isRunning)
                    return;
                Point coord = e.getPoint();
                double dx = coord.getX() - mousePt.x;
                double dy = coord.getY() - mousePt.y;
                if (isNotInBounds((int) Math.round(coord.getX()), (int) Math.round(coord.getY()), image))
                    return;
                if (isNotInBounds((int) (mousePt.x + dx), (int) (mousePt.y + dy), image))
                    return;

                int oldValue = image.getRGB((int) Math.round(coord.getX()), (int) Math.round(coord.getY()));

                if (oldValue == Color.WHITE.getRGB() || oldValue == Color.BLACK.getRGB())
                    return;
                Hex hex = coordsToHexCell(mousePt.x + dx, mousePt.y + dy);

                if (lastHex == null || !lastHex.equals(hex)) {
                    if (xorOn)
                        if (hex.color == COLOR_NEUTRAL_CELL)
                            hex.setAlive(false);
                        else
                            hex.setNeutral(false);
                    else {
                        if (hex == null) {
                            return;
                        }
                        hex.setAlive(false);
                    }

                }
                lastHex = hex;
                if (impactOn)
                    showImpact();
            }
        });

        if (oldPanel != null) {
            paintImmediately(0, 0, getWidth(), getHeight());
            restoreField(oldPanel);
        }

    }

    private boolean isNotInBounds(int x, int y, BufferedImage image) {
        return x >= image.getWidth() ||
                y >= image.getHeight() ||
                x < 0 ||
                y < 0;
    }

    public void showImpact() {
        impactOn = true;
        repaint();
    }

    public void hideImpact() {
        impactOn = false;
        repaint();
    }

    public boolean step() {
        Color tempColor;
        newField = new Hex[cols][rows];
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                if ((i == (cols - 1) || cols == 1) && (j & 1) == 1)
                    continue;
                newField[i][j] = new Hex(size, i, j, field[i][j].center, field[i][j].color);
                newField[i][j].impact = field[i][j].impact;
            }
        }

        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                if ((i == (cols - 1) || cols == 1) && (j & 1) == 1)
                    continue;
                tempColor = field[i][j].step();
                if (tempColor == COLOR_ALIVE_CELL) {
                    newField[i][j].setAlive(true);
                } else {
                    newField[i][j].setNeutral(true);
                }
            }
        }
        if (isFieldSame()) {
            field = newField;
            newField = null;
            return false;
        } else {
            field = newField;
            newField = null;
            return true;
        }
    }

    public void enableXor() {
        xorOn = true;
    }

    public void disableXor() {
        xorOn = false;
    }

    public void setState(LifeState newState) {
        int oldCols = this.cols;
        int oldRows = this.rows;
        this.cols = newState.cols;
        this.rows = newState.rows;
        this.size = newState.size;
        this.stroke = newState.stroke;
        this.liveBeginValue = newState.liveBeginValue;
        this.birthEndValue = newState.liveEndValue;
        this.liveBeginValue = newState.birthBeginValue;
        this.birthEndValue = newState.birthEndValue;
        this.firstImpactValue = newState.firstImpactValue;
        this.secondImpactValue = newState.secondImpactValue;
        this.xorOn = newState.isXor;
        newField = new Hex[cols][rows];
        paintField();
        fieldWidth = (int) Math.round(Math.sqrt(3) * size) * cols + 10;
        fieldHeight = 2 * size * rows * 3 / 4 + 30;
        double width = Math.sqrt(3.0) * size;
        int height = 2 * size;
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                if ((i == (cols - 1) || cols == 1) && (j & 1) == 1)
                    continue;
                HexPoint center = new HexPoint(size * 0.9 + width * (i % (cols - (j & 1))) + (width / 2) * (j & 1), size + ((height * 3.0 / 4.0) * j));
                newField[i][j] = new Hex(size, i, j, center, COLOR_NEUTRAL_CELL);
            }
        }
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                if (i >= oldCols || j >= oldRows)
                    continue;
                if ((i == (oldCols - 1) || oldCols == 1) && (j & 1) == 1)
                    continue;
                if ((i == (cols - 1) || cols == 1) && (j & 1) == 1)
                    continue;
                if (field[i][j].color.getRGB() == COLOR_ALIVE_CELL.getRGB())
                    newField[i][j].setAlive(true);
            }
        }
        field = newField;
        newField = null;

    }

    public boolean isFieldEmpty() {
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                if ((i == (cols - 1) || cols == 1) && (j & 1) == 1)
                    continue;
                if (field[i][j].color == COLOR_ALIVE_CELL)
                    return false;
            }
        }
        return true;
    }

    private boolean isFieldSame() {
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                if ((i == (cols - 1) || cols == 1) && (j & 1) == 1)
                    continue;
                if (field[i][j].color != newField[i][j].color)
                    return false;
            }
        }
        return true;
    }

    public void clearField() {
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                if ((i == (cols - 1) || cols == 1) && (j & 1) == 1)
                    continue;
                field[i][j].setNeutral(false);
            }
        }
    }

    private void restoreField(MainPanel oldPanel) {
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                if ((i == (cols - 1) || cols == 1) && (j & 1) == 1)
                    continue;
                if (oldPanel.field[i][j].color.getRGB() == this.COLOR_ALIVE_CELL.getRGB())
                    field[i][j].setAlive(false);
            }
        }
    }

    private void paintField() {
        int imageWidth = (int) Math.round(Math.sqrt(3) * size * cols) + size;
        int imageHeight = 2 * size * rows * 3 / 4 + 2 * size;
        imgHeight = imageHeight;
        imgWidth = imageWidth;
        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        setPreferredSize(new Dimension(imageWidth, imageHeight));
        g = image.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                image.setRGB(i, j, Color.WHITE.getRGB());
            }
        }
        double width = Math.sqrt(3.0) * size;
        int height = 2 * size;
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                if ((i == (cols - 1) || cols == 1) && (j & 1) == 1)
                    continue;
                HexPoint center = new HexPoint(size + width * i + width / 2d * (j & 1), size + height * 0.75 * j + stroke);
                ArrayList<HexPoint> corners = getHexCorners(center);
                for (int k = 0; k < 6; ++k) {
                    HexPoint corner1, corner2;
                    if ((j & 1) == 0) {
                        corner1 = corners.get(k);
                        corner2 = corners.get((k + 1) % 6);
                    } else {
                        corner1 = corners.get((6 - k) % 6);
                        corner2 = corners.get(5 - k);
                    }
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(stroke));
                    if (stroke == 1) {
                        bDrawLine((int) Math.round(corner1.getX()), (int) Math.round(corner1.getY()), (int) Math.round(corner2.getX()), (int) Math.round(corner2.getY()));
                    } else {
                        g2.drawLine((int) Math.round(corner1.getX()), (int) Math.round(corner1.getY()), (int) Math.round(corner2.getX()), (int) Math.round(corner2.getY()));
                    }
                }
                spanFill((int) Math.round(center.getX()), (int) Math.round(center.getY()), COLOR_NEUTRAL_CELL.getRGB(), Color.WHITE.getRGB());
            }
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (impactOn && size >= IMPACT_SIZE_MINIMUM) {
            BufferedImage impactsImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
            Graphics2D impactsG2 = (Graphics2D) impactsImage.getGraphics();
            impactsG2.drawImage(image, null, null);
            impactsG2.setColor(Color.BLACK);
            BigDecimal cellImpact;
            String text;
            Font font = new Font("Calibri", Font.BOLD, (int) Math.round(size * 0.8));
            impactsG2.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            for (int i = 0; i < cols; ++i) {
                for (int j = 0; j < rows; ++j) {
                    if ((i == (cols - 1) || cols == 1) && (j & 1) == 1)
                        continue;
                    cellImpact = new BigDecimal(field[i][j].impact);
                    cellImpact = cellImpact.setScale(2, BigDecimal.ROUND_HALF_UP);
                    if (field[i][j].impact % 1 == 0)
                        text = new DecimalFormat("#").format(cellImpact);
                    else
                        text = new DecimalFormat("#.#").format(cellImpact);
                    int x = (int) Math.round(field[i][j].center.getX()) - (metrics.stringWidth(text)) / 2;
                    int y = (int) Math.round(field[i][j].center.getY()) - (metrics.getHeight() / 2) + metrics.getAscent();
                    impactsG2.drawString(text, x, y);
                }
            }
            g2.drawImage(impactsImage, null, null);
        } else
            g2.drawImage(image, null, null);
    }

    private ArrayList<HexPoint> getHexCorners(HexPoint center) {
        ArrayList<HexPoint> corners = new ArrayList<>(6);
        double width = Math.sqrt(3) * size;
        corners.add(new HexPoint(center.getX(), center.getY() - size));
        corners.add(new HexPoint(center.getX() + width / 2d, center.getY() - size / 2d));
        corners.add(new HexPoint(center.getX() + width / 2d, center.getY() + size / 2d));
        corners.add(new HexPoint(center.getX(), center.getY() + size));
        corners.add(new HexPoint(center.getX() - width / 2d, center.getY() + size / 2d));
        corners.add(new HexPoint(center.getX() - width / 2d, center.getY() - size / 2d));
        return corners;
    }

    private void setPixel(int x, int y) {
        image.setRGB(x, y, Color.BLACK.getRGB());
    }

    private void bDrawLine(int x0, int y0, int x1, int y1) {
        int x = x0;
        int y = y0;
        int dx = x1 - x0;
        int dy = y1 - y0;
        int err;
        int incrY = Integer.compare(dy, 0);
        int incrX = Integer.compare(dx, 0);
        dx = abs(dx);
        dy = abs(dy);
        if (dx > dy) {
            err = -dx;
            for (int i = 0; i < dx; ++i) {
                setPixel(x, y);
                x += incrX;
                err += 2 * dy;
                if (err > 0) {
                    err -= 2 * dx;
                    y += incrY;
                }
            }
        } else {
            err = -dy;
            for (int i = 0; i < dy; ++i) {
                setPixel(x, y);
                y += incrY;
                err += 2 * dx;
                if (err > 0) {
                    x += incrX;
                    err -= 2 * dy;
                }
            }
        }
    }

    private Hex coordsToHexCell(double x, double y) {
        Hex cell;
        Hex result = null;
        double distance = Double.MAX_VALUE;
        double horizontalSpacing = Math.sqrt(3.0) * size;
        double verticalSpacing = 2 * size * 3 / 4.0;
        int colGuess = (int) Math.round(x / horizontalSpacing);
        int rowGuess = (int) Math.round(y / verticalSpacing);
        for (int c = colGuess - 1; c < colGuess + 1; c++) {
            for (int r = rowGuess - 1; r < rowGuess + 1; r++) {
                if ((c == (cols - 1) || cols == 1) && (r & 1) == 1)
                    continue;
                if (r < 0 || r >= rows || c < 0 || c >= cols)
                    continue;
                cell = field[c][r];
                final double dx = x - cell.center.getX();
                final double dy = y - cell.center.getY();
                final double newDistance = Math.sqrt(dx * dx + dy * dy);

                if (newDistance < distance) {
                    distance = newDistance;
                    result = cell;
                }
            }
        }

        return result;
    }

    private boolean isOldColor(int x, int y, int oldColor) {
        return image.getRGB(x, y) == oldColor;
    }

    private void spanFill(int x, int y, int newColor, int oldColor) {
        if (oldColor == newColor)
            return;

        int x1, y1;
        boolean spanAbove, spanBelow;
        Stack<SpanLine> stack = new Stack<>();
        stack.push(new SpanLine(x, y));
        while (!stack.isEmpty()) {
            SpanLine tempLine = stack.pop();
            x1 = tempLine.x;
            y1 = tempLine.y;
            while (x1 >= 0 && isOldColor(x1, y1, oldColor))
                x1--;
            x1++;
            spanAbove = spanBelow = false;
            while (x1 < imgWidth && isOldColor(x1, y1, oldColor)) {
                image.setRGB(x1, y1, newColor);
                if (!spanAbove && y1 > 0 && isOldColor(x1, y1 - 1, oldColor)) {
                    stack.push(new SpanLine(x1, y1 - 1));
                    spanAbove = true;
                } else if (spanAbove && !isOldColor(x1, y1 - 1, oldColor)) {
                    spanAbove = false;
                }
                if (!spanBelow && y1 < (imgHeight - 1) && isOldColor(x1, y1 + 1, oldColor)) {
                    stack.push(new SpanLine(x1, y1 + 1));
                    spanBelow = true;
                } else if (spanBelow && y1 < (imgHeight - 1) && !isOldColor(x1, y1 + 1, oldColor)) {
                    spanBelow = false;
                }
                x1++;
            }
        }
    }
}
