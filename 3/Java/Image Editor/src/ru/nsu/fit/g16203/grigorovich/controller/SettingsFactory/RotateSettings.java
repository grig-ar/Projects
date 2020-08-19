package ru.nsu.fit.g16203.grigorovich.controller.SettingsFactory;

import ru.nsu.fit.g16203.grigorovich.utilityFiles.SpringUtilities;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;

public class RotateSettings extends Settings {
    private JFormattedTextField angleText;
    private JSlider angleSlider;
    private static final int ANGLE_MINIMUM = -180;
    private static final int ANGLE_MAXIMUM = 180;

    private void createConnection(JFormattedTextField textField, JSlider slider) {
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

    RotateSettings() {
        setLayout(new SpringLayout());
        setBorder(BorderFactory.createTitledBorder("Rotation"));
        JLabel gammaLabel = new JLabel("Angle:", SwingConstants.TRAILING);
        NumberFormatter angleNf = new NumberFormatter();
        angleNf.setMinimum(ANGLE_MINIMUM);
        angleNf.setMaximum(ANGLE_MAXIMUM);
        angleSlider = new JSlider(ANGLE_MINIMUM, ANGLE_MAXIMUM, 45);
        angleText = new JFormattedTextField(angleNf);
        angleText.setColumns(4);
        angleText.setText(String.valueOf(45));
        createConnection(angleText, angleSlider);
        add(gammaLabel);
        add(angleText);
        add(angleSlider);
        SpringUtilities.makeCompactGrid(this,
                1, 3,            //rows, cols
                6, 6,         //initX, initY
                10, 10);         //xPad, yPad
    }

    @Override
    public Object[] getParameters() {
        Object[] args = new Object[1];
        String text = angleText.getText();
        if (text.length() == 0) {
            JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        args[0] = Integer.parseInt(text);
        return args;
    }

    @Override
    public void setParameters(Object[] args) {
        if (args != null) {
            angleText.setText(args[0].toString());
            int value = Integer.parseInt(args[0].toString());
            angleSlider.setValue(value);
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
