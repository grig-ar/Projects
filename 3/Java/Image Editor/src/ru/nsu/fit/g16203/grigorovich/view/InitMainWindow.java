package ru.nsu.fit.g16203.grigorovich.view;

import ru.nsu.fit.g16203.grigorovich.controller.FilterFactory.Filter;
import ru.nsu.fit.g16203.grigorovich.controller.MainFrame;
import ru.nsu.fit.g16203.grigorovich.controller.SettingsFactory.Settings;
import ru.nsu.fit.g16203.grigorovich.model.MainPanel;
import ru.nsu.fit.g16203.grigorovich.utilityFiles.FileUtils;
import ru.nsu.fit.g16203.grigorovich.utilityFiles.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class InitMainWindow extends MainFrame {
    private MainPanel mainPanel;
    private JPanel settingsPanel;
    private boolean isEmission = false;
    private boolean isAbsorption = false;
    private List<Pair<Integer, Double>> absorptionPoints = new ArrayList<>();
    private List<Pair<Integer, int[]>> emissionPoints = new ArrayList<>();
    private List<Pair<double[], Double>> chargePoints = new ArrayList<>();
    private static final Pattern PATTERN_COMMENT = Pattern.compile(
            "^\\s*$|^(\\s*/{2}).*");

    private static boolean validate(final String ip) {
        return !(PATTERN_COMMENT.matcher(ip).matches());
    }

    private Map<String, Object[]> savedSettings;

    public InitMainWindow() {
        super(1100, 700, "FIT_16203_Grigorovich_Filter");
        FileUtils.getDataDirectory();
        savedSettings = new HashMap<>();
        try {
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/New", "Create a new document", KeyEvent.VK_N, "New.png", "onNew");
            addToolBarButton("File/New");
            addMenuItem("File/Open", "Open file", KeyEvent.VK_O, "Open.png", "onOpen");
            addToolBarButton("File/Open");
            addMenuItem("File/Save", "Save result image", KeyEvent.VK_S, "Save.png", "onSave");
            addToolBarButton("File/Save");
            addToolBarSeparator();
            addMenuSeparator("File");
            addMenuItem("File/Exit", "Exit", KeyEvent.VK_X, "Exit.png", "onExit");
            addSubMenu("Edit", KeyEvent.VK_E);
            addMenuItem("Edit/Desaturate", "Desaturate image", KeyEvent.VK_D, "Desaturate.png", "onApplyFilter");
            addToolBarButton("Edit/Desaturate");
            addMenuItem("Edit/Invert", "Invert colors", KeyEvent.VK_I, "Invert.png", "onApplyFilter");
            addToolBarButton("Edit/Invert");
            addMenuItem("Edit/OrderedDithering", "Ordered dithering", KeyEvent.VK_O, "OrderedDithering.png", "onApplyFilter");
            addToolBarButton("Edit/OrderedDithering");
            addMenuItem("Edit/FloydSteinbergDithering", "Floyd-Steinberg dithering", KeyEvent.VK_F, "FloydSteinbergDithering.png", "onApplyFilter");
            addToolBarButton("Edit/FloydSteinbergDithering");
            addMenuItem("Edit/Double", "Double image", KeyEvent.CTRL_DOWN_MASK | KeyEvent.VK_D, "Double.png", "onApplyFilter");
            addToolBarButton("Edit/Double");
            addMenuItem("Edit/FindEdgesRoberts", "Find edges using Roberts operator", KeyEvent.VK_R, "FindEdgesRoberts.png", "onApplyFilter");
            addToolBarButton("Edit/FindEdgesRoberts");
            addMenuItem("Edit/FindEdgesSobel", "Find edges using Sobel operator", KeyEvent.VK_S, "FindEdgesSobel.png", "onApplyFilter");
            addToolBarButton("Edit/FindEdgesSobel");
            addMenuItem("Edit/Blur", "Blur image", KeyEvent.VK_B, "Blur.png", "onApplyFilter");
            addToolBarButton("Edit/Blur");
            addMenuItem("Edit/Sharpen", "Sharpen image", KeyEvent.CTRL_DOWN_MASK | KeyEvent.VK_S, "Sharpen.png", "onApplyFilter");
            addToolBarButton("Edit/Sharpen");
            addMenuItem("Edit/Emboss", "Emboss", KeyEvent.VK_E, "Emboss.png", "onApplyFilter");
            addToolBarButton("Edit/Emboss");
            addMenuItem("Edit/Watercolor", "Watercolor", KeyEvent.VK_W, "Watercolor.png", "onApplyFilter");
            addToolBarButton("Edit/Watercolor");
            addMenuItem("Edit/Rotate", "Rotate", KeyEvent.VK_C, "Rotate.png", "onApplyFilter");
            addToolBarButton("Edit/Rotate");
            addMenuItem("Edit/Gamma", "Gamma correction", KeyEvent.VK_G, "Gamma.png", "onApplyFilter");
            addToolBarButton("Edit/Gamma");
            addToolBarSeparator();
            addMenuSeparator("Edit");
            addMenuItem("Edit/CopyRight", "Copy image from zone B to C", KeyEvent.CTRL_DOWN_MASK | KeyEvent.VK_R, "CopyRight.png", "onCopyRight");
            addToolBarButton("Edit/CopyRight");
            addMenuItem("Edit/CopyLeft", "Copy image from zone C to B", KeyEvent.CTRL_DOWN_MASK | KeyEvent.VK_L, "CopyLeft.png", "onCopyLeft");
            addToolBarButton("Edit/CopyLeft");
            addMenuToolbarGroup("Edit/Select", "Select a part of image", KeyEvent.ALT_DOWN_MASK | KeyEvent.VK_S, "Select.png", "onSelect");
            addToolBarSeparator();
            addMenuSeparator("Edit");
            addMenuItem("Edit/Open configuration", "Open a configuration file", KeyEvent.ALT_DOWN_MASK | KeyEvent.VK_O, "OpenConfig.png", "onOpenConfig");
            addToolBarButton("Edit/Open configuration");
            addMenuToolbarGroup("Edit/Enable absorption", "Enable light absorption", KeyEvent.ALT_DOWN_MASK | KeyEvent.VK_L, "EnableAbsorb.png", "onEnableAbsorption");
            addMenuToolbarGroup("Edit/Enable emission", "Enable light emission", KeyEvent.ALT_DOWN_MASK | KeyEvent.VK_E, "EnableEmission.png", "onEnableEmission");
            addMenuItem("Edit/RenderVolume", "Render volume", KeyEvent.ALT_DOWN_MASK | KeyEvent.VK_V, "RenderVolume.png", "onApplyFilter");
            addToolBarButton("Edit/RenderVolume");
            addToolBarSeparator();
            addSubMenu("View", KeyEvent.VK_V);
            addMenuItem("View/ToolBar", "Add/Remove ToolBar", KeyEvent.VK_T, null, "onToolBar");
            addMenuItem("View/StatusBar", "Add/Remove StatusBar", KeyEvent.VK_S, null, "onStatusBar");
            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Info about the author and program", KeyEvent.VK_A, "Help.png", "onAbout");
            addToolBarButton("Help/About...");

            mainPanel = new MainPanel();

            JScrollPane scrollPane = new JScrollPane(mainPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            add(scrollPane);
            pack();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void onNew() {
        onSave();
        onInit();
        pack();
    }

    public void onOpen() {
        File file = FileUtils.getOpenFileName(this, "png", "Image files");
        if (file == null) {
            return;
        }
        String extension = "";
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        if (!extension.toLowerCase().equals("png") && !extension.toLowerCase().equals("bmp")) {
            JOptionPane.showMessageDialog(this, "Incorrect file extension!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        mainPanel.setOriginalImage(file);
        mainPanel.loadImageA(file);
        pack();
    }


    public void onSave() {
        if (mainPanel.panelC.getImage() == null)
            return;
        File file = FileUtils.getSaveFileName(this, "BMP", "Image files");
        if (file == null) {
            return;
        }
        BufferedImage outImage = mainPanel.panelC.getImage();
        if (outImage == null) {
            JOptionPane.showMessageDialog(this, "Nothing to save!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String extension = "";
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        if (!extension.toLowerCase().equals("png") && !extension.toLowerCase().equals("bmp")) {
            JOptionPane.showMessageDialog(this, "Incorrect file extension!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            ImageIO.write(outImage, extension, file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void onExit() {
        System.exit(0);
    }

    public void onInit() {
        mainPanel.panelA.removeAll();
        mainPanel.panelB.removeAll();
        mainPanel.panelC.removeAll();
        mainPanel.removeAll();
        mainPanel.init();
        if (globalSelectButton.isSelected()) {
            globalSelectButton.doClick();
            mainPanel.panelA.isSelectEnabled = !mainPanel.panelA.isSelectEnabled;
            mainPanel.isSelect = !mainPanel.isSelect;
        }
        if (globalAbsorptionButton.isSelected()) {
            globalAbsorptionButton.doClick();
            isAbsorption = false;
        }
        if (globalEmissionButton.isSelected()) {
            globalEmissionButton.doClick();
            isEmission = false;
        }
    }

    public void onApplyFilter(String filterName) {
        if (mainPanel.panelA.getImage() == null)
            return;
        Filter filter = filterMaker.createFilter(filterName);
        Settings settings = settingsMaker.createSettings(filterName);
        if (settings != null) {
            settingsPanel = settings.getPanel();
            JOptionPane optionPane = new JOptionPane();
            optionPane.setMessage(settingsPanel);
            optionPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
            settings.setParameters(savedSettings.getOrDefault(filterName, null));
            int dialogResult = JOptionPane.showConfirmDialog(this, settingsPanel, "Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (dialogResult == JOptionPane.OK_OPTION) {
                Object[] args = settings.getParameters();
                if (args == null)
                    return;
                if (filterName.equals("RenderVolume")) {
                    Object[] extendedArgs = new Object[args.length + 5];
                    System.arraycopy(args, 0, extendedArgs, 0, args.length);
                    extendedArgs[args.length] = isAbsorption;
                    extendedArgs[args.length + 1] = isEmission;
                    extendedArgs[args.length + 2] = absorptionPoints;
                    extendedArgs[args.length + 3] = emissionPoints;
                    extendedArgs[args.length + 4] = chargePoints;
                    mainPanel.panelC.setImage(filter.applyTo(mainPanel.panelB.getImage(), extendedArgs));
                    savedSettings.put(filterName, args);
                    return;
                }
                mainPanel.panelC.filterOn = true;
                mainPanel.panelC.setImage(filter.applyTo(mainPanel.panelB.getImage(), args));
                savedSettings.put(filterName, args);
                return;
            } else
                return;
        }
        mainPanel.panelC.setImage(filter.applyTo(mainPanel.panelB.getImage(), (Object[]) null));
    }

    public void onCopyRight() {
        if (mainPanel.panelB.getImage() == null)
            return;
        mainPanel.copyImageToC();
    }

    public void onCopyLeft() {
        if (mainPanel.panelC.getImage() == null)
            return;
        mainPanel.copyImageToB();
    }

    public void onSelect() {
        mainPanel.panelA.isSelectEnabled = !mainPanel.panelA.isSelectEnabled;
        mainPanel.isSelect = !mainPanel.isSelect;
        mainPanel.translateImage();
    }

    public void onOpenConfig() {
        File file = FileUtils.getOpenFileName(this, "txt", "Text files");
        if (file == null) {
            return;
        }
        try {
            FileReader reader = new FileReader(file);
            Scanner scan = new Scanner(reader);
            emissionPoints.clear();
            absorptionPoints.clear();
            chargePoints.clear();
            int i = 0;
            int absorptionPointsAmount = 0;
            int emissionPointsAmount = 0;
            int chargePointsAmount = 0;
            while (scan.hasNextLine()) {
                String temp = scan.nextLine();
                if (validate(temp)) {
                    String[] values = temp.split("//");
                    try {
                        if (!temp.contains("//") && temp.replaceAll("(\\s+|\\d+)", "").length() > 4) {
                            throw new RuntimeException("Corrupted file!");
                        }
                    } catch (Exception ex) {
                        System.out.println("Corrupted file!");
                        return;
                    }
                    values = values[0].split(" ");
                    try {
                        if (values.length == 1 && i == 0) {
                            absorptionPointsAmount = Integer.parseInt(values[0]);
                            ++i;
                            continue;
                        }
                        if (values.length == 2) {
                            if (absorptionPoints.size() >= absorptionPointsAmount) {
                                System.out.println("Corrupted file!");
                                return;
                            }
                            int coord = Integer.parseInt(values[0]);
                            double absorptionValue = Double.parseDouble(values[1]);
                            absorptionPoints.add(new Pair<>(coord, absorptionValue));
                            continue;
                        }
                        if (values.length == 1 && i == 1) {
                            emissionPointsAmount = Integer.parseInt(values[0]);
                            ++i;
                            continue;
                        }
                        if (values.length == 4 && i == 2) {
                            if (emissionPoints.size() >= emissionPointsAmount) {
                                System.out.println("Corrupted file!");
                                return;
                            }
                            int[] rgb = new int[3];
                            String[] stringRGB;
                            stringRGB = Arrays.copyOfRange(values, 1, 4);
                            for (int k = 0; k < 3; ++k) {
                                rgb[k] = Integer.parseInt(stringRGB[k]);
                            }
                            emissionPoints.add(new Pair<>(Integer.parseInt(values[0]), rgb));
                            continue;
                        }
                        if (values.length == 1 && i == 2) {
                            chargePointsAmount = Integer.parseInt(values[0]);
                            ++i;
                            continue;
                        }
                        if (values.length == 4 && i == 3) {
                            if (chargePoints.size() >= chargePointsAmount) {
                                System.out.println("Corrupted file!");
                                return;
                            }
                            double[] coords = new double[3];
                            String[] stringCoords;
                            stringCoords = Arrays.copyOfRange(values, 0, 3);
                            for (int k = 0; k < 3; ++k) {
                                coords[k] = Double.parseDouble(stringCoords[k]);
                            }
                            chargePoints.add(new Pair<>(coords, Double.parseDouble(values[3])));
                        }
                    } catch (Exception ex) {
                        System.out.println("Corrupted file!");
                        return;
                    }
                }
            }
            mainPanel.showConfig(absorptionPoints, emissionPoints);
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void onEnableAbsorption() {
        isAbsorption = !isAbsorption;
    }

    public void onEnableEmission() {
        isEmission = !isEmission;
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
        JOptionPane.showMessageDialog(this, "Author: Grigorovich Artyom NSU, FIT\nGroup: 16203\nTask: Filter\nVersion: 0.0.4", "About Author", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        InitMainWindow mainFrame = new InitMainWindow();
        mainFrame.setVisible(true);
    }
}
