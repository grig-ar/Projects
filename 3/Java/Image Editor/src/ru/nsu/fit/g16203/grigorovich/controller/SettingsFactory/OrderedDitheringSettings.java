package ru.nsu.fit.g16203.grigorovich.controller.SettingsFactory;

import ru.nsu.fit.g16203.grigorovich.utilityFiles.SpringUtilities;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

public class OrderedDitheringSettings extends Settings {
    private JFormattedTextField redText;
    private JFormattedTextField greenText;
    private JFormattedTextField blueText;
    private JFormattedTextField sizeText;
    private static final double COLOR_MINIMUM = 2;
    private static final double COLOR_MAXIMUM = 255;
    private static final double SIZE_MINIMUM = 4;
    private static final double SIZE_MAXIMUM = 64;

    OrderedDitheringSettings() {
        setLayout(new SpringLayout());
        setBorder(BorderFactory.createTitledBorder("Dithering"));
        JLabel redLabel = new JLabel("Red:", SwingConstants.TRAILING);
        JLabel greenLabel = new JLabel("Green:", SwingConstants.TRAILING);
        JLabel blueLabel = new JLabel("Blue:", SwingConstants.TRAILING);
        JLabel sizeLabel = new JLabel("Size:", SwingConstants.TRAILING);
        NumberFormatter colorNf = new NumberFormatter();
        NumberFormatter sizeNf = new NumberFormatter();
        colorNf.setMinimum(COLOR_MINIMUM);
        colorNf.setMaximum(COLOR_MAXIMUM);
        sizeNf.setMinimum(SIZE_MINIMUM);
        sizeNf.setMaximum(SIZE_MAXIMUM);
        redText = new JFormattedTextField(colorNf);
        greenText = new JFormattedTextField(colorNf);
        blueText = new JFormattedTextField(colorNf);
        sizeText = new JFormattedTextField(sizeNf);

        redText.setText(String.valueOf(2));
        greenText.setText(String.valueOf(2));
        blueText.setText(String.valueOf(2));
        sizeText.setText(String.valueOf(16));
        add(redLabel);
        add(redText);
        add(greenLabel);
        add(greenText);
        add(blueLabel);
        add(blueText);
        add(sizeLabel);
        add(sizeText);
        SpringUtilities.makeCompactGrid(this,
                4, 2,            //rows, cols
                6, 6,         //initX, initY
                10, 10);         //xPad, yPad
    }

    @Override
    public Object[] getParameters() {
        Object[] args = new Object[4];
        String text = redText.getText();
        if (text.length() == 0) {
            JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        args[0] = Integer.parseInt(text);
        text = greenText.getText();
        if (text.length() == 0) {
            JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        args[1] = Integer.parseInt(text);

        text = blueText.getText();
        if (text.length() == 0) {
            JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        args[2] = Integer.parseInt(text);

        text = sizeText.getText();
        if (text.length() == 0) {
            JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        args[3] = Integer.parseInt(text);

        return args;
    }

    @Override
    public void setParameters(Object[] args) {
        if (args != null) {
            redText.setText(args[0].toString());
            greenText.setText(args[1].toString());
            blueText.setText(args[2].toString());
            sizeText.setText(args[3].toString());
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
