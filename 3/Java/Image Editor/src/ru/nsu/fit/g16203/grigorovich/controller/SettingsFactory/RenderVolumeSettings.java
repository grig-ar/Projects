package ru.nsu.fit.g16203.grigorovich.controller.SettingsFactory;

import ru.nsu.fit.g16203.grigorovich.utilityFiles.SpringUtilities;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

public class RenderVolumeSettings extends Settings {
    private JFormattedTextField layersXText;
    private JFormattedTextField layersYText;
    private JFormattedTextField layersZText;
    private static final double LAYERS_MINIMUM = 1;
    private static final double LAYERS_MAXIMUM = 350;

    RenderVolumeSettings() {
        setLayout(new SpringLayout());
        setBorder(BorderFactory.createTitledBorder("Volume Settings"));
        JLabel layersXLabel = new JLabel("X layers:", SwingConstants.TRAILING);
        JLabel layersYLabel = new JLabel("Y layers:", SwingConstants.TRAILING);
        JLabel layersZLabel = new JLabel("Z layers:", SwingConstants.TRAILING);
        NumberFormatter layersNf = new NumberFormatter();
        layersNf.setMinimum(LAYERS_MINIMUM);
        layersNf.setMaximum(LAYERS_MAXIMUM);
        layersXText = new JFormattedTextField(layersNf);
        layersYText = new JFormattedTextField(layersNf);
        layersZText = new JFormattedTextField(layersNf);
        layersXText.setText(String.valueOf(150));
        layersYText.setText(String.valueOf(150));
        layersZText.setText(String.valueOf(150));
        add(layersXLabel);
        add(layersXText);
        add(layersYLabel);
        add(layersYText);
        add(layersZLabel);
        add(layersZText);
        SpringUtilities.makeCompactGrid(this,
                3, 2,            //rows, cols
                6, 6,         //initX, initY
                10, 10);         //xPad, yPad
    }

    @Override
    public Object[] getParameters() {
        Object[] args = new Object[3];
        String text = layersXText.getText();
        if (text.length() == 0) {
            JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        args[0] = Integer.parseInt(text);
        text = layersYText.getText();
        if (text.length() == 0) {
            JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        args[1] = Integer.parseInt(text);

        text = layersZText.getText();
        if (text.length() == 0) {
            JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        args[2] = Integer.parseInt(text);

        return args;
    }

    @Override
    public void setParameters(Object[] args) {
        if (args != null) {
            layersXText.setText(args[0].toString());
            layersYText.setText(args[1].toString());
            layersZText.setText(args[2].toString());
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
