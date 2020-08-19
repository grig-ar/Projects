package ru.nsu.fit.g16203.grigorovich.controller.SettingsFactory;

import javax.swing.*;

public abstract class Settings extends JPanel {
    public abstract Object[] getParameters();

    public abstract void setParameters(Object[] args);

    public abstract JPanel getPanel();
}
