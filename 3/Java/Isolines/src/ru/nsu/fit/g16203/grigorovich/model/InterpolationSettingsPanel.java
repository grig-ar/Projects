package nsu.fit.g16203.grigorovich.model;

import ru.nsu.fit.g16203.grigorovich.utilityFiles.SpringUtilities;

import javax.swing.*;

public class InterpolationSettingsPanel extends JPanel {
    private static final int INTERPOLATION_TYPE_WITHOUT_INTERPOLATION = -1;
    private static final int INTERPOLATION_TYPE_RGB = 1;
    private static final int INTERPOLATION_TYPE_LAB = 2;
    private JRadioButton withoutInterpolationRadioButton = new JRadioButton("Off");
    private JRadioButton rgbInterpolationRadioButton = new JRadioButton("RGB");
    private JRadioButton labInterpolationRadioButton = new JRadioButton("LAB");

    public InterpolationSettingsPanel() {
        setLayout(new SpringLayout());
        setBorder(BorderFactory.createTitledBorder("Interpolation"));
        withoutInterpolationRadioButton.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(withoutInterpolationRadioButton);
        buttonGroup.add(rgbInterpolationRadioButton);
        buttonGroup.add(labInterpolationRadioButton);
        add(withoutInterpolationRadioButton);
        add(rgbInterpolationRadioButton);
        add(labInterpolationRadioButton);
        add(labInterpolationRadioButton);

        SpringUtilities.makeCompactGrid(this,
                3, 1,
                6, 6,
                10, 10);
    }

    public int getInterpolationType() {
        if (withoutInterpolationRadioButton.isSelected())
            return INTERPOLATION_TYPE_WITHOUT_INTERPOLATION;
        if (rgbInterpolationRadioButton.isSelected())
            return INTERPOLATION_TYPE_RGB;
        if (labInterpolationRadioButton.isSelected())
            return INTERPOLATION_TYPE_LAB;
        return 0;
    }

    public void setInterpolationType(int interpolationType) {
        if (interpolationType == INTERPOLATION_TYPE_WITHOUT_INTERPOLATION)
            withoutInterpolationRadioButton.setSelected(true);
        if (interpolationType == INTERPOLATION_TYPE_RGB)
            rgbInterpolationRadioButton.setSelected(true);
        if (interpolationType == INTERPOLATION_TYPE_LAB)
            labInterpolationRadioButton.setSelected(true);
    }

}
