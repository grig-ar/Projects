package nsu.fit.g16203.grigorovich.model;

import javax.swing.*;

import ru.nsu.fit.g16203.grigorovich.utilityFiles.SpringUtilities;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class FieldSettingsPanel extends JPanel {
    private JFormattedTextField xGridText;
    private JFormattedTextField yGridText;
    private JFormattedTextField xMinText;
    private JFormattedTextField xMaxText;
    private JFormattedTextField yMinText;
    private JFormattedTextField yMaxText;
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
    //private static final int[] DEFAULT_GRID = {5, 5};
    //private static final double[] DEFAULT_SIZE = {-3d, 3d, -3d, 3d};
//    private static final double COLOR_MINIMUM = 2;
//    private static final double COLOR_MAXIMUM = 255;
//    private static final double SIZE_MINIMUM = 4;
//    private static final double SIZE_MAXIMUM = 64;

    public FieldSettingsPanel(double[] sizeDefault, int xGridDefault, int yGridDefault) {
        setLayout(new SpringLayout());
        setBorder(BorderFactory.createTitledBorder("Settings"));
        JLabel xGridLabel = new JLabel("xGrid", SwingConstants.TRAILING);
        JLabel yGridLabel = new JLabel("yGrid", SwingConstants.TRAILING);
        JLabel xMinLabel = new JLabel("xMin", SwingConstants.TRAILING);
        JLabel xMaxLabel = new JLabel("xMax", SwingConstants.TRAILING);
        JLabel yMinLabel = new JLabel("yMin", SwingConstants.TRAILING);
        JLabel yMaxLabel = new JLabel("yMax", SwingConstants.TRAILING);
        NumberFormatter gridNf = new NumberFormatter();
        NumberFormatter sizeNf = new NumberFormatter();
        sizeNf.setFormat(df);
//        NumberFormatter sizeNf = new NumberFormatter();
//        colorNf.setMinimum(COLOR_MINIMUM);
//        colorNf.setMaximum(COLOR_MAXIMUM);
//        sizeNf.setMinimum(SIZE_MINIMUM);
//        sizeNf.setMaximum(SIZE_MAXIMUM);
        xGridText = new JFormattedTextField(gridNf);
        yGridText = new JFormattedTextField(gridNf);
        xMinText = new JFormattedTextField(sizeNf);
        xMaxText = new JFormattedTextField(sizeNf);
        yMinText = new JFormattedTextField(sizeNf);
        yMaxText = new JFormattedTextField(sizeNf);

        xGridText.setText(String.valueOf(xGridDefault));
        yGridText.setText(String.valueOf(yGridDefault));
        xMinText.setText(df.format(sizeDefault[0]));
        xMaxText.setText(df.format(sizeDefault[1]));
        yMinText.setText(df.format(sizeDefault[2]));
        yMaxText.setText(df.format(sizeDefault[3]));

        add(xGridLabel);
        add(xGridText);
        add(yGridLabel);
        add(yGridText);
        add(xMinLabel);
        add(xMinText);
        add(xMaxLabel);
        add(xMaxText);
        add(yMinLabel);
        add(yMinText);
        add(yMaxLabel);
        add(yMaxText);

        SpringUtilities.makeCompactGrid(this,
                6, 2,            //rows, cols
                6, 6,         //initX, initY
                10, 10);         //xPad, yPad
    }

    private boolean empty(final String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean textBoxesEmpty() {
        return (empty(xGridText.getText()) || empty(yGridText.getText()) ||
                empty(xMinText.getText()) || empty(xMaxText.getText()) ||
                empty(yMinText.getText()) || empty(yMaxText.getText()));
    }

    public FieldState getFieldState() throws ParseException {
        if (textBoxesEmpty()) {
            JOptionPane.showMessageDialog(this, "Incorrect settings!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new FieldState(Integer.parseInt(xGridText.getText()), Integer.parseInt(yGridText.getText()),
                format.parse(xMinText.getText()).doubleValue(), format.parse(xMaxText.getText()).doubleValue(),
                format.parse(yMinText.getText()).doubleValue(), format.parse(yMaxText.getText()).doubleValue());
    }

}
