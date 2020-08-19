package nsu.fit.g16203.grigorovich.view;


import nsu.fit.g16203.grigorovich.controller.MainFrame;
import nsu.fit.g16203.grigorovich.model.FieldState;
import nsu.fit.g16203.grigorovich.model.InterpolationSettingsPanel;
import nsu.fit.g16203.grigorovich.model.PlotPanel;
import nsu.fit.g16203.grigorovich.model.FieldSettingsPanel;
import ru.nsu.fit.g16203.grigorovich.utilityFiles.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

public class InitMainWindow extends MainFrame {
    private static final int LEVEL_DEFAULT = 10;
    private static final int GRID_X_DEFAULT = 5;
    private static final int GRID_Y_DEFAULT = 5;
    private static final double[] DEFAULT_SIZE = {-6d, 6d, -6d, 6d};
    private static final int INTERPOLATION_TYPE_WITHOUT_INTERPOLATION = -1;
    private static final int INTERPOLATION_TYPE_RGB = 1;
    private static final int INTERPOLATION_TYPE_LAB = 2;
    private JPanel mainPanel = new JPanel();
    private PlotPanel plotPanel;
    private PlotPanel plotLegend;
    private FieldSettingsPanel fieldSettingsPanel = new FieldSettingsPanel(DEFAULT_SIZE, GRID_X_DEFAULT, GRID_Y_DEFAULT);
    private InterpolationSettingsPanel interpolationSettingsPanel = new InterpolationSettingsPanel();
    private static final Pattern PATTERN_COMMENT = Pattern.compile(
            "^\\s*$|^(\\s*/{2}).*");

    private static boolean validate(final String ip) {
        return !(PATTERN_COMMENT.matcher(ip).matches());
    }

    private Map<String, Object[]> savedSettings;

    public InitMainWindow() {
        super(700, 880, "FIT_16203_Grigorovich_Filter");
        setBounds(100, 100, 700, 880);
        //setContentPane();
        FileUtils.getDataDirectory();
        savedSettings = new HashMap<>();
        try {
//            addComponentListener(new ComponentAdapter() {
//                @Override
//                public void componentResized(ComponentEvent e) {
//                    super.componentResized(e);
//                    doSize();
//                }
//            });
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/Open", "Open file", KeyEvent.VK_O, "Open.png", "onOpen");
            addToolBarButton("File/Open");
            addToolBarSeparator();
            addMenuSeparator("File");
            addMenuItem("File/Exit", "Exit", KeyEvent.VK_X, "Exit.png", "onExit");
            addSubMenu("Edit", KeyEvent.VK_E);
            addMenuItem("Edit/Settings", "Change parameters", KeyEvent.VK_S, "Settings.png", "onSettings");
            addToolBarButton("Edit/Settings");
            addMenuItem("Edit/Interpolation", "Interpolate colors", KeyEvent.VK_I, "Interpolation.png", "onInterpolation");
            addToolBarButton("Edit/Interpolation");
            addMenuItem("Edit/Grid", "Grid", KeyEvent.VK_G, "Grid.png", "onGrid");
            addToolBarButton("Edit/Grid");
            addMenuItem("Edit/Map", "Show colored map", KeyEvent.VK_M, "Map.png", "onMap");
            addToolBarButton("Edit/Map");
            addMenuItem("Edit/Isoline", "Draw isolines", KeyEvent.VK_D, "Isoline.png", "onIsoline");
            addToolBarButton("Edit/Isoline");
            addMenuItem("Edit/Erase", "Erase user isolines", KeyEvent.VK_E, "Erase.png", "onErase");
            addToolBarButton("Edit/Erase");
            addMenuItem("Edit/ControlPoints", "Show control points", KeyEvent.VK_C, "ControlPoints.png", "onControlPoints");
            addToolBarButton("Edit/ControlPoints");
            addToolBarSeparator();
            addSubMenu("View", KeyEvent.VK_V);
            addMenuItem("View/ToolBar", "Add/Remove ToolBar", KeyEvent.VK_T, null, "onToolBar");
            addMenuItem("View/StatusBar", "Add/Remove StatusBar", KeyEvent.VK_S, null, "onStatusBar");
            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Info about the author and program", KeyEvent.VK_A, "Help.png", "onAbout");
            addToolBarButton("Help/About...");
            //setLayout(new FlowLayout());

            mainPanel.setLayout(new FlowLayout());
            plotPanel = new PlotPanel(GRID_X_DEFAULT, GRID_Y_DEFAULT, DEFAULT_SIZE[0], DEFAULT_SIZE[1], DEFAULT_SIZE[2], DEFAULT_SIZE[3], LEVEL_DEFAULT, null, Color.BLACK, false);
            plotLegend = new PlotPanel(GRID_X_DEFAULT, GRID_Y_DEFAULT, DEFAULT_SIZE[0], DEFAULT_SIZE[1], DEFAULT_SIZE[2], DEFAULT_SIZE[3], LEVEL_DEFAULT, null, Color.BLACK, true);
            //mainPanel.add(getToolBar());
            mainPanel.add(plotPanel);
            mainPanel.add(plotLegend);

            add(mainPanel);
            //setContentPane(mainPanel);
            plotPanel.connectLegend(plotLegend);
            plotPanel.connectStatusLabel(statusLabel);
            //setLayout(new FlowLayout());
            //add(plotPanel);
            //add(plotLegend);
            plotPanel.paintPlot();
            plotLegend.paintPlot();
            //pack();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void onOpen() {
        File file = FileUtils.getOpenFileName(this, "txt", "Text files");
        if (file == null) {
            return;
        }

        try {
            FileReader reader = new FileReader(file);
            Scanner scan = new Scanner(reader);
            int i = 0;
            int xGrid = 0;
            int yGrid = 0;
            int levelAmount = 0;
            Color[] paletteColors = null;
            Color isolineColor = null;
            while (scan.hasNextLine()) {
                String temp = scan.nextLine();
                if (validate(temp)) {
                    String[] values = temp.split("//");
                    try {
                        if (!temp.contains("//") && temp.replaceAll("(\\s+|\\d+)", "").length() > 2) {
                            throw new RuntimeException("Corrupted file!");
                        }
                    } catch (Exception ex) {
                        System.out.println("Corrupted file!");
                        return;
                    }
                    values = values[0].split(" ");
                    try {
                        if (values.length == 2 && i == 0) {
                            xGrid = Integer.parseInt(values[0]);
                            yGrid = Integer.parseInt(values[1]);
                            if (xGrid <= 0 || yGrid <= 0) {
                                System.out.println("Corrupted file!");
                                return;
                            }
                            ++i;
                            continue;
                        }
                        if (values.length == 1 && i == 1) {
                            levelAmount = Integer.parseInt(values[0]);
                            if (levelAmount <= 0) {
                                System.out.println("Corrupted file!");
                                return;
                            }
                            paletteColors = new Color[levelAmount];
                            ++i;
                            continue;
                        }
                        if (values.length == 3 && i > 1 && i < (2 + levelAmount)) {
                            paletteColors[i - 2] = new Color(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                            ++i;
                            continue;
                        }
                        if (values.length == 3 && i == (2 + levelAmount)) {
                            isolineColor = new Color(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                            ++i;
                            continue;
                        }
                        if (i > (2 + levelAmount)) {
                            System.out.println("Corrupted file!");
                            return;
                        }
                    } catch (Exception ex) {
                        System.out.println("Corrupted file!");
                        return;
                    }
                }
            }
            interpolationSettingsPanel.setInterpolationType(INTERPOLATION_TYPE_WITHOUT_INTERPOLATION);
            plotPanel.setColorParameters(paletteColors, isolineColor, xGrid, yGrid);
            plotLegend.setColorParameters(paletteColors, isolineColor, xGrid, yGrid);
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void doSize() {
        int width = (int) (mainPanel.getWidth() * 0.8);
        int height = (int) (mainPanel.getHeight() * 0.9);

        plotPanel.setBounds(0, 0, width, height);
        plotLegend.setBounds(0, height, mainPanel.getWidth() - width, mainPanel.getHeight() - height);

    }

    public void onMap() {

    }

    public void onControlPoints() {

    }

    public void onIsoline() {
        plotPanel.switchIsolinesMode();
    }

    public void onErase() {

    }

    public void onGrid() {
        plotPanel.switchGridMode();
    }

    public void onInterpolation() {
        //InterpolationSettingsPanel interpolationSettingsPanel = new InterpolationSettingsPanel();
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(interpolationSettingsPanel);
        optionPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
        int dialogResult = JOptionPane.showConfirmDialog(this, interpolationSettingsPanel, "Interpolation type", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (dialogResult == JOptionPane.OK_OPTION) {
            plotPanel.interpolationType = interpolationSettingsPanel.getInterpolationType();
            plotPanel.paintPlot();
            plotLegend.interpolationType = interpolationSettingsPanel.getInterpolationType();
            plotLegend.paintPlot();
        }
    }

    private boolean validateFieldState(FieldState fieldState) {
        return (fieldState.getXGrid() >= 0 && fieldState.getYGrid() >= 0 &&
                fieldState.getXMin() < fieldState.getXMax() &&
                fieldState.getYMin() < fieldState.getYMax());
    }

    public void onSettings() {
        //FieldSettingsPanel fieldSettingsPanel = new FieldSettingsPanel();
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(fieldSettingsPanel);
        optionPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
        int dialogResult = JOptionPane.showConfirmDialog(this, fieldSettingsPanel, "Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (dialogResult == JOptionPane.OK_OPTION) {
            try {
                FieldState fieldState = fieldSettingsPanel.getFieldState();
                if (fieldState == null)
                    return;
                if (!validateFieldState(fieldState)) {
                    JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                plotPanel.setFieldParameters(fieldState.getXGrid(), fieldState.getYGrid(), fieldState.getXMin(),
                        fieldState.getXMax(), fieldState.getYMin(), fieldState.getYMax());
                plotLegend.setFieldParameters(fieldState.getXGrid(), fieldState.getYGrid(), fieldState.getXMin(),
                        fieldState.getXMax(), fieldState.getYMin(), fieldState.getYMax());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void onExit() {
        System.exit(0);
    }

    public void onToolBar() {
        if (this.getToolBar().isVisible())
            this.setToolBarState(false);
        else {
            this.setToolBarState(true);
        }
    }

    public void onStatusBar() {
        if (this.getStatusBar().isVisible())
            this.setStatusBarState(false);
        else {
            this.setStatusBarState(true);
        }
    }

    public void onAbout() {
        JOptionPane.showMessageDialog(this, "Author: Grigorovich Artyom NSU, FIT\nGroup: 16203\nTask: Isolines\nVersion: 0.0.1", "About Author", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        InitMainWindow mainFrame = new InitMainWindow();
        mainFrame.setVisible(true);
    }
}
