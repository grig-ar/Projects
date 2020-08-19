package ru.nsu.fit.g16203.grigorovich.controller.SettingsFactory;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class GammaSettings extends Settings {
    private JFormattedTextField gammaText;
    private static final double GAMMA_MINIMUM = 0.0;
    private static final double GAMMA_MAXIMUM = 10.0;
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);

    GammaSettings() {
        setPreferredSize(new Dimension(60, 75));
        setLayout(new FlowLayout());
        JLabel gammaLabel = new JLabel("Gamma:", SwingConstants.TRAILING);
        NumberFormatter gammaNf = new NumberFormatter();
        gammaNf.setMinimum(GAMMA_MINIMUM);
        gammaNf.setMaximum(GAMMA_MAXIMUM);
        gammaNf.setFormat(df);
        gammaText = new JFormattedTextField(gammaNf);
        gammaText.setText(df.format(1.0));
        add(gammaLabel);
        add(gammaText);
    }

    @Override
    public Object[] getParameters() {
        Object[] args = new Object[1];
        try {
            String text = gammaText.getText();
            if (text.length() == 0) {
                JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            args[0] = format.parse(gammaText.getText()).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return args;
    }

    @Override
    public void setParameters(Object[] args) {
        if (args != null) {
            gammaText.setText(df.format(Double.parseDouble(args[0].toString())));
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
