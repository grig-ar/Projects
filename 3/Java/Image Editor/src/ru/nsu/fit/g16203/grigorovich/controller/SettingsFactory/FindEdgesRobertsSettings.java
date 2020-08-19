package ru.nsu.fit.g16203.grigorovich.controller.SettingsFactory;

import ru.nsu.fit.g16203.grigorovich.utilityFiles.SpringUtilities;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FindEdgesRobertsSettings extends Settings {
    private JFormattedTextField levelText;
    private JSlider levelSlider;
    private static final int LEVEL_MINIMUM = 1;
    private static final int LEVEL_DEFAULT = 18;
    private static final int LEVEL_MAXIMUM = 255;

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

    FindEdgesRobertsSettings() {
        setLayout(new SpringLayout());
        setBorder(BorderFactory.createTitledBorder("Level"));
        JLabel levelLabel = new JLabel("Level:", SwingConstants.TRAILING);
        NumberFormatter levelNf = new NumberFormatter();
        levelNf.setMinimum(LEVEL_MINIMUM);
        levelNf.setMaximum(LEVEL_MAXIMUM);
        levelSlider = new JSlider(LEVEL_MINIMUM, LEVEL_MAXIMUM, LEVEL_DEFAULT);
        levelText = new JFormattedTextField(levelNf);
        levelText.setColumns(4);
        levelText.setText(String.valueOf(LEVEL_DEFAULT));
        createConnection(levelText, levelSlider);
        add(levelLabel);
        add(levelText);
        add(levelSlider);
        SpringUtilities.makeCompactGrid(this,
                1, 3,            //rows, cols
                6, 6,         //initX, initY
                10, 10);         //xPad, yPad
    }

    @Override
    public Object[] getParameters() {
        Object[] args = new Object[1];
        String text = levelText.getText();
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
            levelText.setText(args[0].toString());
            int value = Integer.parseInt(args[0].toString());
            levelSlider.setValue(value);
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
