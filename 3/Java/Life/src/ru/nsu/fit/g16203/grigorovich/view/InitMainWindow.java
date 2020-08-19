package ru.nsu.fit.g16203.grigorovich.view;

import ru.nsu.fit.g16203.grigorovich.controller.MainFrame;
import ru.nsu.fit.g16203.grigorovich.model.LifeState;
import ru.nsu.fit.g16203.grigorovich.utilityFiles.FileUtils;
import ru.nsu.fit.g16203.grigorovich.utilityFiles.SpringUtilities;
import ru.nsu.fit.g16203.grigorovich.model.MainPanel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.Timer;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import javax.swing.SpringLayout;

class Pair<T, U> {
    T t;
    U u;

    Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }
}

public class InitMainWindow extends MainFrame {
    private JScrollPane scrollPane;
    private MainPanel panel;
    private SettingsPanel settingsPanel;
    private static final Pattern PATTERN_COMMENT = Pattern.compile(
            "^\\s*$|^(\\s*/{2}).*");
    private int timerPeriod = TIMER_DEFAULT;
    private static final int COLS_MINIMUM = 1;
    private static final int COLS_DEFAULT = 10;
    private static final int COLS_MAXIMUM = 70;
    private static final int ROWS_MINIMUM = 1;
    private static final int ROWS_DEFAULT = 10;
    private static final int ROWS_MAXIMUM = 70;
    private static final int SIZE_MINIMUM = 5;
    private static final int SIZE_DEFAULT = 25;
    private static final int SIZE_MAXIMUM = 100;
    private static final int STROKE_MINIMUM = 1;
    private static final int STROKE_DEFAULT = 2;
    private static final int STROKE_MAXIMUM = 10;
    private static final int TIMER_MINIMUM = 10;
    private static final int TIMER_DEFAULT = 1000;
    private static final int TIMER_MAXIMUM = 2000;
    private static final int TIMER_DELAY = 250;
    private static final double LIVE_BEGIN_MINIMUM = 0.01;
    private static final double LIVE_BEGIN_DEFAULT = 2.00;
    private static final double LIVE_BEGIN_MAXIMUM = 9.96;
    private static final double LIVE_END_MINIMUM = 0.04;
    private static final double LIVE_END_DEFAULT = 3.30;
    private static final double LIVE_END_MAXIMUM = 9.99;
    private static final double BIRTH_BEGIN_MINIMUM = 0.02;
    private static final double BIRTH_BEGIN_DEFAULT = 2.30;
    private static final double BIRTH_BEGIN_MAXIMUM = 9.97;
    private static final double BIRTH_END_MINIMUM = 0.03;
    private static final double BIRTH_END_DEFAULT = 2.90;
    private static final double BIRTH_END_MAXIMUM = 9.98;
    private static final double IMPACT_MINIMUM = 0.01;
    private static final double IMPACT_MAXIMUM = 9.99;
    private static final double IMPACT_FIRST_DEFAULT = 1.00;
    private static final double IMPACT_SECOND_DEFAULT = 0.30;
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);

    private static boolean validate(final String ip) {
        return !(PATTERN_COMMENT.matcher(ip).matches());
    }

    class MyTimerTask extends TimerTask {


        @Override
        public void run() {
            boolean keepGoing;
            keepGoing = panel.step();
            if (!keepGoing || panel.isFieldEmpty()) {
                this.cancel();
                panel.isRunning = false;
            }
            panel.repaint();
            panel.isRunning = true;
        }
    }


    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

    class SettingsPanel extends JPanel {
        JFormattedTextField colsText;
        JFormattedTextField rowsText;
        JFormattedTextField strokeText;
        JFormattedTextField sizeText;
        JFormattedTextField timerText;
        JFormattedTextField liveBeginText;
        JFormattedTextField liveEndText;
        JFormattedTextField birthBeginText;
        JFormattedTextField birthEndText;
        JFormattedTextField firstImpactText;
        JFormattedTextField secondImpactText;
        JRadioButton xorRadioButton;

        SettingsPanel() {
            JFormattedTextField max = new JFormattedTextField();
            max.setText(df.format(LIVE_END_MAXIMUM));
            setLayout(new SpringLayout());

            JPanel fieldPropertiesPanel = new JPanel();
            JPanel modePanel = new JPanel();
            JPanel environmentPanel = new JPanel();
            JPanel impactPanel = new JPanel();

            fieldPropertiesPanel.setLayout(new SpringLayout());
            fieldPropertiesPanel.setBorder(BorderFactory.createTitledBorder("Field properties"));

            modePanel.setLayout(new SpringLayout());
            modePanel.setBorder(BorderFactory.createTitledBorder("Mode"));

            environmentPanel.setLayout(new SpringLayout());
            environmentPanel.setBorder(BorderFactory.createTitledBorder("Environment"));

            impactPanel.setLayout(new SpringLayout());
            impactPanel.setBorder(BorderFactory.createTitledBorder("Impact"));

            JLabel rowsLabel = new JLabel("Rows", SwingConstants.TRAILING);
            JLabel colsLabel = new JLabel("Columns", SwingConstants.TRAILING);
            JLabel sizeLabel = new JLabel("Cell size", SwingConstants.TRAILING);
            JLabel strokeLabel = new JLabel("Grid width", SwingConstants.TRAILING);
            JLabel timerLabel = new JLabel("Timer", SwingConstants.TRAILING);

            JSlider rowsSlider = new JSlider(ROWS_MINIMUM, ROWS_MAXIMUM, ROWS_DEFAULT);
            JSlider colsSlider = new JSlider(COLS_MINIMUM, COLS_MAXIMUM, COLS_DEFAULT);
            JSlider sizeSlider = new JSlider(SIZE_MINIMUM, SIZE_MAXIMUM, SIZE_DEFAULT);
            JSlider strokeSlider = new JSlider(STROKE_MINIMUM, STROKE_MAXIMUM, STROKE_DEFAULT);
            JSlider timerSlider = new JSlider(TIMER_MINIMUM, TIMER_MAXIMUM, TIMER_DEFAULT);

            NumberFormatter rowsNf = new NumberFormatter();
            rowsNf.setMinimum(ROWS_MINIMUM);
            rowsNf.setMaximum(ROWS_MAXIMUM);

            NumberFormatter colsNf = new NumberFormatter();
            colsNf.setMinimum(COLS_MINIMUM);
            colsNf.setMaximum(COLS_MAXIMUM);

            NumberFormatter sizeNf = new NumberFormatter();
            sizeNf.setMinimum(SIZE_MINIMUM);
            sizeNf.setMaximum(SIZE_MAXIMUM);

            NumberFormatter strokeNf = new NumberFormatter();
            strokeNf.setMinimum(STROKE_MINIMUM);
            strokeNf.setMaximum(STROKE_MAXIMUM);

            NumberFormatter timerNf = new NumberFormatter();
            timerNf.setMinimum(TIMER_MINIMUM);
            timerNf.setMaximum(TIMER_MAXIMUM);

            rowsText = new JFormattedTextField(rowsNf);
            rowsText.setText(String.valueOf(panel.rows));
            createConnection(rowsText, rowsSlider);

            colsText = new JFormattedTextField(colsNf);
            colsText.setText(String.valueOf(panel.cols));
            createConnection(colsText, colsSlider);

            sizeText = new JFormattedTextField(sizeNf);
            sizeText.setText(String.valueOf(panel.size));
            createConnection(sizeText, sizeSlider);

            strokeText = new JFormattedTextField(strokeNf);
            strokeText.setText(String.valueOf(panel.stroke));
            createConnection(strokeText, strokeSlider);

            timerText = new JFormattedTextField(timerNf);
            timerText.setColumns(4);
            timerText.setText(String.valueOf(timerPeriod));
            createConnection(timerText, timerSlider);

            fieldPropertiesPanel.add(rowsLabel);
            fieldPropertiesPanel.add(rowsText);
            fieldPropertiesPanel.add(rowsSlider);

            fieldPropertiesPanel.add(colsLabel);
            fieldPropertiesPanel.add(colsText);
            fieldPropertiesPanel.add(colsSlider);

            fieldPropertiesPanel.add(sizeLabel);
            fieldPropertiesPanel.add(sizeText);
            fieldPropertiesPanel.add(sizeSlider);

            fieldPropertiesPanel.add(strokeLabel);
            fieldPropertiesPanel.add(strokeText);
            fieldPropertiesPanel.add(strokeSlider);

            fieldPropertiesPanel.add(timerLabel);
            fieldPropertiesPanel.add(timerText);
            fieldPropertiesPanel.add(timerSlider);

            SpringUtilities.makeCompactGrid(fieldPropertiesPanel,
                    5, 3,            //rows, cols
                    6, 6,         //initX, initY
                    10, 10);         //xPad, yPad


            JRadioButton replaceRadioButton = new JRadioButton("Replace");
            replaceRadioButton.setSelected(!panel.xorOn);
            xorRadioButton = new JRadioButton("Xor");
            xorRadioButton.setSelected(panel.xorOn);
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(replaceRadioButton);
            buttonGroup.add(xorRadioButton);
            modePanel.add(replaceRadioButton);
            modePanel.add(xorRadioButton);

            SpringUtilities.makeCompactGrid(modePanel,
                    2, 1,
                    6, 6,
                    10, 10);


            JLabel liveBeginLabel = new JLabel("Live begin");
            JLabel liveEndLabel = new JLabel("Live end");
            JLabel birthBeginLabel = new JLabel("Birth begin");
            JLabel birthEndLabel = new JLabel("Birth end");


            NumberFormatter liveBeginNf = new NumberFormatter();
            liveBeginNf.setMinimum(LIVE_BEGIN_MINIMUM);
            liveBeginNf.setMaximum(LIVE_BEGIN_MAXIMUM);
            liveBeginNf.setFormat(df);

            NumberFormatter liveEndNf = new NumberFormatter();
            liveEndNf.setMinimum(LIVE_END_MINIMUM);
            liveEndNf.setMaximum(LIVE_END_MAXIMUM);
            liveEndNf.setFormat(df);

            NumberFormatter birthBeginNf = new NumberFormatter();
            birthBeginNf.setMinimum(BIRTH_BEGIN_MINIMUM);
            birthBeginNf.setMaximum(BIRTH_BEGIN_MAXIMUM);
            birthBeginNf.setFormat(df);

            NumberFormatter birthEndNf = new NumberFormatter();
            birthEndNf.setMinimum(BIRTH_END_MINIMUM);
            birthEndNf.setMaximum(BIRTH_END_MAXIMUM);
            birthEndNf.setFormat(df);

            liveBeginText = new JFormattedTextField(liveBeginNf);
            liveBeginText.setText(df.format(panel.liveBeginValue));

            liveEndText = new JFormattedTextField(liveEndNf);
            liveEndText.setText(df.format(panel.liveEndValue));

            birthBeginText = new JFormattedTextField(birthBeginNf);
            birthBeginText.setText(df.format(panel.birthBeginValue));

            birthEndText = new JFormattedTextField(birthEndNf);
            birthEndText.setText(df.format(panel.birthEndValue));

            createVerification(liveBeginText, LIVE_BEGIN_DEFAULT, birthBeginText);
            createVerification(birthBeginText, BIRTH_BEGIN_DEFAULT, birthEndText);
            createVerification(birthEndText, BIRTH_END_DEFAULT, liveEndText);
            createVerification(liveEndText, LIVE_END_DEFAULT, max);

            environmentPanel.add(liveBeginLabel);
            environmentPanel.add(liveBeginText);

            environmentPanel.add(liveEndLabel);
            environmentPanel.add(liveEndText);

            environmentPanel.add(birthBeginLabel);
            environmentPanel.add(birthBeginText);

            environmentPanel.add(birthEndLabel);
            environmentPanel.add(birthEndText);

            SpringUtilities.makeCompactGrid(environmentPanel,
                    2, 4,
                    6, 6,
                    10, 10);


            JLabel firstImpactLabel = new JLabel("First impact");
            JLabel secondImpactLabel = new JLabel("Second impact");

            NumberFormatter firstImpactNf = new NumberFormatter();
            firstImpactNf.setMinimum(IMPACT_MINIMUM);
            firstImpactNf.setMaximum(IMPACT_MAXIMUM);
            firstImpactNf.setFormat(df);

            NumberFormatter secondImpactNf = new NumberFormatter();
            secondImpactNf.setMinimum(IMPACT_MINIMUM);
            secondImpactNf.setMaximum(IMPACT_MAXIMUM);
            secondImpactNf.setFormat(df);

            firstImpactText = new JFormattedTextField(firstImpactNf);
            firstImpactText.setColumns(4);
            firstImpactText.setText(df.format(panel.firstImpactValue));

            secondImpactText = new JFormattedTextField(secondImpactNf);
            secondImpactText.setColumns(4);
            secondImpactText.setText(df.format(panel.secondImpactValue));

            impactPanel.add(firstImpactLabel);
            impactPanel.add(firstImpactText);

            impactPanel.add(secondImpactLabel);
            impactPanel.add(secondImpactText);

            SpringUtilities.makeCompactGrid(impactPanel,
                    2, 2,
                    6, 6,
                    10, 10);


            add(fieldPropertiesPanel);
            add(modePanel);
            add(environmentPanel);
            add(impactPanel);
            SpringUtilities.makeCompactGrid(this,
                    2, 2,
                    6, 6,
                    10, 10);
        }

        LifeState getLifeState() throws ParseException {
            return new LifeState(Integer.parseInt(colsText.getText()), Integer.parseInt(rowsText.getText()), Integer.parseInt(strokeText.getText()),
                    Integer.parseInt(sizeText.getText()), format.parse(liveBeginText.getText()).doubleValue(), format.parse(liveEndText.getText()).doubleValue(),
                    format.parse(birthBeginText.getText()).doubleValue(), format.parse(birthEndText.getText()).doubleValue(),
                    format.parse(firstImpactText.getText()).doubleValue(), format.parse(secondImpactText.getText()).doubleValue(), xorRadioButton.isSelected(), format.parse(timerText.getText()).intValue());
        }

        void setLifeState(LifeState newState) {
            colsText.setText(String.valueOf(newState.cols));
            rowsText.setText(String.valueOf(newState.rows));
            strokeText.setText(String.valueOf(newState.stroke));
            sizeText.setText(String.valueOf(newState.size));
            timerText.setText(String.valueOf(newState.timer));
            liveBeginText.setText(df.format(newState.liveBeginValue));
            liveEndText.setText(df.format(newState.liveEndValue));
            birthBeginText.setText(df.format(newState.birthBeginValue));
            birthEndText.setText(df.format(newState.birthEndValue));
            firstImpactText.setText(df.format(newState.firstImpactValue));
            secondImpactText.setText(df.format(newState.secondImpactValue));
            xorRadioButton.setSelected(newState.isXor);
        }
    }


    public InitMainWindow() {
        super(800, 600, "FIT_16203_Grigorovich_Life");
        FileUtils.getDataDirectory();
        panel = new MainPanel(COLS_DEFAULT, ROWS_DEFAULT, STROKE_DEFAULT, SIZE_DEFAULT, LIVE_BEGIN_DEFAULT, LIVE_END_DEFAULT,
                BIRTH_BEGIN_DEFAULT, BIRTH_END_DEFAULT, IMPACT_FIRST_DEFAULT, IMPACT_SECOND_DEFAULT, null);
        settingsPanel = new SettingsPanel();
        try {
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/New", "Create new field", KeyEvent.VK_N, "New.png", "onNew");
            addToolBarButton("File/New");
            addMenuItem("File/Open", "Open file", KeyEvent.VK_O, "Open.png", "onOpen");
            addToolBarButton("File/Open");
            addMenuItem("File/Save", "Save file", KeyEvent.VK_S, "Save.png", "onSave");
            addToolBarButton("File/Save");
            addToolBarSeparator();
            addMenuSeparator("File");
            addMenuItem("File/Exit", "Exit", KeyEvent.VK_X, "Exit.png", "onExit");

            addSubMenu("Modify", KeyEvent.VK_M);
            addMenuItem("Modify/Settings", "Modify field parameters", KeyEvent.VK_S, "Settings.png", "onSettings");
            addToolBarButton("Modify/Settings");
            addMenuToolbarGroup("Modify/Replace", "Change state to live", KeyEvent.VK_R, "Replace.png", "onReplace",
                    "Modify/Xor", "Change state to another", KeyEvent.VK_X, "Xor.png", "onXor");
            addMenuSeparator("Modify");
            addMenuItem("Modify/Impact", "Show impact values", KeyEvent.VK_I, "Impact.png", "onImpact");

            addSubMenu("Action", KeyEvent.VK_A);
            addMenuItem("Action/Init", "Clear the field", KeyEvent.VK_I, "Delete.png", "onInit");
            addMenuItem("Action/Step", "Next step", KeyEvent.VK_S, "Step.png", "onStep");
            addMenuItem("Action/Run", "Step automatically", KeyEvent.VK_R, "Run.png", "onRun");

            addSubMenu("View", KeyEvent.VK_V);
            addMenuItem("View/ToolBar", "Add/Remove ToolBar", KeyEvent.VK_T, null, "onToolBar");
            addMenuItem("View/StatusBar", "Add/Remove StatusBar", KeyEvent.VK_S, null, "onStatusBar");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Info about the author and program", KeyEvent.VK_A, "Help.png", "onAbout");

            addToolBarButton("Modify/Impact");
            addToolBarSeparator();

            addToolBarButton("Action/Init");
            addToolBarButton("Action/Step");
            addToolBarButton("Action/Run");
            addToolBarSeparator();

            addToolBarButton("Help/About...");

            scrollPane = new JScrollPane(panel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            add(scrollPane);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to save your game?", "Warning", dialogButton);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        onSave();
                    }
                }
            });
            pack();
            panel.revalidate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void onNew() {
        onSave();
        onInit();
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
            int[] arr = new int[1024];
            while (scan.hasNextLine()) {
                String temp = scan.nextLine();
                if (validate(temp)) {
                    String[] sizes = temp.split("//");
                    try {
                        if (!temp.contains("//") && temp.replaceAll("\\s+", "").length() > 4) {
                            throw new RuntimeException("Corrupted file!");
                        }
                    } catch (Exception ex) {
                        System.out.println("Corrupted file!");
                        return;
                    }
                    sizes = sizes[0].split(" ");
                    try {
                        switch (i) {
                            case 0:
                                arr[0] = Integer.parseInt(sizes[0]);
                                arr[1] = Integer.parseInt(sizes[1]);
                                ++i;
                                break;
                            case 1:
                            case 2:
                            case 3:
                                arr[++i] = Integer.parseInt(sizes[0]);
                                break;
                            default:
                                arr[++i] = Integer.parseInt(sizes[0]);
                                arr[++i] = Integer.parseInt(sizes[1]);
                                break;
                        }
                    } catch (Exception ex) {
                        System.out.println("Corrupted file!");
                        return;
                    }
                }
            }
            LifeState lifeState = new LifeState(arr[0], arr[1], arr[2], arr[3], LIVE_BEGIN_DEFAULT, LIVE_END_DEFAULT,
                    BIRTH_BEGIN_DEFAULT, BIRTH_END_DEFAULT, IMPACT_FIRST_DEFAULT, IMPACT_SECOND_DEFAULT, false, TIMER_DEFAULT);
            panel.setState(lifeState);
            onInit();
            panel.revalidate();
            pack();
            reader.close();
            int count = 5;
            for (int k = 0; k < arr[4]; ++k) {
                panel.field[arr[count]][arr[count + 1]].setAlive(false);
                count += 2;

            }
            settingsPanel.setLifeState(lifeState);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void onSave() {
        File file = FileUtils.getSaveFileName(this, "txt", "Text files");
        if (file == null) {
            return;
        }
        ArrayList<Pair<Integer, Integer>> aliveCells = new ArrayList<>(panel.cols * panel.rows);
        for (int i = 0; i < panel.cols; ++i) {
            for (int j = 0; j < panel.rows; ++j) {
                if ((i == (panel.cols - 1) || panel.cols == 1) && (j & 1) == 1)
                    continue;
                if (panel.field[i][j].color.getRGB() == panel.COLOR_ALIVE_CELL.getRGB())
                    aliveCells.add(new Pair<>(panel.field[i][j].col, panel.field[i][j].row));
            }
        }
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(panel.cols + " " + panel.rows + "\n");
            writer.write(panel.stroke + "\n");
            writer.write(panel.size + "\n");
            writer.write(String.valueOf(aliveCells.size()) + "\n");
            for (Pair<Integer, Integer> pair : aliveCells) {
                writer.write(pair.t + " " + pair.u + "\n");
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void onExit() {
        System.exit(0);
    }

    public void onSettings() {
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(settingsPanel);
        optionPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
        int dialogResult = JOptionPane.showConfirmDialog(this, settingsPanel, "Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (dialogResult == JOptionPane.OK_OPTION) {
            try {
                LifeState lifeState = settingsPanel.getLifeState();
                panel.setState(lifeState);
                timerPeriod = lifeState.timer;
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (panel.xorOn) {
                for (ActionListener a : globalXorButton.getActionListeners()) {
                    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null)
                    );
                }
            }
            panel.revalidate();
        }
    }

    public void createConnection(JFormattedTextField textField, JSlider slider) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String typed = textField.getText();
                slider.setValue(0);
                if (!typed.matches("\\d+") || typed.length() > 5) {
                    return;
                }
                int value = Integer.parseInt(typed);
                slider.setValue(value);
            }
        });

        slider.addChangeListener(e -> textField.setText(String.valueOf(slider.getValue())));
    }

    public void createVerification(JFormattedTextField curTextField, double defaultValue, JFormattedTextField nextTextField) {
        curTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String typed = curTextField.getText();
                if (!typed.matches("\\d+(,\\d*)?") || typed.length() > 5) {
                    return;
                }
                try {
                    Number number = format.parse(typed);
                    double value = number.doubleValue();
                    if (value > format.parse(nextTextField.getText()).doubleValue())
                        curTextField.setText(df.format(defaultValue));
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void onReplace() {
        if (panel.xorOn)
            panel.disableXor();

    }

    public void onXor() {
        if (!panel.xorOn)
            panel.enableXor();
    }

    public void onImpact() {
        if (!panel.impactOn)
            panel.showImpact();
        else
            panel.hideImpact();
    }

    public void onInit() {
        panel.clearField();
        panel.repaint();
    }

    public void onStep() {
        panel.step();
        panel.repaint();
    }

    public void onRun() {
        if (mTimer != null) {
            mTimer.cancel();
            panel.isRunning = false;
            mTimer = null;
            return;
        }
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, TIMER_DELAY, timerPeriod);
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
        JOptionPane.showMessageDialog(this, "Author: Grigorovich Artyom NSU, FIT\nGroup: 16203\nTask: Life\nVersion: 0.0.5", "About Author", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        InitMainWindow mainFrame = new InitMainWindow();
        mainFrame.setVisible(true);
    }
}
