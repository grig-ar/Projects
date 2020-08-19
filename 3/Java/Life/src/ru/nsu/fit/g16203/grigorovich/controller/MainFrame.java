package ru.nsu.fit.g16203.grigorovich.controller;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class MainFrame extends JFrame {
    public JButton globalXorButton = null;
    private static final long serialVersionUID = 1L;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JPanel statusBar;
    private JLabel statusLabel;

    public MainFrame() {
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {

        }
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        toolBar = new JToolBar("Main toolbar");
        toolBar.setRollover(true);
        add(toolBar, BorderLayout.PAGE_START);

        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusBar.setPreferredSize(new Dimension(this.getWidth(), 16));
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Ready!");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

    }

    public MainFrame(int x, int y, String title) {
        this();
        setSize(x, y);
        setLocationByPlatform(true);
        setTitle(title);
    }


    public JMenuItem createMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod) throws SecurityException, NoSuchMethodException {
        JMenuItem item = new JMenuItem(title);
        if (actionMethod.equals("onToolBar") || actionMethod.equals("onStatusBar")) {
            item = new JCheckBoxMenuItem(title);
            item.setSelected(true);
        }
        if (actionMethod.equals("onImpact")) {
            item = new JCheckBoxMenuItem(title);
            item.setSelected(false);
        }
        if (actionMethod.equals("onRun")) {
            item = new JCheckBoxMenuItem(title);
        }
        if (actionMethod.equals("onReplace")) {
            item = new JCheckBoxMenuItem(title);
            item.setSelected(true);
        }
        if (actionMethod.equals("onXor")) {
            item = new JCheckBoxMenuItem(title);
        }
        item.setMnemonic(mnemonic);
        item.setToolTipText(tooltip);
        if (icon != null)
            item.setIcon(new ImageIcon(getClass().getResource("resources/" + icon), title));
        final Method method = getClass().getMethod(actionMethod);
        item.addActionListener(evt -> {
            try {
                method.invoke(MainFrame.this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return item;
    }

    public void addMenuToolbarGroup(String title1, String tooltip1, int mnemonic1, String icon1, String actionMethod1,
                                    String title2, String tooltip2, int mnemonic2, String icon2, String actionMethod2) throws NoSuchMethodException {
        MenuElement replaceElement = getParentMenuElement(title1);
        MenuElement xorElement = getParentMenuElement(title2);
        JMenuItem replaceItem = createMenuItem(getMenuPathName(title1), tooltip1, mnemonic1, icon1, actionMethod1);
        JMenuItem xorItem = createMenuItem(getMenuPathName(title2), tooltip2, mnemonic2, icon2, actionMethod2);
        ((JPopupMenu) replaceElement).add(replaceItem);
        ((JPopupMenu) xorElement).add(xorItem);
        JCheckBoxMenuItem replaceMenuButton = (JCheckBoxMenuItem) getMenuElement("Modify/Replace");
        JCheckBoxMenuItem xorMenuButton = (JCheckBoxMenuItem) getMenuElement("Modify/Xor");
        JButton replaceButton = new JButton(replaceMenuButton.getIcon());
        JButton xorButton = new JButton(xorMenuButton.getIcon());

        for (ActionListener listener : replaceMenuButton.getActionListeners())
            replaceButton.addActionListener(listener);

        for (ActionListener listener : xorMenuButton.getActionListeners())
            xorButton.addActionListener(listener);

        replaceButton.setToolTipText(replaceMenuButton.getToolTipText());
        xorButton.setToolTipText(xorMenuButton.getToolTipText());
        replaceButton.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                statusLabel.setText(replaceMenuButton.getToolTipText());
            }
        });

        xorButton.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                statusLabel.setText(xorMenuButton.getToolTipText());
            }
        });
        replaceMenuButton.addActionListener(e -> {
            replaceMenuButton.setSelected(true);
            replaceButton.setSelected(true);
            xorMenuButton.setSelected(false);
            xorButton.setSelected(false);
        });
        xorMenuButton.addActionListener(e -> {
            xorMenuButton.setSelected(true);
            xorButton.setSelected(true);
            replaceMenuButton.setSelected(false);
            replaceButton.setSelected(false);
        });
        replaceButton.setSelected(true);
        replaceButton.addActionListener(e -> {
            replaceButton.setSelected(true);
            replaceMenuButton.setSelected(true);
            xorButton.setSelected(false);
            xorMenuButton.setSelected(false);
        });
        xorButton.addActionListener(e -> {
            xorButton.setSelected(true);
            xorMenuButton.setSelected(true);
            replaceButton.setSelected(false);
            replaceMenuButton.setSelected(false);

        });
        toolBar.add(replaceButton);
        toolBar.add(xorButton);
        globalXorButton = xorButton;
    }

//    public JMenuItem createMenuItem(String title, String tooltip, int mnemonic, String actionMethod) throws SecurityException, NoSuchMethodException {
//        return createMenuItem(title, tooltip, mnemonic, null, actionMethod);
//    }

    public JMenu createSubMenu(String title, int mnemonic) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        return menu;
    }

    public void addSubMenu(String title, int mnemonic) {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        JMenu subMenu = createSubMenu(getMenuPathName(title), mnemonic);
        if (element instanceof JMenuBar)
            ((JMenuBar) element).add(subMenu);
        else if (element instanceof JMenu)
            ((JMenu) element).add(subMenu);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(subMenu);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    public void addMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod) throws SecurityException, NoSuchMethodException {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        JMenuItem item = createMenuItem(getMenuPathName(title), tooltip, mnemonic, icon, actionMethod);
        if (element instanceof JMenu)
            ((JMenu) element).add(item);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    public void addMenuItem(String title, String tooltip, int mnemonic, String actionMethod) throws SecurityException, NoSuchMethodException {
        addMenuItem(title, tooltip, mnemonic, null, actionMethod);
    }

    public void addMenuSeparator(String title) {
        MenuElement element = getMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        if (element instanceof JMenu)
            ((JMenu) element).addSeparator();
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).addSeparator();
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    private String getMenuPathName(String menuPath) {
        int pos = menuPath.lastIndexOf('/');
        if (pos > 0)
            return menuPath.substring(pos + 1);
        else
            return menuPath;
    }

    private MenuElement getParentMenuElement(String menuPath) {
        int pos = menuPath.lastIndexOf('/');
        if (pos > 0)
            return getMenuElement(menuPath.substring(0, pos));
        else
            return menuBar;
    }

    public MenuElement getMenuElement(String menuPath) {
        MenuElement element = menuBar;
        for (String pathElement : menuPath.split("/")) {
            MenuElement newElement = null;
            for (MenuElement subElement : element.getSubElements()) {
                if ((subElement instanceof JMenu && ((JMenu) subElement).getText().equals(pathElement))
                        || (subElement instanceof JMenuItem && ((JMenuItem) subElement).getText().equals(pathElement))) {
                    if (subElement.getSubElements().length == 1 && subElement.getSubElements()[0] instanceof JPopupMenu)
                        newElement = subElement.getSubElements()[0];
                    else
                        newElement = subElement;
                    break;
                }
            }
            if (newElement == null) return null;
            element = newElement;
        }
        return element;
    }

    public JButton createToolBarButton(JMenuItem item) {
        JButton button = new JButton(item.getIcon());
        for (ActionListener listener : item.getActionListeners())
            button.addActionListener(listener);
        button.setToolTipText(item.getToolTipText());
        if (item.getText().equals("Impact")) {
            button.addActionListener(e -> {
                boolean buttonState = button.isSelected();
                button.setSelected(!buttonState);
                item.setSelected(!buttonState);
            });
            item.addActionListener(e -> {
                boolean itemState = item.isSelected();
                button.setSelected(itemState);
                item.setSelected(itemState);
            });
        }

        if (item.getText().equals("Run")) {
            button.addActionListener(e -> {
                boolean buttonState = button.isSelected();
                JMenuItem initItem = (JMenuItem) getMenuElement("Action/Init");
                JMenuItem stepItem = (JMenuItem) getMenuElement("Action/Step");
                initItem.setEnabled(buttonState);
                stepItem.setEnabled(buttonState);
                button.setSelected(!buttonState);
                item.setSelected(!buttonState);
            });
            item.addActionListener(e -> {
                boolean itemState = item.isSelected();
                JMenuItem initItem = (JMenuItem) getMenuElement("Action/Init");
                JMenuItem stepItem = (JMenuItem) getMenuElement("Action/Step");
                initItem.setEnabled(!itemState);
                stepItem.setEnabled(!itemState);
                button.setSelected(itemState);
                item.setSelected(itemState);
            });
        }

        button.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                statusLabel.setText(item.getToolTipText());
            }
        });
        return button;
    }

    public JButton createToolBarButton(String menuPath) {
        JMenuItem item = (JMenuItem) getMenuElement(menuPath);
        if (item == null)
            throw new InvalidParameterException("Menu path not found: " + menuPath);
        return createToolBarButton(item);
    }

    public void addToolBarButton(String menuPath) {
        toolBar.add(createToolBarButton(menuPath));
    }

    public void addToolBarSeparator() {
        toolBar.addSeparator();
    }

    public void setToolBarState(boolean state) {
        toolBar.setVisible(state);
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public void setStatusBarState(boolean state) {
        statusBar.setVisible(state);
    }

    public JPanel getStatusBar() {
        return statusBar;
    }
}
